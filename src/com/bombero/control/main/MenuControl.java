package com.bombero.control.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.mail.MessagingException;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.bombero.model.dao.ConfiguracionDAO;
import com.bombero.model.dao.MenuDAO;
import com.bombero.model.dao.PermisoDAO;
import com.bombero.model.dao.UsuarioDAO;
import com.bombero.model.entity.Configuracion;
import com.bombero.model.entity.Menu;
import com.bombero.model.entity.Permiso;
import com.bombero.model.entity.Usuario;
import com.bombero.util.FileUtil;
import com.bombero.util.Globals;
import com.bombero.util.SecurityUtil;

public class MenuControl {
	@Wire Tree menu;
	@Wire Include areaContenido;
	@Wire Image fotoUsuario;
	Menu opcionSeleccionado;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	PermisoDAO permisoDAO = new PermisoDAO();
	MenuDAO menuDAO = new MenuDAO();
	ConfiguracionDAO configuracionDAO = new ConfiguracionDAO();
	List<Menu> listaPermisosPadre = new ArrayList<Menu>();
	List<Permiso> listaPermisosHijo = new ArrayList<Permiso>();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException, MessagingException{
		Selectors.wireComponents(view, this, false);
		loadTree();
		cargarDatosConfiguracion();
		//startLongOperation();
	}
	public void cargarDatosConfiguracion(){
		List<Configuracion> lista = configuracionDAO.buscarConfiguracionActiva();
		if(lista.size() > 0) {
			Globals.CANTIDAD_PREGUNTAS = lista.get(0).getCantidadPreguntas();
			Globals.PUNTAJE_POR_PREGUNTA = lista.get(0).getPuntaje();
			Globals.NOMBRE_PRIMER_JEFE = lista.get(0).getNombrePrimerJefe();
			Globals.CARGO_PRIMER_JEFE = lista.get(0).getCargoPrimerJefe();
			Globals.NOMBRE_SEGUNDO_JEFE = lista.get(0).getNombreSegundoJefe();
			Globals.CARGO_SEGUNDO_JEFE = lista.get(0).getCargoSegundoJefe();
			Globals.NOMBRE_CAPITAN = lista.get(0).getNombreCapitan();
			Globals.CARGO_CAPITAN = lista.get(0).getCargoCapitan();
		}
	}
	public void loadTree() throws IOException{		
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 
		if (usuario != null){
			//listaPermisosPadre = permisoDAO.getListaPermisosPadre(usuario.getSegPerfil().getIdPerfil());
			listaPermisosHijo = permisoDAO.getListaPermisosHijo(usuario.getPerfil().getIdPerfil());
			
			//obtener la lista de menus padre de cada menu
			boolean bandera = false;
			for(Permiso per : listaPermisosHijo) {
				bandera = false;
				List<Menu> listaMenu = menuDAO.getMenuPadre(per.getMenu().getIdMenuPadre());
				if(listaMenu.size() > 0) {
					for(Menu mnu : listaPermisosPadre) {
						for(Menu mnu2 : listaMenu) {
							if(mnu.getIdMenu() == mnu2.getIdMenu())
								bandera = true;
						}
					}
					if(bandera == false)
						listaPermisosPadre.add(listaMenu.get(0));
				}
			}
			
			if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<Menu> listaMenu = new ArrayList<Menu>();
				for(Menu permiso : listaPermisosPadre) {
					listaMenu.add(permiso);
				}
				Collections.sort(listaMenu, new Comparator<Menu>() {
					@SuppressWarnings("deprecation")
					@Override
					public int compare(Menu p1, Menu p2) {
						return new Integer(p1.getPosicion()).compareTo(new Integer(p2.getPosicion()));
					}
				});
				menu.appendChild(getTreechildren(listaMenu));   
			}
			cargarFotoUsuario(usuario);
		}
	}
	private Treechildren getTreechildren(List<Menu> listaMenu) {
		Treechildren retorno = new Treechildren();
		for(Menu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);
			ti.setStyle("color: #FFFFFF;");
			retorno.appendChild(ti);
			List<Menu> listaPadreHijo = new ArrayList<Menu>();
			for(Permiso permiso : listaPermisosHijo) {
				if(permiso.getMenu().getIdMenuPadre() == opcion.getIdMenu()) {
					listaPadreHijo.add(permiso.getMenu());
				}
			}
			if (!listaPadreHijo.isEmpty()) {
				Collections.sort(listaPadreHijo, new Comparator<Menu>() {
					@SuppressWarnings("deprecation")
					@Override
					public int compare(Menu p1, Menu p2) {
						return new Integer(p1.getPosicion()).compareTo(new Integer(p2.getPosicion()));
					}
				});
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private Treeitem getTreeitem(Menu opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());
		//System.out.println("titulomenu: " + tc);
		if (opcion.getIcono() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getIcono());
		}
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}
	@NotifyChange({"areaContenido"})
	public void loadContenido(Menu opcion) {	
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
	}
	public void cargarFotoUsuario(Usuario us) {
		if(us != null) {
			if(us.getFoto() != null) {
				fotoUsuario.setContent(getImagenUsuario(us));
			}
		}
	}
	public AImage getImagenUsuario(Usuario us) {
		AImage retorno = null;
		if (us.getFoto() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(us.getFoto()), 100, -1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}
	public String getNombreUsuario() {
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
		String nombreUsuario = usuario.getNombres() + " " + usuario.getApellidos();
		return nombreUsuario;
	}
	
	public String getPerfilUsuario() {
		Usuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername());
		String perfilUsuario = usuario.getPerfil().getPerfil();
		return perfilUsuario;
	}
	
	@Command
	public void dashboard() {
		areaContenido.setSrc("/contenido.zul");
	}
}
