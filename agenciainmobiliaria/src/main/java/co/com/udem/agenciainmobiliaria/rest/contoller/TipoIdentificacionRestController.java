package co.com.udem.agenciainmobiliaria.rest.contoller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;
import co.com.udem.agenciainmobiliaria.repositories.TipoIdentificacionRepository;
import co.com.udem.agenciainmobiliaria.util.Constantes;
import co.com.udem.agenciainmobiliaria.util.ConvertTipoIdentificacion;

@RestController
@RequestMapping("/agenciaInmobiliaria")
public class TipoIdentificacionRestController {

	@Autowired
	private TipoIdentificacionRepository tipoIdentificacionRepository;
	@Autowired
	private ConvertTipoIdentificacion convertTipoIdentificacion;

	@PostMapping(path = "/adicionarTipoDocumento", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> adicionarTipoDocumento(@RequestBody TipoIdentificacionDTO tipoIdentificacionDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			TipoIdentificacion tipoIdentificacion = convertTipoIdentificacion.convertToEntity(tipoIdentificacionDTO);
			TipoIdentificacion tipoDocumentoExiste = tipoIdentificacionRepository
					.findByTipoDocumento(tipoIdentificacion.getTipoDocumento());

			if (tipoDocumentoExiste != null) {

				response.put(Constantes.MENSAJE, "El tipo de documento ya existe");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			} else {

				tipoIdentificacionRepository.save(tipoIdentificacion);

				response.put(Constantes.MENSAJE, "Se insertado el Tipo de Documento  exitosamente");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al adicionar el Tipo de Documento");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(path = "/tiposDocumentos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> listarTiposDocumentos() {
		Map<String, Object> response = new HashMap<>();
		List<TipoIdentificacionDTO> tipoDocumentosDTO = new ArrayList<>();
		try {
			Iterable<TipoIdentificacion> tipoDocumentos = tipoIdentificacionRepository.findAll();
			tipoDocumentosDTO = convertTipoIdentificacion.convertToDTOIterable(tipoDocumentos);

			response.put("datosTipoDocumeto", tipoDocumentosDTO);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put(Constantes.MENSAJE, "Ocurrió un problema al listar los documentos");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping(path = "/tiposDocumentos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> eliminarTipoDocumento(@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		if (tipoIdentificacionRepository.findById(id).isPresent()) {
			tipoIdentificacionRepository.deleteById(id);

			response.put(Constantes.MENSAJE, "Registro eliminado de forma exitosa");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {

			response.put(Constantes.MENSAJE, "El registro no existe en la base de datos");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}

	}

	@GetMapping(path = "/tiposDocumentos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> buscarTipoDocumento(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		TipoIdentificacionDTO tipoIdentificacionDTO = new TipoIdentificacionDTO();

		try {
			Optional<TipoIdentificacion> tipoIdent = tipoIdentificacionRepository.findById(id);
			if (tipoIdent.isPresent()) {
				TipoIdentificacion tipoIdentificacion = tipoIdent.get();

				tipoIdentificacionDTO = convertTipoIdentificacion.convertToDTO(tipoIdentificacion);

				response.put("datos", tipoIdentificacionDTO);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

			else {

				response.put(Constantes.MENSAJE, "El tipo de documento buscado no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al buscar el Tipo de Documento");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping(path = "/tiposDocumentos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateTipoDocumento(@RequestBody TipoIdentificacionDTO newTipoDocDTO,
			@PathVariable Long id) {
		Map<String, String> response = new HashMap<>();
		try {
			Optional<TipoIdentificacion> tipoIdent = tipoIdentificacionRepository.findById(id);
			if (tipoIdent.isPresent()) {

				TipoIdentificacion newTipoDoc = convertTipoIdentificacion.convertToEntity(newTipoDocDTO);

				TipoIdentificacion tipoDoc = tipoIdent.get();

				tipoDoc.setTipoDocumento(newTipoDoc.getTipoDocumento());
				tipoDoc.setDescripcion(newTipoDoc.getDescripcion());
				tipoIdentificacionRepository.save(tipoDoc);

				response.put(Constantes.MENSAJE, "Se actualizo el Tipo de Documento exitosamente");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				response.put(Constantes.MENSAJE, "El tipo de documento no existe");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

			}

		} catch (Exception e) {

			response.put(Constantes.MENSAJE, "Fallo al realizar la actualización del Tipo de Documento");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
