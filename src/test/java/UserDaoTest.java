import com.revature.exceptions.UserNotCreatedException;
import com.revature.repository.UserDAO;
import org.junit.Test;
import org.mockito.Mockito;

public class UserDaoTest {

    UserDAO userDAO = Mockito.mock(UserDAO.class);

    @Test(expected = UserNotCreatedException.class)
    public void createUserWithNoUsernameThrowsException() {
        Mockito.when(userDAO.createUser("", "password")).thenThrow(UserNotCreatedException.class);
        userDAO.createUser("", "password");
    }


}
