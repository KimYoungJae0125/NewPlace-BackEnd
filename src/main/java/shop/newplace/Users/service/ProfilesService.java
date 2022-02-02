package shop.newplace.Users.service;

import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.entity.Profiles;

public interface ProfilesService {
	
	public Profiles profileSignUp(ProfileSignUpForm profileSignUpForm);

}
