package shop.newplace.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.newplace.Users.controller.ProfilesController;
import shop.newplace.Users.controller.UsersController;
import shop.newplace.common.advice.exception.ValidFailureException;
import shop.newplace.common.response.ResponseMessage;

@RestControllerAdvice(basePackageClasses = {UsersController.class, ProfilesController.class})
public class UsersExceptionAdvice {
	
	@ExceptionHandler(ValidFailureException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage signUpRValidFailureHandler(ValidFailureException e) {

		e.printStackTrace();
		
		return ResponseMessage.VALID_FAILURE_EXCEPTION_MESSAGE(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), e.getBindingResult());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage signUpValidFailureExceptionHandler(MethodArgumentNotValidException e) {

		e.printStackTrace();

        return ResponseMessage.VALID_FAILURE_EXCEPTION_MESSAGE(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), e.getBindingResult());
	}


}
