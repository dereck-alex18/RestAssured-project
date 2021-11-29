import helpers.Constants;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;

public class PostUserTest extends TestBase {
    private User validUser, fixedUser;

    @BeforeClass
    public void generateTestData() {
        fixedUser = new User("Angelina Jolie", "aj@schools.com", "abcd", "false");
        validUser = new User("Tom Cruise", "tc@schools.com", "1234", "true");

        registerUserRequest(SPEC, fixedUser);
    }

    @AfterClass
    public void removeTestData(){
        deleteUserRequest(SPEC, validUser);
        deleteUserRequest(SPEC, fixedUser);
    }

   @Test
    public void shouldReturnSuccessfulMessageAnd201Status(){
       registerUserRequest(SPEC, validUser)
           .then()
               .assertThat()
               .statusCode(201)
               .body("message", equalTo(Constants.POST_USER_SUCCESS));
    }

    @Test
    public void shouldReturnFailMessageAnd400Status(){
        registerUserRequest(SPEC, fixedUser)
            .then()
                .assertThat()
                .statusCode(400)
                .body("message", equalTo(Constants.POST_USER_FAIL));

    }

}
