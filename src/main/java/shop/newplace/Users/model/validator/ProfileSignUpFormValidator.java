package shop.newplace.Users.model.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.repository.ProfilesRepository;
import shop.newplace.Users.model.repository.UsersRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileSignUpFormValidator implements Validator {
	
	private final UsersRepository usersRepository;

	private final ProfilesRepository profilesRepository;

	@Override
		public boolean supports(Class<?> clazz) {
			return clazz.isAssignableFrom(SignUpForm.class);
		}
	
	@Override
	public void validate(Object target, Errors errors) {
		ProfileSignUpForm profileSignUpForm = (ProfileSignUpForm) target;
		Long userId = profileSignUpForm.getUserId();
		
		log.info("2");
		if(!usersRepository.existsById(userId)) {
			errors.rejectValue("userId", "invalid.userId",
					new Object[] {profileSignUpForm.getUserId()}, "존재하지 않는 사용자입니다.");
		}
		
		log.info("1");
		
		
		//countById
		List<Profiles> profilesList = profilesRepository.findAllById(userId);
		log.info("profilesList : " + profilesList);
		log.info("profileSize : " + profilesList.size());
		if(profilesList.size() > 3) {
			errors.rejectValue("profiles", "invalid.profiles",
					new Object[] {profilesList.size()}, "최대 프로필 개수를 초과하였습니다.");
		}
		
	}

}
