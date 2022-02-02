package shop.newplace.Users.model.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.ValidFailureException;
import shop.newplace.common.util.CipherUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignInFormValidator implements Validator {
	
	private final UsersRepository usersRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(SignInForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		SignInForm signInForm = (SignInForm)target;
		
		String loginEmail = CipherUtil.Email.encrypt(signInForm.getLoginEmail());
		
		log.info("loginEmail = " + loginEmail);
		
		Users usersInfo = usersRepository.findByLoginEmail(loginEmail)
				.orElseThrow(() ->
				new ValidFailureException(signInForm.getLoginEmail() + "는 존재하지 않는 이메일입니다.", (BindingResult) errors));
		
		if(!passwordEncoder.matches(signInForm.getPassword(), usersInfo.getPassword())) {
			errors.rejectValue("password", "invalid.password",
					new Object[] {signInForm.getPassword()}, "비밀번호가 일치하지 않습니다.");
		}

		if(!usersInfo.isAccountNonLocked()) {
			errors.rejectValue("accountNonLocked", "invalid.accountNonLocked",
					new Object[] {usersInfo.getAccountNonLocked()}, "계정이 잠겼습니다.");
		}

		if(!usersInfo.isEnabled()){
			errors.rejectValue("enabled", "invalid.enabled",
					new Object[] {usersInfo.isEnabled()}, "사용 불가능한 계정입니다.");
		}
		
		if(!usersInfo.isCredentialsNonExpired()) {
			errors.rejectValue("credentialsNonExpired", "invalid.credentialsNonExpired",
					new Object[] {usersInfo.isCredentialsNonExpired()}, "비밀번호가 만료되었습니다.");
		}
		
		if(errors.hasErrors()) {
		
			throw new ValidFailureException("로그인을 실패하였습니다.", (BindingResult) errors);
		}
		
	}

}
