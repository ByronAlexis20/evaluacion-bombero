package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Pregunta;

public class PreguntaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Pregunta> buscarPreguntaPorPeriodoYModulo(Integer idPeriodo, Integer idModulo) {
		List<Pregunta> resultado = new ArrayList<Pregunta>(); 
		Query query = getEntityManager().createNamedQuery("Pregunta.buscarPorPeriodoYModulo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo",idPeriodo);
		query.setParameter("idModulo",idModulo);
		resultado = (List<Pregunta>) query.getResultList();
		return resultado;
	}
	
	public Pregunta buscarPreguntaPorId(Integer idPregunta) {
		Pregunta pregunta; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Pregunta.buscarPorId");
		consulta.setParameter("idPregunta", idPregunta);
		pregunta = (Pregunta) consulta.getSingleResult();
		return pregunta;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pregunta> getPreguntaUltima() {
		List<Pregunta> resultado = new ArrayList<Pregunta>(); 
		Query query = getEntityManager().createNamedQuery("Pregunta.buscarUltimo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Pregunta>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Pregunta> buscarPorEvaluacion(Integer idEvaluacion) {
		List<Pregunta> resultado = new ArrayList<Pregunta>(); 
		Query query = getEntityManager().createNamedQuery("Pregunta.buscarPorEvaluacion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idEvaluacion", idEvaluacion);
		resultado = (List<Pregunta>) query.getResultList();
		return resultado;
	}
}