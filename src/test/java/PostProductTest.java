import io.restassured.response.Response;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static requests.UserEndpoint.*;

import org.testng.annotations.DataProvider;
import static requests.ProductEndpoint.*;

public class PostProductTest extends TestBase{

    private User user;
    private Product product, fixedProduct;

    @BeforeClass
    public void registerProduct(){
        user = new User("Alexander", "alex@test.com", "1234", "true");
        product = new Product("iPhone 11", "4000", "8gb ram, 128gb storage", "100");
        fixedProduct = new Product("Samsung Galaxy s20", "3800", "8gb ram, 128gb storage", "100");

        registerUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, user);
        postProductRequest(SPEC, user, fixedProduct);
    }

    @AfterClass
    public void deleteProduct(){
        deleteProductRequest(SPEC, user, product);
    }

    @Test
    public void shouldReturnSuccessfulMessageAndStatus201(){
        postProductRequest(SPEC, user, product).
                then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnFailMessageAndStatus400(){
        postProductRequest(SPEC, user, fixedProduct)
                .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_FAIL));
    }
}
