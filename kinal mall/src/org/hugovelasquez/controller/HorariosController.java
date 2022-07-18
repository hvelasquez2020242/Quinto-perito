/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Horarios;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.system.Principal;
import java.time.Year;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import java.sql.Time;
import java.time.format.FormatStyle;
import java.util.Locale;
import javafx.css.converter.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 2 jul. 2021
 * @time 20:29:25
 */
public class HorariosController implements Initializable {

    @FXML
    private Button btnNuevo;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colHorarioEntrada;
    @FXML
    private TableColumn colHorarioSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;
    private ObservableList<Horarios> listaHorarios;
    private ObservableList<Horarios> listaLunes;
    @FXML
    private TableView tblHoraios;

    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";

    private Principal escenarioPrincipal;
    @FXML
    private CheckBox chkMartes;
    @FXML
    private CheckBox chkMiercoles;
    @FXML
    private CheckBox chkLunes;
    @FXML
    private CheckBox chkJueves;
    @FXML
    private CheckBox chkViernes;
    @FXML
    private JFXTimePicker tpHoraEntrada;
    @FXML
    private JFXTimePicker tpHoraSalida;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        tpHoraEntrada.setEditable(false);
        tpHoraSalida.setEditable(false);
         //Locale locale = new Locale("es", "GT");
         //locale.setDefault(locale);
         
