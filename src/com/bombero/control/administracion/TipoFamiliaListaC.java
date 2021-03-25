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

import com.bombero.model.dao.TipoFamiliaDAO;
import com.bombero.model.entity.TipoFamiliar;

public class TipoFamiliaListaC {
	public String textoBuscar;
	TipoFamiliaDAO tipoFamiliaDAO = new TipoFamiliaDAO();
	List<TipoFamiliar> tipoFamiliarLista;
	@Wire private Listbox lstTipoFamiliar;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("TipoFamiliar.buscarPorPatron")
	@Command
	@NotifyChange({"tipoFamiliarLista"})
	public void buscar(){
		if (tipoFamiliarLista != null) {
			tipoFamiliarLista = null; 
		}
		tipoFamiliarLista = tipoFamiliaDAO.getTipoFamiliarPorDescripcion(textoBuscar);
		lstTipoFamiliar.setModel(new ListModelList(tipoFamiliarLista));
		if(tipoFamiliarLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoFamiliaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("tipoFamilia") TipoFamiliar tipoFamiliarSeleccionado){
		if(tipoFamiliarSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		tipoFamiliaDAO.getEntityManager().refresh(tipoFamiliarSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoFamilia", tipoFamiliarSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoFamiliaEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("tipoFamilia") TipoFamiliar tipoFamiliarSeleccionado){
		if (tipoFamiliarSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoFamiliaDAO.getEntityManager().getTransaction().begin();
						tipoFamiliarSeleccionado.setEstado("I");
						tipoFamiliaDAO.getEntityManager().merge(tipoFamiliarSeleccionado);
						tipoFamiliaDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						tipoFamiliaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<TipoFamiliar> getTipoFamiliarLista() {
		return tipoFamiliarLista;
	}

	public void setTipoFamiliarLista(List<TipoFamiliar> tipoFamiliarLista) {
		this.tipoFamiliarLista = tipoFamiliarLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
