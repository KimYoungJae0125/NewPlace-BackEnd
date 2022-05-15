package shop.newplace.users.model.validator;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.common.exception.ValidFailureException;
import shop.newplace.common.security.CustomAuthenticationProvider;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.model.dto.UsersRequestDto;
import shop.newplace.users.repository.UsersRepository;


@Slf4j
public class UsersValidator {
	
	@Component
	@RequiredArgsConstructor
	public static class SignUp implements Validator{
		private final UsersRepository userRepository;
		
		@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(UsersRequestDto.SignUp.class);
		}
		
		@Override
		public void validate(Object target, Errors errors) {
			UsersRequestDto.SignUp usersSignUpForm = (UsersRequestDto.SignUp)target;
			String loginEmail = CipherUtil.Email.encrypt(usersSignUpForm.getLoginEmail());
			if(userRepository.existsByLoginEmail(loginEmail)) {
				errors.rejectValue("loginEmail", "invalid.loginEmail",
						new Object[] {usersSignUpForm.getLoginEmail()}, "이미 사용중인 이메일입니다.");
			}
			if(usersSignUpForm.getPassword() != null && !usersSignUpForm.getPassword().equals(usersSignUpForm.getPasswordVerified())) {
				errors.rejectValue("password", "invalid.password",
						new Object[] {usersSignUpForm.getPassword(), usersSignUpForm.getPasswordVerified()}, "비밀번호가 일치하지 않습니다.");
			}
			if(errors.hasErrors()) {
				System.out.println(errors.toString());
				throw new ValidFailureException("회원가입에 실패하였습니다.", (BindingResult) errors);
			}
		}
	}
	
	@Component
	@RequiredArgsConstructor
	public static class LogIn implements Validator {
		
		private final CustomAuthenticationProvider customAuthenticationProvider;
		
		@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(UsersRequestDto.LogIn.class);
		}
		
		@Override
		public void validate(Object target, Errors errors) {
			if(errors.hasErrors()) {
				throw new ValidFailureException("로그인을 실패하였습니다.", (BindingResult) errors);
			}
			//UsersService로 이동했다가 해당 클래스로 재 이동 후 메소드 분리(UsersService에서는 Transactional이 걸려있어 failCount Update가 안 됨
//			LogInForm loginForm = (LogInForm)target;
//			customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getLoginEmail(), loginForm.getPassword()));
		}
		
		public Authentication authentication(UsersRequestDto.LogIn logInForm) {
			return customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(logInForm.getLoginEmail(), logInForm.getPassword()));
		}

	}
	
}
