package shop.newplace.Users.model.validator;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.common.advice.exception.ValidFailureException;
import shop.newplace.common.config.CustomAuthenticationProvider;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogInFormValidator implements Validator {
	
	private final CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(LogInForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		if(errors.hasErrors()) {
			throw new ValidFailureException("로그인을 실패하였습니다.", (BindingResult) errors);
		}
		LogInForm loginForm = (LogInForm)target;
		customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getLoginEmail(), loginForm.getPassword()));
	}

}
