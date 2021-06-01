package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Respuesta;

public class RespuestaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Respuesta> buscarRespuestaPorPregunta(Integer idPregunta) {
		List<Respuesta> resultado = new ArrayList<Respuesta>(); 
		Query query = getEntityManager().createNamedQuery("Respuesta.buscarPorPregunta");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPregunta", idPregunta);
		resultado = (List<Respuesta>) query.getResultList();
		return resultado;
	}
}
