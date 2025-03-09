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
            post001.setBody("In the ever-evolving world of programming, where languages like Python, Java, and JavaScript dominate, there exist two powerful yet vastly different languages that have made a significant impact: Perl and COBOL 85. While one is a scripting powerhouse known for its text manipulation capabilities, the other has been the backbone of business applications for decades. Let’s dive into their history, uses, and how they continue to play a role in modern computing.\n" + //
                                "\n" + //
                                "Perl: The Swiss Army Knife of Scripting\n" + //
                                "\n" + //
                                "Perl, created by Larry Wall in 1987, was initially designed for text processing, but it quickly grew into a general-purpose scripting language. It gained popularity in the 1990s due to its powerful regular expressions, ease of integration with databases, and ability to handle various system administration tasks.\n" + //
                                "\n" + //
                                "Key Features of Perl:\n" + //
                                "\n" + //
                                "Text Processing: Excellent at pattern matching and data extraction.\n" + //
                                "\n" + //
                                "CPAN (Comprehensive Perl Archive Network): A vast collection of libraries for various functionalities.\n" + //
                                "\n" + //
                                "Flexibility: Supports multiple programming paradigms (procedural, object-oriented, and functional).\n" + //
                                "\n" + //
                                "Web Development: Used extensively for CGI scripts and early web applications.\n" + //
                                "\n" + //
                                "Automation & Scripting: Still widely used in system administration and DevOps.\n" + //
                                "\n" + //
                                "COBOL 85: The Business Giant That Won’t Die\n" + //
                                "\n" + //
                                "COBOL (Common Business-Oriented Language) was first introduced in 1959, but the COBOL 85 standard became a major milestone, improving the language’s capabilities. It is widely used in the financial sector, government agencies, and large-scale enterprise applications.\n" + //
                                "\n" + //
                                "Key Features of COBOL 85:\n" + //
                                "\n" + //
                                "Human-Readable Syntax: Designed to be understandable by non-programmers.\n" + //
                                "\n" + //
                                "Business-Oriented: Ideal for handling banking, insurance, and payroll systems.\n" + //
                                "\n" + //
                                "High Stability: Many legacy applications built in COBOL 85 are still running today.\n" + //
                                "\n" + //
                                "Batch Processing: Efficient for handling large-scale data transactions.\n" + //
                                "\n" + //
                                "Mainframe Compatibility: Runs on IBM mainframes and modern cloud-based architectures.\n" + //
                                "\n" + //
                                "Why Are Perl & COBOL 85 Still Relevant?\n" + //
                                "\n" + //
                                "Despite their age, both Perl and COBOL 85 continue to be used in specific industries where their unique strengths shine:\n" + //
                                "\n" + //
                                "Legacy Systems: Many financial institutions and government bodies still rely on COBOL 85 for critical infrastructure.\n" + //
                                "\n" + //
                                "Data Processing & ETL Pipelines: Perl is still utilized in UNIX/Linux environments for log parsing, automation, and DevOps scripts.\n" + //
                                "\n" + //
                                "Interoperability: COBOL applications are now being modernized to work with cloud computing, and Perl still finds its place in backend processing.\n" + //
                                "\n" + //
                                "The Future: Modernization & Integration\n" + //
                                "\n" + //
                                "While COBOL 85 developers are in high demand due to the need to maintain legacy systems, efforts are underway to integrate COBOL with modern technologies like cloud computing and microservices. On the other hand, Perl’s popularity has declined, but it remains an excellent tool for text manipulation and automation in DevOps environments.\n" + //
                                "\n" + //
                                "Organizations that depend on COBOL are now adopting COBOL modernization tools, such as converting COBOL code to Java or integrating COBOL with REST APIs. Meanwhile, Perl is being used alongside Python and Shell scripting for automation tasks.\n" + //
                                "\n" + //
                                "Conclusion\n" + //
                                "\n" + //
                                "Both Perl and COBOL 85 have stood the test of time, proving their resilience in an industry that rapidly evolves. While they may not be the first choice for new projects, they continue to power mission-critical applications. Whether you’re maintaining legacy banking systems in COBOL or automating tasks with Perl, understanding these languages can be a valuable skill in the tech world.\n" + //
                                "\n" + //
                                "Are you currently working with Perl or COBOL? Share your experiences in the comments!");
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
