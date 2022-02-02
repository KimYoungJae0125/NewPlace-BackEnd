package shop.newplace.Users.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
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
@Api
public class UsersController {
	
	private final UsersService usersService;

	private final SignUpFormValidator signUpFormValidator;
	
	private final SignInFormValidator signInFormValidator;
	
	//알아보기
	@InitBinder("signUpForm")
	public void signUpFormValidator(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpFormValidator);
	}

	@InitBinder("signInForm")
	public void signInFormValidator(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signInFormValidator);
	}

    @PostMapping
    public ResponseEntity createSignUp(@Valid @RequestBody SignUpForm signUpForm) {
    	usersService.signUp(signUpForm);
        return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "회원가입에 성공하였습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity goCreateLoginForm(@Valid @RequestBody SignInForm signInForm) {

    	JwtForm jwtForm = usersService.signIn(signInForm);
    	
    	ResponseMessage body = ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "로그인 성공하였습니다.", jwtForm);

    	return ResponseEntity.ok().body(body);
    }
    
}
