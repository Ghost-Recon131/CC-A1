package rmit.cc.a1.Account.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.utils.UserRole;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminRegisterService {

    @Autowired
    private AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Takes Post Request & generates new admin account
    public void registerAdminAccount(AccountRegisterRequest request){

        // Checks entered email against database of existing emails
        if(accountRepository.findByUsername(request.getUsername()) != null){
            if(accountRepository.findByUsername(request.getUsername()).getUsername().equals(request.getUsername())){
                throw new IllegalStateException("Email already in use, did you forget your password?");
            }
        }

        // Create the new account & input details, then enable the account
        String hashedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        String hashedSecretQuestionAnswer = bCryptPasswordEncoder.encode(request.getSecretQuestionAnswer());
        UUID uuid = UUID.randomUUID();
        Account newAdminAccount = new Account(request.getUsername(), request.getFullName(), hashedPassword, UserRole.ADMIN, request.getSecretQuestion(), hashedSecretQuestionAnswer, uuid.toString());
        newAdminAccount.setEnabled(true);

        // Save the new account
        accountRepository.save(newAdminAccount);
    }

}
