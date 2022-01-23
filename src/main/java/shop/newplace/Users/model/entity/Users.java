package shop.newplace.Users.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import shop.newplace.common.entity.BaseEntity;

@Entity
@Getter
@EqualsAndHashCode(of = "USER_ID")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@DynamicUpdate
public class Users extends BaseEntity implements UserDetails {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;
    
    @OneToMany
	@JoinTable(name = "USERS_PROFILE"
	 , joinColumns = @JoinColumn(name = "USER_ID")
	 , inverseJoinColumns = @JoinColumn(name = "PROFILE_ID"))
    private List<Profiles> profiles = new ArrayList<Profiles>();

    @Column(unique = true, name = "LOGIN_EMAIL", length = 50, nullable = false)
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

    @Column(length = 30, nullable = false)
    private String failCount;
    
    private LocalDateTime lastLoginTime;
    
    @Column(length = 30, nullable = false)
    private String mainPhoneNumber;
    
    private Boolean accountNonExpired;
    
    private Boolean accountNonLocked;
    
    private String authId;
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    	
    	for(String role : authId.split(",")) {
    		roles.add(new SimpleGrantedAuthority(role));
    	}
    	
    	// TODO 권한을 어떻게 넣을지
    	return roles;
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
    	return true;
    }
    
    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
    	// TODO Auto-generated method stub
    	return true;
    }
    
    //패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
    	// TODO Auto-generated method stub
    	return true;
    }
    
    //계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
    	// TODO Auto-generated method stub
    	return true;
    }


}
