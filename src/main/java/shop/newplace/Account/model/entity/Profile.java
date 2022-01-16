package shop.newplace.Account.model.entity;

import lombok.*;
import shop.newplace.Account.model.dto.ProfileId;
import shop.newplace.Bank.model.dto.Bank;

import javax.persistence.*;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @EmbeddedId
    private ProfileId profileId;

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

    @Builder
    public Profile(String nickname, String email, String introduction, Bank bankId, String accountNumber, String phoneNumber){
        this.nickname = nickname;
        this.email = email;
        this.introduction = introduction;
        this.bankId = bankId;
        this.accountNumber = accountNumber;
        this.phoneNumber = phoneNumber;
    }
}
