package shop.newplace.users.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.users.model.validator.UsersValidator;
import shop.newplace.users.service.UsersService;
import shop.newplace.common.response.ResponseMessage;

@Slf4j // 기본 log 찍을 수 있는 객체
@RestController
@RequiredArgsConstructor // final인 얘들한테 생성자 주입(AutoWired 효과)
@RequestMapping("/users")
public class UsersController {
	
	private final UsersService usersService;

	private final UsersValidator.SignUp signUpValidator;
	
	private final UsersValidator.LogIn logInValidator;
	
	//알아보기
	@InitBinder
	public void addUsersValidator(WebDataBinder webDataBinder) {
		if(webDataBinder.getTarget() instanceof UsersDto.RequestSignUp) {
			webDataBinder.addValidators(signUpValidator);
		}
		if(webDataBinder.getTarget() instanceof UsersDto.RequestLogIn) {
			webDataBinder.addValidators(logInValidator);
		}
	}

	@ApiOperation(value = "회원가입", notes = "새로운 사용자의 정보를 등록합니다.")
    @PostMapping
    public ResponseEntity createSignUp(@Valid @RequestBody UsersDto.RequestSignUp signUpForm) {
    	usersService.signUp(signUpForm);
        return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "회원가입에 성공하였습니다."));
    }

	@ApiOperation(value = "로그인", notes = "로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity goLogin(@Valid @RequestBody UsersDto.RequestLogIn logInForm, HttpServletResponse response) {
		Authentication authentication = logInValidator.authentication(logInForm);
    	return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "로그인 성공하였습니다.", usersService.logIn(authentication, response)));
    }
	
	@GetMapping("/{userId}")
	public ResponseEntity viewUsersById(@PathVariable(name = "userId") Long userId) {
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), "회원정보 조회에 성공하였습니다.", usersService.getUserInfo(userId)));
	}
    
}
