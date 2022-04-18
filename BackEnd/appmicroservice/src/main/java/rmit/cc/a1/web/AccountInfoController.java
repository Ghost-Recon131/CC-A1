package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.AccountInfo.model.AccountInfo;
import rmit.cc.a1.AccountInfo.requests.AccountInfoRequest;
import rmit.cc.a1.AccountInfo.services.AccountInfoService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/AccountInfo")
@AllArgsConstructor
public class AccountInfoController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private AccountInfoService accountInfoService;
    private MapValidationErrorService mapValidationErrorService;


    @PostMapping(path = "/updateAccountInfo")
    public ResponseEntity<?> updateAccountInfo(@RequestParam("userID") Long userID,@RequestBody AccountInfoRequest request, BindingResult result){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        try{
            accountInfoService.updateAccountInfo(userID, request);
        }catch (Exception e) {
            logger.error("Failed to update account info" +  e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/getAccountInfo")
    public AccountInfo getAccountInfo(@RequestParam("userID") Long userID){
        AccountInfo userAccountInfo = null;
        try{
            userAccountInfo = accountInfoService.getAccountInfo(userID);
        }catch (Exception e) {
            logger.error("Failed get account info" +  e.getMessage());
        }
        return userAccountInfo;
    }


}
