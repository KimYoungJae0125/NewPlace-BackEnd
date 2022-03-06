package shop.newplace.users.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import shop.newplace.common.constant.Bank;

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
