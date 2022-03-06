package shop.newplace.common.advice.exception;

import org.springframework.security.authentication.DisabledException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class DisabledUsersException extends DisabledException {

	String loginEmail;
	
	public DisabledUsersException(String msg, String loginEmail) {
		super(msg);
		this.loginEmail = loginEmail;
	}
	
}
