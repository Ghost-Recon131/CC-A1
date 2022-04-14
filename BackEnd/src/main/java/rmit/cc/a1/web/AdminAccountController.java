package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.Account.services.AdminRegisterService;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.Account.validator.LoginValidator;
import rmit.cc.a1.utils.VerifyAdminUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/adminAccount")
@AllArgsConstructor
public class AdminAccountController {

    private AdminRegisterService adminRegisterService;
    private MapValidationErrorService mapValidationErrorService;
    private AccountRepository accountRepository;
    private VerifyAdminUtil verifyAdminUtil;
    private LoginValidator loginValidator;

    // Register an admin user
    @PostMapping(path = "admin/AdminRegister")
    public ResponseEntity<?> registerAdmin(@RequestBody AccountRegisterRequest request, BindingResult result){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        adminRegisterService.registerAdminAccount(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Suspend a user
    @PutMapping(path = "admin/suspendUser{id}")
    public ResponseEntity<?> suspendUser(@PathVariable(value = "id") Long id, HttpServletRequest request, BindingResult result){
        // Checks user is admin
        if(verifyAdminUtil.isUserAdmin(request)){

            Account account = accountRepository.getById(id);

            // Validate input
            loginValidator.validate(account, result);

            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null) return errorMap;

            // Save new course if all checks pass
            account.setEnabled(false);
        }else{
            // If not admin, throw exception
            throw new org.springframework.security.access.AccessDeniedException("403 returned");
        }
        return ResponseEntity.ok(true);
    }

    // Enable a user
    @PutMapping(path = "admin/enableUser{id}")
    public ResponseEntity<?> enableUser(@PathVariable(value = "id") Long id, HttpServletRequest request, BindingResult result){
        // Checks user is admin
        if(verifyAdminUtil.isUserAdmin(request)){
            Account account = accountRepository.getById(id);

            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null) return errorMap;

            // Save new course if all checks pass
            account.setEnabled(true);
        }else{
            // If not admin, throw exception
            throw new org.springframework.security.access.AccessDeniedException("403 returned");
        }
        return ResponseEntity.ok(true);
    }


}
