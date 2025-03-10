package com.adminnick.SpringBootProject.Config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.adminnick.SpringBootProject.Models.Account;
import com.adminnick.SpringBootProject.Models.Authority;
import com.adminnick.SpringBootProject.Models.Post;
import com.adminnick.SpringBootProject.Services.AccountService;
import com.adminnick.SpringBootProject.Services.AuthorityService;
import com.adminnick.SpringBootProject.Services.PostService;
import com.adminnick.SpringBootProject.Util.Constants.Privillages;
import com.adminnick.SpringBootProject.Util.Constants.Roles;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public void run(String... args) throws Exception {
        // Account registration possibility @SeedData 

        for(Privillages auth: Privillages.values()) {
            Authority authority = new Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getPrivillage());
            authorityService.save(authority);
        }

        Account account001 = new Account();
        Account account002 = new Account();
        Account account003 = new Account();
        Account account004 = new Account();

        account001.setEmail("account001@techsphere.com");
        account001.setPassword("pass1234");
        account001.setFirstname("Admin");
        account001.setLastname("Nick");
        account001.setAge(32);
        account001.setDate_of_birth(LocalDate.parse("1992-08-21"));
        account001.setGender("Male");
        account001.setRole(Roles.ADMIN.getRole());

        Set<Authority> authorities = new HashSet<>();
        authorityService.findById(Privillages.ACCESS_ADMIN_PANEL.getId()).ifPresent(authorities::add);
        authorityService.findById(Privillages.RESET_ANY_USER_PASSWORD.getId()).ifPresent(authorities::add);
        account001.setAuthorities(authorities);

        account002.setEmail("account002@techsphere.com");
        account002.setPassword("pass5678");
        account002.setFirstname("Editor");
        account002.setLastname("James");
        account002.setAge(26);
        account002.setDate_of_birth(LocalDate.parse("1999-01-01"));
        account002.setGender("Male");
        account002.setRole(Roles.EDITOR.getRole());
        
        account003.setEmail("account003@techsphere.com");
        account003.setPassword("pass7878");
        account003.setFirstname("User");
        account003.setLastname("Jefferson");
        account003.setAge(26);
        account003.setDate_of_birth(LocalDate.parse("1999-01-01"));
        account003.setGender("Male");
        account003.setRole(Roles.USER.getRole());

        account004.setEmail("account004@techsphere.com");
        account004.setPassword("pass4545");
        account004.setFirstname("Jockey");
        account004.setLastname("Blunt");
        account004.setAge(26);
        account004.setDate_of_birth(LocalDate.parse("1999-01-01"));
        account004.setGender("Male");
        account004.setRole(Roles.USER.getRole());

        accountService.save(account001);
        accountService.save(account002);
        accountService.save(account003);
        accountService.save(account004);

        List<Post> posts = postService.getAll();
        if (posts.size() == 0) {
            Post post001 = new Post();
            post001.setTitle("Perl & COBOL 85: Bridging the Gap Between Legacy and Modern Programming");
            post001.setBody("...");
            post001.setAccount(account001);
            postService.save(post001);

            Post post002 = new Post();
            post002.setTitle(("New Frameworks"));
            post002.setBody("<h4>2+ years experience.</h4>");
            post002.setAccount(account002);
            postService.save(post002);

            Post post003 = new Post();
            post003.setTitle(("Technology advanced"));
            post003.setBody("New ways of improving UI.");
            post003.setAccount(account002);
            postService.save(post003);

            Post post004 = new Post();
            post004.setTitle(("Technology advanced pt.2"));
            post004.setBody("New ways of improving UI and UX.");
            post004.setAccount(account002);
            postService.save(post004);

            Post post005 = new Post();
            post005.setTitle(("PERL 6"));
            post005.setBody("The perl empire strikes back!");
            post005.setAccount(account001);
            postService.save(post005);
        }
    }
}
