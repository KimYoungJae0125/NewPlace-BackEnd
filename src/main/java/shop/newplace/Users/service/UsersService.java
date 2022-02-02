package shop.newplace.Users.service;

import shop.newplace.Users.model.dto.JwtForm;
import shop.newplace.Users.model.dto.SignInForm;
import shop.newplace.Users.model.dto.SignUpForm;
import shop.newplace.Users.model.entity.Users;
import shop.newplace.common.advice.exception.ValidFailureException;

public interface UsersService {
	
	public Users signUp(SignUpForm signUpForm);

	public JwtForm signIn(SignInForm logInForm);

}
