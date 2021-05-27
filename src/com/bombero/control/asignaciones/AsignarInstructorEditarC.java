package com.bombero.control.asignaciones;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.InstructorDAO;
import com.bombero.model.dao.ModuloDAO;
import com.bombero.model.entity.Instructor;
import com.bombero.model.entity.Modulo;
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
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		periodo = (Periodo)Executions.getCurrent().getArg().get("Periodo");
	}
	
	@Command
	public void salir() {
		winAsignarInstructor.detach();
	}
	public Periodo getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Periodo periodo) {
		this.periodo = periodo;
	}
	public List<Modulo> getModulos() {
		return moduloDAO.getModuloPorDescripcion("");
	}
	public List<Instructor> getInstructores() {
		return instructorDAO.buscarPorNombreApellido("");
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
