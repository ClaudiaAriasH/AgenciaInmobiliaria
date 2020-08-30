package co.com.udem.agenciainmobiliaria.filtros;

import java.math.BigInteger;

import org.springframework.data.jpa.domain.Specification;

import co.com.udem.agenciainmobiliaria.entities.Propiedad;

public class FilterSpecification {
	private FilterSpecification() {
	}

	public static Specification<Propiedad> withFilter(Object object, String columName) {
		return object == null ? null : (root, query, cb) -> cb.equal(root.get(columName), object);
	}

	public static Specification<Propiedad> withFilterBetween(BigInteger precioIni, BigInteger precioFinal,
			String columName) {
		return (precioIni == null || precioFinal == null) ? null
				: (root, query, cb) -> cb.between(root.get(columName), precioIni, precioFinal);
	}
}
