package co.com.udem.agenciainmobiliaria.rest.contoller;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.udem.agenciainmobiliaria.dto.AuthenticationRequestDTO;
import co.com.udem.agenciainmobiliaria.repositories.RegistrarUsuarioRepository;
import co.com.udem.agenciainmobiliaria.security.jwt.JwtTokenProvider;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenProvider jwtTokenProvider;

	@Autowired
	RegistrarUsuarioRepository users;

	@PostMapping(path = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> signin(@RequestBody AuthenticationRequestDTO data) {
		JSONObject respuesta = new JSONObject();
		try {
			String username = data.getUsername();
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
			String token = jwtTokenProvider.createToken(username, this.users.findByNumeroIdentificacion(username)
					.orElseThrow(() -> new UsernameNotFoundException("Usuario " + username + "no existe.")).getRoles());
			respuesta.put("username", username);
			respuesta.put("token", token);

			return ok(respuesta);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException("Invalid username/password supplied");
		}

	}

}
