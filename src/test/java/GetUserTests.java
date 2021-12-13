import io.restassured.response.Response;
import models.User;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import static requests.UserEndpoint.*;
import org.testng.annotations.DataProvider;

public class GetUserTests extends TestBase {

    private User user;

    @BeforeClass
    public void generateTestData() {
        user = new User("Dereck Portela", "dereck@email.com", "123abc", "true");
        registerUserRequest(SPEC, user);
    }

    @AfterClass
    public void removeTestData() {
        deleteUserRequest(SPEC, user);
    }

    @DataProvider(name = "usersData")
    public Object[][] createTestData() {
        return new Object[][] {
                {"?nome=" + user.name, 1},
        };
    }

    @Test(dataProvider = "usersData")
    public void shouldReturnUsersAndStatus200ToListUsers(String query, int totalUsers) {
        Response getUserResponse = getUserRequest(SPEC, query);
        getUserResponse.
            then().
                    assertThat().
                    statusCode(200).
                    body("quantidade", equalTo(totalUsers));
    }
}