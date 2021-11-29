import helpers.Constants;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

public class PutUserTest extends TestBase {

    private User validUser, notRegisteredUser, validUser2;

    @BeforeClass
    public void generateTestData() {
        validUser = new User("Joaquina", "joaquina@email.com", "123abc", "true");
        registerUserRequest(SPEC, validUser);
        notRegisteredUser = new User("Betania", "betania@email.com", "minhasenha123", "true");
        validUser2 = new User("Joaquina", "joaquina@email.com", "minhasenha8983", "false");
        registerUserRequest(SPEC, validUser2);
    }

    @AfterClass
    public void removeTestData(){
        deleteUserRequest(SPEC, validUser);
        deleteUserRequest(SPEC, notRegisteredUser);
        deleteUserRequest(SPEC, validUser2);
    }

    @Test
    public void shouldReturnSuccessMessageAndStatus200ToUpdateSuccessfull() {
        putUserRequest(SPEC, validUser).
                then().
                assertThat().
                statusCode(200).
                body("message", equalTo(Constants.PUT_SUCCESS));
    }

    @Test
    public void shouldReturnSuccessMessageAndStatus201ToRegistSuccessfully() {
        putUserRequest(SPEC, notRegisteredUser).
                then().
                assertThat().
                statusCode(201).
                body("message", equalTo(Constants.PUT_REGISTRATION_SUCCESS));
    }

    @Test
    public void shouldReturnErrorMessageAndStatus400ToEmailAlreadyInUse() {
        putUserRequest(SPEC, validUser2).
                then().
                assertThat().
                statusCode(400).
                body("message", equalTo(Constants.PUT_USER_REGISTRATION_FAIL));
    }
}
