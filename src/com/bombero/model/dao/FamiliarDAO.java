package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Familiar;

public class FamiliarDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Familiar> buscarPorFichaMedica(Integer id) {
		List<Familiar> resultado; 
		Query query = getEntityManager().createNamedQuery("Familiar.buscarPorFichaMedica");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<Familiar>) query.getResultList();
		return resultado;
	}
}
