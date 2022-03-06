package shop.newplace.common.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.newplace.users.model.entity.Users;
import shop.newplace.common.constant.Role;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

	private Users users;
	
    private String authId;

//    @Builder.Default
    private Set<String> roles;
    
    private Boolean accountNonExpired;
    
    private Boolean accountNonLocked;
    
    private Boolean credentialsNonExpired;
    
    private Boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	// TODO 권한을 어떻게 넣을지
    	// 2022-03-04 영재 : Main 테이블인 Users에는 권한 관련 컬럼 X Detail?(Part?) 테이블인 Profiles에 있는 authId를 이용해 Users가 가진 권한들 표시예정
    	return roles.stream()
    			.map(code -> new SimpleGrantedAuthority(Role.getNameByValue(code)))
    			.collect(Collectors.toList());
    }
    
	@Override
    public String getUsername() {
    	// TODO Auto-generated method stub
    	return users.getLoginEmail();
    }
    
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return users.getPassword();
	}
    
    //계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
    	// TODO Auto-generated method stub
    	return accountNonExpired;
    }
    
    //계정 잠금 여부
    @Override
    public boolean isAccountNonLocked() {
    	// TODO Auto-generated method stub
    	return accountNonLocked;
    }
    
    //패스워드 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
    	// TODO Auto-generated method stub
    	return credentialsNonExpired;
    }
    
    //계정 사용 가능 여부
    @Override
    public boolean isEnabled() {
    	// TODO Auto-generated method stub
    	return enabled;
    }
    
    public void addRole(String role) {
    	this.roles.add(role);
    }
    

}
