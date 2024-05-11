package com.example.springreferallmain.service;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.repository.IPostRepository;
import com.example.springreferallmain.repository.ITagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    ITagRepository iTagRepository;

    @Autowired
    IPostRepository iPostRepository;

    // ------- CREATE --------

    // Add a tag (user creates a tag)
    public Tag addTag(Tag tagToAdd) {
        return iTagRepository.save(tagToAdd);
    }


    // Create a list of tags for testing
    public List<Tag> addTags(List<Tag> tagsToAdd) {
        return iTagRepository.saveAll(tagsToAdd);
    }


    // Add a tag to post
    public Tag addTagToPost(Integer postId, Integer tagId) {
        // Let's find the post first and tag first
        // The tag must exist in our database aka have an id associated with it (also for joining with post down the line.)
        // If this tag doesn't exist, the user must first add a tag (see 1s service method), and then add tag to post.
        Post existingPost = iPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " does not exist."));

        Tag existingTag =  iTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag with id " + tagId + " not found"));


        // Add tag to the post's list of tags.
        List<Tag> existingPostTags = new ArrayList<>(existingPost.getTags());
         existingPostTags.add(existingTag);
        existingPost.setTags(existingPostTags);

        // Save the post with the updated tag
        // Post and tags have a many to many (aka bidirectional relationship) so when you add a tag to a post, you are also modifying the state of the post. Thus, you need to save the updated post with its newly added tags to the DB. (With this below, you don't need to set the tag to the post).
        iPostRepository.save(existingPost);

        // Return the tag that was added to the post
        return existingTag;
    }


    // ------- RETRIEVE --------
    // Get all tags

    public List<Tag> getAllTags() {
        return iTagRepository.findAll();
    }


    // Get posts associated with a tag
    // This way we can show a posts with that tag.
    public List<Post> getPostsByTagById (Integer tagId) {
        // Let's find the tag first
        Tag existingTag = iTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag with id " + tagId + " does not exist."));

        // Return the list of posts associated with the tag
        // This will allow the posts to still show regardless of that JSON ignore
        return existingTag.getPosts();

    }


    // ------- UPDATE--------
    // Update tag by id (i.e. user wants to edit tag he/she/they created)
    public Tag updateTagById(Integer tagId, Tag editedTag) {
        Tag existingTag = iTagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag with ID " + tagId + " does not exist."));
        existingTag.setTagName(editedTag.getTagName());
        existingTag.setTagDescription(editedTag.getTagDescription());
        // Save the updated tag to the database
        return iTagRepository.save(existingTag);

        }


    // ------- DELETE--------
    // Delete tag by id (if a user wants to delete a tag)
    public boolean deleteTagById(Integer tagId) {

        iTagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag with id " + tagId + " does not exist."));

        iTagRepository.deleteById(tagId);
        return !iTagRepository.findById(tagId).isPresent();

    }

}