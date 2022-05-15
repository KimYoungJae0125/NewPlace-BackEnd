package shop.newplace.users.advice.exception;

import org.springframework.security.authentication.CredentialsExpiredException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ExpiredPasswordException extends CredentialsExpiredException {

	private String password;
	
	public ExpiredPasswordException(String msg, String password) {
		super(msg);
		this.password = password;
	}
	
}
