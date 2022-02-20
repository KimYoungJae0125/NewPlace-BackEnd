package shop.newplace.Users.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.validator.LogInFormValidator;
import shop.newplace.Users.model.validator.SignUpFormValidator;
import shop.newplace.Users.service.UsersService;
import shop.newplace.Users.token.model.dto.JwtTokenForm;
import shop.newplace.common.response.ResponseMessage;

@Slf4j // 기본 log 찍을 수 있는 객체
@RestController
@RequiredArgsConstructor // final인 얘들한테 생성자 주입(AutoWired 효과)
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;

	private final SignUpFormValidator signUpFormValidator;
	
	private final LogInFormValidator logInFormValidator;
	
	//알아보기
	@InitBinder("signUpForm")
	public void signUpFormValidator(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(signUpFormValidator);
	}

	@InitBinder("logInForm")
	public void logInFormValidator(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(logInFormValidator);
	}

	@ApiOperation(value = "회원가입", notes = "새로운 사용자의 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity createSignUp(@Valid @RequestBody SignUpForm signUpForm) {
    	usersService.signUp(signUpForm);
        return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "회원가입에 성공하였습니다."));
    }

	@ApiOperation(value = "로그인", notes = "로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity goLogin(@Valid @RequestBody LogInForm logInForm, HttpServletResponse response) {
    	JwtTokenForm jwtForm = usersService.logIn(logInForm, response);
    	return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "로그인 성공하였습니다.", jwtForm));
    }
    
}
