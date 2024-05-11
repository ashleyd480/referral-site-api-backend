package com.example.springreferallmain.testdata;

import com.example.springreferallmain.model.*;

import java.util.Arrays;
import java.util.List;

public class TestData {

// 👋 Hello human: I have placed test data that I'm using in my tests here for SOC and to keep things DRY. Teachers love DRY SOCs ;). We then can reference the return value of these functions in their respective classes with a private variable. Private because... remember with testing, we test one class/layer at a time so those variable should be private to that class/layer.
// For each of the 5 categories of test data, I've created private variables. This way, in this class,I can set the user of a test post, or the address of a  test user, or even the tags/comments of a test post. This keeps the methods clean.
// The method for how those variables are produced are defined underneath each set of variables respectively.
// I've also created methods to simulate "empty" instances when sad path testing empty request body for Controller methods.

    // ----------- 💬 TEST COMMENT DATA 💬 -----------
    // creating this, so we can test POST and PUT methods

    private static Comment testComment1 = makeTestComment1();

    private static Comment testComment2 = makeTestComment2();

    private static List<Comment> testComments= makeTestComments();


    // Create a test comment for testing
    public static Comment makeTestComment1() {
        Comment testComment1 = new Comment();
        testComment1.setCommentId(1);
        testComment1.setCommentText("Testing. I want cheetos.");
        testComment1.setNumberLikesOnComment(0);
        testComment1.setCommentedByUsername("testUser");
        return testComment1;
    }

    public static Comment makeTestComment2() {
        Comment testComment2 = new Comment();
        testComment2.setCommentId(2);
        testComment2.setCommentText("Testing. I want boba.");
        testComment2.setNumberLikesOnComment(0);
        testComment2.setCommentedByUsername("testUser2");
        return testComment2;

    }

    // Create an empty test comment for testing

    public static Comment makeEmptyTestComment() {
        Comment emptyComment = new Comment();
        emptyComment.setCommentId(1);
        emptyComment.setCommentText("");
        emptyComment.setNumberLikesOnComment(0);
        emptyComment.setCommentedByUsername("testUser");
        return emptyComment;
    }


    // Create multiple test comments for testing
    public static List<Comment> makeTestComments() {
        // Return a list of test comments
        return Arrays.asList(testComment1, testComment2);

    }

    // ----------- 🏠  TEST ADDRESS DATA 🏠 -----------

    private static Address testAddress1 = makeTestAddress1();
    private static Address testAddress2 = makeTestAddress2();

    public static Address makeTestAddress1() {
        // Create an address for testUser1
        Address address1 = new Address();
        address1.setAddressId(1);
        address1.setStreet("123 Main St");
        address1.setCity("City");
        address1.setState("State");
        address1.setZipCode("12345");
        address1.setCountry("USA");
        return address1;

    }

    public static Address makeTestAddress2() {
        // Create an address for testUser2
        Address address2 = new Address();
        address2.setAddressId(2);
        address2.setStreet("456 Elm St");
        address2.setCity("City");
        address2.setState("State");
        address2.setZipCode("67890");
        address2.setCountry("USA");
        return address2;
    }

    public static Address makeTestEmptyAddress() {
        // Create an empty address
        Address testEmptyAddress = new Address();
        testEmptyAddress.setAddressId(3);
        testEmptyAddress.setStreet(""); // Set street as empty
        testEmptyAddress.setCity("City");
        testEmptyAddress.setState("State");
        testEmptyAddress.setZipCode("67890");
        testEmptyAddress.setCountry("USA");
        return testEmptyAddress;
    }


    // ----------- 👤  TEST USER DATA 👤-----------

    // Variables to represent Test Users and methods below to define them

    public static User testUser1 = makeTestUser1();
    public static User testUser2 = makeTestUser2();

    public static User makeTestUser1() {
        User testUser1 = new User();
        testUser1.setUserId(1);
        testUser1.setUsername("user1");
        testUser1.setEmail("user1@example.com");
        testUser1.setPassword("password1");
        testUser1.setProfilePictureURL("https://example.com/profile1.jpg");
        testUser1.setUserBio("Bio for user 1");

        // Set the address for testUser1
        testUser1.setAddress(testAddress1);

        return testUser1;
    }

