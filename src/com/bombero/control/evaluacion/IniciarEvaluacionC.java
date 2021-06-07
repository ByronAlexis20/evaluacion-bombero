package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Evaluacion;
import com.bombero.model.entity.Periodo;

public class IniciarEvaluacionC {
	Periodo periodoSeleccionado;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	Evaluacion evaluacionSeleccionado;
	List<Evaluacion> listaEvaluacion;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	public List<Periodo> getPeriodoLista() {
		return periodoDAO.buscarActivos();
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public Evaluacion getEvaluacionSeleccionado() {
		return evaluacionSeleccionado;
	}
	public void setEvaluacionSeleccionado(Evaluacion evaluacionSeleccionado) {
		this.evaluacionSeleccionado = evaluacionSeleccionado;
	}
	public List<Evaluacion> getListaEvaluacion() {
		return listaEvaluacion;
	}
	public void setListaEvaluacion(List<Evaluacion> listaEvaluacion) {
		this.listaEvaluacion = listaEvaluacion;
	}
}