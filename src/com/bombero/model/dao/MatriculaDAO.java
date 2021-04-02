package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Matricula;

public class MatriculaDAO extends ClaseDAO {
	
	@SuppressWarnings("unchecked")
	public List<Matricula> obtenerAspirantesPorIdPeriodo(Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarAspirantePorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
}
