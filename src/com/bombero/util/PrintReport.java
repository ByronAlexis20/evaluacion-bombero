package com.bombero.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Map;
import java.util.UUID;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import com.bombero.model.dao.ClaseDAO;

import net.sf.jasperreports.engine.JasperRunManager;

public class PrintReport {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";

	public void crearReporte(String path, ClaseDAO claseDAO, Map<String, Object> param) {
		try {
			String pathAbsoluto = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
			String nombreReporte = pathAbsoluto + path;
			Connection cn = claseDAO.abreConexion();

			String nombreArchivo = null;
			nombreArchivo = pathAbsoluto + "temp";
			File folder = new File(nombreArchivo);
			if (folder.exists()) {
			} else {
				folder.mkdir();
			}
			// Obtiene un nombre aleatorio para el reporte
			nombreArchivo = nombreArchivo + "/" + UUID.randomUUID().toString() + ".pdf";
			byte[] b = null;
			b = JasperRunManager.runReportToPdf(nombreReporte, param, cn);
			FileOutputStream fos = new FileOutputStream(nombreArchivo);
			fos.write(b);
			fos.close();
			Filedownload.save(new File(nombreArchivo), "pdf");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}