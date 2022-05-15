package shop.newplace.users.advice.exception;

import org.springframework.security.authentication.AccountExpiredException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpiredUsersException extends AccountExpiredException {
	
	String loginEmail;

	public ExpiredUsersException(String msg, String loginEmail) {
		super(msg);
		this.loginEmail = loginEmail;
	}
	
}
