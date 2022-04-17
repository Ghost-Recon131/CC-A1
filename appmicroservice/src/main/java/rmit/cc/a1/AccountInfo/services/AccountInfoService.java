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

        if(updateRequest.getAddress() != null){
            existingAccountInfo.setAddress(updateRequest.getAddress());
        }

        if(updateRequest.getDob() != null){
            existingAccountInfo.setDob(updateRequest.getDob());
        }

        if(updateRequest.getPhone() != null){
            existingAccountInfo.setPhone(updateRequest.getPhone());
        }

        AccountInfoRepository.save(existingAccountInfo);
    }

    // Generate new student info line in database
    public void createAccountInfoEntry(Account account){
        String address = null;
        String dob = null;
        String phone = null;

        AccountInfo BlankAccountInfo = new AccountInfo(account, address, dob, phone);
        AccountInfoRepository.save(BlankAccountInfo);
    }

    public AccountInfo getAccountInfo(Long ID){
        return AccountInfoRepository.getById(ID);
    }

}
