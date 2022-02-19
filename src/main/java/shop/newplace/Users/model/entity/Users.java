package shop.newplace.Users.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;
import shop.newplace.common.entity.BaseEntity;
import shop.newplace.common.role.Role;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "profiles")
@DynamicUpdate
public class Users extends BaseEntity implements UserDetails {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    
    @OneToMany(mappedBy = "users")
//	@JoinTable(name = "USERS_PROFILE"
//	 , joinColumns = @JoinColumn(name = "USER_ID")
//	 , inverseJoinColumns = @JoinColumn(name = "PROFILE_ID"))
//    @JoinColumn(name = "USER_ID")
    private List<Profiles> profiles = new ArrayList<Profiles>();

    @Column(unique = true, name = "LOGIN_EMAIL", length = 500, nullable = false)
    private String loginEmail;
    
    private boolean emailVerified;

    @Column(length = 1000, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;
    
    @Column(length = 30)
    private String bankId;

    @Column(length = 30)
    private String accountNumber;

    @Column(length = 1, nullable = false)
    private int failCount;
    
    private LocalDateTime lastLoginTime;
    
    @Column(length = 30, nullable = false)
    private String mainPhoneNumber;
    
    @Getter(AccessLevel.NONE)
    private Boolean accountNonExpired;
    
    @Getter(AccessLevel.NONE)
    private Boolean accountNonLocked;

    @Getter(AccessLevel.NONE)
    private Boolean credentialsNonExpired;
    
    @Getter(AccessLevel.NONE)
    private Boolean enabled;
    
    private int authId;
    
//    @Builder.Default
//    @Transient
//    private List<Integer> roles = new ArrayList<>();
    
    @Singular
    @Transient
    private List<Integer> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	// TODO 권한을 어떻게 넣을지
    	return roles.stream()
    			.map(code -> new SimpleGrantedAuthority(Role.getNameByValue(code)))
    			.collect(Collectors.toList());
    }
    
    @Override
    public String getUsername() {
    	// TODO Auto-generated method stub
    	return loginEmail;
    }
    
    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
    	// TODO Auto-generated method stub
    	return this.accountNonExpired;
    }
    
    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
    	// TODO Auto-generated method stub
    	return this.accountNonLocked;
    }
    
    //패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
    	// TODO Auto-generated method stub
    	return this.credentialsNonExpired;
    }
    
    //계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
    	// TODO Auto-generated method stub
    	return this.enabled;
    }
    
    @PrePersist
    public void preSignUp() {
    	resetFailCount();
    	unlockAccount();
    	unlockAccountExpired();
    	unlockCredentials();
    	EnableAccount();
    }

    public void successLogin() {
        setLastLogin();
        resetFailCount();
        unlockAccount();
        unlockAccountExpired();
        unlockCredentials();
    }
    
    public void failLogin() {
    	addFailCount();
    	if(this.failCount > 5) {
    		lockAccount();
    	}
    }
    
    private void addFailCount() {
    	this.failCount = this.failCount + 1;
    }

    private void setLastLogin() {
        this.lastLoginTime = LocalDateTime.now();
    }

    private void resetFailCount() {
        this.failCount = 0;
    }

    private void unlockAccount() {
    	this.accountNonLocked = true;
    }
    
    private void lockAccount() {
    	this.accountNonLocked = false;
    }
    
    private void unlockAccountExpired() {
    	this.accountNonExpired = true;
    }

    private void lockAccountExpired() {
    	this.accountNonExpired = false;
    }


    private void unlockCredentials() {
    	this.credentialsNonExpired = true;
    }
    
    private void lockCredentials() {
    	this.credentialsNonExpired = false;
    }
    
    private void EnableAccount() {
    	this.enabled = true;
    }
    
    private void disableAccount() {
    	this.enabled = false;
    }

}
