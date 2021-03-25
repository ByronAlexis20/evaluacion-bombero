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

import com.bombero.model.dao.TipoDocumentoDAO;
import com.bombero.model.entity.TipoDocumento;

public class TipoDocListaC {
	public String textoBuscar;
	TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
	List<TipoDocumento> tipoDocumentoLista;
	@Wire private Listbox lstTipoDocumento;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("TipoDocumento.buscarPorPatron")
	@Command
	@NotifyChange({"tipoDocumentoLista"})
	public void buscar(){
		if (tipoDocumentoLista != null) {
			tipoDocumentoLista = null; 
		}
		tipoDocumentoLista = tipoDocumentoDAO.getTipoDocumentoPorDescripcion(textoBuscar);
		lstTipoDocumento.setModel(new ListModelList(tipoDocumentoLista));
		if(tipoDocumentoLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoDocEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("tipoDocumento") TipoDocumento tipoDocumentoSeleccionado){
		if(tipoDocumentoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		tipoDocumentoDAO.getEntityManager().refresh(tipoDocumentoSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoDocumento", tipoDocumentoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/tipoDocEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("tipoDocumento") TipoDocumento tipoDocumentoSeleccionado){
		if (tipoDocumentoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoDocumentoDAO.getEntityManager().getTransaction().begin();
						tipoDocumentoSeleccionado.setEstado("I");
						tipoDocumentoDAO.getEntityManager().merge(tipoDocumentoSeleccionado);
						tipoDocumentoDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						tipoDocumentoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});		
	}

	public List<TipoDocumento> getTipoDocumentoLista() {
		return tipoDocumentoLista;
	}

	public void setTipoDocumentoLista(List<TipoDocumento> tipoDocumentoLista) {
		this.tipoDocumentoLista = tipoDocumentoLista;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
