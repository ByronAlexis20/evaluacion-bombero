package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.EstadoCivil;

public class EstadoCivilDAO extends ClaseDAO {

	@SuppressWarnings("unchecked")
	public List<EstadoCivil> getEstadoCivils() {
		List<EstadoCivil> resultado; 
		Query query = getEntityManager().createNamedQuery("EstadoCivil.buscarEstadoCivil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<EstadoCivil>) query.getResultList();
		return resultado;
	}
}
