package shop.newplace.users.exception;

import org.springframework.security.authentication.BadCredentialsException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class NotMatchPasswordException extends BadCredentialsException {

	private String password;
	
	public NotMatchPasswordException(String msg, String password) {
		super(msg);
		this.password = password;
	}
	
}
