package com.example.springreferallmain.repository;

import com.example.springreferallmain.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Integer> {
}
