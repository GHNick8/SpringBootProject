package com.adminnick.SpringBootProject.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adminnick.SpringBootProject.Models.Account;
import com.adminnick.SpringBootProject.Models.Post;
import com.adminnick.SpringBootProject.Services.AccountService;
import com.adminnick.SpringBootProject.Services.PostService;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        // Account registration possibility @SeedData 
        Account account001 = new Account();
        Account account002 = new Account();

        account001.setEmail("account001@techsphere.com");
        account001.setPassword("1234");
        account001.setFirstname("Admin");

        account002.setEmail("account002@techsphere.com");
        account002.setPassword("5678");
        account002.setFirstname("Mod");

        accountService.save(account001);
        accountService.save(account002);

        List<Post> posts = postService.getAll();
        if (posts.size() == 0) {
            Post post001 = new Post();
            post001.setTitle("Technology");
            post001.setBody("Perl & Cobol 85.");
            post001.setAccount(account001);
            postService.save(post001);

            Post post002 = new Post();
            post002.setTitle(("New Frameworks"));
            post002.setBody("2+ years experience.");
            post002.setAccount(account002);
            postService.save(post002);
        }
    }
}
