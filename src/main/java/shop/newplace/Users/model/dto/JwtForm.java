package shop.newplace.Users.model.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import shop.newplace.Users.model.entity.Profiles;

@Data
@Builder
public class JwtForm {


	private List<Profiles> profilesList;
	
    private String token;
    
}
