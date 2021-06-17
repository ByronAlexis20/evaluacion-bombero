package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Matricula;

public class MatriculaDAO extends ClaseDAO {
	
	@SuppressWarnings("unchecked")
	public List<Matricula> obtenerAspirantesPorIdPeriodo(Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarAspirantePorPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Matricula> validarAspiranteExistente(String cedulaUsuario, Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedulaUsuario);
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Matricula> validarAspiranteExistenteDiferente(String cedulaUsuario,Integer idMatricula, Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPorCedulaDiferenteAlUsuarioActual");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula", cedulaUsuario);
		query.setParameter("idMatricula", idMatricula);
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Matricula> obtenerPorAspiranteYPeriodo(Integer idAspirante,Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPorAspiranteYPeriodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idAspirante", idAspirante);
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<Matricula> buscarPorPeriodoTerminado(Integer idPeriodo) {
		List<Matricula> resultado; 
		Query query = getEntityManager().createNamedQuery("Matricula.buscarPorPeriodoTerminado");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Matricula>) query.getResultList();
		return resultado;
	}
}