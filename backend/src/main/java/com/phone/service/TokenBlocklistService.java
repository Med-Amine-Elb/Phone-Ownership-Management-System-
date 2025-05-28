package com.phone.service;

import com.phone.model.BlocklistedToken;
import com.phone.repository.BlocklistedTokenRepository;
import com.phone.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenBlocklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlocklistService.class);

    @Autowired
    private BlocklistedTokenRepository blocklistedTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public void blocklistToken(String token) {
        logger.info("Blocklisting token: {}", token);
        // Check if the token is already blocklisted to avoid duplicates
        Optional<BlocklistedToken> existingToken = blocklistedTokenRepository.findByToken(token);
        if (existingToken.isEmpty()) {
            Date expirationDate = jwtUtil.extractExpirationDate(token);
            BlocklistedToken blocklistedToken = new BlocklistedToken();
            blocklistedToken.setToken(token);
            blocklistedToken.setExpiryDate(expirationDate.toInstant());
            blocklistedTokenRepository.save(blocklistedToken);
            logger.info("Token saved to blocklist database: {}", token);
        } else {
            logger.info("Token already exists in blocklist database: {}", token);
        }
    }

    public boolean isTokenBlocklisted(String token) {
        logger.info("Checking if token is blocklisted: {}", token);
        boolean isBlocklisted = blocklistedTokenRepository.findByToken(token).isPresent();
        logger.info("Token {} blocklisted status: {}", token, isBlocklisted);
        return isBlocklisted;
    }
} 