/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.report;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hugovelasquez.db.Conexion;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 15 jul. 2021
 * @time 16:58:29
 */
public class GenerarReporte {

    private static GenerarReporte instancia;

    private GenerarReporte() {
    Locale locale = new Locale("es","GT");
    locale.setDefault(locale);
    
    }

    public static GenerarReporte getInstance() {
        if (instancia == null) {
            instancia = new GenerarReporte();
        }
        return instancia;
    }

    public void mostrarReporte(String nombreReporte, String titulo, Map parametros) {

        try {
            parametros.put("LOGO_HEADER", getClass().getResourceAsStream("/org/hugovelasquez/resource/images/Kinal.png"));
            parametros.put("LOGO_FOOTER", getClass().getResourceAsStream("/org/hugovelasquez/resource/images/LogoKinal.png"));
            
            
            InputStream archivo = GenerarReporte.class.getResourceAsStream(nombreReporte);
            JasperReport report = (JasperReport) JRLoader.loadObject(archivo);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, Conexion.getInstance().getConexion());
            JasperViewer viewer = new JasperViewer(print, false );
            viewer.setTitle(titulo);
            viewer.setVisible(true);
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

}
