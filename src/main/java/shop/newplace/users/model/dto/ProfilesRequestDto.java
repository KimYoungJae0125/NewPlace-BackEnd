package shop.newplace.users.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import shop.newplace.users.model.entity.Users;

public class ProfilesRequestDto {
	
	@Getter
	@Setter
	@Builder @AllArgsConstructor @NoArgsConstructor
	@Accessors(chain = true)
	public static class SignUp{
		private Long userId;
		
		private Users users;
		
	    @Email(message = "이메일 형식에 맞지 않습니다.")
	    private String email;

	    @NotBlank(message = "닉네임은 필수 입력값입니다.")
	    private String nickName;
	    
	    private String introduction;
	    
	    private String bankId;
	    
	    private String accountNumber;
	    
	    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "전화번호 형식에 맞지 않습니다.")
	    private String phoneNumber;
	    
	    private String authId;
	}
	
 
    
}
