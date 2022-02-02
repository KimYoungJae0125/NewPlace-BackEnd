package shop.newplace.Users.model.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String loginEmail;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8)
    @Pattern(regexp = "^[A-Za-z0-9-_!@#$%^&*()]{8,}$", message = "비밀번호는 숫자, 영어, 특수문자 포함 8자리 이상으로 입력해주세요.")
    private String password;
    
    private LocalDateTime lastLoginAt;

}
