package com.example.springreferallmain.repository;

import com.example.springreferallmain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Integer> {
}
