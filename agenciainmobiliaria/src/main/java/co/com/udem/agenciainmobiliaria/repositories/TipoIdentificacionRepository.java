package co.com.udem.agenciainmobiliaria.repositories;

import org.springframework.data.repository.CrudRepository;

import co.com.udem.agenciainmobiliaria.entities.TipoIdentificacion;

public interface TipoIdentificacionRepository extends CrudRepository<TipoIdentificacion, Long> {
	TipoIdentificacion findByTipoDocumento(String tipoDocumento);

}
