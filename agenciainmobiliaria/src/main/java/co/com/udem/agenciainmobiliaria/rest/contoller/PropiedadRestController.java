package co.com.udem.agenciainmobiliaria.rest.contoller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import co.com.udem.agenciainmobiliaria.dto.PropiedadDTO;
import co.com.udem.agenciainmobiliaria.dto.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.Propiedad;
import co.com.udem.agenciainmobiliaria.entities.RegistrarUsuario;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;
import co.com.udem.agenciainmobiliaria.filtros.FilterSpecification;
import co.com.udem.agenciainmobiliaria.repositories.PropiedadRepository;
import co.com.udem.agenciainmobiliaria.repositories.RegistrarUsuarioRepository;
import co.com.udem.agenciainmobiliaria.util.Constantes;
import co.com.udem.agenciainmobiliaria.util.ConvertPropiedad;
import co.com.udem.agenciainmobiliaria.util.ConvertRegistrarUsuario;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@RestController
@RequestMapping("/agenciaInmobiliaria")
public class PropiedadRestController {

	@Autowired
	private PropiedadRepository propiedadRepository;

	@Autowired
	private ConvertPropiedad convertPropiedad;

	@Autowired
	private RegistrarUsuarioRepository registrarUsuarioRepository;

	@Autowired
	private ConvertRegistrarUsuario convertRegistrarUsuario;

	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

