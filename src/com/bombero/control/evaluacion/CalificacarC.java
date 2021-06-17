package com.bombero.control.evaluacion;

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
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.CalificacionDAO;
import com.bombero.model.entity.Calificacion;
import com.bombero.util.Globals;

public class CalificacarC {
	@Wire private Window winCalificar;
	@Wire private Decimalbox txtNota1;
	@Wire private Decimalbox txtNota2;
	@Wire private Decimalbox txtNota3;
	@Wire private Decimalbox txtNota4;
	
	Calificacion calificacion;
	CalificacionDAO calificacionDAO = new CalificacionDAO();
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		// Recupera el objeto pasado como parametro. 
		calificacion = (Calificacion) Executions.getCurrent().getArg().get("Calificacion");
		recuperarCalificaciones();
	}
	private void recuperarCalificaciones() {
		txtNota1.setText(String.valueOf(calificacion.getNota1()));
		txtNota2.setText(String.valueOf(calificacion.getNota2()));
		txtNota3.setText(String.valueOf(calificacion.getNota3()));
		txtNota4.setText(String.valueOf(calificacion.getNota4()));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		if(validarNotas() == false)
			return;
		Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {		
					try {
						if(txtNota1.getValue() != null)
							calificacion.setNota1(Float.parseFloat(String.valueOf(txtNota1.getValue())));
						else
							calificacion.setNota1(0);
						
						if(txtNota2.getValue() != null)
							calificacion.setNota2(Float.parseFloat(String.valueOf(txtNota2.getValue())));
						else
							calificacion.setNota2(0);
						
						if(txtNota3.getValue() != null)
							calificacion.setNota3(Float.parseFloat(String.valueOf(txtNota3.getValue())));
						else
							calificacion.setNota3(0);
						
						if(txtNota4.getValue() != null)
							calificacion.setNota4(Float.parseFloat(String.valueOf(txtNota4.getValue())));
						else
							calificacion.setNota4(0);
						
						calificacionDAO.getEntityManager().getTransaction().begin();
						calificacionDAO.getEntityManager().merge(calificacion);
						calificacionDAO.getEntityManager().getTransaction().commit();
						Clients.showNotification("Proceso Ejecutado con exito.");
						salir();						
					} catch (Exception e) {
						e.printStackTrace();
						calificacionDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
	}
	private boolean validarNotas() {
		try {
			if(txtNota1.getValue() != null) {
				if(Float.parseFloat(String.valueOf(txtNota1.getValue())) < 0) {
					Clients.showNotification("Nota 1, no debe tener valores negativos","info",txtNota1,"end_center",2000);
					return false;
				}
				if(Float.parseFloat(String.valueOf(txtNota1.getValue())) > Globals.PUNTAJE_NOTA1) {
					Clients.showNotification("Nota 1, no puede ser mayor a " + Globals.PUNTAJE_NOTA1,"info",txtNota1,"end_center",2000);
					return false;
				}
			}
			if(txtNota2.getValue() != null) {
				if(Float.parseFloat(String.valueOf(txtNota2.getValue())) < 0) {
					Clients.showNotification("Nota 2, no debe tener valores negativos","info",txtNota2,"end_center",2000);
					return false;
				}
				if(Float.parseFloat(String.valueOf(txtNota2.getValue())) > Globals.PUNTAJE_NOTA2) {
					Clients.showNotification("Nota 2, no puede ser mayor a " + Globals.PUNTAJE_NOTA2,"info",txtNota2,"end_center",2000);
					return false;
				}
			}
			if(txtNota3.getValue() != null) {
				if(Float.parseFloat(String.valueOf(txtNota3.getValue())) < 0) {
					Clients.showNotification("Nota 3, no debe tener valores negativos","info",txtNota3,"end_center",2000);
					return false;
				}
				if(Float.parseFloat(String.valueOf(txtNota3.getValue())) > Globals.PUNTAJE_NOTA3) {
					Clients.showNotification("Nota 3, no puede ser mayor a " + Globals.PUNTAJE_NOTA3,"info",txtNota3,"end_center",2000);
					return false;
				}
			}
			if(txtNota4.getValue() != null) {
				if(Float.parseFloat(String.valueOf(txtNota4.getValue())) < 0) {
					Clients.showNotification("Nota 4, no debe tener valores negativos","info",txtNota4,"end_center",2000);
					return false;
				}
				if(Float.parseFloat(String.valueOf(txtNota4.getValue())) > Globals.PUNTAJE_NOTA4) {
					Clients.showNotification("Nota 4, no puede ser mayor a " + Globals.PUNTAJE_NOTA4,"info",txtNota4,"end_center",2000);
					return false;
				}
			}
			return true;
		}catch(Exception ex) {
			return false;
		}
	}
	@Command
	public void salir(){
		BindUtils.postGlobalCommand(null, null, "Matricula.buscarAspirantePorPeriodo", null);
		winCalificar.detach();
	}
	public Calificacion getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(Calificacion calificacion) {
		this.calificacion = calificacion;
	}
}