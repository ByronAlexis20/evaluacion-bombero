package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Genero;

public class GeneroDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Genero> getGeneros() {
		List<Genero> resultado; 
		Query query = getEntityManager().createNamedQuery("Genero.buscarGenero");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Genero>) query.getResultList();
		return resultado;
	}
}
