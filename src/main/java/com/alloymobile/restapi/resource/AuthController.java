package com.alloymobile.restapi.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.alloymobile.restapi.service.TokenBlackListService;
import com.alloymobile.restapi.service.UsersService;
import com.alloymobile.restapi.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest,
            HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
            return new AuthResponse(false,
                    "Incorrect username or password",
                    null);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        jwtUtil.setJwtInCookie(response, token);

        return new AuthResponse(true,
                "Authentication successful",
                jwtUtil.extractUsername(token));
    }

    @PostMapping("/extend")
    public AuthResponse extendAuthenticationToken(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            String token = jwtUtil.extractTokenFromCookies(request);
            String username = jwtUtil.extractUsername(token);
            // Blacklist the existing token
            tokenBlacklistService.blacklistToken(token);

            // Generate a new token and set it in the cookie
            String newToken = jwtUtil.generateToken(username);
            jwtUtil.setJwtInCookie(response, newToken);
            return new AuthResponse(true,
                    "Authentication extended successfully",
                    username);

        } catch (AuthenticationException e) {
            return new AuthResponse(false,
                    "Session can not be extended",
                    null);
        }
    }

    @PostMapping("/signout")
    public SignOutResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.extractTokenFromCookies(request);
        tokenBlacklistService.blacklistToken(token);
        jwtUtil.clearJwtCookie(response);
        return new SignOutResponse(true,
                "Logged out successfully");
    }

    @GetMapping("/istokenexpired")
    public Boolean isTokenExpired(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookies(request);
        boolean isExpired = jwtUtil.isTokenExpired(token);
        return isExpired;
    }

    @GetMapping("/gettimeout")
    public Integer getTimeout() {
        return jwtUtil.getTimeoutSeconds();
    }
}

class AuthResponse {
    private boolean success;
    private String message;
    private String username;

    public AuthResponse(boolean success,
            String message,
            String username) {
        this.success = success;
        this.message = message;
        this.username = username;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
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