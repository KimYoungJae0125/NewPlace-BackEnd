package shop.newplace.Users.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import shop.newplace.Users.model.dto.LogInForm;
import shop.newplace.Users.model.dto.SignUpForm;

public interface UsersService extends UserDetailsService {
	
	public int signUp(SignUpForm signUpForm);

	public String login(LogInForm logInForm);


}
