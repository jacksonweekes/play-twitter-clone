package model;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Post model
 *
 * @author Jackson Cleary
 */
public class Post implements Comparable<Post> {
    private String postID, username, message;
    private String[] tags;
    private long postTime;

    /**
     * Public constructor for the Post object
     * @param username username of the {@link User} creating the post
     * @param message the message contained in the post
     */
    public Post(String username, String message) {
        this.postID = new ObjectId().toHexString();
        this.username = username.toLowerCase();
        this.message = message;
        this.tags = extractTags(message);
        this.postTime = System.currentTimeMillis();
    }

    private Post(String postID, String username, String message, String[] tags, long postTime) {
        this.postID = postID;
        this.username = username;
        this.message = message;
        this.tags = tags;
        this.postTime = postTime;
    }

    /**
     * Gets the name of the {@link User} who created the post
     *
     * @return username of the {@link User} who created the post
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the postID of the post
     *
     * @return postID of the post
     */
    public String getPostID() {
        return postID;
    }

    /**
     * Gets the message contained in the post
     *
     * @return the message contained in the post
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets an array of all tags contained in the post
     *
     * @return array of tags
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Checks if the post has the given tag
     *
     * @param tag the tag to test against the post's tags
     * @return true if the post is tagged with the given tag, else false
     */
    public boolean hasTag(String tag) {
        for(String t: tags) {
            if(t.equals(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses a regular expression to extract tags(strings preceded by #) from the post to return as an array
     *
     * @param fullMessage the complete message as given by the user
     * @return array of all the tags in post(not including preceding #)
     */
    public static String[] extractTags(String fullMessage) {
        List<String> tags = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?<=#)[\\w]*").matcher(fullMessage);
        while(matcher.find()) {
            tags.add(matcher.group());
        }
        return tags.toArray(new String[tags.size()]);
    }

    /**
     * Replaces all tags with 'linked tags' so the user can simply click the tag to search
     * for other posts with the same tag
     *
     * @return message with all tags hyperlinked
     */
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

    /**
     * Converts a Post object into a BSON document for storage in MongoDB
     *
     * @param p the Post object to convert
     * @return a BSON Document representing the Post object
     */
    public static Document postToBson(Post p) {
        Document d = new Document("_id", new ObjectId(p.getPostID()))
                .append("username", p.username)
                .append("message", p.getMessage())
                .append("tags", Arrays.asList(p.getTags()))
                .append("postTime", p.postTime);

        return d;
    }

    /**
     * Converts a BSON Document into a Post object for retrieval from MongoDB
     *
     * @param d the BSON Document containing the Post
     * @return a Post object constructed from the BSON Document
     */
    public static Post postFromBson(Document d) {
        String id = d.getObjectId("_id").toHexString();
        String username = d.getString("username");
        String message = d.getString("message");
        List<String> tagList = d.get("tags", List.class);
        String[] tagArray = tagList.toArray(new String[tagList.size()]);
        long postTime = d.getLong("postTime");
        return new Post(id, username, message, tagArray, postTime);
    }

    /**
     * Allows comparison of two Post objects by their postTime
     *
     * @param p the Post to compare to
     * @return -1 if this.postTime is less than p.postTime, 0 if equal,
     * 1 if this.postTime is greater than p.postTime
     */
    @Override
    public int compareTo(Post p) {
        if(postTime < p.postTime) {
            return -1;
        } else if(postTime == p.postTime) {
            return 0;
        } else return 1;
    }
}
