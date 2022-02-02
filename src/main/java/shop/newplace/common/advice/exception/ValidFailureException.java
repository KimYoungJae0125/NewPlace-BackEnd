package shop.newplace.common.advice.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ValidFailureException extends RuntimeException {

	private BindingResult bindingResult;

	public ValidFailureException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public ValidFailureException(String msg) {
		super(msg);
	}

	public ValidFailureException(String msg, BindingResult bindingResultParam) {
		super(msg);
		this.bindingResult = bindingResultParam;
	}
	
	public ValidFailureException() {
		super();
	}
	

}
