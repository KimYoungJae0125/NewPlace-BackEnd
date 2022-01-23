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

    @Column
    private boolean emailVerified;
}
