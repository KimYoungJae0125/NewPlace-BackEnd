package shop.newplace.Users.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import shop.newplace.common.entity.BaseEntity;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "users")
@DynamicUpdate
public class Profiles extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROFILE_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private Users users;
	
	@Column(length = 10, nullable = false)
	private String nickName;
	
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
	
	@Column(length = 10)
	private String authId;
	
}
