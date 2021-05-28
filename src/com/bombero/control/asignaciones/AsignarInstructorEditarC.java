package com.bombero.control.asignaciones;

import java.io.IOException;
import java.util.List;

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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.InstructorDAO;
import com.bombero.model.dao.ModuloAsignadoDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.entity.Instructor;
import com.bombero.model.entity.Modulo;
import com.bombero.model.entity.ModuloAsignado;
import com.bombero.model.entity.Periodo;

public class AsignarInstructorEditarC {
	@Wire Window winAsignarInstructor;
	@Wire Combobox cbomodulo;
	@Wire Combobox cboInstructor;
	Periodo periodo;
	Modulo moduloSeleccionado;
	Instructor instructorSeleccionado;
	InstructorDAO instructorDAO = new InstructorDAO();
	ModuloDAO moduloDAO = new ModuloDAO();
	ModuloAsignadoDAO moduloAsignadoDAO = new ModuloAsignadoDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		periodo = (Periodo)Executions.getCurrent().getArg().get("Periodo");
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if(moduloSeleccionado == null) {
				Clients.showNotification("Obligatoria seleccionar un módulo","info",cbomodulo,"end_center",2000);
				return;
			}
			if(instructorSeleccionado == null) {
				Clients.showNotification("Obligatoria seleccionar un instructor","info",cboInstructor,"end_center",2000);
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {
							ModuloAsignado modAsignado = new ModuloAsignado();
							modAsignado.setEstado("A");
							modAsignado.setIdAsignacion(null);
							modAsignado.setInstructor(instructorSeleccionado);
							modAsignado.setModulo(moduloSeleccionado);
							modAsignado.setPeriodo(periodo);
							moduloAsignadoDAO.getEntityManager().getTransaction().begin();
							if (modAsignado.getIdAsignacion() == null) {
								moduloAsignadoDAO.getEntityManager().persist(modAsignado);
							}else{
								modAsignado = (ModuloAsignado) moduloAsignadoDAO.getEntityManager().merge(modAsignado);
							}			
							moduloAsignadoDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							salir();						
						} catch (Exception e) {
							e.printStackTrace();
							moduloAsignadoDAO.getEntityManager().getTransaction().rollback();
						}
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void salir() {
		BindUtils.postGlobalCommand(null, null, "ModuloAsignado.buscarAsignacionesPorPeriodo", null);
		winAsignarInstructor.detach();
	}
	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public List<Modulo> getModulos() {
		return moduloDAO.buscarSinAsignacionPorPeriodo(periodo.getIdPeriodo());
	}
	public List<Instructor> getInstructores() {
		return instructorDAO.buscarSinAsignacionPorPeriodo(periodo.getIdPeriodo());
	}
	public Modulo getModuloSeleccionado() {
		return moduloSeleccionado;
	}
	public void setModuloSeleccionado(Modulo moduloSeleccionado) {
		this.moduloSeleccionado = moduloSeleccionado;
	}
	public Instructor getInstructorSeleccionado() {
		return instructorSeleccionado;
	}
	public void setInstructorSeleccionado(Instructor instructorSeleccionado) {
		this.instructorSeleccionado = instructorSeleccionado;
	}
}
