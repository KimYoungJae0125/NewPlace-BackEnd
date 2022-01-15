package shop.newplace.Account.model.entity;

import lombok.*;

import javax.persistence.*;
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

    @Column(length = 16, nullable = true)
    private String bankId;

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
}
