package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Modulo;

public class ModuloDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Modulo> getModuloPorDescripcion(String value) {
		List<Modulo> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Modulo.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Modulo>) query.getResultList();
		return resultado;
	}
}
