package co.com.udem.agenciainmobiliaria;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.util.ConvertPropiedad;
import co.com.udem.agenciainmobiliaria.util.ConvertRegistrarUsuario;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@SpringBootApplication
@EnableDiscoveryClient
public class AgenciainmobiliariaApplication {

	public static void main(String[] args) {

		SpringApplication.run(AgenciainmobiliariaApplication.class, args);
	}

	@Bean
	public ConvertRegistrarUsuario convertRegistrarUsuario() {
		return new ConvertRegistrarUsuario();
	}

	@Bean
	public ConvertTipoIdentificacion convertTipoIdentificacion() {
		return new ConvertTipoIdentificacion();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public TipoIdentificacionDTO tipoIdentificacionDTO() {
		return new TipoIdentificacionDTO();
	}

	@Bean
	PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ConvertPropiedad convertPropiedad() {
		return new ConvertPropiedad();
	}
}
