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
}
