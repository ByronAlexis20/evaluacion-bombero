package com.bombero.model.dao;

import javax.persistence.Query;

import com.bombero.model.entity.Usuario;

public class UsuarioDAO extends ClaseDAO{
	public Usuario getUsuario(String nombreUsuario) {
		Usuario usuario; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("Usuario.buscaUsuario");
		consulta.setParameter("nombreUsuario", nombreUsuario);
		usuario = (Usuario) consulta.getSingleResult();
		return usuario;
	}
}
