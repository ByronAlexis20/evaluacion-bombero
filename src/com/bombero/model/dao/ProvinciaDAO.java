package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Provincia;

public class ProvinciaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Provincia> buscarProvinciaPorIdPais(Integer id) {
		List<Provincia> resultado; 
		Query query = getEntityManager().createNamedQuery("Provincia.buscarPorPais");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPais", id);
		resultado = (List<Provincia>) query.getResultList();
		return resultado;
	}

}
