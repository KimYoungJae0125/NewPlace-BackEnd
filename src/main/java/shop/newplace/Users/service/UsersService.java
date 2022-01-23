package shop.newplace.Users.service;

import shop.newplace.Users.model.dto.JwtForm;
import shop.newplace.Users.model.dto.ProfileSignUpForm;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Profiles;
import shop.newplace.Users.model.entity.Users;

public interface UsersService {
	
	public Users signUp(SignUpForm signUpForm) throws Exception;

	public JwtForm signIn(SignInForm logInForm) throws Exception;

	public Profiles profileSignUp(ProfileSignUpForm profileSignUpForm) throws Exception;

}
