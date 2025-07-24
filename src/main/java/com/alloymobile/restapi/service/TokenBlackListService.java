package com.alloymobile.restapi.service;

import java.util.Set;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet(); // In-memory version

    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
