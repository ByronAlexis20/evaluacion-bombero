package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Pai;

public class PaisDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Pai> getPaises() {
		List<Pai> resultado; 
		Query query = getEntityManager().createNamedQuery("Pai.buscarPaises");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Pai>) query.getResultList();
		return resultado;
	}
}
