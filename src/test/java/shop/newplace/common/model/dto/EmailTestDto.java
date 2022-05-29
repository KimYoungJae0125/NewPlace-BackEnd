package shop.newplace.common.model.dto;

import java.util.Random;
import java.util.stream.IntStream;

import shop.newplace.common.mail.model.dto.EmailRequestDto;
import shop.newplace.common.model.vo.UsersTestVo;

public class EmailTestDto {
	
	private UsersTestVo usersTestVo = new UsersTestVo();

	public EmailRequestDto.EmailAuthentication createEmailAuthenticationForm(){
		return EmailRequestDto.EmailAuthentication.builder()
												  .loginEmail(usersTestVo.getLoginEmail())
												  .certificationNumber(null)
												  .expirationTime(null)
												  .build();
	}
	
	public EmailRequestDto.EmailAuthentication createEmailAuthenticationFormByWrongCertificationNumber(){
		return EmailRequestDto.EmailAuthentication.builder()
												  .loginEmail(usersTestVo.getLoginEmail())
												  .certificationNumber(certificationNumber())
												  .expirationTime(null)
												  .build();
	}

	public EmailRequestDto.EmailAuthentication createNullEmailAuthenticationForm (){
		return EmailRequestDto.EmailAuthentication.builder()
												  .loginEmail(null)
												  .certificationNumber(null)
												  .expirationTime(null)
												  .build();
								
	}
	
    private String certificationNumber() {
		StringBuffer certificationNumber = new StringBuffer();
		IntStream.range(0, 6).forEach(i -> certificationNumber.append(new Random().ints(0, 9).findFirst().getAsInt()));
		return certificationNumber.toString();
    }
}
