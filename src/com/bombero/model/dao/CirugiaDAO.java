package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Cirugia;

public class CirugiaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Cirugia> buscarPorFichaMedica(Integer id) {
		List<Cirugia> resultado; 
		Query query = getEntityManager().createNamedQuery("Cirugia.buscarPorFichaMedica");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("id", id);
		resultado = (List<Cirugia>) query.getResultList();
		return resultado;
	}
}
