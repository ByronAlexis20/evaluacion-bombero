package com.bombero.control.registros;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;

import com.bombero.model.dao.EstadoCivilDAO;
import com.bombero.model.dao.GeneroDAO;
import com.bombero.model.dao.PaisDAO;
import com.bombero.model.dao.TipoSangreDAO;
import com.bombero.model.entity.EstadoCivil;
import com.bombero.model.entity.Genero;
import com.bombero.model.entity.Pai;
import com.bombero.model.entity.TipoSangre;

public class AspiranteEditarC {

	PaisDAO paisDAO = new PaisDAO();
	GeneroDAO generoDAO = new GeneroDAO();
	TipoSangreDAO tipoSangreDAO = new TipoSangreDAO();
	EstadoCivilDAO estadoivilDAO = new EstadoCivilDAO();
	Pai paisSeleccionado;
	Genero generoSeleccionado;
	TipoSangre tipoSangreSeleccionado;
	EstadoCivil estadoCivilSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}
	
	public List<Pai> getPaises(){
		return paisDAO.getPaises();
	}
	public List<Genero> getGeneros(){
		return generoDAO.getGeneros();
	}
	public List<TipoSangre> getTipoSangres(){
		return tipoSangreDAO.getTipoSangrePorDescripcion("");
	}
	public List<EstadoCivil> getEstadoCivils(){
		return estadoivilDAO.getEstadoCivils();
	}
	public Pai getPaisSeleccionado() {
		return paisSeleccionado;
	}
	public void setPaisSeleccionado(Pai paisSeleccionado) {
		this.paisSeleccionado = paisSeleccionado;
	}
	public Genero getGeneroSeleccionado() {
		return generoSeleccionado;
	}
	public void setGeneroSeleccionado(Genero generoSeleccionado) {
		this.generoSeleccionado = generoSeleccionado;
	}
	public TipoSangre getTipoSangreSeleccionado() {
		return tipoSangreSeleccionado;
	}
	public void setTipoSangreSeleccionado(TipoSangre tipoSangreSeleccionado) {
		this.tipoSangreSeleccionado = tipoSangreSeleccionado;
	}
	public EstadoCivil getEstadoCivilSeleccionado() {
		return estadoCivilSeleccionado;
	}
	public void setEstadoCivilSeleccionado(EstadoCivil estadoCivilSeleccionado) {
		this.estadoCivilSeleccionado = estadoCivilSeleccionado;
	}
}
