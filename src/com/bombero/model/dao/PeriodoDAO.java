package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Periodo;

public class PeriodoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Periodo> getPeriodoPorDescripcion(String value) {
		List<Periodo> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Periodo.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Periodo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Periodo> buscarActivos() {
		List<Periodo> resultado; 
		Query query = getEntityManager().createNamedQuery("Periodo.buscarActivos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Periodo>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Periodo> buscarEnProceso() {
		List<Periodo> resultado; 
		Query query = getEntityManager().createNamedQuery("Periodo.buscarEnProceso");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Periodo>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Periodo> buscarTerminados() {
		List<Periodo> resultado; 
		Query query = getEntityManager().createNamedQuery("Periodo.buscarTerminados");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Periodo>) query.getResultList();
		return resultado;
	}
}