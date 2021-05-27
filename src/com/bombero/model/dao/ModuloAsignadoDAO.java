package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.ModuloAsignado;

public class ModuloAsignadoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<ModuloAsignado> obtenerAsignacionesPorPeriodo(Integer idPeriodo) {
		List<ModuloAsignado> resultado; 
		Query query = getEntityManager().createNamedQuery("ModuloAsignado.buscarAsignacionesPorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<ModuloAsignado>) query.getResultList();
		return resultado;
	}
}
