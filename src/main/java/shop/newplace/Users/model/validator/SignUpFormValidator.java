package shop.newplace.Users.model.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.util.CipherUtil;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignUpFormValidator implements Validator {
	
	private final UsersRepository userRepository;

	private final CipherUtil.Email cipherEmail;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(SignUpForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		SignUpForm signUpForm = (SignUpForm)target;
		
		String loginEmail = "";
		try {
			loginEmail = cipherEmail.encrypt(signUpForm.getLoginEmail());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(userRepository.existsByLoginEmail(loginEmail)) {
			errors.rejectValue("loginEmail", "invalid.loginEmail",
					new Object[] {signUpForm.getLoginEmail()}, "이미 사용중인 이메일입니다.");
		}
		
		if(!signUpForm.getPassword().equals(signUpForm.getPasswordVerified())) {
			errors.rejectValue("password", "invalid.password",
					new Object[] {signUpForm.getPassword(), signUpForm.getPasswordVerified()}, "비밀번호가 일치하지 않습니다.");
		}
		
		
	}

}
