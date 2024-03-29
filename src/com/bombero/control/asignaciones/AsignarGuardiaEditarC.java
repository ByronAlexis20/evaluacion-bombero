package com.bombero.control.asignaciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.AsignarGuardiaDAO;
import com.bombero.model.dao.PersonalAutorizadoDAO;
import com.bombero.model.entity.AsignarGuardia;
import com.bombero.model.entity.Compania;
import com.bombero.model.entity.PersonalAutorizado;

import org.zkoss.zul.Messagebox.ClickEvent;

public class AsignarGuardiaEditarC {
	@Wire Window winRealizarAsignacion;
	@Wire Datebox dtpFechaInicio;
	@Wire Datebox dtpFechaFin;
	@Wire Label lblCompania;
	@Wire Combobox cboBombero;
	private Compania compania;
	AsignarGuardiaListaC asignacionC;
	private PersonalAutorizado bomberoSeleccionado;
	AsignarGuardiaDAO guardiaDAO = new AsignarGuardiaDAO();
	PersonalAutorizadoDAO personalAutorizadoDAO = new PersonalAutorizadoDAO();
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		compania = (Compania)Executions.getCurrent().getArg().get("Compania");
		asignacionC = (AsignarGuardiaListaC)Executions.getCurrent().getArg().get("Ventana");
		if(compania != null) {
			lblCompania.setValue(compania.getNombre());
		}
	}
	@Command
	public void grabar() {
		try {
			if(bomberoSeleccionado == null) {
				Clients.showNotification("Obligatoria seleccionar un bombero","info",cboBombero,"end_center",2000);
				return;
			}
			if(dtpFechaInicio.getValue() == null) {
				Clients.showNotification("Debe seleccionar fecha de inicio");
				return;
			}
			if(dtpFechaFin.getValue() == null) {
				Clients.showNotification("Debe seleccionar fecha de finalización");
				return;
			}
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						AsignarGuardia guardia = new AsignarGuardia();
						guardia.setCompania(compania);
						guardia.setEstado("A");
						guardia.setIdGuardia(null);
						guardia.setObservacion("");
						guardia.setFechaIngreso(dtpFechaInicio.getValue());
						guardia.setFechaFin(dtpFechaFin.getValue());
						guardia.setPersonalAutorizado(bomberoSeleccionado);
						
						guardiaDAO.getEntityManager().getTransaction().begin();
						guardiaDAO.getEntityManager().merge(guardia);
						guardiaDAO.getEntityManager().getTransaction().commit();
						salir();
					}
				}
			};
			Messagebox.show("¿Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void salir() {
		try {
			BindUtils.postGlobalCommand(null, null, "AsignarGuardia.buscarPorCompania", null);
			winRealizarAsignacion.detach();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public PersonalAutorizado getBomberoSeleccionado() {
		return bomberoSeleccionado;
	}
	public void setBomberoSeleccionado(PersonalAutorizado bomberoSeleccionado) {
		this.bomberoSeleccionado = bomberoSeleccionado;
	}
	public List<PersonalAutorizado> getListaBomberos() {
		List<PersonalAutorizado> personal = personalAutorizadoDAO.getPersonalAutorizado();
		List<PersonalAutorizado> listaAgregar = new ArrayList<>();
		List<AsignarGuardia> asignaciones = guardiaDAO.getListaAsignaciones();
		boolean bandera = false;
		for(PersonalAutorizado per : personal) {
			bandera = false;
			for(AsignarGuardia asig : asignaciones) {
				if(per.getIdPersonal() == asig.getPersonalAutorizado().getIdPersonal())
					bandera = true;
			}
			if(bandera == false)
				listaAgregar.add(per);
		}
		return listaAgregar;
	}
}
