package rmit.cc.a1.Account.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.EmailConfirmation.model.EmailConfirmationToken;
import rmit.cc.a1.EmailConfirmation.repository.EmailConfirmTokenRepository;
import rmit.cc.a1.EmailConfirmation.repository.EmailSender;
import rmit.cc.a1.EmailConfirmation.services.EmailBuilderService;
import rmit.cc.a1.EmailConfirmation.services.EmailConfirmationTokenService;
import rmit.cc.a1.EmailConfirmation.services.EmailValidator;
import rmit.cc.a1.utils.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class RegisterService {

    private EmailValidator emailValidator;
    private AccountService accountService;
    private EmailConfirmationTokenService emailConfirmationTokenService;
    private EmailSender emailSender;
    private AccountRepository accountRepository;
    private EmailBuilderService emailBuilderService;
    private EmailConfirmTokenRepository emailConfirmTokenRepository;

    public Account register(AccountRegisterRequest request) {
        boolean validEmail = emailValidator.test(request.getUsername());

        if(!validEmail){
            throw new IllegalStateException("Your email is not valid");
        }

        String token = accountService.registerStudentAccount(
                new Account(
                request.getUsername(),
                request.getFullName(),
                request.getPassword(),
                UserRole.USER
                )
        );

        // Link to confirm registration
        // Currently a localhost link, will need to be updated later
        String link = "http://localhost:8080/api/RegisterLogin/confirm?token=" + token;
        emailSender.sendEmail(request.getUsername(), emailBuilderService.buildConfirmEmail(request.getFullName(), link));

        return emailConfirmTokenRepository.getByToken(token).getAccount();
    }

    // Used to validate the email confirmation token
    @Transactional
    public String confirmEmailToken(String token){
        EmailConfirmationToken emailConfirmEmailToken = emailConfirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found!"));

        LocalDateTime expiredAt = emailConfirmEmailToken.getExpireAt();


        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        emailConfirmationTokenService.setConfirmedAt(token);
        accountService.enableStudentAccount(emailConfirmEmailToken.getAccount().getUsername());
        return "confirmed";
    }


    // Send email with token for reset forgotten password
    private void sendForgotPasswordToken(String token, Account account){
        String link = "http://localhost:8080/api/RegisterLogin/forgotPassword/validate?token=" + token;
        emailSender.sendEmail(account.getUsername(), emailBuilderService.buildForgotPasswordEmail(account.getFullName(), link));
    }

    // For when user forgets password
    public String generateForgotPasswordToken(String username) {
        Account account = accountRepository.findByUsername(username);
        String Token = UUID.randomUUID().toString();

        EmailConfirmationToken token = accountService.generateToken(account, Token);
        emailConfirmationTokenService.saveEmailConfirmToken(token);

        // Sends email
        sendForgotPasswordToken(Token, account);

        return Token;
    }



    // Validate token for reset password
    @Transactional
    public boolean validateForgotPasswordToken(String token){
        boolean isTokenValid;

        EmailConfirmationToken emailConfirmEmailToken = emailConfirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found!"));

        LocalDateTime expiredAt = emailConfirmEmailToken.getExpireAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            isTokenValid = false;
        }else{
            isTokenValid = true;
        }

        emailConfirmationTokenService.setConfirmedAt(token);

        return isTokenValid;
    }

}
