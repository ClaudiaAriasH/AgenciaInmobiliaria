package co.com.udem.agenciainmobiliaria.rest.contoller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import co.com.udem.agenciainmobiliaria.dto.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.RegistrarUsuario;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;
import co.com.udem.agenciainmobiliaria.repositories.RegistrarUsuarioRepository;
import co.com.udem.agenciainmobiliaria.repositories.TipoIdentificacionRepository;
import co.com.udem.agenciainmobiliaria.util.Constantes;
import co.com.udem.agenciainmobiliaria.util.ConvertRegistrarUsuario;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@RestController
@RequestMapping("/agenciaInmobiliaria")
public class RegistrarUsuarioRestController {

	@Autowired
	private RegistrarUsuarioRepository registrarUsuarioRepository;
	@Autowired
	private ConvertRegistrarUsuario convertRegistrarUsuario;
	@Autowired
	private TipoIdentificacionRepository tipoIdentificacionRepository;
	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping(path = "/adicionarUsuario", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> adicionarUsuario(@RequestBody RegistrarUsuarioDTO registrarUsuarioDTO) {
		Map<String, String> response = new HashMap<>();

		try {
			RegistrarUsuario registrarUsuario = convertRegistrarUsuario.convertToEntity(registrarUsuarioDTO);

			Long idTipoDoc = (registrarUsuario.getTipoIdentificacion().getId());
			Optional<TipoIdentificacion> tipoDocExiste = tipoIdentificacionRepository.findById(idTipoDoc);
			RegistrarUsuario usuarioExiste = registrarUsuarioRepository
					.buscarDocumentoTipo(registrarUsuario.getNumeroIdentificacion(), idTipoDoc);
			if (tipoDocExiste.isPresent()) {
				if (usuarioExiste != null) {

					response.put(Constantes.MENSAJE, "El usuario ya existe");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				} else {

					registrarUsuarioRepository.save(
							RegistrarUsuario.builder().numeroIdentificacion(registrarUsuario.getNumeroIdentificacion())
									.password(this.passwordEncoder.encode(registrarUsuario.getPassword()))
									.roles(Arrays.asList("ROLE_USER")).apellidos(registrarUsuario.getApellidos())
									.nombres(registrarUsuario.getNombres()).email(registrarUsuario.getEmail())
									.telefono(registrarUsuario.getTelefono())
									.tipoIdentificacion(registrarUsuario.getTipoIdentificacion())
									.direccion(registrarUsuario.getDireccion()).build());

					response.put(Constantes.MENSAJE, "Registrado insertado exitosamente");
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			} else {

				response.put(Constantes.MENSAJE, "El tipo de documento no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Ocurrió un problema al insertar");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping(path = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarUsuarios() {
		Map<String, Object> response = new HashMap<>();

		try {

			Iterable<RegistrarUsuario> iRegistro = registrarUsuarioRepository.findAll();
			List<RegistrarUsuario> listaRegistro = new ArrayList<>();
			List<RegistrarUsuarioDTO> listaRegistroDTO = new ArrayList<>();
			iRegistro.iterator().forEachRemaining(listaRegistro::add);

			for (int i = 0; i < listaRegistro.size(); i++) {
				TipoIdentificacionDTO tipoIdentificacion = null;
				if (listaRegistro.get(i).getTipoIdentificacion() != null) {
					tipoIdentificacion = convertTipoIdentificacion
							.convertToDTO(listaRegistro.get(i).getTipoIdentificacion());
				}
				RegistrarUsuarioDTO registroDTO = convertRegistrarUsuario.convertToDTO(listaRegistro.get(i));
				registroDTO.setTipoIdentificacionDTO(tipoIdentificacion);
				listaRegistroDTO.add(registroDTO);

				response.put("datosUsuario", listaRegistroDTO);

			}
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Ocurrió un problema al listar los usuarios");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarUsuario(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		if (registrarUsuarioRepository.findById(id).isPresent()) {
			registrarUsuarioRepository.deleteById(id);

			response.put(Constantes.MENSAJE, "Registro eliminado de forma exitosa");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {

			response.put(Constantes.MENSAJE, "El registro no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> buscarUsuario(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		RegistrarUsuarioDTO registrarUsuarioDTO = new RegistrarUsuarioDTO();

		try {
			Optional<RegistrarUsuario> registrarUser = registrarUsuarioRepository.findById(id);
			if (registrarUser.isPresent()) {
				RegistrarUsuario usuario = registrarUser.get();
				TipoIdentificacionDTO tipoDocDTO = convertTipoIdentificacion
						.convertToDTO(usuario.getTipoIdentificacion());

				registrarUsuarioDTO = convertRegistrarUsuario.convertToDTO(usuario);
				registrarUsuarioDTO.setTipoIdentificacionDTO(tipoDocDTO);

				response.put("datos", registrarUsuarioDTO);
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {

				response.put(Constantes.MENSAJE, "El usuario no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al buscar el Usuario");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(path = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUser(@RequestBody RegistrarUsuarioDTO newUserDTO, @PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {

			Optional<RegistrarUsuario> registrarUser = registrarUsuarioRepository.findById(id);
			Optional<TipoIdentificacion> tipoDocExiste = tipoIdentificacionRepository
					.findById(newUserDTO.getTipoIdentificacionDTO().getId());
			if (registrarUser.isPresent() && tipoDocExiste.isPresent()) {
				RegistrarUsuario newUser = convertRegistrarUsuario.convertToEntity(newUserDTO);
				RegistrarUsuario user = registrarUser.get();
				user.setApellidos(newUser.getApellidos());
				user.setNombres(newUser.getNombres());
				user.setDireccion(newUser.getDireccion());
				user.setEmail(newUser.getEmail());
				user.setNumeroIdentificacion(newUser.getNumeroIdentificacion());
				user.setTipoIdentificacion(newUser.getTipoIdentificacion());
				user.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
				user.setTelefono(newUser.getTelefono());
				registrarUsuarioRepository.save(user);

				response.put(Constantes.MENSAJE, "Se actualizo el Usuario exitosamente");
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {

				response.put(Constantes.MENSAJE, "El usuario o tipo de documento no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al realizar la actualización del Usuario");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraint(ConstraintViolationException ex, WebRequest request) {
		Map<String, String> response = new HashMap<>();

		response.put(Constantes.MENSAJE, "El tipo de documento ingresado es incorrecto o no existe.");

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
