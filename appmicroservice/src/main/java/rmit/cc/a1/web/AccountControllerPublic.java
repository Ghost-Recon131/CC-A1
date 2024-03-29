package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.Account.requests.LoginRequest;
import rmit.cc.a1.Account.requests.ResetPasswordRequest;
import rmit.cc.a1.Account.services.AccountService;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.Account.validator.AccountRegisterValidator;
import rmit.cc.a1.Account.validator.LoginValidator;
import rmit.cc.a1.Account.validator.ResetPasswordValidator;
import rmit.cc.a1.security.JWTLoginSucessReponse;
import rmit.cc.a1.security.JwtTokenProvider;


import javax.validation.Valid;

import static rmit.cc.a1.security.SecurityConstant.TOKEN_PREFIX;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/RegisterLogin")
@AllArgsConstructor
public class AccountControllerPublic {

    private MapValidationErrorService mapValidationErrorService;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private AccountService accountService;
    private AccountRegisterValidator accountRegisterValidator;
    private LoginValidator loginValidator;
    private ResetPasswordValidator resetPasswordValidator;
    private AccountRepository accountRepository;
    private final Logger logger = LogManager.getLogger(this.getClass());

    // Registers a new account
    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody AccountRegisterRequest request, BindingResult result){
        accountRegisterValidator.validate(request, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        accountService.registerStudentAccount(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    // Checks login if correct and returns a JWT token
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        Account user = null;
        String jwt = null;

        loginValidator.validate(loginRequest, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            jwt = TOKEN_PREFIX +  jwtTokenProvider.generateToken(authentication);
        }catch (Exception e){
            logger.error(e);
        }


        if(jwt != null){
            user = accountRepository.findByUsername(loginRequest.getUsername());
        }

        return ResponseEntity.ok(user);
    }

    //Reset password using secret question & secret question answer
    @PostMapping(path = "/resetForgottenPassword")
    public ResponseEntity<?> resetForgottenPassword(@Valid @RequestBody ResetPasswordRequest ResetPasswordRequest, @RequestParam(value = "username") String username, BindingResult result){
        resetPasswordValidator.validate(ResetPasswordRequest, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        accountService.changeForgottenPassword(username, ResetPasswordRequest);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}
