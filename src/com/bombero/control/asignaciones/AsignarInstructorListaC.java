package com.bombero.control.asignaciones;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ModuloAsignadoDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.ModuloAsignado;
import com.bombero.model.entity.Periodo;
import com.bombero.util.Globals;

public class AsignarInstructorListaC {
	PeriodoDAO periodoDAO = new PeriodoDAO();
	ModuloAsignadoDAO moduloAsignadoDAO = new ModuloAsignadoDAO();
	List<Periodo> periodoLista;
	Periodo periodoSeleccionado;
	@Wire private Listbox lstPeriodos;
	List<ModuloAsignado> listaModulosAsignados;
	ModuloAsignado moduloAsignadoSeleccionado;
	@Wire Button btnNuevo;
	@Wire Button btnEliminar;
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
		periodoLista = periodoDAO.buscarActivos();
		lstPeriodos.setModel(new ListModelList(periodoLista));
		if(periodoLista.size() == 0) 
			Clients.showNotification("No hay Periodos para mostrar.!!");
	}
	@GlobalCommand("ModuloAsignado.buscarAsignacionesPorPeriodo")
	@NotifyChange({"listaModulosAsignados"})
	@Command
	public void seleccionarPeriodo() {
		if(listaModulosAsignados != null)
			listaModulosAsignados = null;
		listaModulosAsignados = moduloAsignadoDAO.obtenerAsignacionesPorPeriodo(periodoSeleccionado.getIdPeriodo());
		if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
			btnNuevo.setDisabled(true);
			btnEliminar.setDisabled(true);
		} else {
			btnNuevo.setDisabled(false);
			btnEliminar.setDisabled(false);
		}
	}
	@Command
	public void nuevaAsignacion() {
		try {
			if(periodoSeleccionado == null) {
				Clients.showNotification("Debe Seleccionar un periodo");
				return;
			}
			if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
				Clients.showNotification("Debe Seleccionar un periodo");
				return;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Periodo", periodoSeleccionado);
			Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/asignaciones/instructores/asignarInstructorEditar.zul", null, params);
			ventanaCargar.doModal();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminarAsignacion(){
		if(moduloAsignadoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						moduloAsignadoDAO.getEntityManager().getTransaction().begin();
						moduloAsignadoSeleccionado.setEstado("I");
						moduloAsignadoDAO.getEntityManager().merge(moduloAsignadoSeleccionado);
						moduloAsignadoDAO.getEntityManager().getTransaction().commit();;
						BindUtils.postGlobalCommand(null, null, "ModuloAsignado.buscarAsignacionesPorPeriodo", null);
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						moduloAsignadoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
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
	public List<ModuloAsignado> getListaModulosAsignados() {
		return listaModulosAsignados;
	}
	public void setListaModulosAsignados(List<ModuloAsignado> listaModulosAsignados) {
		this.listaModulosAsignados = listaModulosAsignados;
	}
	public ModuloAsignado getModuloAsignadoSeleccionado() {
		return moduloAsignadoSeleccionado;
	}
	public void setModuloAsignadoSeleccionado(ModuloAsignado moduloAsignadoSeleccionado) {
		this.moduloAsignadoSeleccionado = moduloAsignadoSeleccionado;
	}
}