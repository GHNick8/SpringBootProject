package com.adminnick.SpringBootProject.Services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.adminnick.SpringBootProject.Models.Account;
import com.adminnick.SpringBootProject.Models.Authority;
import com.adminnick.SpringBootProject.Repository.AccountRepository;
import com.adminnick.SpringBootProject.Util.AppUtil;
import com.adminnick.SpringBootProject.Util.Constants.Roles;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByResetToken(String resetToken) {
        return accountRepository.findByPasswordResetToken(resetToken);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole() == null) {
            account.setRole(Roles.USER.getRole());
        }
        if (account.getPhoto() == null) {
            // default picture
            account.setPhoto("/uploads/Anon.png");
        }        
        return accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findOneByEmailIgnoreCase(username);
        if(!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }
        Account account = optionalAccount.get();

        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));

        Set<Authority> authorities = account.getAuthorities();
        for (Authority _auth: authorities) {
            grantedAuthority.add(new SimpleGrantedAuthority(_auth.getName()));
        }

        return new User(account.getEmail(), account.getPassword(), grantedAuthority);
    }

    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public boolean handleProfilePhotoUpload(MultipartFile file, String email) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            RandomStringGenerator generator = new RandomStringGenerator.Builder()
                    .withinRange('0', 'z')
                    .filteredBy(Character::isLetterOrDigit)
                    .get();
            String uniqueString = generator.generate(10);
            String finalPhotoName = uniqueString + fileName;

            // Check where the file should be saved
            String absoluteFilePath = AppUtil.get_upload_path(finalPhotoName);
            System.out.println("Saving file to: " + absoluteFilePath); // Debugging Line

            Path path = Paths.get(absoluteFilePath);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Verify if file was actually written
            if (Files.exists(path)) {
                System.out.println("File successfully saved: " + absoluteFilePath);
            } else {
                System.out.println("ERROR: File was NOT saved!");
            }

            // Update the database entry
            Optional<Account> optionalAccount = findOneByEmail(email);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                String relativeFilePath = "/uploads/" + finalPhotoName;
                System.out.println("Updating account with photo path: " + relativeFilePath); // Debugging Line
                account.setPhoto(relativeFilePath);
                save(account);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: File upload failed.");
        }

        return false;
    }
}
