package com.example.springreferallmain.repository;

import com.example.springreferallmain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository  extends JpaRepository<User, Integer> {
}
