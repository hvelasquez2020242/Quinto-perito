/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hugovelasquez.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.hugovelasquez.bean.Locales;
import org.hugovelasquez.system.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.hugovelasquez.db.Conexion;
import org.hugovelasquez.report.GenerarReporte;

/**
 *
 * @author Hugo Daniel Velasquez Patzan Codigo Tecnico IN5BV
 * @date 15 jun. 2021
 * @time 14:53:09
 */
public class LocalesController implements Initializable {

    private Principal escenarioPrincipal;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtId;
    @FXML
    private TableView tblLocales;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private TextField txtContra;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtValorAdministracion;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgReporte;
    private final String PAQUETE_IMAGE = "/org/hugovelasquez/resource/images/";
    @FXML
    private TextField txtIdLocalesDisponibles;
    @FXML
    private ComboBox cmbDisponibilidad;

    private enum Operaciones {
        NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Locales> listaLocales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
        ObservableList<String> listaOpciones = FXCollections.observableArrayList("Disponible", "No disponible");
        cmbDisponibilidad.getItems().addAll(listaOpciones);
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                habilitarCampos();
                limpiarCampos();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));

                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaTextField = new ArrayList<>();
                listaTextField.add(txtSaldoFavor);
                listaTextField.add(txtContra);
                listaTextField.add(new TextField(txtMesesPendientes.getText()));
                
                listaTextField.add(new TextField(txtValorLocal.getText()));
                listaTextField.add(new TextField(txtValorAdministracion.getText()));

                if (validar(listaTextField)) {

                    if (validarDatos(txtContra.getText(), txtSaldoFavor.getText(),
                            txtValorAdministracion.getText(), txtValorLocal.getText(), txtMesesPendientes.getText())) {

                        Locales registro = new Locales();
                        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
                        registro.setSaldoContra(new BigDecimal(txtContra.getText()));
                        registro.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
                       
                        registro.setDisponibilidad(disponibilidad());
                        registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
                        registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));
                        imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));
                        
                        agregarLocales();
                        cargarDatos();
                        desabilitarCampos();
                        limpiarCampos();

                        btnNuevo.setText("Nuevo");
                        btnEliminar.setText("Eliminar");
                        btnEditar.setDisable(false);
                        btnReporte.setDisable(false);
                        cargarDatos();

                        operacion = Operaciones.NINGUNO;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Kinal mall");
                        alert.setHeaderText(null);
                        alert.setContentText("Los datos no son validos");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Llene todos los campos de texto");
                    alert.show();
                }

        }
    }
    public boolean disponibilidad(){
        if(cmbDisponibilidad.getValue() == "Disponible"){
                    return true;
                } else {
            return false;
        }
    }

    private void agregarLocales() {
        Locales registro = new Locales();
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtContra.getText()));
        registro.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
        registro.setDisponibilidad(disponibilidad());
        registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));

        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarLocales(?, ?, ?, ?, ?, ?)}");

            stmt.setBigDecimal(1, registro.getSaldoFavor());
            stmt.setBigDecimal(2, registro.getSaldoContra());
            stmt.setInt(3, registro.getMesesPendientes());
            stmt.setBoolean(4, registro.getDisponibilidad());
            stmt.setBigDecimal(5, registro.getValorLocal());
            stmt.setBigDecimal(6, registro.getValorAdministracion());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case GUARDAR:
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image(PAQUETE_IMAGE + "boton-agregar.png"));
                imgEliminar.setImage(new Image(PAQUETE_IMAGE + "boton-x.png"));

                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:

              if (existeElementoSeleccionado()) {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Esta seguro que desea eliminar lo que selecciono?");

                    Optional<ButtonType> respuesta = alert.showAndWait();

                    if (respuesta.get() == ButtonType.OK) {
                        limpiarCampos();
                        eliminarLocales();
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
                if (existeElementoSeleccionado()) {

                    habilitarCampos();
                    btnEditar.setText("Actualzar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    operacion = Operaciones.ACTUALIZAR;
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "Guardar.png"));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "cancelar.png"));

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.show();
                }
                break;
            case ACTUALIZAR:

                if (tblLocales.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Kinal mall");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione lo que desea actualizar");
                    alert.show();

                } else {
                    Locales registro = new Locales();
                    registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
                    registro.setSaldoContra(new BigDecimal(txtContra.getText()));
                    registro.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
                    registro.setDisponibilidad(disponibilidad());
                    registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
                    registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));
                    imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                    imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
                    editarLocales();
                    desabilitarCampos();
                    cargarDatos();
                    limpiarCampos();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    operacion = Operaciones.NINGUNO;
                }
                break;
        }
    }

    private void editarLocales() {
        Locales registro = (Locales) tblLocales.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtContra.getText()));
        registro.setMesesPendientes(Integer.valueOf(txtMesesPendientes.getText()));
        registro.setDisponibilidad(disponibilidad());
        registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));
        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarLocales(?, ?, ?, ?, ?, ?, ?)}");
            stmt.setInt(1, registro.getId());
            stmt.setBigDecimal(2, registro.getSaldoFavor());
            stmt.setBigDecimal(3, registro.getSaldoContra());
            stmt.setInt(4, registro.getMesesPendientes());
            stmt.setBoolean(5, registro.getDisponibilidad());
            stmt.setBigDecimal(6, registro.getValorLocal());
            stmt.setBigDecimal(7, registro.getValorAdministracion());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        System.out.println(operacion);
        switch (operacion) {
            case NINGUNO: 
                Map parametros = new HashMap();
                GenerarReporte.getInstance().mostrarReporte("ReporteLocales.jasper", "Reporte locales", parametros);
            case ACTUALIZAR:
                btnReporte.setText("Reporte");
                btnEditar.setText("Editar");
                limpiarCampos();
                desabilitarCampos();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                operacion = Operaciones.NINGUNO;
                imgReporte.setImage(new Image(PAQUETE_IMAGE + "reporte-de-negocios.png"));
                imgEditar.setImage(new Image(PAQUETE_IMAGE + "editar.png"));
        }
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtContra.setText(String.valueOf((((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra())));
            txtMesesPendientes.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            seleccionarDisponibilidad();
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
            txtSaldoLiquido.setText(String.valueOf(calcularSaldoLiquido()));

        }
    }
    public void seleccionarDisponibilidad(){
        if(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getDisponibilidad() == true){
            cmbDisponibilidad.setValue("Disponible");
        } else {
            cmbDisponibilidad.setValue("No disponible");
        }
            
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void mostrarMenuPrincipal(MouseEvent event) {
        escenarioPrincipal.mostrarAdministracion();
    }

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales()}");
            rs = pstmt.executeQuery();
            int contador = 0; 
            while (rs.next()) {
                lista.add(new Locales(rs.getInt("id"),
                        rs.getBigDecimal("saldoFavor"),
                        rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"),
                        rs.getBoolean("disponibilidad"),
                        rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")));
                if(rs.getBoolean("disponibilidad" ) == true){
                    contador ++;
                }
                    
            }
            txtIdLocalesDisponibles.setText(String.valueOf(contador));
            System.out.println("Conexion exitosa ");
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                rs.close();
            } catch (Exception e) {
            }
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;
    }

    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorAdministracion"));
        cmbDisponibilidad.setOpacity(1.0);
        
    }

    public boolean existeElementoSeleccionado() {
        if (tblLocales.getSelectionModel().getSelectedItem() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void habilitarCampos() {
        cmbDisponibilidad.setDisable(false);
        txtId.setDisable(false);
        txtContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtValorAdministracion.setEditable(true);
        txtValorLocal.setEditable(true);
        txtSaldoLiquido.setEditable(false);

    }

    public void desabilitarCampos() {
        cmbDisponibilidad.setDisable(true);
        txtId.setDisable(false);
        txtContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtValorAdministracion.setEditable(false);
        txtValorLocal.setEditable(false);
        txtSaldoLiquido.setEditable(false);
    }

    public void limpiarCampos() {
        cmbDisponibilidad.valueProperty().set(null);
        txtId.clear();
        txtContra.clear();
        txtMesesPendientes.clear();
        txtSaldoFavor.clear();
        txtValorAdministracion.clear();
        txtValorLocal.clear();
        txtSaldoLiquido.clear();
    }

    private void eliminarLocales() {
        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1, ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId());
            stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public boolean validar(ArrayList<TextField> listadoCampos) {
        boolean respuesta = true;

        for (TextField campoTexto : listadoCampos) {
            if (campoTexto.getText().trim().isEmpty()) {
                return false;
            }
        }
        return respuesta;
    }

    public boolean validarDatos(String numero, String numero2, String numero3, String numero4, String numero5) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher2 = pattern.matcher(numero2);
        Matcher matcher = pattern.matcher(numero);
        Matcher matcher3 = pattern.matcher(numero3);
        Matcher matcher4 = pattern.matcher(numero4);
        Matcher matcher5 = pattern.matcher(numero5);

        return matcher.matches();
    }

    public BigDecimal calcularSaldoLiquido() {

        BigDecimal num1 = (((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra());
        BigDecimal num2 = (((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor());

        BigDecimal resultado = num2.subtract(num1);

        txtSaldoLiquido.setText(String.valueOf(resultado));

        return resultado;
    }
    
}
