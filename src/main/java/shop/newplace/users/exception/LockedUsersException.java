package shop.newplace.users.exception;

import org.springframework.security.authentication.LockedException;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class LockedUsersException extends LockedException {
	
	String loginEmail;

	public LockedUsersException(String msg, String loginEmail) {
		super(msg);
		this.loginEmail = loginEmail;
	}
	
}
