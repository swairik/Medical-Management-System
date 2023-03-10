package com.mms.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mms.demo.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{
    Optional<Token> findByIdentifier(String identifier);
}
