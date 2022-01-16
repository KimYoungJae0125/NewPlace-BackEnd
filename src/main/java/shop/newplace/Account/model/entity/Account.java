package shop.newplace.Account.model.entity;

import lombok.*;
import shop.newplace.Bank.model.dto.Bank;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "userId")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, length = 50, nullable = false)
    private String loginEmail;

    @Column(length = 12, nullable = false)
    private String password;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(nullable = true)
    private Bank bankId;

    @Column(length = 30, nullable = true)
    private String accountNumber;

    @Column(length = 10, nullable = false)
    private String failCount;

    private boolean accountExpired;

    private boolean accountLocked;

    private LocalDateTime lastLoginAt;

    @Column(length = 30, nullable = false)
    private String mainPhoneNumber;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    @Builder
    public Account(String loginEmail, String password, String name, Bank bankId, String accountNumber, String failCount, boolean accountExpired, boolean accountLocked, LocalDateTime lastLoginAt, String mainPhoneNumber, LocalDateTime joinedAt, boolean emailVerified){
        this.loginEmail = loginEmail;
        this.password = password;
        this.name = name;
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.failCount = failCount;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.lastLoginAt = lastLoginAt;
        this.mainPhoneNumber = mainPhoneNumber;
        this.joinedAt = joinedAt;
        this.emailVerified = emailVerified;
    }
}
