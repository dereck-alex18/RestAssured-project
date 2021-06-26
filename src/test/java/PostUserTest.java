import io.restassured.response.Response;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static requests.UserEndpoint.*;

public class PostUserTest extends TestBase {
    private User validUser, fixedUser;
    private Response postUserResponse;

    @BeforeClass
    public void generateTestData(){
        fixedUser = new User("Grace kelly", "gc@schools.com", "abcd", "false");
        validUser = new User("Tom Cruise", "tc@schools.com", "1234", "true");

        registerUserRequest(SPEC, fixedUser);
    }

    @AfterClass
    public void removeTestData(){
        deleteUserRequest(SPEC, validUser);
    }

   @Test
    public void shouldReturnSucessfulMesageAnd200Status(){
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
