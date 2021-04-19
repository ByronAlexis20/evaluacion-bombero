package com.bombero.control.registros;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.DocumentoDAO;
import com.bombero.model.entity.Aspirante;
import com.bombero.model.entity.Documento;
import com.bombero.model.entity.Matricula;

public class DocumentoListaC {
	Matricula matricula;
	Aspirante aspirante;
	@Wire private Listbox lstDocumentos;
	@Wire private Window winDocumentosLista;
	List<Documento> listaDocumentos;
	DocumentoDAO documentoDAO = new DocumentoDAO();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		matricula = (Matricula) Executions.getCurrent().getArg().get("Matricula");
		aspirante = matricula.getAspirante();
		cargarDocumentos();
	}
	@GlobalCommand("Documento.buscarPorAspirante")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@NotifyChange({"listaDocumentos"})
	public void cargarDocumentos() {
		try {
			if(listaDocumentos != null)
				listaDocumentos = null;
			listaDocumentos = documentoDAO.buscarPorAspirante(aspirante.getIdAspirante());
			lstDocumentos.setModel(new ListModelList(listaDocumentos));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Command
	public void nuevoDocumento() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Aspirante", aspirante);
		Window ventanaCargar = (Window) Executions.createComponents("/recursos/forms/registros/aspirantes/documentoRegistro.zul", null, params);
		ventanaCargar.doModal();
	}
	@Command
	public void descargar(@BindingParam("documento") Documento documentoSeleccionado) throws FileNotFoundException{
		if(documentoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		Filedownload.save(new File(documentoSeleccionado.getRutaDocumento()), null);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void eliminar(@BindingParam("documento") Documento documentoSeleccionado){
		if(documentoSeleccionado == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		documentoDAO.getEntityManager().refresh(documentoSeleccionado);
		Messagebox.show("Desea dar de baja el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						documentoDAO.getEntityManager().getTransaction().begin();
						documentoSeleccionado.setEstado("I");
						documentoDAO.getEntityManager().merge(documentoSeleccionado);
						documentoDAO.getEntityManager().getTransaction().commit();;
						cargarDocumentos();
						Clients.showNotification("Transaccion ejecutada con exito.");
					} catch (Exception e) {
						e.printStackTrace();
						documentoDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});	
	}
	@Command
	public void salir() {
		winDocumentosLista.detach();
	}
	public Matricula getMatricula() {
		return matricula;
	}
	public void setMatricula(Matricula matricula) {
		this.matricula = matricula;
	}
	public List<Documento> getListaDocumentos() {
		return listaDocumentos;
	}
	public void setListaDocumentos(List<Documento> listaDocumentos) {
		this.listaDocumentos = listaDocumentos;
	}
	public Aspirante getAspirante() {
		return aspirante;
	}
	public void setAspirante(Aspirante aspirante) {
		this.aspirante = aspirante;
	}
}
