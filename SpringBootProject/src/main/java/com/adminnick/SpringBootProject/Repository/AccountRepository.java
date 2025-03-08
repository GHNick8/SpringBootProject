package com.adminnick.SpringBootProject.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminnick.SpringBootProject.Models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findOneByEmailIgnoreCase(String email);
    Optional<Account> findByToken(String token);
} 
