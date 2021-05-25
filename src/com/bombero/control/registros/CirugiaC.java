package com.bombero.control.registros;

import java.io.IOException;

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

import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Cirugia;

public class CirugiaC {
	@Wire private Window winCirugia;
	@Wire private Textbox txtOrgano;
	@Wire private Datebox dtpFecha;
	AspiranteEditarC aspiranteEditarC;
	Aspirante aspirante;
	Cirugia cirugia;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		aspiranteEditarC = (AspiranteEditarC) Executions.getCurrent().getArg().get("Ventana");
		aspirante = (Aspirante) Executions.getCurrent().getArg().get("Aspirante");
		cirugia = new Cirugia();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if(validarDatos() == false) {
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						cirugia.setEstado("A");
						cirugia.setFechaCirugia(dtpFecha.getValue());
						cirugia.setIdCirugia(null);
						cirugia.setNombreOrganoComprometido(txtOrgano.getText());
						aspiranteEditarC.agregarListaCirugia(cirugia);
						salir();
					}
				}
			});
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private boolean validarDatos() {
		if(txtOrgano.getText().isEmpty()) {
			Clients.showNotification("Debe registrar el nombre del organo comprometido","info",txtOrgano,"end_center",2000);
			txtOrgano.focus();
			return false;
		}
		if(dtpFecha.getValue() == null) {
			Clients.showNotification("Debe seleccionar fecha","info",dtpFecha,"end_center",2000);
			return false;
		}
		return true;
	}
	@Command
	public void salir() {
		winCirugia.detach();
	}
	public Cirugia getCirugia() {
		return cirugia;
	}
	public void setCirugia(Cirugia cirugia) {
		this.cirugia = cirugia;
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
}
