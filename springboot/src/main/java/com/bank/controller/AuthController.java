package com.bank.controller;

import com.bank.dto.JwtRequestDto;
import com.bank.dto.JwtResponseDto;
import com.bank.jwt.JwtTokenUtil;
import com.bank.model.BankAccount;
import com.bank.model.User;
import com.bank.repository.UserRepository;
import com.bank.service.JwtUserDetailsService;

import jakarta.annotation.PostConstruct;

import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private static final String ADMIN_EMAIL = "admin@bank.com";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_ROLE = "ADMIN";
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) throws Exception {

       Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
        );
       SecurityContextHolder.getContext().setAuthentication(authentication);
       
        final UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        final String token = jwtTokenUtil.generateToken(userDetails);
        System.out.println(userDetails.getAuthorities().toString());
        return ResponseEntity.ok(new JwtResponseDto(token).getToken());
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
        int userId = userDetailsService.registerUser(user, user.getPassword());

        if (userId != -1) {
            String accountNumber = user.getBankAccounts().iterator().next().getAccountNumber();
            return ResponseEntity.ok("User registered successfully with accountNumber: " + accountNumber);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("User registration failed.");
        }
    }
    
    @PostMapping("/uploadPicture/{username}")
    public ResponseEntity<String> uploadPicture(@PathVariable String username, @RequestParam("picture") MultipartFile pictureFile) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            user.setPicture(pictureFile.getBytes());
            userRepository.save(user);
            return ResponseEntity.ok("Picture uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload picture");
        }
    }

    @GetMapping(value = "/getPicture/{username}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPicture(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        byte[] picture = user.getPicture();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(picture);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserWithAccounts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); 
        String username = jwtTokenUtil.extractUsername(jwtToken);

        User user = userRepository.findByUsername(username);

        if (user != null) {
            String accountNumber = user.getBankAccounts().stream()
                                       .findFirst()
                                       .map(BankAccount::getAccountNumber)
                                       .orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("phoneNumber", user.getPhoneNumber());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            response.put("picture", user.getPicture());
            response.put("accountNumber", accountNumber);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
    
    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin() {
        User adminUser = userDetailsService.findByUsername(ADMIN_USERNAME);
        if (adminUser == null) {
            User newAdmin = new User();
            newAdmin.setUsername(ADMIN_USERNAME);
            newAdmin.setEmail(ADMIN_EMAIL);
            newAdmin.setPassword(ADMIN_PASSWORD); 
            newAdmin.setStatus("ACTIVE");

            userDetailsService.saveAdmin(newAdmin);

            return ResponseEntity.ok("Admin user created successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin user already exists.");
        }
    }


    @PostMapping("/loginAdmin")
    public ResponseEntity<String> loginAdmin(@RequestBody JwtRequestDto authenticationRequest) {
    
        if (ADMIN_USERNAME.equals(authenticationRequest.getUsername()) &&
            ADMIN_PASSWORD.equals(authenticationRequest.getPassword())) {
            
            UserDetails adminDetails = new org.springframework.security.core.userdetails.User(
                ADMIN_USERNAME, 
                passwordEncoder.encode(ADMIN_PASSWORD), 
                Collections.singletonList(new SimpleGrantedAuthority(ADMIN_ROLE))
            );
            
            final String token = jwtTokenUtil.generateToken(adminDetails);
            
            return ResponseEntity.ok(new JwtResponseDto(token).getToken());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials.");
        }
    }
    
    @PostConstruct
    public void init() {
       
        createAdmin(); 
    }
}
