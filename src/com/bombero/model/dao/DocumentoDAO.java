package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Documento;

public class DocumentoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Documento> buscarPorAspirante(Integer idAspirante) {
		List<Documento> resultado; 
		Query query = getEntityManager().createNamedQuery("Documento.buscarPorAspirante");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idAspirante", idAspirante);
		resultado = (List<Documento>) query.getResultList();
		return resultado;
	}
}