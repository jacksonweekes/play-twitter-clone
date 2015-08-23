package model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jackson on 21/08/15.
 */
public class Post implements Comparable<Post> {
    private String postID, username, message;
    private String[] tags;
    private long postTime;

    public Post(String username, String message) {
        postID = java.util.UUID.randomUUID().toString();
        this.username = username.toLowerCase();
        this.message = message;
        this.tags = extractTags(message);
        this.postTime = System.currentTimeMillis();
    }

    public String getUsername() {
        return username;
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

    public boolean hasTag(String tag) {
        for(String t: tags) {
            if(t.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    public static String[] extractTags(String fullPost) {
        List<String> tags = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?<=#)[\\w]*").matcher(fullPost);
        while(matcher.find()) {
            tags.add(matcher.group());
        }
        return tags.toArray(new String[tags.size()]);
    }

    public String getMessageWithLinkedTags() {
        String newMessage = message;
        String regex, replaceText;
        for(String tag: tags) {
            regex = "[(?<= )#]" + tag + "\\b";
            replaceText = "<a href=\"/tags?tag=" + tag + "\">#" + tag + "</a>";
            newMessage = newMessage.replaceAll(regex, replaceText);
        }
        return newMessage;
    }


    @Override
    public int compareTo(Post o) {
        if(postTime < o.postTime) {
            return 1;
        } else if(postTime == o.postTime) {
            return 0;
        } else return -1;
    }
}