    public static User makeTestUser2() {
        User testUser2 = new User();
        testUser2.setUserId(2);
        testUser2.setUsername("user2");
        testUser2.setEmail("user2@example.com");
        testUser2.setPassword("password2");
        testUser2.setProfilePictureURL("https://example.com/profile1.jpg");
        testUser2.setUserBio("Bio for user 2");


        // Set the address for testUser2
        testUser2.setAddress(testAddress2);

        return testUser2;
    }

    public static User makeEmptyTestUser() {
        // simulating a user with some fields missing info
            User emptyTestUser = new User();
            emptyTestUser.setUserId(3);
            emptyTestUser.setUsername("");
            emptyTestUser.setEmail("");
            emptyTestUser.setPassword("password3");
            emptyTestUser.setProfilePictureURL("https://example.com/profile3.jpg");
            emptyTestUser.setUserBio("Bio for user 3");
            emptyTestUser.setAddress(new Address());

            return emptyTestUser;
        }


    public static List<User> makeTestUsers() {
        return Arrays.asList(testUser1, testUser2);
    }


    // ----------- ✏️  TEST POST DATA ✏️ -----------
    // creating some posts that are tied to our users above


    public static Post makeTestPost1() {
        // Create a test post
        Post testPost1 = new Post();
        testPost1.setPostId(1);
        testPost1.setPostTitle("Test Post");
        testPost1.setPostContent("This is a test post about jobs.");
        testPost1.setNumberLikesOnPost(0);
        testPost1.setPostMediaURL("https://example.com");

        //Remember to set testUser to testPost

        testPost1.setUser(testUser1);
        testPost1.setComments(testComments);
        testPost1.setTags(testTags);

        return testPost1;
    }

    public static Post makeTestPost2() {
        // Create a test post
        Post testPost2 = new Post();
        testPost2.setPostId(2); //
        testPost2.setPostTitle("Test Post");
        testPost2.setPostContent("This is a test post about jobs and nunya business.");
        testPost2.setNumberLikesOnPost(10);
        testPost2.setPostMediaURL("https://example.com");

        //Remember to set testUser to testPost
        testPost2.setUser(testUser2);

        return testPost2;
    }

    public static Post makeEmptyTestPost() {
        // Creating an empty test post with some fields missing info
        Post emptyTestPost = new Post();
        emptyTestPost.setPostId(3);
        emptyTestPost.setPostTitle("");
        emptyTestPost.setPostContent("");
        emptyTestPost.setNumberLikesOnPost(0);
        emptyTestPost.setPostMediaURL("");
        emptyTestPost.setUser(new User());

        return emptyTestPost;
    }


    public static List<Post> makeTestPosts() {
        return Arrays.asList(makeTestPost1(), makeTestPost2());
    }


    // ----------- #️⃣   TEST TAG DATA #️⃣  -----------

    private static List<Tag> testTags = makeTestTags();


    public static Tag makeTestTag1() {
        Tag testTag1 = new Tag();
        testTag1.setTagId(1);
        testTag1.setTagName("Test Tag 1");
        testTag1.setTagDescription("Test Description 1");
        return testTag1;
    }

    public static Tag makeTestTag2() {
        Tag testTag2 = new Tag();
        testTag2.setTagId(2);
        testTag2.setTagName("Test Tag 2");
        testTag2.setTagDescription("Test Description 2");
        return testTag2;
    }


    public static Tag makeEmptyTestTag() {
        // Creating an empty test tag with some fields missing info
        Tag emptyTestTag = new Tag();
        emptyTestTag.setTagId(3);
        emptyTestTag.setTagName("");
        emptyTestTag.setTagDescription("");

        return emptyTestTag;
    }


    public static List<Tag> makeTestTags() {
        return Arrays.asList(makeTestTag1(), makeTestTag2());
    }


}
