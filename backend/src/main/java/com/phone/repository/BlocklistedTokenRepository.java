package com.phone.repository;

import com.phone.model.BlocklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlocklistedTokenRepository extends JpaRepository<BlocklistedToken, Long> {

    Optional<BlocklistedToken> findByToken(String token);
} 