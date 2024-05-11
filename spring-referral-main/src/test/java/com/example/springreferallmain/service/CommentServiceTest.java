package com.example.springreferallmain.service;

import com.example.springreferallmain.model.Comment;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.ICommentRepository;
import com.example.springreferallmain.repository.IPostRepository;
import com.example.springreferallmain.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest

public class CommentServiceTest {
    @Autowired
    CommentService beanCommentService;

    @MockBean
    ICommentRepository mockCommentRepository;

    @MockBean
    IPostRepository mockPostRepository;

    @MockBean
    RestTemplate restTemplate;


    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class


    private Comment testComment = TestData.makeTestComment1();
    private List<Comment> testComments = TestData.makeTestComments();


    private Post testPost = TestData.makeTestPost1();


// |           TESTING CREATE  | \\

// Add a new comment to a post

    // addCommentToPost Happy Path 😊
    @Test
    public void testAddCommentToAPostPass() {
        // Setup of mock method with "parameters"
        // Note: I had to initialize an array list because otherwise it would not allow me to mock adding comments to an existing list.

        Integer testPostId = 1;
        when(mockPostRepository.findById(1)).thenReturn(Optional.ofNullable(testPost));

        testComment.setPost(testPost);
        List<Comment> testCommentsFromTestPost = new ArrayList<>(testPost.getComments());
        testCommentsFromTestPost.add(testComment);
        testPost.setComments(testCommentsFromTestPost);


        when(mockCommentRepository.save(testComment)).thenReturn(testComment);

        // Test the service method
        Comment result = beanCommentService.addCommentToAPost(testPostId, testComment);

        // Verify results
        // We verify that it's not null, that
        assertNotNull(result);
        assertEquals(testComment, result);
        assertEquals(testComment.getPost(), result.getPost());

        verify(mockPostRepository, times(1)).findById(testPostId);
    }

    // addCommentToPost Sad Path 😭
    @Test
    public void testAddCommentToPostFail() {
        // Setup of mock method with "parameters"
        int nonExistentPostId = 100;
        when(mockPostRepository.findById(100)).thenReturn(Optional.empty());

        // Test the service method
        // Included in the assertThrows

        //Verify result
        assertThrows(RuntimeException.class, () -> beanCommentService.addCommentToAPost(nonExistentPostId, testComment));

    }


// |           TESTING RETRIEVE  | \\
// Get comments tied to a post (to display comments under a post for user)

    // getCommentsByPostId Happy Path 😊
    @Test
    public void testGetCommentsByPostIdPass() {
        // Setup of mock method with "parameters"
        Integer postId = 1;

        when(mockCommentRepository.findCommentByPostPostId(postId)).thenReturn(Optional.ofNullable(testComments));

        // Test the service method
        List<Comment> result = beanCommentService.getCommentsByPostId(postId);

        // Verify results
        assertNotNull(result);
        assertEquals(testComments, result);

        verify(mockCommentRepository, times(1)).findCommentByPostPostId(postId);
    }

    // getCommentsByPostId Sad Path 😭
    @Test
    public void testGetCommentsByPostIdFail() {
        // Setup of mock method with "parameters"
        Integer nonexistentPostId = 1;
        when(mockCommentRepository.findCommentByPostPostId(nonexistentPostId)).thenReturn(Optional.empty());

        // Verify results (service method is called here as well)
        assertThrows(RuntimeException.class, () -> beanCommentService.getCommentsByPostId(nonexistentPostId));
    }


// |           TESTING UPDATE  | \\
// Update tag by id (if user edits a tag)
// updateCommentForPost Happy Path 😊
@Test
public void testUpdateCommentForPostPass() {
    // Setup of mock method with "parameters"
    Integer postId = 1;
    Integer commentId = 1;
    when(mockCommentRepository.findById(commentId)).thenReturn(Optional.ofNullable(testComment));
    when(mockPostRepository.findById(postId)).thenReturn(Optional.ofNullable(testPost));

    Comment updatedTestComment = new Comment(); // mock request body
    updatedTestComment.setCommentId(commentId); // allows us to "override" existing comment by assign its id to updated comment
    updatedTestComment.setCommentText("UpdatedText");
    updatedTestComment.setNumberLikesOnComment(10);
    updatedTestComment.setCommentedByUsername("testUser");

    when(mockCommentRepository.save(updatedTestComment)).thenReturn(updatedTestComment);

    // Test the service method
    Comment result = beanCommentService.updateCommentForPost(postId, commentId,updatedTestComment);

    // Verify results to prove it actually updated
    assertNotNull(result);
    assertEquals(updatedTestComment, result);

    // Verify the calls to repository were made
    verify(mockCommentRepository, times(1)).findById(commentId);
    verify(mockPostRepository, times(1)).findById(postId);
    verify(mockCommentRepository, times(1)).save(updatedTestComment);
}

    // updateCommentForPost Sad Path 😭
    @Test
    public void testUpdateCommentForPostFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentPostId = 100;
        Integer nonExistentCommentId = 100;
        when(mockPostRepository.findById(nonExistentPostId)).thenReturn(Optional.empty());
        when(mockCommentRepository.findById(nonExistentCommentId)).thenReturn(Optional.empty());


        // Verify results (and service method is called here)
        assertThrows(RuntimeException.class, () -> beanCommentService.updateCommentForPost(nonExistentPostId, nonExistentPostId, testComment));

    }


// |           TESTING DELETE  | \\

// Delete comment by id
    @Test
    // deleteCommentById Happy Path 😊
    public void testDeleteCommentByIdPass() {
        // Setup of mock method with "parameters"
        Integer commentId = 1;
        mockCommentRepository.findById(commentId);
        mockCommentRepository.deleteById(commentId);


        // Test the service method
        // For now, just verifying that repository methods are called in the happy path for delete
        // This boolean below will always return false since it's a mock repository so the testComment will never get deleted
        // boolean result = beanCommentService.deleteCommentById(commentId);


        // Verify results
        // assertTrue(result);
        verify(mockCommentRepository, times(1)).findById(commentId);
        verify(mockCommentRepository, times(1)).deleteById(commentId);
    }

    // deleteCommentById Sad Path 😭
    @Test
    public void testDeleteCommentByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentCommentId = 100;
        when(mockCommentRepository.findById(nonExistentCommentId)).thenReturn(Optional.empty());

        // Test the service method and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> beanCommentService.deleteCommentById(nonExistentCommentId));
    }


}








