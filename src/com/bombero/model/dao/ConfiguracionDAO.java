package com.bombero.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.Configuracion;

public class ConfiguracionDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<Configuracion> buscarConfiguracionActiva() {
		List<Configuracion> resultado = new ArrayList<Configuracion>(); 
		Query query = getEntityManager().createNamedQuery("Configuracion.findAll");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<Configuracion>) query.getResultList();
		return resultado;
	}
}