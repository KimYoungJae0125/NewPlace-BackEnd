package shop.newplace.Users.model.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.repository.UsersRepository;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
	
	Logger logger = LoggerFactory.getLogger(SignUpFormValidator.class);

	@Autowired
	private UsersRepository userRepository;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(SignUpForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		SignUpForm signUpForm = (SignUpForm)target;
		
		if(userRepository.existsByLoginEmail(signUpForm.getLoginEmail())) {
			errors.rejectValue("loginEmail", "invalid.loginEmail",
					new Object[] {signUpForm.getLoginEmail()}, "이미 사용중인 이메일입니다.");
		}
		
		if(!signUpForm.getPassword().equals(signUpForm.getPasswordVerified())) {
			errors.rejectValue("password", "invalid.password",
					new Object[] {signUpForm.getPassword(), signUpForm.getPasswordVerified()}, "비밀번호가 일치하지 않습니다.");
		}
		
		
	}

}
