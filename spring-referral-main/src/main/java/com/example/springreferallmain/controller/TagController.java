package com.example.springreferallmain.controller;

import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    TagService tagService;

    //Note: Post is the owning entity side so that is why most tag methods are mapped from Post.


    // ------- CREATE --------

    // Add a tag (user creates a tag)
    // Tag must have a tagName and tagDescription

    @PostMapping
    public ResponseEntity<?> addTag(@RequestBody Tag tagToAdd) {

        if (tagToAdd == null || tagToAdd.getTagName().isEmpty() || tagToAdd.getTagDescription().isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null tag provided. You must provide tag name and description");
        }
        else {
            Tag newTagCreated = tagService.addTag (tagToAdd);
            return new ResponseEntity<>(newTagCreated, HttpStatus.CREATED);
        }
    }

    // Create a list of tags for testing

    @PostMapping("bulk")
    public ResponseEntity<?> addTags(@RequestBody List<Tag> tagsToAdd) {
        if (tagsToAdd.isEmpty()) {
            return ResponseEntity.badRequest().body("Tsk tsk. Null tags provided");
        }

        List<Tag> newTagsCreated = tagService.addTags(tagsToAdd);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTagsCreated);
    }

    // Add tag to a post
    @PostMapping("/{tagId}/posts/{postId}")
    public ResponseEntity<?> addTagToPost(@PathVariable Integer postId, @PathVariable Integer tagId) {
        Tag tagAddedToPost = tagService.addTagToPost(postId, tagId);
        if (tagAddedToPost == null) {
            return ResponseEntity.badRequest().body("Tsk tsk. Null tag provided");
        }
        return new ResponseEntity<>(tagAddedToPost, HttpStatus.CREATED);
    }



    // ------- RETRIEVE --------
    // Get all tags (display tag options to user)
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> listOfTagsOptions = tagService.getAllTags();
        if (listOfTagsOptions == null || listOfTagsOptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listOfTagsOptions);
    }

    // Get posts associated with a tag
    // This way we can show a posts with that tag.
    // Note: I realize after the fact, I probably could have put this in tag controller, however since this still technically has to do with tags and since I'm still querying from the iTagRepository with tag.getPosts()...leaving it alone for now as project is almost due. Sowwy :(
    @GetMapping("{tagId}/posts")

    public ResponseEntity<List<Post>> getPostsWithTagId(@PathVariable Integer tagId) {
        List <Post> postsWithTagId = tagService.getPostsByTagById(tagId);
        if (postsWithTagId  == null || postsWithTagId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postsWithTagId);
    }

    // ------- UPDATE--------
    // Update tag by id (i.e. user wants to edit tag he/she/they created)
    // Tag must have a tagName and tagDescription
    @PutMapping("/{tagId}")
    public ResponseEntity<?> updateTagById(@PathVariable Integer tagId, @RequestBody Tag editedTag) {
        Tag updatedTag = tagService.updateTagById(tagId, editedTag);
        if (updatedTag == null || editedTag.getTagName().isEmpty() || editedTag.getTagDescription().isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null tag update. You must provide tag name and description");
        }
        return ResponseEntity.ok(updatedTag);
    }


    // ------- DELETE--------
    // Delete tag by id
    @DeleteMapping("{tagId}")
    public ResponseEntity<?> deleteTagById(@PathVariable Integer tagId) {
        boolean deleted = tagService.deleteTagById(tagId);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body("Tag deleted successfully.");
    }
}
