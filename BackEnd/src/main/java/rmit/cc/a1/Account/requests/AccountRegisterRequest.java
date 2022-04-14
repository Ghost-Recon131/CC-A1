package rmit.cc.a1.Account.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountRegisterRequest {
    private String fullName;
    private String password;
    private String username;
}
