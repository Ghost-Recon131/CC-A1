package rmit.cc.a1.AccountInfo.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.AccountInfo.model.AccountInfo;
import rmit.cc.a1.AccountInfo.repository.AccountInfoRepository;
import rmit.cc.a1.AccountInfo.requests.AccountInfoRequest;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoService {

    @Autowired
    private AccountInfoRepository AccountInfoRepository;

    // Takes a PUT request and updates the student's info
    public void updateAccountInfo(Long ID, AccountInfoRequest updateRequest){
        AccountInfo existingAccountInfo = AccountInfoRepository.getById(ID);
        existingAccountInfo.setGender(updateRequest.getGender());
        existingAccountInfo.setNationality(updateRequest.getNationality());
        existingAccountInfo.setDob(updateRequest.getDob());
        existingAccountInfo.setPhone(updateRequest.getPhone());
        existingAccountInfo.setUniversity(updateRequest.getUniversity());
        existingAccountInfo.setUniLevel(updateRequest.getUniLevel());
        existingAccountInfo.setField(updateRequest.getField());
        AccountInfoRepository.save(existingAccountInfo);
    }

    // Generate new student info line in database
    public void createAccountInfoEntry(Account account){
        String gender = null;
        String nationality = null;
        String dob = null;
        Long phone = null;
        String university = null;
        String uniLevel = null;
        String field = null;

        AccountInfo BlankAccountInfo = new AccountInfo(account, gender, nationality, dob, phone, university, uniLevel, field);
        AccountInfoRepository.save(BlankAccountInfo);
    }

    public AccountInfo getAccountInfo(Long ID){
        return AccountInfoRepository.getById(ID);
    }

}
