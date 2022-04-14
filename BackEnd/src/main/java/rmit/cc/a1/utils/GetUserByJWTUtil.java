package rmit.cc.a1.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.security.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;

import static rmit.cc.a1.security.SecurityConstant.HEADER_STRING;
import static rmit.cc.a1.security.SecurityConstant.TOKEN_PREFIX;

// Utility class to return user id using their JWT token
@Service
@AllArgsConstructor
public class GetUserByJWTUtil {

    private JwtTokenProvider jwtTokenProvider;
    private AccountRepository accountRepository;


    public Account getUserIdByJWT(HttpServletRequest request){
        String authHeader = request.getHeader(HEADER_STRING);

        Long userId = null;

        if(authHeader != null && authHeader.startsWith(TOKEN_PREFIX)){
            String jwt = authHeader.substring(7);
            userId = jwtTokenProvider.getUserIdFromJWT(jwt);
        }

        return accountRepository.getById(userId);
    }


}
