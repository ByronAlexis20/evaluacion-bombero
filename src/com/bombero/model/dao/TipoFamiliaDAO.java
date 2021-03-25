package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.TipoFamiliar;

public class TipoFamiliaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoFamiliar> getTipoFamiliarPorDescripcion(String value) {
		List<TipoFamiliar> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoFamiliar.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoFamiliar>) query.getResultList();
		return resultado;
	}
}
