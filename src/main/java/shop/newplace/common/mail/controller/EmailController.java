package shop.newplace.common.mail.controller;


import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.mail.model.dto.EmailResponseDto;
import shop.newplace.common.mail.model.validator.EmailValidator;
import shop.newplace.common.mail.service.EmailService;
import shop.newplace.common.response.ResponseMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
	
	private final EmailService emailService;
	
	private final EmailValidator.Authentication emailAuthenticationValidator;
	
	private final EmailValidator.TemporyPassword emailTemporyPasswordValidator;
	
	@InitBinder
	public void addEmailValidator(WebDataBinder webDataBinder) {
		if(webDataBinder.getTarget() instanceof EmailRequestDto.EmailAuthentication) {
			webDataBinder.addValidators(emailAuthenticationValidator);
		}
		if(webDataBinder.getTarget() instanceof EmailRequestDto.TemporyPassword) {
			webDataBinder.addValidators(emailTemporyPasswordValidator);
		}
	}
	
	
	@GetMapping(value = "/authentication", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity emailAuthentication(@Valid @RequestBody EmailRequestDto.EmailAuthentication emailDto) {
		EmailResponseDto.Info emailInfo = emailService.emailAuthentication(emailDto);
		String descriptionMessage = emailInfo.isEmailVerified() ? "이메일 인증에 성공하였습니다." : "이메일 인증에 실패하셨습니다.";
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), descriptionMessage, emailInfo));
	}

	@PostMapping("/authentication")
	public ResponseEntity sendEmailAuthentication(@Valid @RequestBody EmailRequestDto.EmailAuthentication emailDto) {
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), emailService.sendEmailAuthentication(emailDto.getLoginEmail())));
	}
	
	@PostMapping("/temporaryPassword")
	public ResponseEntity temporaryPassword(@Valid @RequestBody EmailRequestDto.TemporyPassword usersTemporyPassword) {
		emailService.sendTemporaryPassword(usersTemporyPassword.getLoginEmail());
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "임시 비밀번호가 발송 되었습니다."));
	}

/*
	@GetMapping("/authentication/{tokenId}")
	public ResponseEntity emailAuthentication(@PathVariable(name = "tokenId") Long tokenId) {
		emailService.emailAuthentication(tokenId);
		return ResponseEntity.ok().body(ResponseMessage.OK(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "이메일 인증 성공"));
	}
 */	
}
