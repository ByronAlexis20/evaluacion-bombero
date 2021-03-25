package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.TipoDocumento;

public class TipoDocumentoDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> getTipoDocumentoPorDescripcion(String value) {
		List<TipoDocumento> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoDocumento.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<TipoDocumento>) query.getResultList();
		return resultado;
	}
}
