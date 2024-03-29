package com.mycompany.advertising.repository;

import com.mycompany.advertising.repository.entity.UserTo;
import com.mycompany.advertising.repository.entity.VerificationTokenTo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Amir on 9/10/2021.
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenTo, Long> {
    VerificationTokenTo findByToken(String token);

    VerificationTokenTo findByUser(UserTo user);

    @Modifying
    @Query("DELETE FROM VerificationTokenTo t where t.expiryDate <= ?1")
    void deleteAllExpiredTokenSince(LocalDateTime now);

    //@Query(value = "SELECT token FROM verification_token_to WHERE verification_token_to.user_id = (SELECT id FROM user_to WHERE username = ?1 LIMIT 1) LIMIT 1", nativeQuery = true)
    //String findTokenByPhoneNumber(String phonenumber);

    VerificationTokenTo findByUser_Username(String username);

    long deleteByUser(UserTo user);

    //List<VerificationTokenTo> findByExpiryDateGreaterThan(Date date);
    //List<VerificationTokenTo> findByExpiryDateGreaterThanEqual(Date date);
    //List<VerificationTokenTo> findByExpiryDateLessThanEqual(Date date);
    List<VerificationTokenTo> findByExpiryDateLessThan(LocalDateTime date);

    //void deleteAll(List<VerificationTokenTo> verificationTokenTos);
    boolean existsByUser_Username(String username);

    boolean existsByUser(UserTo userTo);
}
