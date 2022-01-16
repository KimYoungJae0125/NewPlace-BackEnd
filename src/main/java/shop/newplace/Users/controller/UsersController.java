package shop.newplace.Users.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.validator.SignUpFormValidator;
import shop.newplace.Users.service.UsersService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
	
	Logger logger = LoggerFactory.getLogger(UsersController.class);

	
	@Autowired
	private UsersService usersService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;
	
    @PostMapping("/signup")
    public ResponseEntity createSignUp(@Valid SignUpForm signUpForm, Errors errors) throws URISyntaxException{
    	URI redirectUri = new URI("/signup");
    	HttpHeaders httpHeaders = new HttpHeaders();
    	httpHeaders.setLocation(redirectUri);
    
    	if(errors.hasErrors()) {
    		logger.error(errors.toString());
    		
    		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER); 
    	}
    	
    	logger.info("signUpFormCtr : " + signUpForm.toString());
    	
    	signUpFormValidator.validate(signUpForm, errors);
    	if(errors.hasErrors()){
    		logger.error(errors.toString());

    		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER); 
    	}
    	
    	usersService.signUp(signUpForm);
    	
        return ResponseEntity.ok("성공!");
    }

    @GetMapping("/signup")
    public ResponseEntity goCreateSignUpForm() {
    	return ResponseEntity.ok("회원가입해보기!");
    }
    
    
    @GetMapping("/login")
    public ResponseEntity goCreateLoginForm() throws URISyntaxException {
    	String userName = "세션 확인";
//    	accountService.login(account);
    	
    	return ResponseEntity.ok(userName);
    }
    
    @GetMapping("/loginSuccess")
    public ResponseEntity goCreateLoginSuccess() {
    	
    	return ResponseEntity.ok("로그인성공");
    }

    @GetMapping("/loginFailure")
    public ResponseEntity goCreateLoginFailure() throws URISyntaxException {
    	
    	URI redirectUri = new URI("/login");
    	HttpHeaders httpHeaders = new HttpHeaders();
    	httpHeaders.setLocation(redirectUri);
    	
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
