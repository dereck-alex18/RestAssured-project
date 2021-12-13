import io.restassured.response.Response;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

import static requests.ProductEndpoint.*;


public class GetProductTest extends TestBase {

    private User user;
    private Product product, product2;

    @BeforeClass
    public void generateTestData() {
        user = new User ("Dereck", "dereck@test.com", "12345", "true");
        registerUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, user);
        product = new Product ("iPhone 13", 4000, "16gb ram, 128gb storage", 500);
        postProductRequest(SPEC, user, product);
    }

    @AfterClass
    public void removeTestData() {
        deleteProductRequest(SPEC, user, product);
        deleteUserRequest(SPEC, user);
    }


    @DataProvider(name = "product")
    public Object[][] createTestData() {
        return new Object[][] {
                {"?nome=" + product.name, 1},
        };
    }

    @Test(dataProvider = "product")
    public void shouldReturnProductsAndStatus200ToListProducts(String query, int totalProducts) {
        Response getProductResponse = getProductRequest(SPEC, query);
        getProductResponse.
                then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalProducts));
    }
}
