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
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	Periodo periodoSeleccionado;
	Modulo moduloSeleccionado;
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
			Clients.showNotification("Debe seleccionar el m�dulo","info",cboModulo,"end_center",2000);
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
	public List<Periodo> getPeriodos(){
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
}