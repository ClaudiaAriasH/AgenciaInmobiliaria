package co.com.udem.agenciainmobiliaria.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;

import co.com.udem.agenciainmobiliaria.dto.TipoIdentificacionDTO;
import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;

public class ConvertTipoIdentificacion {

	@Autowired
	private ModelMapper modelMapper;

	public TipoIdentificacion convertToEntity(TipoIdentificacionDTO tipoIdentificacionDTO) {
		return modelMapper.map(tipoIdentificacionDTO, TipoIdentificacion.class);
	}

	public TipoIdentificacionDTO convertToDTO(TipoIdentificacion tipoIdentificacion) {
		return modelMapper.map(tipoIdentificacion, TipoIdentificacionDTO.class);
	}

	public List<TipoIdentificacionDTO> convertToDTOIterable(Iterable<TipoIdentificacion> tipoDocumentos) {

		List<TipoIdentificacion> tipoDoc = new ArrayList<>();
		tipoDocumentos.forEach(tipoDoc::add);
		return modelMapper.map(tipoDoc, new TypeToken<List<TipoIdentificacionDTO>>() {
		}.getType());

	}

}
