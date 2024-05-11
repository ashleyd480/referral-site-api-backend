package com.example.springreferallmain.repository;

import com.example.springreferallmain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer> {


  // Find comments tied to that post (by post id)
  // Note: my attribute is postId- hence the repetition of Post

  Optional <List<Comment>> findCommentByPostPostId (Integer postId);

}
