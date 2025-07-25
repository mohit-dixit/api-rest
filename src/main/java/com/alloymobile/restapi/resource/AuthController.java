package com.alloymobile.restapi.resource;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.alloymobile.restapi.service.TokenBlackListService;
import com.alloymobile.restapi.service.UsersService;
import com.alloymobile.restapi.util.JwtUtil;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService userDetailsService;

    private final TokenBlackListService tokenBlacklistService;

    public AuthController(TokenBlackListService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/authenticate")
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
                return new AuthResponse(false,
                "Incorrect username or password",
                null,
                null,
                null);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthResponse(true,
                "Authentication successful",
                token,
                jwtUtil.extractUsername(token),
                jwtUtil.extractTokenExpiry(token));
    }

    @PostMapping("/extend")
    public AuthResponse extendAuthenticationToken(@RequestBody ExtendRequest extendRequest, @RequestHeader("Authorization") String authHeader) throws Exception {
        try {
            String token = authHeader.replace("Bearer ", "");
            jwtUtil.validateToken(token, extendRequest.getUsername());
            tokenBlacklistService.blacklistToken(token);
        } catch (AuthenticationException e) {
               return new AuthResponse(false,
                "Session can not be extended",
                null,
                null,
                null);
        }
        String token = jwtUtil.generateToken(extendRequest.getUsername());
        return new AuthResponse(true,
                "Authentication extended successfully",
                token,
                jwtUtil.extractUsername(token),
                jwtUtil.extractTokenExpiry(token));
    }

    @PostMapping("/signout")
    public SignOutResponse logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        tokenBlacklistService.blacklistToken(token);
        return new SignOutResponse(true,
                "Logged out successfully");
    }
}

class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String username;
    private Date tokenExpiry;

    public AuthResponse(boolean success,
            String message,
            String token,
            String username,
            Date tokenExpiry) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.username = username;
        this.tokenExpiry = tokenExpiry;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Date getTokenExpiry() {
        return tokenExpiry;
    }
}

class AuthRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class ExtendRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class SignOutResponse {
    private boolean success;
    private String message;

    public SignOutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}