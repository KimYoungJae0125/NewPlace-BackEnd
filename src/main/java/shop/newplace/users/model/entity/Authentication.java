package shop.newplace.users.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authenticationId;

    //program = 구글 로그인인지 네이버 로그인인지?
    @Column
    private Long program;

    @Column
    private String nickname;
}
