package shop.newplace.Account.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import shop.newplace.Bank.model.dto.Bank;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SignupResponseDto {
    private Long userId;
    private String email;
    private String password;
    private String name;
    private Bank bankId;
    private String accountNumber;
    private String failCount; // 로그인 실패 횟수
    private boolean accountExpired;
    private boolean accountLocked;
    private LocalDateTime lastLoginAt;
    private String mainPhoneNumber;
    private LocalDateTime joinedAt;
    private boolean emailVerified;
}
