package shop.newplace.Account.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, name = "EMAIL", length = 50, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String phoneNumber;

    @Column(length = 30, nullable = false)
    private String bankName;

    @Column(length = 30, nullable = false)
    private String accountNumber;

    private boolean emailVerified;

    @Column(length = 30, nullable = false)
    private String failCount;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private LocalDateTime lastLoginAt;

    private LocalDateTime joinedAt;



}
