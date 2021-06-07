package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.ResultadoEvaluacion;

public class ResultadoEvaluacionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<ResultadoEvaluacion> buscarPorEvaluacionYAspirante(Integer idEvaluacion, Integer idMatricula) {
		List<ResultadoEvaluacion> resultado = new ArrayList<ResultadoEvaluacion>(); 
		Query query = getEntityManager().createNamedQuery("ResultadoEvaluacion.buscarPorEvaluacionYAspirante");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEvaluacion", idEvaluacion);
		query.setParameter("idMatricula", idMatricula);
		resultado = (List<ResultadoEvaluacion>) query.getResultList();
		return resultado;
	}
}
