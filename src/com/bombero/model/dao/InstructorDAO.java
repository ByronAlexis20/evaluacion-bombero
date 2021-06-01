package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Instructor;

public class InstructorDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Instructor> buscarPorNombreApellido(String value) {
		List<Instructor> resultado = new ArrayList<Instructor>(); 
		Query query = getEntityManager().createNamedQuery("Instructor.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron","%" + value.toLowerCase() + "%");
		resultado = (List<Instructor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Instructor> buscarPorCedulaInstructor(String cedula, Integer idInstructor) {
		List<Instructor> resultado = new ArrayList<Instructor>(); 
		Query query = getEntityManager().createNamedQuery("Instructor.buscarPorCedulaInstructor");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula",cedula);
		query.setParameter("id",idInstructor);
		resultado = (List<Instructor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Instructor> buscarPorCedula(String cedula) {
		List<Instructor> resultado = new ArrayList<Instructor>(); 
		Query query = getEntityManager().createNamedQuery("Instructor.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("cedula",cedula);
		resultado = (List<Instructor>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Instructor> buscarSinAsignacionPorPeriodo(Integer idPeriodo) {
		List<Instructor> resultado; 
		Query query = getEntityManager().createNamedQuery("Instructor.buscarSinAsignacion");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPeriodo", idPeriodo);
		resultado = (List<Instructor>) query.getResultList();
		return resultado;
	}
}
