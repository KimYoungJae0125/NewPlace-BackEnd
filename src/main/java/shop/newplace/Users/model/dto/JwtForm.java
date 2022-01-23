package shop.newplace.Users.model.dto;

import lombok.Builder;
import lombok.Data;
import shop.newplace.Users.model.entity.Users;

@Data
@Builder
public class JwtForm {


	private Users users;
	
    private String token;
    
    private String resCd;
    
    private String resMsg;
    
}
