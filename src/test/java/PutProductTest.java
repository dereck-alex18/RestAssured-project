import helpers.Constants;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static requests.ProductEndpoint.*;
import static requests.UserEndpoint.authenticateUserRequest;
import static requests.UserEndpoint.registerUserRequest;

public class PutProductTest extends TestBase{

    private User user, notAdminUser, userNotAuthenticated;
    private Product product, product2, product3;

    @BeforeClass
    public void registerProduct(){
        user = new User("Alexander", "alex@test.com", "1234", "true");
        notAdminUser = new User("Pedro", "pedro@test.com", "1234", "false");
        userNotAuthenticated = new User("Joana", "joana@test.com", "1234", "true");

        product = new Product("iPhone 12", 4000, "8gb ram, 128gb storage", 100);
        product2 = new Product("Logitech MX Vertical", 470, "Mouse", 382);
        product3 = new Product("Monitor LG widescreen 25 polegadas", 70, "Monitor", 10);

        registerUserRequest(SPEC, user);
        registerUserRequest(SPEC, notAdminUser);
        registerUserRequest(SPEC, userNotAuthenticated);
        authenticateUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, notAdminUser);
        authenticateUserRequest(SPEC, userNotAuthenticated);
    }

    @AfterClass
    public void deleteProduct(){
        deleteProductRequest(SPEC, user, product);
        deleteProductRequest(SPEC, user, product2);
        deleteProductRequest(SPEC, user, product3);
    }

    @Test
    public void shouldReturnSuccessAndStatus200() {
        postProductRequest(SPEC, user, product);
        putProductRequest(SPEC, user, product).
            then().
                assertThat().
                statusCode(200).
                body("message", equalTo(Constants.PUT_SUCCESS));
    }

    @Test
    public void shouldReturnSuccessAndStatus201() {
        putProductRequest(SPEC, user, product3).
                then().
                assertThat().
                statusCode(201).
                body("message", equalTo(Constants.PUT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnErrorAndStatus400() {
        postProductRequest(SPEC, user, product2);
        putProductRequest(SPEC, user, product2).
                then().
                assertThat().
                statusCode(400).
                body("message", equalTo(Constants.PRODUCT_REGISTRATION_FAIL));
    }

    @Test
    public void shouldReturnErrorAndStatus401() {
        userNotAuthenticated.setUserAuthToken("testing invalid token");
        putProductRequest(SPEC, userNotAuthenticated, product)
                .then()
                .assertThat()
                .statusCode(401)
                .body("message", equalTo(Constants.INVALID_TOKEN));
    }

    @Test
    public void shouldReturnErrorAndStatus403() {
        putProductRequest(SPEC, notAdminUser, product2)
                .then()
                .assertThat()
                .statusCode(403)
                .body("message", equalTo(Constants.PRODUCT_REGISTRATION_UNAUTHORIZED));
    }
}