        /*StringConverter<LocalTime> convertidor = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.UK);
        tpHoraEntrada.setConverter(convertidor);

        StringConverter<LocalTime> convertidor2 = new LocalTimeStringConverter(FormatStyle.SHORT, Locale.US);
        tpHoraSalida.setConverter(convertidor2);
        */
    }

    @FXML
    private void nuevo(ActionEvent event) {
        {
            switch (operacion) {
                case NINGUNO:
                    habilitarCampos();
                    limpiarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    operacion = Operaciones.GUARDAR;
                    imgNuevo.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                    break;
                case GUARDAR:

                     
                    ArrayList<JFXTimePicker> listaJFXTimePicker = new ArrayList<>();
                    listaJFXTimePicker.add(tpHoraEntrada);
                    listaJFXTimePicker.add(tpHoraSalida);

                    if (validar(listaJFXTimePicker)) {
                                agregarHorarios();
                                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));

                                cargarDatos();
                                desactivarControles();
                                limpiarControles();
                                btnNuevo.setText("Nuevo");
                                btnEliminar.setText("Eliminar");
                                btnEditar.setDisable(false);
                                btnReporte.setDisable(false);
                                operacion = Operaciones.NINGUNO;
                            
                        

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Por favor llene todos los de fecha");
                        alert.setHeaderText(null);
                        alert.setTitle("Alerta");
                        alert.show();

                    }
                    break;

            }

        }
    }

    

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));

                break;

            case NINGUNO:

                if (existeElementoSeleccionado()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Esta seguro que desea eliminar lo que selecciono?");

                    Optional<ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        limpiarControles();
                        eliminarHorarios();
                        cargarDatos();

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea eliminar");
                    alert.show();
                }
        }

    }
    
    @FXML
    private void editar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (existeElementoSeleccionado() && tpHoraEntrada.getValue() != null) {
                    habilitarCampos();
                    btnEditar.setText("Actualzar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                    break;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea editar");
                    alert.show();
                }
            case ACTUALIZAR:
                if(existeElementoSeleccionado()){
                    if(tpHoraEntrada.getValue() != null && existeElementoSeleccionado()){
                editarProveedores();
                cargarDatos();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                
                operacion = Operaciones.NINGUNO;
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                 } else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea editar");
                    alert.show();
                    }
                } else {
                    
                }
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
            case NINGUNO: 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", "Reporte Horarios", parametros);
                break;
            case ACTUALIZAR:
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                limpiarControles();
                desactivarControles();
               
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));

        }
    }
    public void editarProveedores() {
        Horarios registro = new Horarios();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        registro.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        registro.setLunes(chkLunes.isSelected());
        registro.setMartes(chkMartes.isSelected());
        registro.setMiercoles(chkMiercoles.isSelected());
        registro.setJueves(chkJueves.isSelected());
        registro.setViernes(chkViernes.isSelected());
            
        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarHorarios(?, ?, ?, ?, ?, ?, ?, ?)}");
           pstmt.setInt(1, registro.getId());
           pstmt.setTime(2, registro.getHorarioEntrada());
           pstmt.setTime(3, registro.getHorarioSalida());
           pstmt.setBoolean(4, registro.getLunes());
           pstmt.setBoolean(5, registro.getMartes());
           pstmt.setBoolean(6, registro.getMiercoles());
           pstmt.setBoolean(7, registro.getJueves());
           pstmt.setBoolean(8, registro.getViernes());
           pstmt.executeQuery();
           
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }
        
    }
    
    public void eliminarHorarios() {
        Horarios horarios = ((Horarios) tblHoraios.getSelectionModel().getSelectedItem());
            PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarHorarios(?)}");
            pstmt.setInt(1, horarios.getId());
            pstmt.executeQuery();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kinal mall");
            alert.setHeaderText(null);
            alert.setContentText("Registro eliminado correctamente");
            alert.show();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            try {
                pstmt.close();
            } catch (Exception e) {
            }
        }

    } 
    
    private void habilitarCampos() {
        txtId.setEditable(false);
        tpHoraEntrada.setDisable(false);
        tpHoraSalida.setDisable(false);
        chkLunes.setDisable(false);
        chkMartes.setDisable(false);
        chkMiercoles.setDisable(false);
        chkJueves.setDisable(false);
        chkViernes.setDisable(false);

    }

    public void desactivarControles() {
        txtId.setEditable(false);
        tpHoraEntrada.setDisable(true);
        tpHoraSalida.setDisable(true);
        chkLunes.setDisable(true);
        chkMartes.setDisable(true);
        chkMiercoles.setDisable(true);
        chkJueves.setDisable(true);
        chkViernes.setDisable(true);

    }

    public void limpiarControles() {
        txtId.clear();
        tpHoraEntrada.getEditor().clear();
        tpHoraSalida.getEditor().clear();
        chkLunes.setSelected(false);
        chkMartes.setSelected(false);
        chkMiercoles.setSelected(false);
        chkJueves.setSelected(false);
        chkViernes.setSelected(false);

    }
    
    @FXML
    private void seleccionarElemento(MouseEvent event) {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getId()));
            tpHoraEntrada.setValue(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpHoraSalida.setValue(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());
            chkLunes.setSelected(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getLunes());
            chkMartes.setSelected(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getMartes());
            chkMiercoles.setSelected(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getMiercoles());
            chkJueves.setSelected(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getJueves());
            chkViernes.setSelected(((Horarios) tblHoraios.getSelectionModel().getSelectedItem()).getViernes());
        }
    }

    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Horarios(rs.getInt("id"),
                        rs.getTime("horarioEntrada"),
                        rs.getTime("horarioSalida"),
                        rs.getBoolean("lunes"),
                        rs.getBoolean("martes"),
                        rs.getBoolean("miercoles"),
                        rs.getBoolean("jueves"),
                        rs.getBoolean("viernes")));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaHorarios = FXCollections.observableArrayList(lista);
        return listaHorarios;
    }

    public void cargarDatos() {
        tblHoraios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colHorarioEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));

    }

    @FXML
    private void mostrarVistaMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarEmpleados();
    }

        public boolean validar(ArrayList<JFXTimePicker> listaTimePicker) {

        boolean respuesta = true;

        /*if(tpFechaLimitePago.getValue() == null) {
            return false;
        }*/
        for (JFXTimePicker jFXTimePicker : listaTimePicker) {
            if (jFXTimePicker.getValue() == null) {
                return false;
            }
        }
        
        return respuesta;
    }


    public boolean existeElementoSeleccionado() {
        if (tblHoraios.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }

    }

    public boolean validarDatos(String numero, String numero2) {

        Pattern pattern = Pattern.compile("^([01]?[0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9])?$");
        Matcher matcher = pattern.matcher(numero);
        Matcher matcher2 = pattern.matcher(numero2);
        return matcher.matches();
    }

    public void agregarHorarios() {

        Horarios registro = new Horarios();
        registro.setHorarioEntrada(Time.valueOf(tpHoraEntrada.getValue()));
        registro.setHorarioSalida(Time.valueOf(tpHoraSalida.getValue()));
        registro.setLunes(chkLunes.isSelected());
        registro.setMartes(chkMartes.isSelected());
        registro.setMiercoles(chkMiercoles.isSelected());
        registro.setJueves(chkJueves.isSelected());
        registro.setViernes(chkViernes.isSelected());

        PreparedStatement pstmt = null;
        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarHorarios(?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setTime(1, registro.getHorarioEntrada());
            pstmt.setTime(2, registro.getHorarioSalida());
            pstmt.setBoolean(3, registro.getLunes());
            pstmt.setBoolean(4, registro.getMartes());
            pstmt.setBoolean(5, registro.getMiercoles());
            pstmt.setBoolean(6, registro.getJueves());
            pstmt.setBoolean(7, registro.getViernes());
            pstmt.executeQuery();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
