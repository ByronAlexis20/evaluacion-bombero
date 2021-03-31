package com.bombero.control.administracion;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.EquipoVestimentaDAO;
import com.bombero.model.entity.EquipoVestimenta;

public class EquipoEditarC {
	@Wire private Window winEquipoEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	@Wire private Textbox txtPrecio;
	@Wire private Textbox txtStock;

	private EquipoVestimentaDAO equipoDAO = new EquipoVestimentaDAO();
	private EquipoVestimenta equipo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		equipo = (EquipoVestimenta) Executions.getCurrent().getArg().get("Equipo");
		if (equipo == null) {
			equipo = new EquipoVestimenta();
			equipo.setEstado("A");
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtDescripcion.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar la descripción del equipo","info",txtDescripcion,"end_center",2000);
				txtDescripcion.setFocus(true);
				return retorna;
			}
			if(txtPrecio.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el precio de adquisición del equipo","info",txtPrecio,"end_center",2000);
				txtPrecio.setFocus(true);
				return retorna;
			}
			if(txtStock.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar el Stock","info",txtStock,"end_center",2000);
				txtStock.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar(){
		if(isValidarDatos() == true) {
			return;
		}
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						
						equipoDAO.getEntityManager().getTransaction().begin();
						if (equipo.getIdEquipo() == null) {
							equipoDAO.getEntityManager().persist(equipo);
						}else{
							equipo = (EquipoVestimenta) equipoDAO.getEntityManager().merge(equipo);
						}			
						equipoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						equipoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "EquipoVestimenta.buscarPorPatron", null);
		winEquipoEditar.detach();
	}

	public EquipoVestimenta getEquipo() {
		return equipo;
	}

	public void setEquipo(EquipoVestimenta equipo) {
		this.equipo = equipo;
	}

	
}
