package rmit.cc.a1.EmailConfirmation.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmit.cc.a1.EmailConfirmation.model.EmailConfirmationToken;
import rmit.cc.a1.EmailConfirmation.repository.EmailConfirmTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenService {

    @Autowired
    private EmailConfirmTokenRepository emailConfirmTokenRepository;

    public void saveEmailConfirmToken(EmailConfirmationToken token){
        emailConfirmTokenRepository.save(token);
    }

    public Optional<EmailConfirmationToken> getToken(String token) {
        return emailConfirmTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return emailConfirmTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }

}
