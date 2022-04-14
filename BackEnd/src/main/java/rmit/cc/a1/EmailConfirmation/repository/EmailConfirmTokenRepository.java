package rmit.cc.a1.EmailConfirmation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmit.cc.a1.EmailConfirmation.model.EmailConfirmationToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailConfirmTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    Optional<EmailConfirmationToken> findByToken(String token);

    @Query("FROM EmailConfirmationToken WHERE token = ?1")
    EmailConfirmationToken getByToken(String token);


    @Transactional
    @Modifying
    @Query("UPDATE EmailConfirmationToken c " +
            "SET c.confirmAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);

}
