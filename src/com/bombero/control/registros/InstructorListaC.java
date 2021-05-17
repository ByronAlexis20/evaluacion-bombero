package com.bombero.control.registros;

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
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.bombero.model.entity.Instructor;

public class InstructorListaC {
	public String textoBuscar;
	@Wire private Listbox lstInstructores;
	List<Instructor> instructorLista;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}
	@Command
	public void nuevo(){
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/instructores/instructorEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	public String getTextoBuscar() {
		return textoBuscar;
	}
	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}
	public List<Instructor> getInstructorLista() {
		return instructorLista;
	}
	public void setInstructorLista(List<Instructor> instructorLista) {
		this.instructorLista = instructorLista;
	}
	
}
