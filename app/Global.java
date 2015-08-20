import model.User;
import model.UserService;
import play.GlobalSettings;

import play.*;

/**
 * Created by jackson on 20/08/15.
 */
public class Global extends GlobalSettings {

    // Provide some example users
    @Override
    public void onStart(Application application) {
        try {
            UserService.instance.addUser(new User("Bob", "bob@example.com", "password"));
            UserService.instance.addUser(new User("Jack", "jack@example.com", "password"));
            UserService.instance.addUser(new User("Jill", "jill@example.com", "password"));
            UserService.instance.addUser(new User("Jan", "jan@example.com", "password"));
        } catch(Exception e) {
            // Do nothing
        }
    }
}
