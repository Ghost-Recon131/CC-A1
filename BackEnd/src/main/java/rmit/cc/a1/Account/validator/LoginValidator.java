package rmit.cc.a1.Account.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;

@Component
@AllArgsConstructor
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        Account loginRequest = (Account) object;

        if (!loginRequest.getEnabled()) {
            errors.rejectValue("Account", "Status", "Account not enabled, please check your confirmation email");
        }
    }

}
