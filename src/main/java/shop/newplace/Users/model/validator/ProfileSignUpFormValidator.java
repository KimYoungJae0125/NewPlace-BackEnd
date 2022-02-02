package shop.newplace.Users.model.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;
import shop.newplace.common.advice.exception.ValidFailureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSignUpFormValidator implements Validator {
	
	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;

	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(ProfileSignUpForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileSignUpForm profileSignUpForm = (ProfileSignUpForm) target;
		Long userId = profileSignUpForm.getUserId();
		
		if(!usersRepository.existsById(userId)) {
			errors.rejectValue("userId", "invalid.userId",
					new Object[] {profileSignUpForm.getUserId()}, "존재하지 않는 사용자입니다.");
		}
		
		//countById
		Long userInProfileCount = profilesRepository.countByUsers(Users.builder().id(userId).build());
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
