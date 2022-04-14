package rmit.cc.a1.Account.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.ResetPasswordRequest;
import rmit.cc.a1.AccountInfo.services.AccountInfoService;
import rmit.cc.a1.EmailConfirmation.model.EmailConfirmationToken;
import rmit.cc.a1.EmailConfirmation.services.EmailConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailConfirmationTokenService emailConfirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private AccountInfoService accountInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username);
    }

    // Handles creating of new account to database
    public String registerStudentAccount(Account account) {

        // Checks entered email against database of existing emails
        if(accountRepository.findByUsername(account.getUsername()) != null){
            if(accountRepository.findByUsername(account.getUsername()).getUsername().equals(account.getUsername())){
                throw new IllegalStateException("Email already in use, did you forget your password?");
            }
        }

        String hashedPassword = bCryptPasswordEncoder.encode(account.getPassword());
        account.setPassword(hashedPassword);

        // Saves student account to database & create student info entry
        accountRepository.save(account);
        accountInfoService.createAccountInfoEntry(account);

        String Token = UUID.randomUUID().toString();
        EmailConfirmationToken token = generateToken(account, Token);
        emailConfirmationTokenService.saveEmailConfirmToken(token);

        return Token;
    }

    // Saves the confirmation token to user
    public EmailConfirmationToken generateToken(Account account, String Token){
        return new EmailConfirmationToken(
                Token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(24),
                account
        );
    }


    public String changeForgottenPassword(String username, ResetPasswordRequest request){
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmNewPassword();

        String status = "Password reset failed";

        if(newPassword.equals(confirmPassword)){
            Account account = accountRepository.findByUsername(username);
            account.setPassword(bCryptPasswordEncoder.encode(newPassword));
            accountRepository.save(account);
            status = "Password changed successfuly";
        }else{
            throw new IllegalStateException("Entered passwords do not match!");
        }

        return status;
    }

    public void enableStudentAccount(String email) {
       accountRepository.confirmAccountEmail(email);
    }


    @Transactional
    public Account loadUserById(Long id){
        return accountRepository.getById(id);
    }

}
