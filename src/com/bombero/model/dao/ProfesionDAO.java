package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Profesion;

public class ProfesionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Profesion> getProfesionPorDescripcion(String value) {
		List<Profesion> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Profesion.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Profesion>) query.getResultList();
		return resultado;
	}
}
