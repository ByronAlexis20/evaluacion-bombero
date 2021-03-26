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

import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Periodo;
import com.bombero.util.Globals;

public class PeriodoEditarC {
	@Wire private Window winPeriodoEditar;
	@Wire private Textbox txtDescripcion;
	@Wire private Datebox dtpFechaInicio;
	@Wire private Datebox dtpFechaFin;

	private PeriodoDAO periodoDAO = new PeriodoDAO();
	private Periodo periodo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		periodo = (Periodo) Executions.getCurrent().getArg().get("Periodo");
		dtpFechaFin.setDisabled(true);
		if (periodo == null) {
			periodo = new Periodo();
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
						periodo.setEstadoPeriodo(Globals.ESTADO_PERIODO_EN_PROCESO);
						
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
		BindUtils.postGlobalCommand(null, null, "Periodo.buscarPorPatron", null);
		winPeriodoEditar.detach();
	}

	public Periodo getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}

}
