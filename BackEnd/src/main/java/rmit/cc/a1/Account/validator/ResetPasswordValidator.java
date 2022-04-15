package rmit.cc.a1.Account.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.requests.ResetPasswordRequest;

@Component
@AllArgsConstructor
public class ResetPasswordValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return ResetPasswordRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ResetPasswordRequest resetRequest = (ResetPasswordRequest) object;

        if (resetRequest.getSecretQuestion() == null) {
            errors.rejectValue("ResetPassword", "SecretQuestion", "Secret Question is not entered");
        }

        if (resetRequest.getSecretQuestionAnswer() == null) {
            errors.rejectValue("ResetPassword", "SecretQuestionAnswer", "Answer to Secret Question is not entered");
        }

        if (!resetRequest.getNewPassword().equals(resetRequest.getConfirmNewPassword())) {
            errors.rejectValue("ResetPassword", "PasswordMismatch", "Passwords do not match, please try again");
        }

    }

}
