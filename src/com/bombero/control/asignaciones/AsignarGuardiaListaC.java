package com.bombero.control.asignaciones;

import java.io.IOException;
import java.util.ArrayList;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Window;

import com.bombero.model.dao.AsignarGuardiaDAO;
import com.bombero.model.dao.CompaniaDAO;
import com.bombero.model.entity.AsignarGuardia;
import com.bombero.model.entity.Compania;

public class AsignarGuardiaListaC {
	@Wire Listbox lstGuardias;
	@Wire Window winAsignacionGuardia;
	private Compania companiaSeleccionado;
	AsignarGuardia guardiaSeleccionado;
	List<AsignarGuardia> guardiaLista;
	CompaniaDAO companiaDAO = new CompaniaDAO();
	AsignarGuardiaDAO guardiaDAO = new AsignarGuardiaDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("AsignarGuardia.buscarPorCompania")
	@Command
	@NotifyChange({"guardiaLista"})
	public void seleccionarGuardias() {
		try {
			if(companiaSeleccionado == null) {
				Clients.showNotification("Debe seleccionar una compañía");
				List<AsignarGuardia> lista = new ArrayList<>();
				lstGuardias.setModel(new ListModelList(lista));
				return;
			}
			guardiaLista = guardiaDAO.getListaGuardias(companiaSeleccionado.getIdCompania());
			//lstGuardias.setModel(new ListModelList(guardiaLista));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void agregarGuardia() {
		try {
			if(companiaSeleccionado == null) {
				Clients.showNotification("Debe seleccionar una Compañía!!!!!",false);
				return;
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("Ventana", this);
			params.put("Compania", companiaSeleccionado);
			Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/asignaciones/guardia/asignarEditar.zul", winAsignacionGuardia, params);
			ventanaCargar.doModal();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void quitarGuardia() {
		try {
			System.out.println("quitar guardia");
			if(guardiaSeleccionado == null) {
				Clients.showNotification("Debe seleccionar un guardia a eliminar");
				return;
			}
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						guardiaSeleccionado.setEstado("I");
						guardiaDAO.getEntityManager().getTransaction().begin();
						guardiaDAO.getEntityManager().merge(guardiaSeleccionado);
						guardiaDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Transaccion ejecutada con exito.");
						BindUtils.postGlobalCommand(null, null, "AsignarGuardia.buscarPorCompania", null);
					}
				}
			};
			Messagebox.show("¿Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public List<Compania> getCompanias(){
		try {
			List<Compania> lista = companiaDAO.getCompaniaPorDescripcion("");
			return lista;
		}catch(Exception ex) {
			return null;
		}
	}
	public Compania getCompaniaSeleccionado() {
		return companiaSeleccionado;
	}
	public void setCompaniaSeleccionado(Compania companiaSeleccionado) {
		this.companiaSeleccionado = companiaSeleccionado;
	}
	public List<AsignarGuardia> getGuardiaLista() {
		return guardiaLista;
	}
	public void setGuardiaLista(List<AsignarGuardia> guardiaLista) {
		this.guardiaLista = guardiaLista;
	}
	public AsignarGuardia getGuardiaSeleccionado() {
		return guardiaSeleccionado;
	}
	public void setGuardiaSeleccionado(AsignarGuardia guardiaSeleccionado) {
		this.guardiaSeleccionado = guardiaSeleccionado;
	}
}