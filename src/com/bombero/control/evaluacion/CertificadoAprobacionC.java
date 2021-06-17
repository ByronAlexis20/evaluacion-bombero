package com.bombero.control.evaluacion;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Periodo;

public class CertificadoAprobacionC {
	PeriodoDAO periodoDAO = new PeriodoDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	Periodo periodoSeleccionado;
	List<Matricula> listaPersonas;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@GlobalCommand("Matricula.buscarPorPeriodoTerminado")
	@Command
	@NotifyChange({"listaPersonas"})
	public void seleccionarPersonas() {
		listaPersonas = matriculaDAO.buscarPorPeriodoTerminado(periodoSeleccionado.getIdPeriodo());
	}
	@Command
	public void imprimir(@BindingParam("matricula") Matricula mat){
		if(mat == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
	
	}
	public List<Periodo> getPeriodos(){
		return periodoDAO.getPeriodoPorDescripcion("");
	}
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	public List<Matricula> getListaPersonas() {
		return listaPersonas;
	}
	public void setListaPersonas(List<Matricula> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}
}