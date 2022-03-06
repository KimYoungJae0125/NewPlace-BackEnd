package shop.newplace.users.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.newplace.common.entity.BaseEntity;
import shop.newplace.common.util.DateUtil;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "profiles")
@DynamicUpdate
public class Users extends BaseEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    
    @OneToMany(mappedBy = "users")
//	@JoinTable(name = "USERS_PROFILE"
//	 , joinColumns = @JoinColumn(name = "USER_ID")
//	 , inverseJoinColumns = @JoinColumn(name = "PROFILE_ID"))
//    @JoinColumn(name = "USER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Profiles> profiles = new ArrayList<Profiles>();

    @Column(unique = true, name = "LOGIN_EMAIL", length = 500, nullable = false)
    private String loginEmail;
    
    @Column(length = 1000, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;
    
    @Column(length = 30)
    private String bankId;

    @Column(length = 30)
    private String accountNumber;

    @Column(length = 30, nullable = false)
    private String mainPhoneNumber;

    @Column(length = 1, nullable = false)
    private int failCount;
    
    @Column
    private boolean emailVerified;
  
    @Column
    private LocalDateTime passwordExpiredTime;
    
    @Column
    private LocalDateTime lastLoginTime;
    
    @Builder
    //@AllArgsConstructor를 안 쓰는 이유 : id같은 경우 autoIncrement 되는 컬럼은 생성자에 넣지 않는게 좋다고 하여 필요한 컬럼만 생성자로 만든 후 Builder 패턴을 적용시킨다.
    public Users(String loginEmail, String password, String name, String bankId, String accountNumber, String mainPhoneNumber) {
    	this.loginEmail = loginEmail;
    	this.password = password;
    	this.name = name;
    	this.bankId = bankId;
    	this.accountNumber = accountNumber;
    	this.mainPhoneNumber = mainPhoneNumber;
    }
    
    @PrePersist
    public void preSignUp() {
    	resetFailCount();
    	setPasswordExpiredTime();
    	setLastLogin();
    }

    //로그인 성공
    public void successLogin() {
        setLastLogin();
        resetFailCount();
    }
    
    //로그인 실패
    public void failLogin() {
    	addFailCount();
    }
    
    //이메일 인증 완료
    public void emailAuthentication() {
    	finishEmailAuthentication();
    }
    
    //패스워드 만료시 연장 버튼 클릭하면 나옴
    public void passwordExtensionOfExpiryPeriod() {
    	setPasswordExpiredTime();
    }
    
    public void changePassword(String password) {
    	this.password = password;
    }
    
    private void finishEmailAuthentication() {
    	this.emailVerified = true;
    }
    
    private void setPasswordExpiredTime() {
    	this.passwordExpiredTime = DateUtil.todayPlusMonths(3);
    }
    
    private void addFailCount() {
    	this.failCount += 1;
    }

    private void setLastLogin() {
        this.lastLoginTime = DateUtil.getToday();
    }

    private void resetFailCount() {
        this.failCount = 0;
    }

}
