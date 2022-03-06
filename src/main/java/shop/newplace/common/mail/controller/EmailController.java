package shop.newplace.common.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.newplace.users.model.dto.UsersDto;
import shop.newplace.common.mail.service.EmailService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
	
	private final EmailService emailService;
	
	@GetMapping("/authentication/{tokenId}")
	public ResponseEntity emailAuthentication(@PathVariable(name = "tokenId") Long tokenId) {
		emailService.emailAuthentication(tokenId);
		return ResponseEntity.ok().body("이메일 인증");
	}
	
	@PostMapping("/temporaryPassword")
	public ResponseEntity temporaryPassword(@RequestBody UsersDto.ResponseInfo usersInfo) {
		emailService.sendTemporaryPassword(usersInfo.getLoginEmail());
		return null;
	}

}
