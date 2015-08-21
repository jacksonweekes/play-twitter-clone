package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jackson on 21/08/15.
 */
public class Post {
    private String postID, message;
    private String[] tags;

    public Post(String message) {
        postID = java.util.UUID.randomUUID().toString();
        this.message = message;
        this.tags = extractTags(message);
    }

    public String getPostID() {
        return postID;
    }

    public String getMessage() {
        return message;
    }

    public String[] getTags() {
        return tags;
    }

    public static String[] extractTags(String fullPost) {
        List<String> tags = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?<=#)[\\w]*").matcher(fullPost);
        while(matcher.find()) {
            tags.add(matcher.group());
        }
        return tags.toArray(new String[tags.size()]);
    }

}
