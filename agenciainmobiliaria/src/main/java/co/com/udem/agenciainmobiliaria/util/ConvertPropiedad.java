package co.com.udem.agenciainmobiliaria.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import co.com.udem.agenciainmobiliaria.dto.PropiedadDTO;
import co.com.udem.agenciainmobiliaria.entities.Propiedad;

public class ConvertPropiedad {

	@Autowired
	private ModelMapper modelMapper;

	public Propiedad convertToEntity(PropiedadDTO propiedadDTO) {
		return modelMapper.map(propiedadDTO, Propiedad.class);
	}

	public PropiedadDTO convertToDTO(Propiedad propiedad) {
		return modelMapper.map(propiedad, PropiedadDTO.class);
	}

}
