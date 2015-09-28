package model;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Created by jackson on 21/08/15.
 */
public class PostTest {
    private Post post;
    @Before
    public void setUp() {
        post = new Post("Bob", "This is a #test post, #YOLO!");
    }

    @Test
    public void testGetMessage() throws Exception {
        assertEquals("This is a #test post, #YOLO!", post.getMessage());
    }

    @Test
    public void testGetTags() throws Exception {
        String tags[] = post.getTags();
        assertEquals(2, tags.length);
        assertEquals("test", tags[0]);
        assertEquals("YOLO", tags[1]);
    }

    @Test
    public void testExtractTags() throws Exception {
        String tags[] = Post.extractTags("All #words preceded by a #hash should be #extracted");
        assertEquals(3, tags.length);
        assertEquals("words", tags[0]);
        assertEquals("hash", tags[1]);
        assertEquals("extracted", tags[2]);
    }

    @Test
    public void testHasTag() throws Exception {
        assertTrue(post.hasTag("test"));
        assertTrue(post.hasTag("YOLO"));
        assertFalse(post.hasTag("tigerblood-"));
    }
}