package shop.newplace;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NewplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewplaceApplication.class, args);
    }

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
    
}
