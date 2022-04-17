package rmit.cc.a1.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class VerifyAdminUtil {

    private GetUserByJWTUtil getUserByJWTUtil;

    public boolean isUserAdmin(HttpServletRequest request){
        return getUserByJWTUtil.getUserIdByJWT(request).getUserRole() == UserRole.ADMIN;
    }

}
