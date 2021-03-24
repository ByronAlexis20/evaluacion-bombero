package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.TipoSangre;

public class TipoSangreDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoSangre> getTipoSangrePorDescripcion(String value) {
		List<TipoSangre> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoSangre.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoSangre>) query.getResultList();
		return resultado;
	}
}
