import helpers.Constants;
import io.restassured.response.Response;
import models.Product;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static requests.ProductEndpoint.*;
import static requests.UserEndpoint.*;

public class DeleteProductTest extends TestBase {

    private User validUser, notAdminUser, userNotAuthenticated;
    private Product validProduct;


    @BeforeClass
    public void generateTestData() {
        validUser = new User("Alexander", "alex@test.com", "1234", "true");
        validProduct = new Product("iPhone 11", 4000, "8gb ram, 128gb storage", 100);
        registerUserRequest(SPEC, validUser);
        authenticateUserRequest(SPEC, validUser);
        postProductRequest(SPEC, validUser, validProduct);

        notAdminUser = new User("Pedro", "pedro@test.com", "1234", "false");
        registerUserRequest(SPEC, notAdminUser);
        authenticateUserRequest(SPEC, notAdminUser);

        userNotAuthenticated = new User("Joana", "joana@test.com", "1234", "true");
        registerUserRequest(SPEC, userNotAuthenticated);
        authenticateUserRequest(SPEC, userNotAuthenticated);
        userNotAuthenticated.setUserAuthToken("Token123");
    }

    @AfterClass
    public void removeTestData() {
        deleteUserRequest(SPEC, validUser);
        deleteUserRequest(SPEC, notAdminUser);
        deleteUserRequest(SPEC, userNotAuthenticated);
    }

    @Test
    public void shouldRemoveProductReturnSuccessMessageAndStatus200() {
        Response deleteProductRequest = deleteProductRequest(SPEC, validUser, validProduct);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(200).
                body("message", equalTo(Constants.MESSAGE_SUCCESS_DELETION));
    }

    @Test
    public void shouldRemoveProductReturnErrorMessageAndStatus403() {
        Response deleteProductRequest = deleteProductRequest(SPEC, notAdminUser, validProduct);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(403).
                body("message", equalTo(Constants.PRODUCT_REGISTRATION_UNAUTHORIZED));
     }

    @Test
    public void shouldRemoveProductReturnErrorMessageAndStatus401() {
        Response deleteProductRequest = deleteProductRequest(SPEC, userNotAuthenticated, validProduct);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(401).
                body("message", equalTo(Constants.INVALID_TOKEN));
    }

}


