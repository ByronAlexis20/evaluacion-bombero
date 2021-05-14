package com.bombero.control.registros;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

public class FichaMedicaC {
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@Command
	public void nuevoFamiliar() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FichaMedica", null);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/familiares.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void nuevaCirugia() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("FichaMedica", null);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/cirugia.zul", null, params);
		ventanaCargar.doModal();
	}
}
