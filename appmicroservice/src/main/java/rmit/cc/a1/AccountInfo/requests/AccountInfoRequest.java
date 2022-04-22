package rmit.cc.a1.AccountInfo.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountInfoRequest {
    private String address;
    private String dob;
    private String phone;
}
