import helpers.Constants;
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
        product = new Product ("iPhone 12", 4000, "16gb ram, 128gb storage", 500);
        postProductRequest(SPEC, user, product);
        product2 = new Product ("iPhone 13", 4000, "16gb ram, 128gb storage", 500);
        postProductRequest(SPEC, user, product2);
        product2.setProductId("1");
        product.setProductId("2");
    }

    @AfterClass
    public void removeTestData() {
        deleteUserRequest(SPEC, user);
        deleteProductRequest(SPEC, user, product);
        deleteProductRequest(SPEC, user, product2);
    }


    @DataProvider(name = "product")
    public Object[][] createTestData() {
        return new Object[][] {
                {"?nome=" + product2.name, 1},
                {"?preco=" + product2.price, 2},
                {"?descricao=" + product.descricao, 2},
                {"?quantidade=" + product.quantidade, 2}
        };
    }

    @Test(dataProvider = "product")
    public void shouldReturnProductsAndStatus200(String query, int totalProducts) {
        Response getProductResponse = getProductRequest(SPEC, query);
        getProductResponse.
                then().
                assertThat().
                statusCode(200).
                body("quantidade", equalTo(totalProducts));
    }



}
