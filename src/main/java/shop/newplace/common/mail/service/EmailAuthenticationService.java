package shop.newplace.common.mail.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.SpringBootMail;
import shop.newplace.common.mail.model.entity.EmailAuthenticationToken;
import shop.newplace.common.mail.model.repository.EmailRepository;
import shop.newplace.common.util.CipherUtil;

@RequiredArgsConstructor
@Service
public class EmailAuthenticationService {
	private final EmailRepository emailRepository;
	
	private final SpringBootMail springBootMail;

	private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;
	
	public void sendEmailAuthentication(Long userId, String reciverEmail) {
		EmailAuthenticationToken emailAuthenticationToken = createEmailAuthenticationToken(userId);
		emailRepository.findById(userId);
		emailRepository.save(emailAuthenticationToken);
		StringBuilder emailAuthenticationUrl = new StringBuilder();
		emailAuthenticationUrl.append("http://localhost:8080/emailAuthentication?token=");
		emailAuthenticationUrl.append(userId);
		springBootMail.sendMail(CipherUtil.Email.decrypt(reciverEmail), "회원가입 이메일 인증", emailAuthenticationUrl.toString());
	}

	
	public EmailAuthenticationToken createEmailAuthenticationToken(Long userId) {
		return EmailAuthenticationToken.builder()
					 	 .expirationDate(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE)) 
						 .userId(userId)
						 .expired(false)
						 .build();
	}
	

}
