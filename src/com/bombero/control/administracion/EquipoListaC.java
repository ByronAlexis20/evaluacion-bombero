package com.bombero.control.administracion;

import java.util.List;

import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Listbox;

import com.bombero.model.entity.EquipoVestimenta;

public class EquipoListaC {
	@Wire Listbox lstEquipos;
	List<EquipoVestimenta> equipoLista;

	public List<EquipoVestimenta> getEquipoLista() {
		return equipoLista;
	}

	public void setEquipoLista(List<EquipoVestimenta> equipoLista) {
		this.equipoLista = equipoLista;
	}
	
}
