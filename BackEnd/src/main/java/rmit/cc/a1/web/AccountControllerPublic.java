package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.requests.AccountRegisterRequest;
import rmit.cc.a1.Account.requests.LoginRequest;
import rmit.cc.a1.Account.services.AccountService;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.Account.validator.AccountRegisterValidator;
import rmit.cc.a1.Account.validator.LoginValidator;
import rmit.cc.a1.security.JWTLoginSucessReponse;
import rmit.cc.a1.security.JwtTokenProvider;


import javax.validation.Valid;

import static rmit.cc.a1.security.SecurityConstant.TOKEN_PREFIX;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/RegisterLogin")
@AllArgsConstructor
public class AccountControllerPublic {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AccountService accountService;
    private AccountRegisterValidator accountRegisterValidator;
    private LoginValidator loginValidator;

    // Registers a new student account
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
        loginValidator.validate(loginRequest, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }

    //TODO: Reset password using secret question & secret question answer


}
