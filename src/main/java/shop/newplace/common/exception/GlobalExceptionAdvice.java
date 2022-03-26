package shop.newplace.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.newplace.common.response.ResponseMessage;

@RestControllerAdvice
public class GlobalExceptionAdvice {
	
	@ExceptionHandler(ValidFailureException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage signUpRValidFailureHandler(ValidFailureException e) {
		//e.printStackTrace();
		return validError(e.getMessage(), e.getBindingResult());
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ResponseMessage signUpValidFailureExceptionHandler(MethodArgumentNotValidException e) {
		//e.printStackTrace();
        return validError(e.getMessage(), e.getBindingResult());
	}
	
	private ResponseMessage validError(String exceptionMessage, BindingResult bindingResult) {
		return ResponseMessage.VALID_FAILURE_EXCEPTION_MESSAGE(
				HttpStatus.BAD_REQUEST.value()
				, HttpStatus.BAD_REQUEST.getReasonPhrase()
				, exceptionMessage
				, bindingResult
				);
	}

}
