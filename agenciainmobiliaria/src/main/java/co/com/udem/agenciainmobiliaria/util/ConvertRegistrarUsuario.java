package co.com.udem.agenciainmobiliaria.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import co.com.udem.agenciainmobiliaria.dto.RegistrarUsuarioDTO;
import co.com.udem.agenciainmobiliaria.entities.RegistrarUsuario;

public class ConvertRegistrarUsuario {

	@Autowired
	private ModelMapper modelMapper;

	public RegistrarUsuario convertToEntity(RegistrarUsuarioDTO registrarUsuarioDTO) {
		return modelMapper.map(registrarUsuarioDTO, RegistrarUsuario.class);
	}

	public RegistrarUsuarioDTO convertToDTO(RegistrarUsuario registrarUsuario) {

		return modelMapper.map(registrarUsuario, RegistrarUsuarioDTO.class);
	}

}
