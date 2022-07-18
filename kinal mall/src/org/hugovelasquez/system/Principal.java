/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hugovelasquez.system;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.hugovelasquez.bean.Horarios;
import org.hugovelasquez.controller.AdministracionController;
import org.hugovelasquez.controller.AutorController;
import org.hugovelasquez.controller.CargosController;
import org.hugovelasquez.controller.ClientesController;
import org.hugovelasquez.controller.CuentasPorCobrarController;
import org.hugovelasquez.controller.CuentasPorPagarController;
import org.hugovelasquez.controller.DepartamentosController;
import org.hugovelasquez.controller.EmpleadosController;
import org.hugovelasquez.controller.HorariosController;
import org.hugovelasquez.controller.LocalesController;
import org.hugovelasquez.controller.LoginController;
import org.hugovelasquez.controller.MenuPrincipalController;
import org.hugovelasquez.controller.ProveedoresController;
import org.hugovelasquez.controller.TipoClienteController;
import java.util.Base64;
import org.hugovelasquez.bean.Usuario;
    


/**
 *
 * @author Hugo Daniel Velasquez Patzan
 * Codigo Tecnico IN5BV 
 * @date 5 may. 2021
 * @time 17:27:27
 */
public class Principal extends Application{
      private Usuario usuario; 
    
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VIEW = "/org/hugovelasquez/view/";
    private final String PAQUETE_IMAGES = "org/hugovelasquez/resource/images/";
    private final String PAQUETE_CSS = "org/hugovelasquez/resource/css/";
    public static void main(String[] args) {
        launch(args);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    

    @Override
    public void start(Stage stage) throws Exception {
        this.escenarioPrincipal = stage;
        stage.getIcons().add(new Image(PAQUETE_IMAGES+"Icono.png"));
        //mostrarMenuPrincipal();
        
        usuario = new Usuario();
     //   String password64 = Base64.getEncoder().encodeToString("12345".getBytes());
       // System.out.println(password64);
        
        
        mostrarLogin();
        
        
    }

    public void mostrarMenuPrincipal() {
        try {
                   MenuPrincipalController menuController = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 800, 449);
                   menuController.setEscenarioPrincipal(this);
                   menuController.validarPermisos();
        }        catch (IOException e) {
            System.out.println("Se produjo un error al mostrar la vista del menu principal");
            e.printStackTrace();
            
        }   
    }
    public void mostrarAutor() {
        try {
            AutorController autorController = (AutorController) cambiarEscena("AutorView.fxml", 1280, 650);
            autorController.setEscenarioPrincipal(this);
        } catch (IOException e){
            System.out.println("Se produjo un error al mostrar la vista autor");
            e.printStackTrace();
        }
        
    }
    public Initializable cambiarEscena(String fxml,int ancho, int alto) throws IOException{
        Initializable resultado = null;
        
        FXMLLoader cargadorFXML = new FXMLLoader();
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VIEW + fxml));
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VIEW + fxml);
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escena.getStylesheets().add(PAQUETE_CSS + "estiloKinalMall.css");
        
        this.escenarioPrincipal.setScene(escena);
        this.escenarioPrincipal.show();
        this.escenarioPrincipal.sizeToScene();
        this.escenarioPrincipal.centerOnScreen();
        this.escenarioPrincipal.setResizable(false);
        resultado = (Initializable) cargadorFXML.getController();
        return resultado; 
    }
    public void mostrarAdministracion(){
        try {
          AdministracionController adminController = (AdministracionController)cambiarEscena("AdministracionView.fxml", 1280, 650);
          adminController.setEscenarioPrincipal(this);
        }catch (Exception e){
            System.out.println("Se produjo un erro al mostrar la vista administracion");
            e.printStackTrace();
            
        }
    
            }
    public void mostrarClientes() {
        try {
            ClientesController clientesController = (ClientesController)cambiarEscena("ClientesView.fxml", 1280, 650);
            clientesController.setEscenarioPrincipal(this);
        }catch (Exception e) {
            System.out.println("Se produjo un error al mostrar la vista de clientes");
            e.printStackTrace();
        }
    }
    public void mostrarTipoCliente() {
        try {
            TipoClienteController tipoClienteController = (TipoClienteController)cambiarEscena("TipoCliente.fxml", 1280, 650);
            tipoClienteController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista de tipo cliente");
        }
    }
    public void mostrarLocales () {
        try {
            LocalesController localesController = (LocalesController)cambiarEscena("LocalesView.fxml", 1280, 650);
            localesController.setEscenarioPrincipal(this);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarLogin(){
        try {
            LoginController loginController = (LoginController)cambiarEscena("LoginView.fxml", 800, 449);
            loginController.setEscenarioPrincipal(this);
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarDepartamentos() {
        try {
            DepartamentosController departamentosController = (DepartamentosController)cambiarEscena("DepartamentosView.fxml", 1280, 650);
            departamentosController.setEscenarioPrincipal(this);
        }catch(Exception e) {
            e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista Locales");
        }
    }
    public void mostrarCuentasPorCobrar() {
        try {
            CuentasPorCobrarController cuentasPorCobrarController = (CuentasPorCobrarController)cambiarEscena("CuentasPorCobrarView.fxml", 1280, 650);
            cuentasPorCobrarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
         e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista CuentasPorCobrar");
        }
    }
    public void mostrarProveedores() {
        try {
            ProveedoresController proveedoresController = (ProveedoresController)cambiarEscena("ProveedoresView.fxml", 1280, 650);
            proveedoresController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista Proveedores");
        }
    }
    public void mostrarHorarios(){
        try {
            HorariosController horariosController = (HorariosController)cambiarEscena("HorarioView.fxml", 1280, 650);
            horariosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista Horarios");
        }
    }
    public void mostrarCuentasPorPagar() {
        try {
            CuentasPorPagarController cuentasPorPagarController = (CuentasPorPagarController)cambiarEscena("CuentarPorPagarView.fxml", 1280, 650);
            cuentasPorPagarController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Se produjo un error al mostrar la vista Cuentas por pagar");
        }
    }
    public void mostratCargos() {
        try {
            CargosController cargosController = (CargosController)cambiarEscena("CargosView.fxml", 1280, 650);
            cargosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void mostrarEmpleados() {
        try {
            EmpleadosController empleadosController = (EmpleadosController)cambiarEscena("EmpleadosView.fxml", 1280, 650);
            empleadosController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
