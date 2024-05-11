package com.example.springreferallmain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer tagId;
    private String tagName;
    private String tagDescription;

    @JsonIgnore
    @ManyToMany(mappedBy= "tags")
    private List<Post> posts = new ArrayList<>();
}



// ------- Many to Many with Posts --------
// A tag can be used on multiple posts
// @JsonIgnore: b/c we don't need to see the posts when we display tags, but rather it's more helpful to see the tags when we query posts.
// (Note: JSON ignore does not affect me querying posts by tagId since I use the logic of "return existingTag.getPosts();" in my service method)
//mappedBy: Post entity is the owning side. Users create posts and then tags are associated with that post. The list of posts is mapped to the tag via the tagId in join table.