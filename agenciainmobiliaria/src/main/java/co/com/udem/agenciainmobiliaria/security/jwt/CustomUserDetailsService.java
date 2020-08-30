package co.com.udem.agenciainmobiliaria.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import co.com.udem.agenciainmobiliaria.repositories.RegistrarUsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	private RegistrarUsuarioRepository users;

	public CustomUserDetailsService(RegistrarUsuarioRepository users) {
		this.users = users;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		return this.users.findByNumeroIdentificacion(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
	}
}
