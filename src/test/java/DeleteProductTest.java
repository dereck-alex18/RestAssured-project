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

    private User user, notAdminUser, userNotAuthenticated;
    private Product product;


    @BeforeClass
    public void generateTestData() {
        user = new User("Alexander", "alex@test.com", "1234", "true");
        product = new Product("iPhone 12", 4000, "8gb ram, 128gb storage", 100);
        registerUserRequest(SPEC, user);
        authenticateUserRequest(SPEC, user);
        postProductRequest(SPEC, user, product);

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
        deleteUserRequest(SPEC, user);
        deleteUserRequest(SPEC, notAdminUser);
        deleteUserRequest(SPEC, userNotAuthenticated);
    }

    @Test
    public void shouldRemoveProductReturnSuccessMessageAndStatus200ToProductDeletedSuccessfully() {
        Response deleteProductRequest = deleteProductRequest(SPEC, user, product);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(200).
                body("message", equalTo(Constants.MESSAGE_SUCCESS_DELETION));
    }

    @Test
    public void shouldNotRemoveProductReturnErrorMessageAndStatus403ToRouteExclusiveForAdmins() {
        Response deleteProductRequest = deleteProductRequest(SPEC, notAdminUser, product);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(403).
                body("message", equalTo(Constants.PRODUCT_REGISTRATION_UNAUTHORIZED));
     }

    @Test
    public void shouldNotRemoveProductReturnErrorMessageAndStatus401ToNonexistentInvalidOrExpiredToken() {
        Response deleteProductRequest = deleteProductRequest(SPEC, userNotAuthenticated, product);
        deleteProductRequest.
                then().
                assertThat().
                statusCode(401).
                body("message", equalTo(Constants.INVALID_TOKEN));
    }
}


