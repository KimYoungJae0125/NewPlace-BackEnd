package shop.newplace.common.advice.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class NotFoundUsersException extends UsernameNotFoundException {

	private String loginEmail;
	
	public NotFoundUsersException(String msg, String loginEmail) {
		super(msg);
		this.loginEmail = loginEmail;
	}
	
}
