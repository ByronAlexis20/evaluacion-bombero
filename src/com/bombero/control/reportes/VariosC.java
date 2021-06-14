package com.bombero.control.reportes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;

import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;
import com.bombero.util.PrintReport;

public class VariosC {
	@Wire Combobox cboPeriodo;
	@Wire Combobox cboModulo;
	@Wire Combobox cboPeriodoInstructor;
	@Wire Combobox cboPeriodoAspirante;
	@Wire Combobox cboPeriodoCalificacion;
	@Wire Combobox cboModulo;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
	Periodo periodoInstructorSeleccionado;
	Periodo periodoAspiranteSeleccionado;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@Command
	public void imprimirBancoPreguntas() {
		if(periodoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el periodo","info",cboPeriodo,"end_center",2000);
			return;
		}
		if(moduloSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el módulo","info",cboModulo,"end_center",2000);
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TITULO_REPORTE","BANCO DE PREGUNTAS");
		params.put("MODULO_PERIODO","PERIODO: " + "Modulo: " + moduloSeleccionado.getModulo());
		params.put("ID_PERIODO",periodoSeleccionado.getIdPeriodo());
		params.put("ID_MODULO",moduloSeleccionado.getIdModulo());
		PrintReport obj = new PrintReport();
		obj.crearReporte("/recursos/rpt/rptBancoPreguntas.jasper", periodoDAO, params);
	}
	@Command
	public void imprimirInstructor() {
		if(periodoInstructorSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el periodo","info",cboPeriodoInstructor,"end_center",2000);
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TITULO_REPORTE","NÓMINA DE INSTRUCTORES");
		params.put("ID_PERIODO",periodoInstructorSeleccionado.getIdPeriodo());
		PrintReport obj = new PrintReport();
		obj.crearReporte("/recursos/rpt/rptNominaInstructor.jasper", periodoDAO, params);
	}
	@Command
	public void imprimirAspirante() {
		if(periodoAspiranteSeleccionado == null) {
			Clients.showNotification("Debe seleccionar el periodo","info",cboPeriodoAspirante,"end_center",2000);
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TITULO_REPORTE","NÓMINA DE ASPIRANTE");
		params.put("ID_PERIODO",periodoAspiranteSeleccionado.getIdPeriodo());
		PrintReport obj = new PrintReport();
		obj.crearReporte("/recursos/rpt/rptNominaAspirantes.jasper", periodoDAO, params);
	}
	@Command
	public void imprimirListaModulo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TITULO_REPORTE","LISTADO DE MÓDULOS");
		PrintReport obj = new PrintReport();
		obj.crearReporte("/recursos/rpt/rptListaModulo.jasper", periodoDAO, params);
	}
	public List<Periodo> getPeriodos(){
		return periodoDAO.buscarActivos();
	}
	public List<Periodo> getPeriodosInstructores(){
		return periodoDAO.buscarActivos();
	}
	public List<Periodo> getPeriodosAspirante(){
		return periodoDAO.buscarActivos();
	}
	public List<Modulo> getModulos(){
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
	public Periodo getPeriodoInstructorSeleccionado() {
		return periodoInstructorSeleccionado;
	}
	public void setPeriodoInstructorSeleccionado(Periodo periodoInstructorSeleccionado) {
		this.periodoInstructorSeleccionado = periodoInstructorSeleccionado;
	}
	public Periodo getPeriodoAspiranteSeleccionado() {
		return periodoAspiranteSeleccionado;
	}
	public void setPeriodoAspiranteSeleccionado(Periodo periodoAspiranteSeleccionado) {
		this.periodoAspiranteSeleccionado = periodoAspiranteSeleccionado;
	}
}