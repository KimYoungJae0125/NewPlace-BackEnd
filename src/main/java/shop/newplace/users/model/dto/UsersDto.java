package shop.newplace.users.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

public class UsersDto {
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class ResponseInfo {
		private Long userId;
		private String loginEmail;
		private String name;
		private String bankId;
		private String accountNumber;
		private String mainPhoneNumber;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor	//Builder와 NoArgsConstructor를 위해 필요함
	@NoArgsConstructor	//json parsing을 위해.. 알아보자
	@Accessors(chain = true)
	public static class RequestSignUp {
		    @NotBlank(message = "이메일은 필수 입력 값입니다.")
		    @Email(message = "이메일 형식에 맞지 않습니다.")
		    private String loginEmail;

		    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
		    @Length(min = 8)
		    @Pattern(regexp = "^[A-Za-z0-9-_!@#$%^&*()]{8,}$", message = "비밀번호는 숫자, 영어, 특수문자 포함 8자리 이상으로 입력해주세요.")
		    private String password;

		    @NotBlank(message = "비밀번호를 한 번 더 입력해주세요.")
		    @Length(min = 8)
		    private String passwordVerified;
		    
		    @NotBlank(message = "이름은 필수 입력 값입니다.")
		    @Length(max = 5)
		    private String name;
		    
		    private String bankId;
		    
		    private String accountNumber;
		    
		    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
		    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호 형식에 맞지 않습니다.")
		    private String mainPhoneNumber;

			private String test;
		    
//		    @JsonIgnore
//		    @Builder.Default
//		    private Set<String> roles = new HashSet<String>();
		//    
//		    public SignUpForm setRoles(String authId) {
//		    	roles.add(authId);
//		    	return this;
//		    }
		//    
//		    public SignUpForm setRoles(Set<String> roles) {
//		    	this.roles = roles;
//		    	return this;
//		    }
	}
	
	@Getter
	@Builder @AllArgsConstructor @NoArgsConstructor
	public static class RequestLogIn {

	    @NotBlank(message = "이메일은 필수 입력 값입니다.")
	    @Email(message = "이메일 형식에 맞지 않습니다.")
	    private String loginEmail;

	    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	    @Length(min = 8)
	    @Pattern(regexp = "^[A-Za-z0-9-_!@#$%^&*()]{8,}$", message = "비밀번호는 숫자, 영어, 특수문자 포함 8자리 이상으로 입력해주세요.")
	    private String password;
	    
	}
	
    
}
