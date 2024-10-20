package com.example.bookstorefull.repository;

import com.example.bookstorefull.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
    Authority findByUserAuthority(String userAuthority);
}