	@PostMapping(path = "/adicionarPropiedad", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> adicionarPropiedad(@RequestBody PropiedadDTO propiedadDTO,
			Authentication authentication) {
		Map<String, String> response = new HashMap<>();
		String user = authentication.getName();

		Optional<RegistrarUsuario> infoUser = registrarUsuarioRepository.findByNumeroIdentificacion(user);

		try {
			if (infoUser.isPresent()) {
				RegistrarUsuario idUser = infoUser.get();
				Propiedad propiedad = convertPropiedad.convertToEntity(propiedadDTO);

				propiedad.setRegistrarUsuario(idUser);
				propiedadRepository.save(propiedad);

				response.put(Constantes.MENSAJE, "Se insertado la propiedad de forma exitosamente");

				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				response.put(Constantes.MENSAJE, "El usuario no existe");

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al adicionar la propiedad");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(path = "/propiedades", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarPropiedades() {
		Map<String, Object> response = new HashMap<>();

		try {
			TipoIdentificacion tipoDoc = null;
			Iterable<Propiedad> iPropiedades = propiedadRepository.findAll();
			List<Propiedad> listaPropiedades = new ArrayList<>();
			List<PropiedadDTO> listaPropiedadesDTO = new ArrayList<>();
			iPropiedades.iterator().forEachRemaining(listaPropiedades::add);

			for (int i = 0; i < listaPropiedades.size(); i++) {
				RegistrarUsuarioDTO registroUsuario = null;
				RegistrarUsuario registrarUsuario = listaPropiedades.get(i).getRegistrarUsuario();
				if (registrarUsuario != null) {
					registroUsuario = convertRegistrarUsuario.convertToDTO(registrarUsuario);
					tipoDoc = registrarUsuario.getTipoIdentificacion();
				}

				PropiedadDTO registroDTO = convertPropiedad.convertToDTO(listaPropiedades.get(i));
				TipoIdentificacionDTO tipoDocDTO = convertTipoIdentificacion.convertToDTO(tipoDoc);

				registroDTO.setRegistrarUsuarioDTO(registroUsuario);
				registroDTO.getRegistrarUsuarioDTO().setTipoIdentificacionDTO(tipoDocDTO);
				listaPropiedadesDTO.add(registroDTO);

				response.put("datosUsuario", listaPropiedadesDTO);
			}

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put(Constantes.MENSAJE, "Ocurrió un problema al listar las propiedades");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(path = "/propiedad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarPropiedad(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		if (propiedadRepository.findById(id).isPresent()) {
			propiedadRepository.deleteById(id);

			response.put(Constantes.MENSAJE, "Registro de la propiedad eliminado de forma exitosa");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {

			response.put(Constantes.MENSAJE, "El registro de la propiedad no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/propiedad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> buscarPropiedad(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		PropiedadDTO propiedadDTO = new PropiedadDTO();

		try {
			Optional<Propiedad> propiedadOptional = propiedadRepository.findById(id);
			if (propiedadOptional.isPresent()) {
				Propiedad propiedad = propiedadOptional.get();
				RegistrarUsuarioDTO usuarioDTO = convertRegistrarUsuario.convertToDTO(propiedad.getRegistrarUsuario());
				TipoIdentificacion tipoDoc = propiedad.getRegistrarUsuario().getTipoIdentificacion();
				TipoIdentificacionDTO tipoDocDTO = convertTipoIdentificacion.convertToDTO(tipoDoc);
				propiedadDTO = convertPropiedad.convertToDTO(propiedad);

				propiedadDTO.setRegistrarUsuarioDTO(usuarioDTO);
				propiedadDTO.getRegistrarUsuarioDTO().setTipoIdentificacionDTO(tipoDocDTO);

				response.put("datosPropiedad", propiedadDTO);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			else {

				response.put(Constantes.MENSAJE, "La propiedad buscada no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al buscar la propiedad");

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(path = "/propiedad/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePropiedad(@RequestBody PropiedadDTO newPropiedadDTO, @PathVariable Long id,
			Authentication authentication) {
		Map<String, String> response = new HashMap<>();
		String user = authentication.getName();

		Optional<RegistrarUsuario> infoUser = registrarUsuarioRepository.findByNumeroIdentificacion(user);

		try {
			Optional<Propiedad> propiedad = propiedadRepository.findById(id);

			if (infoUser.isPresent()) {
				if (propiedad.isPresent()) {
					RegistrarUsuario idUser = infoUser.get();
					Propiedad newPropiedad = convertPropiedad.convertToEntity(newPropiedadDTO);

					Propiedad propiedadAct = propiedad.get();

					propiedadAct.setArea(newPropiedad.getArea());
					propiedadAct.setNumeroHabitaciones(newPropiedad.getNumeroHabitaciones());
					propiedadAct.setNumeroBanos(newPropiedad.getNumeroBanos());
					propiedadAct.setTipoPropiedad(newPropiedad.getTipoPropiedad());
					propiedadAct.setValor(newPropiedad.getValor());
					propiedadAct.setRegistrarUsuario(idUser);
					propiedadRepository.save(propiedadAct);

					response.put(Constantes.MENSAJE, "Se actualizó la propiedad exitosamente");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {

					response.put(Constantes.MENSAJE, "La propiedad no existe");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

				}
			} else {

				response.put(Constantes.MENSAJE, "El usuario no existe");

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al realizar la actualización de la propiedad");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	@GetMapping(path = "/propiedades/filtros", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> buscarPropiedadFiltros(@RequestParam(value = "area", required = false) String area,
			@RequestParam(value = "numeroHabitaciones", required = false) Integer numeroHabitaciones,
			@RequestParam(value = "precioIni", required = false) BigInteger precioIni,
			@RequestParam(value = "precioFinal", required = false) BigInteger precioFinal) {
		Map<String, Object> response = new HashMap<>();

		try {

			List<Propiedad> filtroPropiedad = propiedadRepository
					.findAll(Specification.where(FilterSpecification.withFilter(area, "area"))
							.and(FilterSpecification.withFilter(numeroHabitaciones, "numeroHabitaciones"))
							.and(FilterSpecification.withFilterBetween(precioIni, precioFinal, "valor"))

					);

			if (!filtroPropiedad.isEmpty()) {

				List<PropiedadDTO> listPropiedadDTO = new ArrayList<>();
				for (int i = 0; i < filtroPropiedad.size(); i++) {
					Propiedad propiedad = filtroPropiedad.get(i);

					PropiedadDTO propiedadDTO = convertPropiedad.convertToDTO(propiedad);
					RegistrarUsuario usuario = propiedad.getRegistrarUsuario();
					TipoIdentificacion tipoIdentificacion = usuario.getTipoIdentificacion();
					TipoIdentificacionDTO tipoIdentificacionDTO = convertTipoIdentificacion
							.convertToDTO(tipoIdentificacion);
					RegistrarUsuarioDTO registrarUsuarioDTO = convertRegistrarUsuario.convertToDTO(usuario);
					registrarUsuarioDTO.setTipoIdentificacionDTO(tipoIdentificacionDTO);
					propiedadDTO.setRegistrarUsuarioDTO(registrarUsuarioDTO);

					listPropiedadDTO.add(propiedadDTO);
				}

				response.put("datosUsuario", listPropiedadDTO);

				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put(Constantes.MENSAJE,
						"No existe información en la base de datos con los parametros suministrados");

				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			response.put(Constantes.MENSAJE, "Fallo al filtrar la propiedad");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraint(ConstraintViolationException ex, WebRequest request) {
		Map<String, String> response = new HashMap<>();

		response.put(Constantes.MENSAJE, "El usuario ingresado es incorrecto o no existe.");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}