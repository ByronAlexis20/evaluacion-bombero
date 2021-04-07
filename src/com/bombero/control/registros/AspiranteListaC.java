package com.bombero.control.registros;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Periodo;

public class AspiranteListaC {
	PeriodoDAO periodoDAO = new PeriodoDAO();
	List<Periodo> periodoLista;
	Periodo periodoSeleccionado;
	@Wire private Listbox lstPeriodos;
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	List<Matricula> matriculaLista;
	Matricula matriculaSeleccionado;
	@Wire private Listbox lstAspirantes;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		buscarPeriodo();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Periodo.buscarPorPatron")
	@Command
	@NotifyChange({"periodoLista"})
	public void buscarPeriodo(){
		if (periodoLista != null) {
			periodoLista = null; 
		}
		periodoLista = periodoDAO.getPeriodoPorDescripcion("");
		lstPeriodos.setModel(new ListModelList(periodoLista));
		if(periodoLista.size() == 0) 
			Clients.showNotification("No hay Periodos para mostrar.!!");
	}
	
	@Command
	public void seleccionarPeriodo() {
		buscarAspirantes(periodoSeleccionado.getIdPeriodo());
	}
	
	@GlobalCommand("Matricula.buscarAspirantePorPeriodo")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"matriculaLista"})
	public void buscarAspirantes(Integer idPeriodo) {
		matriculaLista = matriculaDAO.obtenerAspirantesPorIdPeriodo(idPeriodo);
		lstAspirantes.setModel(new ListModelList(matriculaLista));
		if(matriculaLista.size() == 0) 
			Clients.showNotification("No hay Aspirantes para mostrar.!!");
		matriculaSeleccionado = null;
	}
	
	@Command
	public void nuevoAspirante() {
		try {
			Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspiranteEditar.zul", null, null);
			ventanaCargar.doModal();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public List<Periodo> getPeriodoLista() {
		return periodoLista;
	}
	
	public void setPeriodoLista(List<Periodo> periodoLista) {
		this.periodoLista = periodoLista;
	}
	
	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	
	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	
	public List<Matricula> getMatriculaLista() {
		return matriculaLista;
	}
	
	public void setMatriculaLista(List<Matricula> matriculaLista) {
		this.matriculaLista = matriculaLista;
	}
	
	public Matricula getMatriculaSeleccionado() {
		return matriculaSeleccionado;
	}
	
	public void setMatriculaSeleccionado(Matricula matriculaSeleccionado) {
		this.matriculaSeleccionado = matriculaSeleccionado;
	}
}
