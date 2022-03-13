package shop.newplace.common.mail.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.newplace.users.model.dto.UsersDto;

public class EmailDto {
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class ResponseInfo {
		
		private String certificationNumber;
		
		private boolean emailVerified;
	}
	
	@Getter
	@Setter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class RequestEmailAuthentication {
		
	    @NotBlank(message = "이메일은 필수 입력 값입니다.")
	    @Email(message = "이메일 형식에 맞지 않습니다.")
		private String loginEmail;
		
//	    @Length(min = 6, max = 6, message = "인증번호는 6자리를 입력해주세요.")
	    private String certificationNumber;

	    private Long expirationTime;
	}
	
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class RequestTemporyPassword {
		
	    @NotBlank(message = "이메일은 필수 입력 값입니다.")
	    @Email(message = "이메일 형식에 맞지 않습니다.")
	    private String loginEmail;
	    
	    @NotBlank(message = "이름은 필수 입력 값입니다.")
	    @Length(max = 5)
	    private String name;
		
	    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
	    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호 형식에 맞지 않습니다.")
	    private String mainPhoneNumber;
	}
	

}
