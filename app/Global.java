import model.Post;
import model.PostService;
import model.User;
import model.UserService;
import play.GlobalSettings;

import play.*;

import static java.lang.Thread.sleep;

/**
 * Created by jackson on 20/08/15.
 */
public class Global extends GlobalSettings {

    // Provide some example users
    @Override
    public void onStart(Application application) {
        try {
            // Add some users
            UserService.instance.addUser(new User("Bob", "bob@example.com", "password"));
            UserService.instance.addUser(new User("Jack", "jack@example.com", "password"));
            UserService.instance.addUser(new User("Jill", "jill@example.com", "password"));
            UserService.instance.addUser(new User("Jan", "jan@example.com", "password"));

            // and some posts (sleep is called between each to ensure different timestamps)
            PostService.instance
                    .addPost(new Post("Bob", "This is the very first post! #trendsetter"));
            sleep(50);
            PostService.instance
                    .addPost(new Post("Bob", "Check out http://www.willworkforsudo.com #awesome #radical"));
            sleep(50);
            PostService.instance
                    .addPost(new Post("Jill", "Going for a walk to the lookout with @Jack! #greatoutdoors"));
            sleep(50);
            PostService.instance
                    .addPost(new Post("Jill", "Jacks in the hospital. Look like hes got a concussion #ouch #felldown"));
            sleep(50);
            PostService.instance
                    .addPost(new Post("Jack", "In hospital, bit dazed, took a tumble :-( #poorme #ouch #downwithhills"));

        } catch(Exception e) {
            // Do nothing
        }
    }
}
