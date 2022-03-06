package shop.newplace.users.model.entity;

import lombok.*;
import shop.newplace.common.constant.Bank;

import javax.persistence.*;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Column(length = 6, nullable = true)
    private String nickname;

    private String email;

    @Column(length = 140)
    private String introduction;

    private Bank bankId;

    @Column(length = 30, nullable = true)
    private String accountNumber;

    @Column(length = 11)
    private String phoneNumber;

    @Column(length = 10)
    private String authId;
}
