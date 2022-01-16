package shop.newplace.Users.model.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.mapping.AccessOptions.GetOptions.GetNulls;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.newplace.common.entity.BaseEntity;

//@Entity
//@Getter
//@EqualsAndHashCode(of = "profileId")
//@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString
public class Profiles extends BaseEntity implements UserDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long profileId;
	
	@Id
	@OneToMany
	private Long UserId;
	
	@Column(length = 10, nullable = false)
	private String NickName;
	
	@Column(length = 50)
	private String email;
	
	@Column(length = 1000)
	private String introduction;
	
	@Column(length = 30)
	private String bankId;
	
	@Column(length = 30)
	private String accountNumber;
	
	@Column(length = 11)
	private String phoneNumber;
	
	@Column(length = 3)
	private String authId;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	

}
