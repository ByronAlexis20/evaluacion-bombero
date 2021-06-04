package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Perfil;

public class PerfilDAO extends ClaseDAO {
	
	@SuppressWarnings("unchecked")
	public List<Perfil> getPerfilesPorDescripcion(String value) {
		List<Perfil> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Perfil.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Perfil>) query.getResultList();
		return resultado;
	}
	
	public Perfil obtenerPerfilPorId(Integer idPerfil) {
		Perfil perfil; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Perfil.buscarPerfilPorId");
		consulta.setParameter("idPerfil", idPerfil);
		perfil = (Perfil) consulta.getSingleResult();
		return perfil;
	}
}
