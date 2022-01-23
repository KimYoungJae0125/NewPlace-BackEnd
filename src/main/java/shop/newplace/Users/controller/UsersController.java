package shop.newplace.Users.controller;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.JwtForm;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.validator.ProfileSignUpFormValidator;
import shop.newplace.Users.model.validator.SignInFormValidator;
import shop.newplace.Users.model.validator.SignUpFormValidator;
import shop.newplace.Users.service.UsersService;
import shop.newplace.common.response.ResponseMessage;

@Slf4j // 기본 log 찍을 수 있는 객체
@RestController
@RequiredArgsConstructor // final인 얘들한테 생성자 주입(AutoWired 효과)
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;

	private final SignUpFormValidator signUpFormValidator;
	
	private final SignInFormValidator signInFormValidator;
	
	private final ProfileSignUpFormValidator profileSignInFormValidator;
	
    @PostMapping
    public ResponseEntity createSignUp(@Valid SignUpForm signUpForm, BindingResult bindingResult) throws Exception{

    	//Exception 던지는걸로 수정
    	if(bindingResult.hasErrors()) {
    		log.error(bindingResult.toString());
    		SignUpForm badResponseData = SignUpForm.builder()
    											   .build();
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), badResponseData, bindingResult);
    		
    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	signUpFormValidator.validate(signUpForm, bindingResult);
    	if(bindingResult.hasErrors()){
    		log.error(bindingResult.toString());
    		SignUpForm badResponseData = SignUpForm.builder()
    											    .build();
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), badResponseData, bindingResult);

    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	
    	Users users = usersService.signUp(signUpForm);

    	ResponseMessage body = ResponseMessage.OK(201, "성공", "회원가입에 성공하였습니다.");
//    	Response body = Response.OK(201, "성공", "회원가입에 성공하였습니다.", users);
    	
        return ResponseEntity.ok().body(body);
    }

    @GetMapping
    public ResponseEntity goCreateSignUpForm() {
    	return ResponseEntity.ok("회원가입해보기!");
    }
    
    
    @GetMapping("/login")
    public ResponseEntity goCreateLoginForm() throws URISyntaxException {
    	String userName = "세션 확인";
//    	accountService.login(account);
    	
    	return ResponseEntity.ok(userName);
    }

    @PostMapping("/login")
    public ResponseEntity goCreateLoginForm(@Valid SignInForm signInForm, BindingResult bindingResult) throws Exception {

    	if(bindingResult.hasErrors()) {
    		log.error(bindingResult.toString());
    		JwtForm badResponseData = JwtForm.builder()
						    		 		 .token(null)
						    				 .resCd("9999")
						    				 .resMsg("로그인 실패")
						    				 .build();
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), badResponseData, bindingResult);
    		
    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	signInFormValidator.validate(signInForm, bindingResult);
    	
    	if(bindingResult.hasErrors()){
    		log.error(bindingResult.toString());
    		JwtForm badResponseData = JwtForm.builder()
    										 .token(null)
    										 .resCd("9999")
    										 .resMsg("로그인 실패")
    										 .build();
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), badResponseData, bindingResult);

    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	
    	JwtForm jwtForm = usersService.signIn(signInForm);
    	
    	ResponseMessage body = ResponseMessage.OK(201, "성공", "로그인 성공하였습니다.", jwtForm);

    	return ResponseEntity.ok().body(body);
    }
    
    ///users/1/profiles
    @PostMapping("/{userId}/profiles")
    public ResponseEntity goCreateProfile(@PathVariable(name = "userId") Long userId
    									, @Valid ProfileSignUpForm profileSignUpForm
    									, BindingResult bindingResult) throws Exception {
    	
    	log.info("테스트");
    	
    	if(bindingResult.hasErrors()) {
    		log.error(bindingResult.toString());
    		
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), null, bindingResult);
    		
    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	profileSignUpForm.setUserId(userId);
    	
    	profileSignInFormValidator.validate(profileSignUpForm, bindingResult);
    	
    	if(bindingResult.hasErrors()) {
    		log.error(bindingResult.toString());
    		
    		ResponseMessage body = ResponseMessage.NOT_VALID_ERROR(400, bindingResult.toString(), null, bindingResult);
    		
    		return ResponseEntity.badRequest().body(body);
    	}
    	
    	Profiles profiles = usersService.profileSignUp(profileSignUpForm);

    	ResponseMessage body = ResponseMessage.OK(201, "프로필 생성", "프로필 생성 성공", profiles);
    	
    	return ResponseEntity.ok().body(body);
    }
    
    
}
