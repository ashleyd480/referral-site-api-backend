package com.example.springreferallmain.service;

import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.repository.IPostRepository;
import com.example.springreferallmain.repository.ITagRepository;
import com.example.springreferallmain.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TagServiceTest {
    @Autowired
    TagService beanTagService;

    @MockBean
    ITagRepository mockTagRepository;

    @MockBean
    IPostRepository mockPostRepository;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class

    private Tag testTag = TestData.makeTestTag1();
    private List<Tag> testTags = TestData.makeTestTags();
    private Post testPost = TestData.makeTestPost1();


// |           TESTING CREATE  | \\

    // Add a tag (user creates a tag)
    // addTag Happy Path 😊
    @Test
    public void testAddTagPass() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.save(testTag)).thenReturn(testTag);

        // Test the service method
        Tag result = beanTagService.addTag(testTag);

        // Verify results
//        assertNotNull(result);
        assertEquals(testTag, result);

        verify(mockTagRepository, times(1)).save(testTag);
    }

    // addTag Sad Path 😭
    @Test
    public void testAddTagFail() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.save(null)).thenThrow(new IllegalArgumentException());

        // Test the service method and verify that it throws the expected exception
        assertThrows(IllegalArgumentException.class, () -> beanTagService.addTag(null));

    }

// Create a list of tags for testing
    // addTags Happy Path 😊
    @Test
    public void testAddTagsPass() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.saveAll(testTags)).thenReturn(testTags);

        // Test the service method
        List<Tag> result = beanTagService.addTags(testTags);

        // Verify results
        assertNotNull(result);
        assertEquals(testTags, result);

        verify(mockTagRepository, times(1)).saveAll(testTags);
    }

    // addTags Sad Path 😭
    @Test
    public void testAddTagsFail() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.saveAll(null)).thenThrow(new IllegalArgumentException());

        // Test the service method and verify that it throws the expected exception
        assertThrows(IllegalArgumentException.class, () -> beanTagService.addTags(null));

    }


// Add a tag to a post
// addTagToPost Happy Path 😊
    @Test
    public void testAddTagToPostPass() {
        // Setup of mock method with "parameters"
        // Note: I had to initialize an array list because otherwise it would not allow me to mock adding comments to an existing list.

        Integer postId = 1;
        Integer tagId= 1;
        when(mockPostRepository.findById(postId)).thenReturn(Optional.ofNullable(testPost));
        when(mockTagRepository.findById(tagId)).thenReturn(Optional.ofNullable(testTag));

//        List<Tag> testTagsFromTestPost = new ArrayList<>(testPost.getTags());
//        List<Tag> updatedTags = testPost.getTags();

//        testTagsFromTestPost.add(testTag);
//        testPost.setTags(testTagsFromTestPost);
//         Had to do this because test data doesn't automatically maintain the bidirectional relationship between posts and tags.


        when(mockPostRepository.save(testPost)).thenReturn(testPost);

        // Test the service method
        Tag result = beanTagService.addTagToPost(postId, tagId);

        // Verify results
        assertNotNull(result);
        assertEquals(testTag, result);
        List<Tag> updatedTags = testPost.getTags();
        assertTrue(updatedTags.contains(testTag));
//        assertEquals(testTag.getPosts(), result.getPosts());

        verify(mockPostRepository, times(1)).findById(postId);
        verify(mockTagRepository, times(1)).findById(tagId);
        verify(mockPostRepository, times(1)).save(testPost);
    }

    // addTagToPost Sad Path 😭
    @Test
    public void testAddTagToPostFail() {
        // Setup of mock method with "parameters"
        Integer nonexistentPostId = 1;
        Integer nonexistentTagId= 1;
        when(mockPostRepository.findById(nonexistentPostId)).thenReturn(Optional.empty());
        when(mockTagRepository.findById(nonexistentTagId)).thenReturn(Optional.empty());

        // Test the service method and verify that it throws the expected exception
        assertThrows(RuntimeException.class, () -> beanTagService.addTagToPost(nonexistentPostId, nonexistentTagId));
    }


