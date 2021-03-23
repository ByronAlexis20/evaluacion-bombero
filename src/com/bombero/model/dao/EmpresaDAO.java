package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Empresa;

public class EmpresaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Empresa> obtenerListaEmpresa() {
		List<Empresa> resultado; 
		Query query = getEntityManager().createNamedQuery("Empresa.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Empresa>) query.getResultList();
		return resultado;
	}
}
