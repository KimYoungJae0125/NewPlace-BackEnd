package shop.newplace.users.model.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.users.model.dto.ProfilesDto;
import shop.newplace.users.model.repository.ProfilesRepository;
import shop.newplace.users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.ValidFailureException;


@Slf4j
public class ProfilesValidator {
	
	@Component
	@RequiredArgsConstructor
	public static class SignUp implements Validator{
		private final UsersRepository usersRepository;

		private final ProfilesRepository profilesRepository;

		@Override
			public boolean supports(Class<?> clazz) {
				return clazz.isAssignableFrom(ProfilesDto.SignUp.class);
			}
		
		@Override
		public void validate(Object target, Errors errors) {
			ProfilesDto.SignUp profileSignUpForm = (ProfilesDto.SignUp) target;
			Long userId = profileSignUpForm.getUserId();
			
			if(!usersRepository.existsById(userId)) {
				errors.rejectValue("userId", "invalid.userId",
						new Object[] {profileSignUpForm.getUserId()}, "존재하지 않는 사용자입니다.");
			}
			
			//countById
			Long userInProfileCount = profilesRepository.countByUsersId(userId);
			log.info("profilesCount : " + userInProfileCount);
			if(userInProfileCount > 2) {
				errors.rejectValue("profilesCount", "invalid.profilesCount",
						new Object[] {userInProfileCount}, "최대 프로필 개수를 초과하였습니다.");
			}
			
			if(errors.hasErrors()) {
				
				throw new ValidFailureException("프로필 생성이 실패하였습니다.", (BindingResult) errors);
			}		
		}
	}
}
