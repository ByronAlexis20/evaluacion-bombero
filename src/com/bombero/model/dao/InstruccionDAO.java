package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Instruccion;

public class InstruccionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Instruccion> getInstrucciones() {
		List<Instruccion> resultado; 
		Query query = getEntityManager().createNamedQuery("Instruccion.buscarInstruccion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Instruccion>) query.getResultList();
		return resultado;
	}
}
