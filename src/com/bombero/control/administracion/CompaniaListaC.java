package com.bombero.control.administracion;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CompaniaDAO;
import com.bombero.model.entity.Compania;

public class CompaniaListaC {
	public String textoBuscar;
	CompaniaDAO companiaDAO = new CompaniaDAO();
	List<Compania> companiaLista;
	@Wire private Listbox lstCompanias;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Compania.buscarPorPatron")
	@Command
	@NotifyChange({"companiaLista"})
	public void buscar(){
		if (companiaLista != null) {
			companiaLista = null; 
		}
		companiaLista = companiaDAO.getCompaniaPorDescripcion(textoBuscar);
		lstCompanias.setModel(new ListModelList(companiaLista));
		if(companiaLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/companiaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("compania") Compania companiaSeleccionado){
		if(companiaSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		companiaDAO.getEntityManager().refresh(companiaSeleccionado);	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Compania", companiaSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/companiaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("compania") Compania companiaSeleccionado){
		if (companiaSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						companiaDAO.getEntityManager().getTransaction().begin();
						companiaSeleccionado.setEstado("I");
						companiaDAO.getEntityManager().merge(companiaSeleccionado);
						companiaDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						companiaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<Compania> getCompaniaLista() {
		return companiaLista;
	}

	public void setCompaniaLista(List<Compania> companiaLista) {
		this.companiaLista = companiaLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
