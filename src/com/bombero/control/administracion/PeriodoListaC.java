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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Periodo;
import com.bombero.util.Globals;

public class PeriodoListaC {
	public String textoBuscar;
	PeriodoDAO periodoDAO = new PeriodoDAO();
	List<Periodo> periodoLista;
	@Wire private Listbox lstPeriodos;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
		buscar();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GlobalCommand("Periodo.buscarPorPatron")
	@Command
	@NotifyChange({"periodoLista"})
	public void buscar(){
		if (periodoLista != null) {
			periodoLista = null; 
		}
		periodoLista = periodoDAO.getPeriodoPorDescripcion(textoBuscar);
		lstPeriodos.setModel(new ListModelList(periodoLista));
		if(periodoLista.size() == 0) {
			Clients.showNotification("No hay datos para mostrar.!!");
		}
	}
	
	@Command
	public void nuevo(){
		boolean bandera = false;
		for(Listitem item : lstPeriodos.getItems()) {
			Periodo per = (Periodo)item.getValue();
			if(per.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_EN_PROCESO)) {
				bandera = true;
			}
		}
		if(bandera == true) {
			Messagebox.show("Existe un periodo EN PROCESO, se debe finalizar antes de crear uno nuevo");
			return;
		}
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/periodoEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(@BindingParam("periodo") Periodo periodoSeleccionado){
		if(periodoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		// Actualiza la instancia antes de enviarla a editar.
		periodoDAO.getEntityManager().refresh(periodoSeleccionado);		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", periodoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/administracion/periodoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void finalizar() {
		if(lstPeriodos.getSelectedItem().getValue() == null) {
			Messagebox.show("Debe seleccionar un periodo");
			return;
		}
		Periodo periodoSeleccionado = (Periodo) lstPeriodos.getSelectedItem().getValue();
		if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
			Messagebox.show("El Periodo ya ha sido FINALIZADO anteriormente");
			return;
		}
		Messagebox.show("Desea dar por finalizado el periodo?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						periodoDAO.getEntityManager().getTransaction().begin();
						periodoSeleccionado.setEstadoPeriodo(Globals.ESTADO_PERIODO_FINALIZADO);
						periodoDAO.getEntityManager().merge(periodoSeleccionado);
						periodoDAO.getEntityManager().getTransaction().commit();;
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						periodoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("periodo") Periodo periodoSeleccionado){
		if (periodoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return; 
		}
		if(periodoSeleccionado.getEstadoPeriodo().equals(Globals.ESTADO_PERIODO_FINALIZADO)) {
			Messagebox.show("No se puede eliminar un periodo FINALIZADO");
			return;
		}
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						periodoDAO.getEntityManager().getTransaction().begin();
						periodoSeleccionado.setEstado("I");
						periodoDAO.getEntityManager().merge(periodoSeleccionado);
						periodoDAO.getEntityManager().getTransaction().commit();;
						// Actualiza la lista
						//BindUtils.postGlobalCommand(null, null, "Categoria.buscarPorPatron", null);
						buscar();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						periodoDAO.getEntityManager().getTransaction().rollback();
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

	public String getTextoBuscar() {
		return textoBuscar;
	}
	
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
}
