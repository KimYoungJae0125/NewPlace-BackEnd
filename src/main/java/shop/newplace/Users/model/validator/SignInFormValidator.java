package shop.newplace.Users.model.validator;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.util.CipherUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignInFormValidator implements Validator {
	
	private final UsersRepository usersRepository;
	
	private final CipherUtil.Email cipherEmail;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(SignUpForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		SignInForm signInForm = (SignInForm)target;
		
		String loginEmail = "";
		try {
			loginEmail = cipherEmail.encrypt(signInForm.getLoginEmail());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Users usersInfo = usersRepository.findByLoginEmail(loginEmail)
				.orElseThrow(() -> new UsernameNotFoundException(signInForm.getLoginEmail() + "해당 사용자가 존재하지 않습니다"));
		
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
		
		
	}

}
