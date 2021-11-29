import helpers.Constants;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

import static requests.ProductEndpoint.*;

public class PostProductTest extends TestBase {

    private User user, notAdminUser, userNotAuthenticated;
    private Product product, product2, fixedProduct;

    @BeforeClass
    public void generateTestData(){
        user = new User("Alexander", "alex@test.com", "1234", "true");
        notAdminUser = new User("Pedro", "pedro@test.com", "1234", "false");
        userNotAuthenticated = new User("Joana", "joana@test.com", "1234", "true");

        product = new Product("iPhone 12", 4000, "8gb ram, 128gb storage", 100);
        product2 = new Product("Moto G6", 2000, "8gb ram, 128gb storage", 40);
        fixedProduct = new Product("Samsung Galaxy s20", 3800, "8gb ram, 128gb storage", 100);

        registerUserRequest(SPEC, user);
        registerUserRequest(SPEC, notAdminUser);
        registerUserRequest(SPEC, userNotAuthenticated);
        authenticateUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, notAdminUser);
        authenticateUserRequest(SPEC, userNotAuthenticated);
        postProductRequest(SPEC, user, fixedProduct);
    }

    @AfterClass
    public void deleteProduct(){
        deleteUserRequest(SPEC, user);
        deleteUserRequest(SPEC, notAdminUser);
        deleteUserRequest(SPEC, userNotAuthenticated);
        deleteProductRequest(SPEC, user, product);
    }

    @Test
    public void shouldReturnSuccessfulMessageAndStatus201() {
        postProductRequest(SPEC, user, product).
            then()
                .assertThat()
                .statusCode(201)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnFailMessageAndStatus400() {
        postProductRequest(SPEC, user, fixedProduct)
            .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_FAIL));
    }

    @Test
    public void shouldReturnFailMessageAndStatus403() {
        postProductRequest(SPEC, notAdminUser, product2)
            .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_UNAUTHORIZED));
    }

    @Test
    public void shouldReturnFailMessageAndStatus401() {
        userNotAuthenticated.setUserAuthToken("testing invalid token");
        postProductRequest(SPEC, userNotAuthenticated, product2)
            .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(Constants.INVALID_TOKEN));
    }
}
