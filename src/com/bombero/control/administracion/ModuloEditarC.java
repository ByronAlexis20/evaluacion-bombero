package com.bombero.control.administracion;

import java.text.SimpleDateFormat;

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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.Periodo;

public class ModuloEditarC {
	@Wire private Window winModuloEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtModulo;

	private ModuloDAO moduloDAO = new ModuloDAO();
	private Modulo modulo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		modulo = (Modulo) Executions.getCurrent().getArg().get("Modulo");
		if (modulo == null) {
			modulo = new Modulo();
			periodo.setEstado("A");
		}else {
			dtpFechaInicio.setValue(periodo.getFechaInicio());
			dtpFechaFin.setDisabled(false);
			dtpFechaFin.setConstraint("after " + new SimpleDateFormat("yyyyMMdd").format(periodo.getFechaInicio()));
			dtpFechaFin.setValue(periodo.getFechaFin());
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean retorna = true;
			if(txtDescripcion.getText().isEmpty()) {
				Clients.showNotification("Obligatoria regitrar la Descripción","info",txtDescripcion,"end_center",2000);
				txtDescripcion.setFocus(true);
				return retorna;
			}
			if(dtpFechaInicio.getValue() == null) {
				Clients.showNotification("Obligatoria regitrar la Fecha de Inicio","info",dtpFechaInicio,"end_center",2000);
				dtpFechaInicio.setFocus(true);
				return retorna;
			}
			if(dtpFechaFin.getValue() == null) {
				Clients.showNotification("Obligatoria regitrar la Fecha Fin","info",dtpFechaFin,"end_center",2000);
				dtpFechaFin.setFocus(true);
				return retorna;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Command
	public void seleccionaFechaFin() {
		dtpFechaFin.setDisabled(false);
		dtpFechaFin.setConstraint("after " + new SimpleDateFormat("yyyyMMdd").format(dtpFechaInicio.getValue()));
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
						periodo.setDescripcion(txtDescripcion.getText());
						periodo.setFechaInicio(dtpFechaInicio.getValue());
						periodo.setFechaFin(dtpFechaFin.getValue());
						
						periodoDAO.getEntityManager().getTransaction().begin();			
						if (periodo.getIdPeriodo() == null) {
							periodoDAO.getEntityManager().persist(periodo);
						}else{
							periodo = (Periodo) periodoDAO.getEntityManager().merge(periodo);
						}			
						periodoDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						periodoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}	

	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Modulo.buscarPorPatron", null);
		winModuloEditar.detach();
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

}
