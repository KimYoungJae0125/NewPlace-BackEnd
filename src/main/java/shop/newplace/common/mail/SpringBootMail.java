package shop.newplace.common.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.util.CipherUtil;

@RequiredArgsConstructor
@Service
public class SpringBootMail {
	
	private final JavaMailSender javaMailSender;
	
	private final SpringTemplateEngine templateEngine;
	
	@Value("${spring.mail.username}")
	private String SENDER; 
	
	private final ResourceLoader resourceLoader;
	
	/*
	 * SimpleMessage : 텍스트 데이터만 전송 가능
	 * MimeMessage   : 멀티파트 데이터(이미지, 파일)도 같이 전송 가능
	 */
	
//	@Async
//	public void sendMail(String reciverEmail, String mailSubject, String mailText) {
//		SimpleMailMessage simpleMessage = new SimpleMailMessage();
//		simpleMessage.setTo(reciverEmail);
//		simpleMessage.setFrom(SENDER);
//		simpleMessage.setSubject(mailSubject);
//		simpleMessage.setText(mailText);
//		javaMailSender.send(simpleMessage);
//	}
/*	
	@Async
	public void sendEmailAuthenticationEmail(Users users, String emailAuthenticationUrl) {
		Map<String, Object> thymeleafVariableMap = new HashMap<String, Object>();
		thymeleafVariableMap.put("emailAuthenticationUrl", emailAuthenticationUrl);
		thymeleafVariableMap.put("userName", CipherUtil.Name.decrypt(users.getName()));

		MimeMessage mimeMessage = setMimeMessage(users, "회원가입 이메일 인증", thymeleafVariableMap, "authenticationEmail");
		
		javaMailSender.send(mimeMessage);
	}
*/	
	@Async
	public void sendEmailAuthenticationEmail(EmailRequestDto.EmailAuthentication emailDto) {
		Map<String, Object> thymeleafVariableMap = new HashMap<String, Object>();
		thymeleafVariableMap.put("certificationNumber", emailDto.getCertificationNumber());
		
		MimeMessage mimeMessage = setMimeMessage(emailDto.getLoginEmail(), "회원가입 이메일 인증", thymeleafVariableMap, "authenticationEmail");
		
		javaMailSender.send(mimeMessage);
	}
	
	@Async
	public void sendTemporaryPasswordEmail(String reciverEmail, String userName, String temporyPassword) {
		Map<String, Object> thymeleafVariableMap = new HashMap<String, Object>();
		thymeleafVariableMap.put("temporaryPassword", temporyPassword);
		thymeleafVariableMap.put("userName", CipherUtil.Name.decrypt(userName));

		MimeMessage mimeMessage = setMimeMessage(reciverEmail, "임시 비밀번호 발송", thymeleafVariableMap, "temporaryPasswordEmail");
		
		javaMailSender.send(mimeMessage);
	}
	
	private MimeMessage setMimeMessage(String reciverEmail, String mailSubject, Map<String, Object> thymeleafVariableMap, String template) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeHelper.setTo(reciverEmail);
			mimeHelper.setFrom(SENDER);
			mimeHelper.setSubject(mailSubject);
			mimeHelper.setText(setContext(thymeleafVariableMap, template), true);
			try {
				Resource resource = resourceLoader.getResource("classpath:/static/images/logoHeader.png");
				mimeHelper.addInline("logoHeader.png", resource.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mimeMessage;
	}

//	public void sendAuthenticationMail(EmailAuthenticationToken emailAuthenticationToken, String reciverEmail) {
//		this.sendMail(reciverEmail, reciverEmail, reciverEmail);
//	}
	
	private String setContext(Map<String, Object> thymeleafVariableMap, String template) {
		Context context = new Context();
		context.setVariables(thymeleafVariableMap);
		return templateEngine.process(template, context);
	}
	
}
