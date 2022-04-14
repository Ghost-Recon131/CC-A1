package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.Account.services.AdminRegisterService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/admin/adminAccount")
@AllArgsConstructor
public class AdminAccountController {

    @Autowired
    private AdminRegisterService adminRegisterService;

    @PostMapping(path = "admin/AdminRegister")
    public void registerAdmin(@RequestBody AccountRegisterRequest request){
        adminRegisterService.registerAdminAccount(request);
    }

}
