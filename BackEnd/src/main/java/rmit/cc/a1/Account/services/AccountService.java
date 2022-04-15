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
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.Account.requests.ResetPasswordRequest;
import rmit.cc.a1.AccountInfo.services.AccountInfoService;
import rmit.cc.a1.utils.UserRole;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private AccountInfoService accountInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username);
    }

    // Handles creating of new account to database
    public void registerStudentAccount(AccountRegisterRequest request) {

        Account newAccount = new Account(
                request.getUsername(),
                request.getFullName(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                UserRole.USER,
                request.getSecretQuestion(),
                bCryptPasswordEncoder.encode(request.getSecretQuestionAnswer()));

        // Saves  account to database & create account info entry
        accountRepository.save(newAccount);
        accountInfoService.createAccountInfoEntry(newAccount);
    }

    public void changeForgottenPassword(String username, ResetPasswordRequest request){
        String newPassword = request.getNewPassword();

        Account account = accountRepository.findByUsername(username);
        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        accountRepository.save(account);

    }

    @Transactional
    public Account loadUserById(Long id){
        return accountRepository.getById(id);
    }

}
