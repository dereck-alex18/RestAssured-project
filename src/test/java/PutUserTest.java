import helpers.Constants;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

public class PutUserTest extends TestBase {

    private User user, notRegisteredUser, user2;

    @BeforeClass
    public void generateTestData() {
        user = new User("Joaquina", "joaquina@email.com", "123abc", "true");
        registerUserRequest(SPEC, user);
        user2 = new User("Joaquina", "joaquina@email.com", "minhasenha8983", "false");
        registerUserRequest(SPEC, user2);
        notRegisteredUser = new User("Betania", "betania@email.com", "minhasenha123", "true");
    }

    @AfterClass
    public void removeTestData(){
        deleteUserRequest(SPEC, user);
        deleteUserRequest(SPEC, user2);
        registerUserRequest(SPEC, notRegisteredUser);
        deleteUserRequest(SPEC, notRegisteredUser);
    }

    @Test
    public void shouldReturnSuccessMessageAndStatus200ToUpdateSuccessfully() {
        putUserRequest(SPEC, user).
                then().
                assertThat().
                statusCode(200).
                body("message", equalTo(Constants.PUT_SUCCESS));
    }

    @Test
    public void shouldReturnSuccessMessageAndStatus201ToRegisterSuccessfully() {
        putUserRequest(SPEC, notRegisteredUser).
                then().
                assertThat().
                statusCode(201).
                body("message", equalTo(Constants.PUT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnErrorMessageAndStatus400ToEmailAlreadyInUse() {
        putUserRequest(SPEC, user2).
                then().
                assertThat().
                statusCode(400).
                body("message", equalTo(Constants.PUT_USER_REGISTRATION_FAIL));
    }
}
