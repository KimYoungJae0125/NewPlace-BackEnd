package shop.newplace.users.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.newplace.account.model.entity.Account;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {
    @Id
    private Long id;

    @Email(message = "Email 형식이 아닙니다.")
    @NotBlank(message = "Email을 입력해주세요.")
    private String email;

    @NotBlank
    //@Length(min = 8, max = 12)
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$", message = "최소 8 자 및 최대 12 자인 하나 이상의 대문자, 하나의 소문자, 하나의 숫자 및 하나의 특수 문자를 포함해서 입력해주세요")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    @Length(max = 10, message = "이름의 최대 길이는 10글자 입니다.")
    private String name;

    @NotBlank(message = "은행을 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private String bankId;

    private String accountNumber;

    private String failCount; // 로그인 실패 횟수

    private boolean accountExpired;

    private boolean accountLocked;

    private LocalDateTime lastLoginAt;

    @Column(length = 30, nullable = false)
    private String mainPhoneNumber;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    public Account toEntity(){
        return Account.builder()
                .userId(id)
                .email(email)
                .password(password)
                .name(name)
//                .bankId(bankId)
                .accountNumber(accountNumber)
                .failCount(failCount)
                .accountExpired(accountExpired)
                .accountLocked(accountLocked)
                .lastLoginAt(lastLoginAt)
                .mainPhoneNumber(mainPhoneNumber)
                .joinedAt(joinedAt)
                .emailVerified(emailVerified)
                .build();
    }

    public void setEncodedPassword(String password){
        this.password = password;
    }
}
