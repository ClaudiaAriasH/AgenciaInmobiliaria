package co.com.udem.agenciainmobiliaria.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import co.com.udem.agenciainmobiliaria.entities.Propiedad;

public interface PropiedadRepository extends CrudRepository<Propiedad, Long>, JpaSpecificationExecutor<Propiedad> {

	Iterable<Propiedad> findByArea(String area);

	Iterable<Propiedad> findByAreaAndNumeroHabitaciones(String area, int numeroHabitaciones);

}
