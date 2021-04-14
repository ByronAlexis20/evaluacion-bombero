package com.bombero.control.registros;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import com.bombero.model.dao.TipoDocumentoDAO;
import com.bombero.model.entity.TipoDocumento;

public class DocumentoRegistroC {
	@Wire Window winDocumentoRegistro;
	TipoDocumentoDAO tipoDocumentoDAO = new TipoDocumentoDAO();
	TipoDocumento tipoDocumentoSeleccionado;
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	@Command
	public void salir() {
		winDocumentoRegistro.detach();
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
