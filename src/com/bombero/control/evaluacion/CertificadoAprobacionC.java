package com.bombero.control.evaluacion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;

import com.bombero.model.dao.MatriculaDAO;
import com.bombero.model.dao.PeriodoDAO;
import com.bombero.model.entity.Matricula;
import com.bombero.model.entity.Periodo;
import com.bombero.util.Globals;
import com.bombero.util.PrintReport;

public class CertificadoAprobacionC {
	PeriodoDAO periodoDAO = new PeriodoDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	Periodo periodoSeleccionado;
	List<Matricula> listaPersonas;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException {
		Selectors.wireComponents(view, this, false);
	}

	@GlobalCommand("Matricula.buscarPorPeriodoTerminado")
	@Command
	@NotifyChange({ "listaPersonas" })
	public void seleccionarPersonas() {
		listaPersonas = matriculaDAO.buscarPorPeriodoTerminado(periodoSeleccionado.getIdPeriodo());
	}

	@Command
	public void imprimir(@BindingParam("matricula") Matricula mat) {
		if (mat == null) {
			Clients.showNotification("Seleccione una opción de la lista.");
			return;
		}
		String descripcion = "Por haber ASISTIDO Y APROBADO satisfactoriamente al curso de ASPIRANTES A BOMBEROS dictado en "
				+ "las instalaciones del Cuerpo de Bomberos de La Libertad, iniciado desde 10/01/2021 al 10/05/2021.";
		
		SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("MX"));
		Date fechaDate = new Date();
		String fechaSistema = formateador.format(fechaDate);
		String fecha = dateFormatter("yyyy-MM-dd hh:mm:ss","d 'de' MMMM 'del' yyyy", fechaSistema);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("NOMBRE_ASPIRANTE", mat.getAspirante().getNombres() + " " + mat.getAspirante().getApellidos());
		params.put("DESCRIPCION", descripcion);
		params.put("FECHA", "La Libertad, " + fecha);
		params.put("NOMBRE_PRIMER_JEFE", Globals.NOMBRE_PRIMER_JEFE);
		params.put("CARGO_PRIMER_JEFE", Globals.CARGO_PRIMER_JEFE);
		params.put("NOMBRE_SEGUNDO_JEFE", Globals.NOMBRE_SEGUNDO_JEFE);
		params.put("CARGO_SEGUNDO_JEFE", Globals.CARGO_SEGUNDO_JEFE);
		params.put("NOMBRE_CAPITAN", Globals.NOMBRE_CAPITAN);
		params.put("CARGO_CAPITAN", Globals.CARGO_CAPITAN);
		PrintReport obj = new PrintReport();
		obj.crearReporte("/recursos/rpt/rptCertificado.jasper", periodoDAO, params);
	}

	public static final Locale LOCALE_MX = new Locale("es", "MX");

	public static String dateFormatter(String inputFormat, String outputFormat, String inputDate) {
		// Define formato default de entrada.
		String input = inputFormat.isEmpty() ? "yyyy-MM-dd hh:mm:ss" : inputFormat;
		// Define formato default de salida.
		String output = outputFormat.isEmpty() ? "d 'de' MMMM 'del' yyyy" : outputFormat;
		String outputDate = inputDate;
		try {
			outputDate = new SimpleDateFormat(output, LOCALE_MX)
					.format(new SimpleDateFormat(input, LOCALE_MX).parse(inputDate));
		} catch (Exception e) {
			System.out.println("dateFormatter(): " + e.getMessage());
		}
		return outputDate;
	}

	public List<Periodo> getPeriodos() {
		return periodoDAO.buscarTerminados();
	}

	public Periodo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}

	public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}

	public List<Matricula> getListaPersonas() {
		return listaPersonas;
	}

	public void setListaPersonas(List<Matricula> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}
}