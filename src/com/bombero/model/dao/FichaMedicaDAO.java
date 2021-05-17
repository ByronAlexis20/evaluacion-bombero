package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.FichaMedica;

public class FichaMedicaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<FichaMedica> buscarPorAspirante(Integer id) {
		List<FichaMedica> resultado; 
		Query query = getEntityManager().createNamedQuery("FichaMedica.buscarPorAspirante");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<FichaMedica>) query.getResultList();
		return resultado;
	}
}
