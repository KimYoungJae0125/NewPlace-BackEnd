package shop.newplace.common.mail.service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.SpringBootMail;
import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.mail.model.dto.EmailResponseDto;
import shop.newplace.common.util.CipherUtil;
import shop.newplace.common.util.RedisUtil;
import shop.newplace.users.advice.exception.NotFoundUsersException;
import shop.newplace.users.model.entity.Users;
import shop.newplace.users.model.repository.UsersRepository;

@RequiredArgsConstructor
@Service
public class EmailService {
	
	private final SpringBootMail springBootMail;
	
	private final UsersRepository usersRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final RedisUtil redisService;
	
	private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 3L;
	
	
	public EmailResponseDto.Info sendEmailAuthentication(String reciverEmail) {
		StringBuffer certificationNumber = new StringBuffer();
		IntStream.range(0, 6).forEach(i -> certificationNumber.append(new Random().ints(0, 9).findFirst().getAsInt()));
		EmailRequestDto.EmailAuthentication emailDto = EmailRequestDto.EmailAuthentication.builder()
																.loginEmail(reciverEmail)
																.certificationNumber(certificationNumber.toString())
																.expirationTime(EMAIL_TOKEN_EXPIRATION_TIME_VALUE)
																.build();
		redisService.setValues(emailDto);
		springBootMail.sendEmailAuthenticationEmail(emailDto);
		
		return EmailResponseDto.Info.builder()
									.certificationNumber(certificationNumber.toString())
									.build();
	}
	
	public EmailResponseDto.Info emailAuthentication(EmailRequestDto.EmailAuthentication emailDto) {
		String redisCertificationNumber = redisService.getValues(emailDto.getLoginEmail());
		boolean emailVerified = false;
		if(redisCertificationNumber != null) {
			emailVerified = redisCertificationNumber.equals(emailDto.getCertificationNumber());
		}
		return EmailResponseDto.Info.builder()
									.emailVerified(emailVerified)
									.build();
		
	}
	
/*
 * 이메일 인증확인 메일 보내고 해당 인증 누를 경우 이메일 인증 가능 시스템 : 인증 확인 버튼 클릭에서 인증번호 방식으로 변경 되어 현재로선 주석 처리
 * 
 	private final EmailRepository emailRepository;

	@Transactional
	public void sendEmailAuthentication(Users users, String reciverEmail) {
		Optional<EmailAuthenticationToken> findToken = emailRepository.findByUserId(users.getId());
		if(findToken.isPresent()) {
			EmailAuthenticationToken oldToken = findToken.get();
			oldToken.useToken();
			emailRepository.save(oldToken);
		}
		EmailAuthenticationToken emailAuthenticationToken = emailRepository.save(createEmailAuthenticationToken(users.getId()));
		StringBuilder emailAuthenticationUrl = new StringBuilder();
		emailAuthenticationUrl.append("http://localhost:8080/email/authentication/");
//		emailAuthenticationUrl.append(findToken.getUserId());
		emailAuthenticationUrl.append(emailAuthenticationToken.getId());
		springBootMail.sendEmailAuthenticationEmail(users, emailAuthenticationUrl.toString());
	}
	
	private EmailAuthenticationToken createEmailAuthenticationToken(Long userId) {
		return EmailAuthenticationToken.builder()
					 	 .expirationDateTime(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE)) 
						 .userId(userId)
						 .expired(false)
						 .certificationNumber("")
						 .build();
	}

	@Transactional
	public void emailAuthentication(Long tokenId) {
		Optional<EmailAuthenticationToken> findEmailToken = Optional.ofNullable(emailRepository.findByIdAndExpirationDateTimeAfterAndExpired(tokenId, LocalDateTime.now(), false)
																.orElseThrow(() -> new UsernameNotFoundException("테스트 익셉션 다른걸로 바꿔야함")));
		EmailAuthenticationToken emailAuthenticationToken = findEmailToken.get();
		emailAuthenticationToken.useToken();
		emailRepository.save(emailAuthenticationToken);
		Users users = usersRepository.findById(emailAuthenticationToken.getUserId())
									 .orElseThrow(() -> new NotFoundUsersException("해당 유저가 존재하지 않습니다.", emailAuthenticationToken.getUserId().toString()));
		users.emailAuthentication();
		usersRepository.save(users);
	}
*/	
	@Transactional
	public void sendTemporaryPassword(String loginEmail) {
		Users users = usersRepository.findByLoginEmail(CipherUtil.Email.encrypt(loginEmail))
				 					 .orElseThrow(() -> new NotFoundUsersException("해당 유저가 존재하지 않습니다.", loginEmail));
		String temporaryPassword = UUID.randomUUID().toString().replaceAll("-", "");
		temporaryPassword = temporaryPassword.substring(0, 10);
		users.changePassword(passwordEncoder.encode(temporaryPassword));
		usersRepository.save(users);
		springBootMail.sendTemporaryPasswordEmail(loginEmail, users.getName(), temporaryPassword);
	}
	
	

}
