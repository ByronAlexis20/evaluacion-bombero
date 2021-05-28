package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;

public class RegistroPreguntasC {
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	
	@Command
	public void nuevaPregunta() {
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/preguntaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editarPregunta() {
		
	}
	@Command
	public void eliminarPregunta() {
		
	}
	public List<Periodo> getPeriodoLista() {
		return periodoDAO.buscarActivos();
	}
	public List<Modulo> getModuloLista(){
		return moduloDAO.getModuloPorDescripcion("");
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public Modulo getModuloSeleccionado() {
		return moduloSeleccionado;
	}
	public void setModuloSeleccionado(Modulo moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}
}
