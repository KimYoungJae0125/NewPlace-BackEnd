package shop.newplace.common.advice.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class ValidFailureException extends RuntimeException {

	private BindingResult bindingResult;
	
	public ValidFailureException(String msg, BindingResult bindingResultParam) {
		super(msg);
		this.bindingResult = bindingResultParam;
	}
	
}
