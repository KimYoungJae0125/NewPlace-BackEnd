package shop.newplace.users.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.newplace.common.response.ResponseMessage;
import shop.newplace.common.response.ResponseMessage.ErrorFiled;
import shop.newplace.users.controller.ProfilesController;
import shop.newplace.users.controller.UsersController;

@RestControllerAdvice(basePackageClasses = {UsersController.class, ProfilesController.class})
public class UsersExceptionAdvice {
	
	@ExceptionHandler(NotFoundUsersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage notFoundUsersExceptionHandler(NotFoundUsersException e) {
		//e.printStackTrace();		
		return loginError(e.getMessage(), e.getLoginEmail());
	}

	@ExceptionHandler(DisabledUsersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage disabledUsersExceptionHandler(DisabledUsersException e) {
		//e.printStackTrace();		
		return loginError(e.getMessage(), e.getLoginEmail());
	}

	@ExceptionHandler(LockedUsersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage lockedUsersExceptionHandler(LockedUsersException e) {
		//e.printStackTrace();		
		return loginError(e.getMessage(), e.getLoginEmail());
	}

	@ExceptionHandler(ExpiredUsersException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage expiredUsersExceptionHandler(ExpiredUsersException e) {
		//e.printStackTrace();		
		return loginError(e.getMessage(), e.getLoginEmail());
	}
	
	private ResponseMessage loginError(String exceptionMessage, String loginEmail) {
		return ResponseMessage.LOGIN_FAILURE_EXCEPTION_MESSAGE(
				HttpStatus.BAD_REQUEST.value()
				, HttpStatus.BAD_REQUEST.getReasonPhrase()
				, exceptionMessage
				, errorFileds(new ErrorFiled("loginEmail", exceptionMessage, loginEmail)));
	}

	@ExceptionHandler(NotMatchPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage notMatchPasswordExceptionHandler(NotMatchPasswordException e) {
		//e.printStackTrace();	
		return passwordError(e.getMessage(), e.getPassword());
	}
	
	@ExceptionHandler(ExpiredPasswordException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage expiredPasswordExceptionHandler(ExpiredPasswordException e) {
		//e.printStackTrace();		
		return passwordError(e.getMessage(), e.getPassword());
	}
	

	private ResponseMessage passwordError(String exceptionMessage, String password) {
		return ResponseMessage.LOGIN_FAILURE_EXCEPTION_MESSAGE(
				HttpStatus.BAD_REQUEST.value()
				, HttpStatus.BAD_REQUEST.getReasonPhrase()
				, exceptionMessage
				, errorFileds(new ErrorFiled("password", exceptionMessage, password)));
	}

	private List<ErrorFiled> errorFileds(ErrorFiled errorFiled){
		List<ErrorFiled> errorFileds = new ArrayList<ErrorFiled>();
		errorFileds.add(errorFiled);
		return errorFileds;
	}

}
