package shop.newplace.common.mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.service.EmailAuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emailAuthentication")
public class EmailAuthenticationController {
	
	private final EmailAuthenticationService emailAuthenticationService;
	
	@GetMapping("/{tokenId}")
	public ResponseEntity test(@PathVariable(name = "tokenId") Long tokenId) {
		emailAuthenticationService.emailAuthentication(tokenId);
		
		
		return ResponseEntity.ok().body("이메일 인증");
	}

}
