package rmit.cc.a1.Account.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.LoginRequest;

@Component
@AllArgsConstructor
public class LoginValidator implements Validator {

    private AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return LoginRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        LoginRequest loginRequest = (LoginRequest) object;

        try{
            Account currentUser = accountRepository.findByUsername(loginRequest.getUsername());
            if (!currentUser.getEnabled()) {
                errors.rejectValue("Account", "Status", "Account not enabled or suspended, please check your confirmation email or contact admin");
            }
        }catch (Exception e){

        }


    }

}
