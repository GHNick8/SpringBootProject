package com.adminnick.SpringBootProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminnick.SpringBootProject.Models.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
} 
