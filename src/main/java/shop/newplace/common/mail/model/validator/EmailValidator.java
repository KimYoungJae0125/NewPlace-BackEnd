package shop.newplace.common.mail.model.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.common.exception.ValidFailureException;
import shop.newplace.common.mail.model.dto.EmailDto;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.users.repository.UsersRepository;


@Slf4j
public class EmailValidator {
	
	@Component
	@RequiredArgsConstructor
	public static class Authentication implements Validator{
		private final UsersRepository userRepository;
		
		@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(EmailDto.RequestEmailAuthentication.class);
		}
		
		@Override
		public void validate(Object target, Errors errors) {
			EmailDto.RequestEmailAuthentication emailDto = (EmailDto.RequestEmailAuthentication)target;
			String loginEmail = CipherUtil.Email.encrypt(emailDto.getLoginEmail());
			if(userRepository.existsByLoginEmail(loginEmail)) {
				errors.rejectValue("loginEmail", "invalid.loginEmail",
						new Object[] {emailDto.getLoginEmail()}, "이미 사용중인 이메일입니다.");
			}
			if(errors.hasErrors()) {
				System.out.println(errors.toString());
				throw new ValidFailureException("이메일 인증에 필요한 정보가 부족합니다.", (BindingResult) errors);
			}
		}
	}
	
	@Component
	@RequiredArgsConstructor
	public static class TemporyPassword implements Validator {

		private final UsersRepository userRepository;

		@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(EmailDto.RequestTemporyPassword.class);
		}
		
		@Override
		public void validate(Object target, Errors errors) {
			EmailDto.RequestTemporyPassword emailDto = (EmailDto.RequestTemporyPassword)target;
			String loginEmail = CipherUtil.Email.encrypt(emailDto.getLoginEmail());
			if(!userRepository.existsByLoginEmail(loginEmail)) {
				errors.rejectValue("loginEmail", "invalid.loginEmail",
						new Object[] {emailDto.getLoginEmail()}, "해당 유저가 존재하지 않습니다.");
			}
			if(errors.hasErrors()) {
				System.out.println(errors.toString());
				throw new ValidFailureException("임시 비밀번호 발송에 필요한 정보가 부족합니다.", (BindingResult) errors);
			}

		}
	}
	
}
