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
    private String gender;
    private String nationality;
    private String dob;
    private Long phone;
    private String university;
    private String uniLevel;
    private String field;
}
