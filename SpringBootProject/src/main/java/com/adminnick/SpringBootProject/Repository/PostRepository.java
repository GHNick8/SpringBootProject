package com.adminnick.SpringBootProject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adminnick.SpringBootProject.Models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    
}
