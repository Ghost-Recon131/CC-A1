package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.security.JwtTokenProvider;
import rmit.cc.a1.utils.GetUserByJWTUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/authorised")
@AllArgsConstructor
public class AccountControllerValidated {

    private AccountRepository accountRepository;
    private JwtTokenProvider jwtTokenProvider;
    private GetUserByJWTUtil getUserByJWTUtil;

    // returns the current user based on their JWT
    @GetMapping(path = "/authenticateUser")
    public Account authenticateUser(HttpServletRequest request){

        return getUserByJWTUtil.getUserIdByJWT(request);
    }


}
