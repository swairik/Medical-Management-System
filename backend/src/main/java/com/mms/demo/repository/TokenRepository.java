package com.mms.demo.repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mms.demo.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByIdentifier(String identifier);

    @Modifying
    @Query("DELETE t from TOKEN t where t.expirationStamp <= :referenceTimeStamp")
    void deleteAllByExpirationStampBefore(
                    @Param("referenceTimeStamp") LocalDateTime referenceTimeStamp);
}
