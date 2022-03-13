package shop.newplace.common.mail.model.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuthenticationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private LocalDateTime expirationDateTime;
	
	@Column
	private boolean expired;
	
	@Column
	private Long userId;
	
	@Column
	private String certificationNumber;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createDate;
	
	@LastModifiedDate
	@Column
	private LocalDateTime lastModifiedDate;
	
	@Builder
	public EmailAuthenticationToken(LocalDateTime expirationDateTime, boolean expired, Long userId, String certificationNumber) {
		this.expirationDateTime = expirationDateTime;
		this.expired = expired;
		this.userId = userId;
		this.certificationNumber = certificationNumber;
	}
	
	public void useToken() {
		this.expired = true;
	}
	

}
