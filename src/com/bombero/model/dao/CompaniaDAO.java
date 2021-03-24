package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Compania;

public class CompaniaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Compania> getCompaniaPorDescripcion(String value) {
		List<Compania> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Compania.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Compania>) query.getResultList();
		return resultado;
	}
}
