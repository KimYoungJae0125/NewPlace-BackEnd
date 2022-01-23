package shop.newplace.Users.model.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@EqualsAndHashCode(of = "PROFILE_ID")
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@DynamicUpdate
public class Profiles extends BaseEntity implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROFILE_ID")
	private Long id;

	@ManyToOne
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
