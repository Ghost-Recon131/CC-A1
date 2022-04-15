package rmit.cc.a1.Account.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;

import java.util.List;

@Component
@AllArgsConstructor
public class AccountRegisterValidator implements Validator {

    private AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountRegisterRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        List<Account> ListOfAccounts = accountRepository.findAll();

        AccountRegisterRequest validateAccount = (AccountRegisterRequest) object;

        for (Account accounts : ListOfAccounts) {
            if (accounts.getUsername().equals(validateAccount.getUsername())) {
                errors.rejectValue("Account", "Username", "Account with this email already exists");
            }
        }
    }

}
