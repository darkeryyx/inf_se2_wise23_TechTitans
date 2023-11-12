package group.artifact;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArtifactApplication {
	@Bean
	public ModelMapper modelMapper()  {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(ArtifactApplication.class, args);
	}
}
