package shop.newplace.Users.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import shop.newplace.Users.model.entity.Profiles;

@Data
@Builder
public class JwtTokenForm {

	private Long id;
	
	private List<Profiles> profilesList;
	
    private String accesToken;
    
    private String refreshToken;
    
    
}
