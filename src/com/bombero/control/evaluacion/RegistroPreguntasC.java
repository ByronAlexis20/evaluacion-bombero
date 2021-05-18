package com.bombero.control.evaluacion;

import java.io.IOException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

public class RegistroPreguntasC {
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@Command
	public void nuevaPregunta() {
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/evaluacion/preguntaEditar.zul", null, null);
		ventanaCargar.doModal();
	}
	@Command
	public void editarPregunta() {
		
	}
	@Command
	public void eliminarPregunta() {
		
	}
}
