package ru.khav.NewsPaper.models;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class BlackListTokens {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void addBlackListToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}

