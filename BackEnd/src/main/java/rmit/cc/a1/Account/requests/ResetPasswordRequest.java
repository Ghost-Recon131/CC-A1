package rmit.cc.a1.Account.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ResetPasswordRequest {
    private String secretQuestion;
    private String secretQuestionAnswer;
    String newPassword;
    String confirmNewPassword;
}
