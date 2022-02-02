package shop.newplace.Users.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import shop.newplace.Users.model.entity.Users;

public class PrincipalVo extends Users implements UserDetails {
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
    	List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
    	
    	for(String role : super.getAuthId().split(",")) {
    		roles.add(new SimpleGrantedAuthority(role));
    	}
    	
    	// TODO 권한을 어떻게 넣을지
    	return roles;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return super.getPassword();
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return super.getLoginEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
