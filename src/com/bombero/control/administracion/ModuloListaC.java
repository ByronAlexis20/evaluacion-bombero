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

import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.entity.Modulo;

public class ModuloListaC {
	
	public String textoBuscar;
	ModuloDAO moduloDAO = new ModuloDAO();
	List<Modulo> moduloLista;
	@Wire private Listbox lstModulos;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Modulo.buscarPorPatron")
	@Command
	@NotifyChange({"moduloLista"})
	public void buscar(){
		if (moduloLista != null) {
			moduloLista = null; 
		}
		moduloLista = moduloDAO.getModuloPorDescripcion(textoBuscar);
		lstModulos.setModel(new ListModelList(moduloLista));
		if(moduloLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/moduloEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("modulo") Modulo moduloSeleccionado){
		if(moduloSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		moduloDAO.getEntityManager().refresh(moduloSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Modulo", moduloSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/moduloEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("modulo") Modulo moduloSeleccionado){
		if (moduloSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						moduloDAO.getEntityManager().getTransaction().begin();
						moduloSeleccionado.setEstado("I");
						moduloDAO.getEntityManager().merge(moduloSeleccionado);
						moduloDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						moduloDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<Modulo> getModuloLista() {
		return moduloLista;
	}

	public void setModuloLista(List<Modulo> moduloLista) {
		this.moduloLista = moduloLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
