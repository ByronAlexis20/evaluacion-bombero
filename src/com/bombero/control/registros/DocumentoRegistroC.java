package com.bombero.control.registros;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.bombero.model.dao.TipoDocumentoDAO;
import com.bombero.model.entity.Documento;
import com.bombero.model.entity.TipoDocumento;
import com.bombero.util.Globals;

public class DocumentoRegistroC {
	@Wire Window winDocumentoRegistro;
	@Wire Combobox cboTipoDocumento;
	@Wire Textbox txtDocumento;
	TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
	TipoDocumento tipoDocumentoSeleccionado;
	Media mediaDocumento;
	AspiranteEditarC aspiranteEditarC;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		aspiranteEditarC = (AspiranteEditarC) Executions.getCurrent().getArg().get("Ventana");
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	public void grabar() {
		try {
			if(tipoDocumentoSeleccionado == null) {
				Clients.showNotification("Campo Obligatorio: Tipo de documento","info",cboTipoDocumento,"end_center",2000);
				return;
			}
			if(mediaDocumento == null) {
				Clients.showNotification("Debe seleccionar archivo","info",txtDocumento,"end_center",2000);
				return;
			}
			Messagebox.show("Desea guardar el registro?", "Confirmación de Guardar", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getName().equals("onYes")) {		
						try {
							String rutaProyecto = Globals.PATH_SISTEMA;
							rutaProyecto = rutaProyecto + Globals.PATH_ARCHIVO;
							String rutaArchivo = rutaProyecto + "\\" + tipoDocumentoSeleccionado.getInicialesArchivo() + "-" + mediaDocumento.getName();
							Documento doc = new Documento();
							doc.setEstado("A");
							doc.setIdDocumento(null);
							doc.setTipoDocumento(tipoDocumentoSeleccionado);
							doc.setRutaDocumento(rutaArchivo);
							File folder = new File(rutaProyecto);
							if (folder.exists()) {
							}else {
								folder.mkdir();
							}
							Files.copy(new File(rutaArchivo),mediaDocumento.getStreamData());
							aspiranteEditarC.recuperarDocumento(doc);
							salir();						
						} catch (Exception e) {
							System.out.println(e.getMessage());
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
		winDocumentoRegistro.detach();
	}
	@Command
	public void subirDocumento(@ContextParam(ContextType.BIND_CONTEXT) BindContext contexto) {
		UploadEvent eventoCarga = (UploadEvent) contexto.getTriggerEvent();
		mediaDocumento = eventoCarga.getMedia();
		txtDocumento.setText(mediaDocumento.getName());
	}
	public List<TipoDocumento> getListaTipoDocumento() {
		return tipoDocumentoDAO.getTipoDocumentoPorDescripcion("");
	}
	public TipoDocumento getTipoDocumentoSeleccionado() {
		return tipoDocumentoSeleccionado;
	}
	public void setTipoDocumentoSeleccionado(TipoDocumento tipoDocumentoSeleccionado) {
		this.tipoDocumentoSeleccionado = tipoDocumentoSeleccionado;
	}
}
