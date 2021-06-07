package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Evaluacion;

public class EvaluacionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Evaluacion> buscarPorModuloYPeriodo(Integer idPeriodo, Integer idModulo) {
		List<Evaluacion> resultado = new ArrayList<Evaluacion>(); 
		Query query = getEntityManager().createNamedQuery("Evaluacion.buscarPorModuloYPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		query.setParameter("idModulo", idModulo);
		resultado = (List<Evaluacion>) query.getResultList();
		return resultado;
	}
	
	public Evaluacion buscarEvaluacionPorId(Integer idEvaluacion) {
		Evaluacion evaluacion; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Evaluacion.buscarPorId");
		consulta.setParameter("idEvaluacion", idEvaluacion);
		evaluacion = (Evaluacion) consulta.getSingleResult();
		return evaluacion;
	}
	
	@SuppressWarnings("unchecked")
	public List<Evaluacion> recuperarUltimaEvaluacion() {
		List<Evaluacion> resultado = new ArrayList<Evaluacion>(); 
		Query query = getEntityManager().createNamedQuery("Evaluacion.buscarUltimo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Evaluacion>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Evaluacion> recuperarPorPeriodo(Integer idPeriodo) {
		List<Evaluacion> resultado = new ArrayList<Evaluacion>(); 
		Query query = getEntityManager().createNamedQuery("Evaluacion.buscarPorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Evaluacion>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Evaluacion> buscarPorEstadoEvaluacion(String estado) {
		List<Evaluacion> resultado = new ArrayList<Evaluacion>(); 
		Query query = getEntityManager().createNamedQuery("Evaluacion.buscarPorEstadoEvaluacion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("estado", estado);
		resultado = (List<Evaluacion>) query.getResultList();
		return resultado;
	}
}
