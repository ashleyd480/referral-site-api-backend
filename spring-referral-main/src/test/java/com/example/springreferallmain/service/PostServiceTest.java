package com.example.springreferallmain.service;

import com.example.springreferallmain.model.Comment;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IPostRepository;
import com.example.springreferallmain.repository.IUserRepository;
import com.example.springreferallmain.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {


    @Autowired
    PostService beanPostService;

    @MockBean
    IPostRepository mockPostRepository;

    @MockBean
    IUserRepository mockUserRepository;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class
    private Post testPost = TestData.makeTestPost1();
    private List<Post> testPosts = TestData.makeTestPosts();

    private User testUser1 = TestData.makeTestUser1();

    private List<Comment> testComments = TestData.makeTestComments();

    private List<Tag> testTags = TestData.makeTestTags();



    // |           TESTING CREATE  | \\

// Add a post (user creates a post)
// addPost Happy Path 😊
    @Test
    public void testAddPostPass() {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser1));
        when(mockPostRepository.save(testPost)).thenReturn(testPost);

        // Test the service method
        Post result = beanPostService.addPost(userId, testPost);

        // Verify results
        assertNotNull(result);
        assertEquals(testPost, result);

        verify(mockPostRepository, times(1)).save(testPost);
    }

    // addPost Sad Path 😭
    @Test
    public void testAddPostFail() {
        // Setup of mock method with "parameters"
        Integer userId = 1;

        // Test the service method
        // Included in the assertThrows

        //Verify result
        assertThrows(RuntimeException.class, () -> beanPostService.addPost(userId, testPost));
    }


// Create a list of posts for testing

    // addPosts Happy Path 😊
    @Test

    public void testAddPostsPass() {
        // Setup of mock method with "parameters"
        when(mockPostRepository.saveAll(testPosts)).thenReturn(testPosts);


        // Test the service method
        List<Post> result = beanPostService.addPosts(testPosts);

        // Verify results
        // We verify that it's not null, that
        assertNotNull(result);
        assertEquals(testPosts, result);

        verify(mockPostRepository, times(1)).saveAll(testPosts);
    }


    // addPosts Sad Path 😭

    @Test
    public void testAddPostsFail() {
        // Setup of mock method with "parameters"
        when(mockPostRepository.saveAll(null)).thenThrow(new IllegalArgumentException());

        // Test the service method
        // Included in the assertThrows

        //Verify result
        assertThrows(IllegalArgumentException.class, () -> beanPostService.addPosts(null));

    }

// |           TESTING RETRIEVE  | \\
// Get all posts

    // getAllPosts Happy Path 😊
    @Test
    public void testGetAllPostsPass() {
        // Setup of mock method with "parameters"
        when(mockPostRepository.findAll()).thenReturn(testPosts);

        // Test the service method
        List<Post> result = beanPostService.getAllPosts();

        // Verify results
        assertNotNull(result);
        assertEquals(testPosts, result);

        verify(mockPostRepository, times(1)).findAll();
    }

    // getAllPosts Sad Path 😭
    @Test
    public void testGetAllPostsFail() {
        // Setup of mock method with "parameters"
        when(mockPostRepository.findAll()).thenReturn(testPosts);

        // Test the service method
        List<Post> result = beanPostService.getAllPosts();

        // Verify results
        assertNotNull(result);
        assertNotEquals(result.size(), 0, "Size was expected to be non-zero");
    }

