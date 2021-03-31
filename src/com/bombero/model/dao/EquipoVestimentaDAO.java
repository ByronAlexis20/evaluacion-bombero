package com.bombero.model.dao;

import java.util.List;

import javax.persistence.Query;

import com.bombero.model.entity.EquipoVestimenta;

public class EquipoVestimentaDAO extends ClaseDAO {
	@SuppressWarnings("unchecked")
	public List<EquipoVestimenta> getEquipoPorDescripcion(String value) {
		List<EquipoVestimenta> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("EquipoVestimenta.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<EquipoVestimenta>) query.getResultList();
		return resultado;
	}
}