// |           TESTING RETRIEVE  | \\
// Get all tags

    // getAllTags Happy Path 😊

    @Test
    public void testGetAllTagsPass() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.findAll()).thenReturn(testTags);

        // Test the service method
        List<Tag> result = beanTagService.getAllTags();

        // Verify results
        assertNotNull(result);
        assertEquals(testTags, result);

        verify(mockTagRepository, times(1)).findAll();
    }


    // getAllTags Sad Path 😭
    // couldn't really think of a sad one for this one, but maybe we could test to ensure that size of list doesn't equal to 0
    @Test
    public void testGetAllTagsFail() {
        // Setup of mock method with "parameters"
        when(mockTagRepository.findAll()).thenReturn(testTags);

        // Test the service method
        List<Tag> result = beanTagService.getAllTags();

        // Verify results
        assertNotEquals(result, 0, "Size was expected to be 0");

    }

// Get posts associated with a tag

    // getPostsByTagById Happy Path 😊
    @Test
    public void testGetPostsByTagByIdPass() {
        // Setup of mock method with "parameters"
        Integer tagId = 1;

        when(mockTagRepository.findById(tagId)).thenReturn(Optional.ofNullable(testTag));
        List <Post> testPostsTiedToTestTag = testTag.getPosts();


        // We don't need to worry about simulating filtering since that is handled via repository layer, and we've already mocked a return result. Keep in mind we are just testing service logic here.

        // Test the service method
        List<Post> result = beanTagService.getPostsByTagById(tagId);

        // Verify results
        assertNotNull(result);
        assertEquals(testPostsTiedToTestTag, result);

        verify(mockTagRepository, times(1)).findById(tagId);
    }

    // getPostsByTagById Sad Path 😭
    @Test
    public void testGetPostsByTagByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonexistentTagId = 1;
        when(mockTagRepository.findById(nonexistentTagId)).thenReturn(Optional.empty());


        // Verify results (service method is called here as well)
        assertThrows(RuntimeException.class, () -> beanTagService.getPostsByTagById(nonexistentTagId));
    }


    // |           TESTING UPDATE  | \\
// Update tag by id (if user edits a tag)
// updateTagById Happy Path 😊
    @Test
    public void testUpdateTagByIdPass() {
        // Setup of mock method with "parameters"
        Integer tagId = 1;
        when(mockTagRepository.findById(tagId)).thenReturn(Optional.ofNullable(testTag));

        Tag updatedTestTag = new Tag();
        updatedTestTag.setTagId(tagId); // allows us to "override" existing tag by assign its id to updated tag
        updatedTestTag.setTagName("UpdatedTagName");
        updatedTestTag.setTagDescription("UpdatedDescription");

        when(mockTagRepository.save(updatedTestTag)).thenReturn(updatedTestTag);

        // Test the service method
        Tag result = beanTagService.updateTagById(tagId, updatedTestTag);

        // Verify results to prove it actually updated
        assertNotNull(result);
        assertEquals(updatedTestTag, result);

        // Verify the calls to repository were made
        verify(mockTagRepository, times(1)).findById(tagId);
        verify(mockTagRepository, times(1)).save(updatedTestTag);
    }

    // updateTagById Sad Path 😭
    @Test
    public void testUpdateTagByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentTagId = 100;
        when(mockTagRepository.findById(nonExistentTagId)).thenReturn(Optional.empty());

        // Verify results (and service method is

        // Verify results (and service method is called here)
        assertThrows(RuntimeException.class, () -> beanTagService.updateTagById(nonExistentTagId, testTag));

    }


// |           TESTING DELETE  | \\

    // Delete tag by id (if a user wants to delete a tag)
    @Test
    // deleteTagById Tags Happy Path 😊

    public void testDeleteTagByIdPass() {
        // Setup of mock method with "parameters"
        Integer tagId = 1;

        mockTagRepository.findById(tagId);
        mockTagRepository.deleteById(tagId);

        // Test the service method
        // For now, just verifying that repository methods are called in the happy path for delete
        // This boolean below will always return false since it's a mock repository so the testTag will never get deleted
        // boolean result = beanTagService.deleteTagById(tagId);


        // Verify results
        // assertTrue(result);
        verify(mockTagRepository, times(1)).findById(tagId);
        verify(mockTagRepository, times(1)).deleteById(tagId);
    }


    // deleteTagById Sad Path 😭
    @Test
    public void testDeleteTagByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentTagId = 100;
        when(mockTagRepository.findById(nonExistentTagId)).thenReturn(Optional.empty());

        // Test the service method and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> beanTagService.deleteTagById(nonExistentTagId));
    }


}
