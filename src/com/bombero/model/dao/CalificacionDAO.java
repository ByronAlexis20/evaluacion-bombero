package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Calificacion;

public class CalificacionDAO extends ClaseDAO {
	
	public Calificacion obtenerCalificacionPorMatriculaYModulo(Integer idModulo, Integer idMatricula) {
		Calificacion calificacion;
		try {
			Query consulta;
			consulta = getEntityManager().createNamedQuery("Calificacion.obtenerCalificacionPorMatriculaYModulo");
			consulta.setParameter("idModulo", idModulo);
			consulta.setParameter("idMatricula", idMatricula);
			calificacion = (Calificacion) consulta.getSingleResult();
			return calificacion;
		}catch(Exception ex) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Calificacion> buscarPorMatriculaYModulo(Integer idMatricula, Integer idModulo) {
		List<Calificacion> resultado = new ArrayList<Calificacion>(); 
		Query query = getEntityManager().createNamedQuery("Calificacion.buscarPorMatriculaYModulo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idMatricula", idMatricula);
		query.setParameter("idModulo", idModulo);
		resultado = (List<Calificacion>) query.getResultList();
		return resultado;
	}
}
