package com.adminnick.SpringBootProject.Controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.adminnick.SpringBootProject.Models.Account;
import com.adminnick.SpringBootProject.Services.AccountService;
import com.adminnick.SpringBootProject.Util.AppUtil;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    // password token timeout set at 15 minutes 
    private static final int password_token_timeout = 15;

    @GetMapping("/register")
    public String register(Model model) {       
        Account account = new Account();
        model.addAttribute("account", account);
        return "account_views/register";
    }

    @PostMapping("/register")
    public String register_user(@Valid @ModelAttribute Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "account_views/register";
        }
        accountService.save(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "account_views/login";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model, Principal principal) {
        String authUser = "email";
        if(principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            model.addAttribute("account", account);
            model.addAttribute("photo", account.getPhoto());
            return "account_views/profile";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String post_profile(@Valid @ModelAttribute Account account, BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return "account_views/profile";
        }
        String authUser = "email";
        if(principal != null) {
            authUser = principal.getName();
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);
        if(optionalAccount.isPresent()) {
            Account account_by_id = accountService.findById(account.getId()).get();
            account_by_id.setAge(account.getAge());
            account_by_id.setDate_of_birth(account.getDate_of_birth());
            account_by_id.setFirstname(account.getFirstname());
            account_by_id.setGender(account.getGender());
            account_by_id.setLastname(account.getLastname());
            account_by_id.setPassword(account.getPassword());

            accountService.save(account_by_id);
            SecurityContextHolder.clearContext();
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/update_photo")
    @PreAuthorize("isAuthenticated()")
    public String update_photo(@RequestParam("file") MultipartFile file,
        RedirectAttributes attributes, Principal principal) {
        
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "No file uploaded");
            return "redirect:/profile";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Generate a unique filename
            RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z') 
                .filteredBy(Character::isLetterOrDigit) 
                .get(); 

            String generatedString = generator.generate(10);
            String final_photo_name = generatedString + fileName;
            
            // Get file storage path
            String absolute_fileLocation = AppUtil.get_upload_path(final_photo_name);
            System.out.println("Uploading file to: " + absolute_fileLocation); // Debugging log

            Path path = Paths.get(absolute_fileLocation);

            // Ensure the "uploads" directory exists before writing the file
            Path uploadDirectory = Paths.get("uploads");
            if (!Files.exists(uploadDirectory)) {
                try {
                    Files.createDirectories(uploadDirectory);
                    System.out.println("Created missing directory: " + uploadDirectory.toAbsolutePath());
                } catch (Exception e) {
                    System.out.println("ERROR: Failed to create uploads directory!");
                    e.printStackTrace();
                    attributes.addFlashAttribute("error", "Failed to create upload directory.");
                    return "redirect:/profile?error";
                }
            }

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File successfully saved at: " + absolute_fileLocation); // Debugging log

            attributes.addFlashAttribute("message", "You successfully uploaded");

            // Fetch the authenticated user
            String authUser = principal != null ? principal.getName() : "email";
            Optional<Account> optionalAccount = accountService.findOneByEmail(authUser);

            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                
                // Ensure account exists before updating
                Optional<Account> optionalAccountById = accountService.findById(account.getId());
                if (!optionalAccountById.isPresent()) {
                    attributes.addFlashAttribute("error", "Account not found!");
                    return "redirect:/profile?error";
                }

                Account account_by_id = optionalAccountById.get();
                String relative_fileLocation = "/uploads/" + final_photo_name;
                account_by_id.setPhoto(relative_fileLocation);
                accountService.save(account_by_id);

                System.out.println("Updated database with new profile picture: " + relative_fileLocation);
            } else {
                attributes.addFlashAttribute("error", "User not found!");
                return "redirect:/profile?error";
            }

            return "redirect:/profile";

        } catch (Exception e) {
            e.printStackTrace(); //Log error
            attributes.addFlashAttribute("error", "File upload failed: " + e.getMessage());
            return "redirect:/profile?error";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        return "account_views/forgot-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("email") String email, RedirectAttributes attributes) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            String resetToken = UUID.randomUUID().toString();
            account.setPasswordResetToken(resetToken);
            account.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(password_token_timeout));
            accountService.save(account);
            
            attributes.addFlashAttribute("message", "Password reset email sent");
            return "redirect:/reset-password-form?token=" + resetToken;
        } else {
            attributes.addFlashAttribute("error", "No user found with the email supplied");
            return "redirect:/forgot-password";
        }
    }

    @GetMapping("/reset-password-form")
    public String reset_password_form(@RequestParam("token") String token, Model model) {
        Optional<Account> optionalAccount = accountService.findByResetToken(token);
        if (optionalAccount.isPresent()) {
            model.addAttribute("token", token);
            return "account_views/reset-password";
        } else {
            return "redirect:/forgot-password?error=invalid-token";
        }
    }
}
