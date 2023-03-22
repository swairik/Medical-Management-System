package com.mms.demo.repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mms.demo.entity.Token;

/**
 * TokenRepository defines an interface to generate JPA defined queries to interact with the Token
 * table.
 * 
 * @author Mananveer Singh
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
    /**
     * Find a token, given the identifier is known.
     *
     * @param identifier the identifier
     * @return the token, wrapped in an optional container.
     */
    Optional<Token> findByIdentifier(String identifier);

    /**
     * Deletes all tokens that have an expiration stamp dated before the given stamp.
     * 
     * @param referenceTimeStamp preferably the current time, all tokens with expiration after this
     *        stamp will be kept
     */
    @Modifying
    @Query("DELETE t from TOKEN t where t.expirationStamp <= :referenceTimeStamp")
    void deleteAllByExpirationStampBefore(
                    @Param("referenceTimeStamp") LocalDateTime referenceTimeStamp);
}