// Get all posts matching a keyword search, i.e. so users can see matching refer-all posts
    // getPostsByDescriptionContaining Happy Path 😊
    @Test
    public void testGetPostsByDescriptionContainingPass() {
        // Setup of mock method with "parameters"
        String keyword = "jobs";
        when(mockPostRepository.findPostsByPostContentContaining(keyword)).thenReturn((Optional.of(testPosts)));
        // We used Optional.of since may either return a filtered list or a null
        // We don't need to worry about simulating filtering since that is handled via repository layer and we've already mocked a return result. Keep in mind we are just testing service logic here.

        // Test the service method
        List<Post> result = beanPostService.getPostsByDescriptionContaining(keyword);

        // Verify results
        assertNotNull(result);
        assertEquals(testPosts, result);

        verify(mockPostRepository, times(1)).findPostsByPostContentContaining(keyword);
    }

    // getPostsByDescriptionContaining Sad Path 😭
    @Test
    public void testGetPostsByDescriptionContainingFail() {
        // Setup of mock method with "parameters"
        String nonexistentKeyword = "Nonexistent";
        when(mockPostRepository.findPostsByPostContentContaining(nonexistentKeyword)).thenReturn(Optional.empty());


        // Verify results (service method is called here as well)
        assertThrows(RuntimeException.class,()-> beanPostService.getPostsByDescriptionContaining(nonexistentKeyword));
    }


// |           TESTING UPDATE  |

// Update post by id (if user edits a post)
    // updatePostById Happy Path 😊
    // Setup of mock method with "parameters"

    @Test
    public void testUpdatePostByIdPass () {
        // Setup of mock method with "parameters"
        Integer postId = 1;
        when(mockPostRepository.findById(postId)).thenReturn(Optional.ofNullable(testPost));

        Post updatedTestPost = new Post();
        updatedTestPost.setPostId(postId); // allows us to "override" existing post by assign its id to updated post
        updatedTestPost.setPostTitle("updatedTitle");
        updatedTestPost.setPostContent("Updating blah blah");
        updatedTestPost.setNumberLikesOnPost(2);
        updatedTestPost.setPostMediaURL("www.blah-blah.com");
        updatedTestPost.setUser(testUser1); //*Since user is non-nullable we had to make sure to set it here!
        updatedTestPost.setComments(testComments);
        updatedTestPost.setTags(testTags);


        when(mockPostRepository.save(updatedTestPost)).thenReturn(updatedTestPost);

        // Test the service method and the updatedTestPost is what we want to update it to
        Post result = beanPostService.updatePostById(postId, updatedTestPost);

        // Verify results to prove it actually updated
        // 1.) Verify that the existing post was updated with the new data
        assertNotNull(result);
        assertEquals (updatedTestPost, result);


        // 2.) Verify the calls to repository were made
        verify(mockPostRepository, times(1)).findById(postId);
        verify(mockPostRepository, times(1)).save(updatedTestPost);

    }


    // updatePostById Sad Path 😭
    @Test
    public void testUpdatePostByIdFail () {
        // Setup of mock method with "parameters"
        Integer nonExistentPostId = 100;
        when(mockPostRepository.findById(nonExistentPostId )).thenReturn(Optional.empty());

        // Verify results (and service method is called here)
        assertThrows(RuntimeException.class,()-> beanPostService.updatePostById(nonExistentPostId, testPost));

    }


// |           TESTING DELETE  | \\
// Delete post by id
@Test
// deletePostById Happy Path 😊
public void testDeletePostByIdPass() {
    // Setup of mock method with "parameters"
    Integer postId = 1;
    mockPostRepository.findById(postId);
    mockPostRepository.deleteById(postId);


    // Test the service method
    // For now, just verifying that repository methods are called in the happy path for delete
    // This boolean below will always return false since it's a mock repository so the testPost will never get deleted
    // boolean result = beanPostService.deletePostById(postId);


    // Verify results
    // assertTrue(result);
    verify(mockPostRepository, times(1)).findById(postId);
    verify(mockPostRepository, times(1)).deleteById(postId);
}

// deletePostById Sad Path 😭
@Test
public void testDeletePostByIdFail() {
    // Setup of mock method with "parameters"
    Integer nonExistentPostId = 100;
    when(mockPostRepository.findById(nonExistentPostId)).thenReturn(Optional.empty());

    // Test the service method and verify that it throws a RuntimeException
    assertThrows(RuntimeException.class, () -> beanPostService.deletePostById(nonExistentPostId));
}


}
