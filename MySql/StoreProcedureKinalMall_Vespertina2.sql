USE IN5BV_KinalMall;
-- -----------------------------------------------------
-- PROCEDURE Empleados
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sp_ListarEmpleados; 
DELIMITER $$
CREATE PROCEDURE sp_ListarEmpleados()
BEGIN 
	SELECT 
    Empleados.id,
    Empleados.nombres,
    Empleados.apellidos, 
    Empleados.email,
    Empleados.telefono,
    Empleados.fechaContratacion,
    Empleados.sueldo,
    Empleados.idDepartamento,
    Empleados.idCargo,
    Empleados.idHorario,
    Empleados.idAdministracion 
    FROM Empleados;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_AgregarEmpleados; 
DELIMITER $$
CREATE PROCEDURE sp_AgregarEmpleados(IN _nombres VARCHAR(45), 
IN _apellidos VARCHAR(45),
 IN _email VARCHAR(45),
 IN _telefono VARCHAR(8), 
IN _fechaContratacion DATE,
 IN _sueldo DECIMAL(11,2),
 IN _idDepartamento INT, 
 IN _idCargo INT, 
 IN _idHorario INT, 
 IN _idAdministracion INT)
BEGIN 
	INSERT INTO Empleados(nombres, apellidos, email, telefono, fechaContratacion, sueldo, idDepartamento, idCargo, idHorario, idAdministracion)
    VALUES(_nombres, _apellidos, _email, _telefono, _fechaContratacion, _sueldo, _idDepartamento, _idCargo, _idHorario, _idAdministracion);
END $$
    
DELIMITER ;
DROP PROCEDURE IF EXISTS sp_EditarEmpleados;
DELIMITER $$
CREATE PROCEDURE sp_EditarEmpleados(IN _id INT,
 IN _nombres VARCHAR(45), 
 IN _apellidos VARCHAR(45),
 IN _email VARCHAR(45),
 IN _telefono VARCHAR(8), 
IN _fechaContratacion DATE, 
IN _sueldo DECIMAL(11,2),
 IN _idDepartamento INT,
 IN _idCargo INT, 
 IN _idHorario INT,
 IN _idAdministracion INT)
	BEGIN 
    UPDATE Empleados
    SET nombres = _nombres,
    apellidos = _apellidos,
    email = _email, 
    telefono = _telefono, 
    fechaContratacion = _fechaContratacion, 
    sueldo = _sueldo, 
    idDepartamento = _idDepartamento, 
    idCargo = _idCargo,
    idHorario = _idHorario, 
    idAdministracion = _idAdministracion
    WHERE 
    id = _id;
    END $$ 
DELIMITER ; 
DROP PROCEDURE IF EXISTS sp_EliminarEmpleados;
DELIMITER $$
CREATE PROCEDURE sp_EliminarEmpleados(IN _id INT)
BEGIN 
	DELETE FROM Empleados WHERE id = _id;
END $$

DELIMITER ;

CALL sp_EliminarEmpleados(3);
CALL sp_ListarEmpleados();
CALL sp_AgregarEmpleados("Lucia", "Velasquez", "seba@outlook.es", "88888888", '1980-03-12', 513,2, 3, 1, 1);

-- -----------------------------------------------------
-- PROCEDURE Cargos
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sp_ListarCargos; 
DELIMITER $$ 
CREATE PROCEDURE sp_ListarCargos()
BEGIN 
	SELECT
    Cargos.id,
    Cargos.nombre
    FROM Cargos;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarCargos;
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarCargos(IN _nombre VARCHAR(45))
BEGIN 
	INSERT INTO Cargos(nombre)
	VALUES(_nombre);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EditarCargos;
DELIMITER $$ 
CREATE PROCEDURE sp_EditarCargos(IN _id INT, IN _nombre VARCHAR(45))
BEGIN 
UPDATE Cargos
	SET 
    nombre = _nombre
    WHERE id = _id; 
END $$

DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EliminarCargos;
DELIMITER $$
CREATE PROCEDURE sp_EliminarCargos(IN _id INT)
BEGIN 
	DELETE FROM Cargos WHERE id = _id; 
END $$ 
DELIMITER ; 


-- -----------------------------------------------------
-- PROCEDURE Cuentas por pagar
-- -----------------------------------------------------
 
DROP PROCEDURE IF EXISTS sp_ListarCuentasPorPagar;
DELIMITER $$
CREATE PROCEDURE sp_ListarCuentasPorPagar()
BEGIN 
	SELECT 
    CuentasPorPagar.id,
    CuentasPorPagar.numeroFactura,
    CuentasPorPagar.fechaLimitePago,
    CuentasPorPagar.valorNetoPago,
    CuentasPorPagar.estadoPago,
    CuentasPorPagar.idAdministracion,
    CuentasPorPagar.idProveedor
    FROM CuentasPorPagar;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarCuentasPorPagar;
DELIMITER $$
CREATE PROCEDURE sp_AgregarCuentasPorPagar(IN _numeroFactura VARCHAR(45),
 IN _fechaLimitePago DATE,
IN _estadoPago VARCHAR(45),
IN _valorNetoPago DECIMAL(11,2) ,
IN _idAdministracion INT, 
IN _idProveedor INT)
BEGIN 
	INSERT INTO CuentasPorPagar(numeroFactura, fechaLimitePago, estadoPago, valorNetoPago,idAdministracion, idProveedor)
    VALUES(_numeroFactura, _fechaLimitePago, _estadoPago, _valorNetoPago ,_idAdministracion, _idProveedor);
END $$
DELIMITER ;
CALL sp_AgregarCuentasPorPagar();

DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorPagar;
DELIMITER $$
CREATE PROCEDURE sp_EliminarCuentasPorPagar(IN _id INT)
BEGIN 
	DELETE FROM CuentasPorPagar
    WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EditarCuentasPorPagar;
DELIMITER $$
CREATE PROCEDURE sp_EditarCuentasPorPagar(IN _id INT, IN _numeroFactura VARCHAR(45), IN _fechaLimitePago DATE, 
IN _estadoPago VARCHAR(45), IN _valorNetoPago DECIMAL(11,2),IN _idAdministracion INT, IN _idProveedor INT) 
BEGIN
	UPDATE CuentasPorPagar
	SET 
    numeroFactura = _numeroFactura,
    fechaLimitePago = _fechaLimitePago, 
    valorNetoPago = _valorNetoPago,
    estadoPago = _estadoPago,
    idAdministracion = _idAdministracion,
    idProveedor = _idProveedor
    WHERE id = _id;

END $$

DELIMITER ; 



-- -----------------------------------------------------
-- PROCEDURE Proveedores
-- -----------------------------------------------------
DELIMITER ;
DROP PROCEDURE IF EXISTS sp_EditarHorarios;
DELIMITER $$
CREATE PROCEDURE sp_EditarHorarios(in _id INT, IN _horarioEntrada TIME, IN _horarioSalida TIME, IN _lunes BOOLEAN, IN _martes BOOLEAN, IN _miercoles BOOLEAN,
IN _jueves BOOLEAN, IN _viernes BOOLEAN)
BEGIN 
	UPDATE Horarios 
    set 
    horarioEntrada = _horarioEntrada, 
    horarioSalida = _horarioSalida,
    lunes = _lunes, 
    martes = _martes, 
    miercoles = _miercoles,
    jueves = _jueves, 
    viernes = _viernes 
    WHERE id = _id;
END $$
DELIMITER ; 
CALL sp_ListarHorarios();
CALL sp_EditarHorarios(1,'12:20:12', '10:10:10', 1, 1, 1, 1, 1);
DROP PROCEDURE IF EXISTS sp_ListarHorarios; 
DELIMITER $$
CREATE PROCEDURE sp_ListarHorarios()
BEGIN
	SELECT
	Horarios.id,
    Horarios.horarioEntrada,
    Horarios.horarioSalida,
    Horarios.lunes,
    horarios.martes,
    Horarios.miercoles,
    Horarios.jueves,
    Horarios.viernes 
    FROM Horarios;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_AgregarHorarios;
DELIMITER $$
CREATE PROCEDURE sp_AgregarHorarios(IN _horarioEntrada TIME, 
IN _horarioSalida TIME, 
IN _lunes BOOLEAN,
IN _martes BOOLEAN, 
IN _miercoles BOOLEAN , 
IN _jueves BOOLEAN, 
IN _viernes BOOLEAN)
BEGIN 
	INSERT INTO Horarios(horarioEntrada, horarioSalida, lunes, martes, miercoles, jueves, viernes)
    VALUES (_horarioEntrada, _horarioSalida, _lunes, _martes, _miercoles, _jueves, _viernes);
END $$

DROP PROCEDURE IF EXISTS sp_EliminarHorarios;
DELIMITER $$
CREATE PROCEDURE sp_EliminarHorarios(IN _id INT)
BEGIN 
	DELETE FROM Horarios WHERE id = _id; 
END $$
DELIMITER ; 
CALL sp_ListarHorarios();
call sp_EliminarHorarios(4);
CALL sp_AgregarHorarios('07:30:46', '12:30:46', true, true, false, false, true);
CALL sp_AgregarHorarios('12:10:47', '10:28:10', false, true, false, false, true);

call sp_ListarHorarios();
-- -----------------------------------------------------
-- PROCEDURE Proveedores
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sp_ListarProveedores; 
DELIMITER $$
CREATE PROCEDURE sp_ListarProveedores()
BEGIN
SELECT 
	Proveedores.id,
    Proveedores.nit,
    Proveedores.servicioPrestado,
    Proveedores.telefono,
    Proveedores.direccion,
    Proveedores.saldoFavor,
    Proveedores.saldoContra
FROM Proveedores;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarProveedores; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarProveedores(IN _nit VARCHAR(45), 
IN _ServicioPrestado VARCHAR(45) ,
IN _Telefono VARCHAR(8), 
IN _Direccion VARCHAR(100),
IN _SaldoFavor DECIMAL(11, 2),
IN _SaldoContra DECIMAL(11,2))
BEGIN 
INSERT INTO Proveedores(nit, servicioPrestado, telefono, direccion, saldoFavor, saldoContra)
VALUES(_nit, _ServicioPrestado, _Telefono, _Direccion, _SaldoFavor, _SaldoContra);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EditarProveedores; 
DELIMITER $$
CREATE PROCEDURE sp_EditarProveedores(IN _id INT, IN _nit VARCHAR(45), IN _servicioPrestado VARCHAR(45),
IN _telefono VARCHAR(8), IN _direccion VARCHAR(100), IN _saldoFavor DECIMAL(11,2), IN _saldoContra DECIMAL(11,2))
BEGIN 
	UPDATE Proveedores 
    set 
    nit = _nit, 
    servicioPrestado = _servicioPrestado,
    telefono = _telefono, 
    direccion = _direccion, 
    saldoFavor = _saldoFavor, 
    saldoContra = _saldoContra
    WHERE id = _id;
    
END $$

DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarProveedores;
DELIMITER $$ 
CREATE PROCEDURE sp_EliminarProveedores(IN _id INT)
BEGIN 
	DELETE FROM Proveedores WHERE id = _id;
END $$
DELIMITER ;
 
DROP PROCEDURE IF EXISTS sp_BuscarProveedores;
DELIMITER $$
CREATE PROCEDURE sp_BuscarProveedores(in _id INT)
BEGIN 
	SELECT 
    Proveedores.id,
    Proveedores.nit,
    Proveedores.servicioPrestado,
    Proveedores.telefono,
    Proveedores.direccion,
    Proveedores.saldoFavor,
    Proveedores.saldoContra
    FROM Proveedores
    Where id = _id;
END $$
DELIMITER ; 
-- -----------------------------------------------------
-- PROCEDURE CuentasPorCobrar
-- -----------------------------------------------------
DROP PROCEDURE IF EXISTS sp_ListarCuentasPorCobrar; 
DELIMITER $$ 
CREATE PROCEDURE sp_ListarCuentasPorCobrar()
BEGIN 
	SELECT 
    CuentasPorCobrar.id, 
    CuentasPorCobrar.numeroFactura,
    CuentasPorCobrar.anio,
    CuentasPorCobrar.mes,
    CuentasPorCobrar.valorNetoPago,
    CuentasPorCobrar.estadoPago,
    CuentasPorCobrar.idAdministracion,
    CuentasPorCobrar.idCliente,
    CuentasPorCobrar.idLocal
    FROM CuentasPorCobrar;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_BuscarCuentasPorCobrar; 
DELIMITER $$ 
CREATE PROCEDURE sp_BuscarCuentasPorCobrar(IN _id INT)
BEGIN 
	SELECT 
    CuentasPorCobrar.id, 
    CuentasPorCobrar.numeroFactura,
    CuentasPorCobrar.anio,
    CuentasPorCobrar.mes,
    CuentasPorCobrar.valorNetoPago,
    CuentasPorCobrar.estadoPago,
    CuentasPorCobrar.idAdministracion,
    CuentasPorCobrar.idCliente,
    CuentasPorCobrar.idLocal
    FROM CuentasPorCobrar
    WHERE id = _id;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarCuentasPorCobrar; 
DELIMITER $$
CREATE PROCEDURE sp_AgregarCuentasPorCobrar(IN _numeroFactura INT, IN _anio YEAR, IN _mes INT, IN _valorNetoPago DECIMAL (11,2), IN _estadoPago VARCHAR(45), IN _idAdministracion INT, IN _idCliente INT, IN _idLocal INT)
BEGIN 
	INSERT INTO CuentasPorCobrar(numeroFactura, anio, mes, valorNetoPago, estadoPago, idAdministracion, idCliente, idLocal)
    VALUES (_numeroFactura, _anio, _mes, _valorNetoPago, _estadoPago, _idAdministracion, _idCliente, _idLocal);
END $$
DELIMITER ; 
CALL sp_AgregarCuentasPorCobrar("2321",'2020' , 5, 50.3, "si", 1, 1 ,1);
CALL sp_AgregarCuentasPorCobrar("2620", '2021', 6, 90.5, "Pagado", 2, 3, 2);
CALL sp_listarCuentasPorCobrar();
DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorCobrar; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarCuentasPorCobrar(IN _id INT)
BEGIN 
	DELETE FROM CuentasPorCobrar WHERE id = _id;
END $$
DELIMITER ; 
DROP PROCEDURE IF EXISTS sp_EditarCuentasPorCobrar; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarCuentasPorCobrar(IN _id INT, 
IN _numeroFactura VARCHAR(45),
IN _anio YEAR, 
IN _mes INT, 
IN _valorNetoPago DECIMAL(11,2),
IN _estadoPago VARCHAR(45),
IN _idAdministracion INT, 
IN _idCliente INT, 
IN _idLocal INT)
BEGIN 
	UPDATE CuentasPorCobrar 
    SET numeroFactura = _numeroFactura, 
    anio = _anio,
    mes = _mes, 
    valorNetoPago = _valorNetoPago, 
    estadoPago = _estadoPago, 
    idAdministracion = _idAdministracion, 
    idCliente = _idCliente,
    idLocal = _idLocal
    WHERE id = _id;
END $$

DELIMITER ; 

-- -----------------------------------------------------
-- PROCEDURE Administracion
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_ListarAdministracion()
BEGIN
	SELECT 
		Administracion.id, 
        Administracion.direccion, 
        Administracion.telefono 
	FROM Administracion;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_BuscarAdministracion(IN _id INT)
BEGIN
	SELECT 
		Administracion.id, 
        Administracion.direccion, 
        Administracion.telefono 
	FROM Administracion
    WHERE id = _id;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarAdministracion; 
DELIMITER $$
CREATE PROCEDURE sp_AgregarAdministracion (
	IN _direccion VARCHAR(100), 
	IN _telefono VARCHAR(8)
)
BEGIN
	INSERT INTO Administracion (direccion, telefono) 
    VALUES (_direccion, _telefono);
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_EditarAdministracion (
	IN _id INT,
	IN _direccion VARCHAR(100),
    IN _telefono VARCHAR(8)
)
BEGIN
	UPDATE Administracion 
    SET 
		direccion = _direccion, 
		telefono = _telefono 
    WHERE id = _id;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarAdministracion;
DELIMITER $$
CREATE PROCEDURE sp_EliminarAdministracion (IN _id INT)
BEGIN
	DELETE FROM Administracion WHERE id = _id;
END $$
DELIMITER ;


-- -----------------------------------------------------
-- PROCEDURE Locales
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarLocales;
DELIMITER $$
CREATE PROCEDURE sp_ListarLocales()
BEGIN
	SELECT
		Locales.id,
		Locales.saldoFavor,
		Locales.saldoContra,
		Locales.mesesPendientes,
		Locales.disponibilidad,
		Locales.valorLocal,
		Locales.valorAdministracion
	FROM Locales;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarLocales;
DELIMITER $$
CREATE PROCEDURE sp_BuscarLocales(IN _id INT)
BEGIN
	SELECT
		Locales.id,
		Locales.saldoFavor,
		Locales.saldoContra,
		Locales.mesesPendientes,
		Locales.disponibilidad,
		Locales.valorLocal,
		Locales.valorAdministracion
	FROM Locales
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarLocales;
DELIMITER $$
CREATE PROCEDURE sp_AgregarLocales(
	IN _saldoFavor DECIMAL(11,2),
	IN _saldoContra DECIMAL(11,2),
	IN _mesesPendientes INT,
	IN _disponibilidad BOOLEAN,
	IN _valorLocal DECIMAL(11,2),
	IN _valorAdministracion DECIMAL(11,2))
BEGIN
	INSERT INTO Locales(saldoFavor, saldoContra, mesesPendientes, disponibilidad, valorLocal, valorAdministracion )
	VALUES (_saldoFavor, _saldoContra, _mesesPendientes, _disponibilidad, _valorLocal, _valorAdministracion);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarLocales;
DELIMITER $$
CREATE PROCEDURE sp_EditarLocales(
	IN _id INT,
	IN _saldoFavor DECIMAL(11,2),
	IN _saldoContra DECIMAL(11,2),
	IN _mesesPendientes INT,
	IN _disponibilidad BOOLEAN,
	IN _valorLocal DECIMAL(11,2),
	IN _valorAdministracion DECIMAL(11,2))
BEGIN
	UPDATE Locales
	SET
		saldoFavor = _saldoFavor,
		saldoContra = _saldoContra,
		mesesPendientes = _mesesPendientes,
		disponibilidad = _disponibilidad,
		valorLocal = _valorLocal,
		valorAdministracion = _valorAdministracion
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarLocales;
DELIMITER $$
CREATE PROCEDURE sp_EliminarLocales(IN _id INT)
BEGIN
	DELETE FROM Locales WHERE id = _id;
END$$
DELIMITER ;


-- -----------------------------------------------------
-- PROCEDURE TipoCliente
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_ListarTipoCliente()
BEGIN
	SELECT
		TipoCliente.id,
		TipoCliente.descripcion
	FROM TipoCliente;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_BuscarTipoCliente(IN _id INT)
BEGIN
	SELECT
		TipoCliente.id,
		TipoCliente.descripcion
	FROM TipoCliente
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_AgregarTipoCliente(
	IN _descripcion VARCHAR(45)
)
BEGIN
	INSERT INTO TipoCliente(descripcion)
	VALUES (_descripcion);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_EditarTipoCliente(
	IN _id INT,
	IN _descripcion VARCHAR(45)
)
BEGIN
	UPDATE TipoCliente
	SET
		descripcion = _descripcion
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarTipoCliente;
DELIMITER $$
CREATE PROCEDURE sp_EliminarTipoCliente(IN _id INT)
BEGIN
	DELETE FROM TipoCliente WHERE id = _id;
END$$
DELIMITER ;


-- -----------------------------------------------------
-- PROCEDURE Cliente
-- -----------------------------------------------------

DROP PROCEDURE IF EXISTS sp_ListarClientes;
DELIMITER $$
CREATE PROCEDURE sp_ListarClientes()
BEGIN
	SELECT 
		Clientes.id,
        Clientes.nombres,
        Clientes.apellidos,
        Clientes.telefono,
        Clientes.direccion,
        Clientes.email,
        Clientes.idTipoCliente
	FROM Clientes;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_BuscarClientes;
DELIMITER $$
CREATE PROCEDURE sp_BuscarClientes(IN _id INT)
BEGIN
	SELECT 
		Clientes.id,
        Clientes.nombres,
        Clientes.apellidos,
        Clientes.telefono,
        Clientes.direccion,
        Clientes.email,
        Clientes.idTipoCliente
	FROM Clientes
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarClientes;
DELIMITER $$
CREATE PROCEDURE sp_AgregarClientes(
    IN _nombres VARCHAR(45),
    IN _apellidos VARCHAR(45),
    IN _telefono VARCHAR(8),
    IN _direccion VARCHAR(100),
    IN _email VARCHAR(45),
    IN _idTipoCliente INT
)
BEGIN
	INSERT INTO Clientes(nombres, apellidos, telefono, direccion, email, idTipoCliente)
	VALUES (_nombres, _apellidos, _telefono, _direccion, _email, _idTipoCliente);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EditarClientes;
DELIMITER $$
CREATE PROCEDURE sp_EditarClientes(
	IN _id INT,
    IN _nombres VARCHAR(45),
    IN _apellidos VARCHAR(45),
    IN _telefono VARCHAR(8),
    IN _direccion VARCHAR(100),
    IN _email VARCHAR(45),
    IN _idTipoCliente INT
)
BEGIN
	UPDATE Clientes
	SET
		nombres = _nombres, 
        apellidos = _apellidos, 
        telefono = _telefono, 
        direccion = _direccion, 
        email = _email, 
        idTipoCliente = _idTipoCliente
	WHERE id = _id;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_EliminarClientes;
DELIMITER $$
CREATE PROCEDURE sp_EliminarClientes(IN _id INT)
BEGIN
	DELETE FROM Clientes WHERE id = _id;
END$$
DELIMITER ;



-- -----------------------------------------------------
-- Datos de prueba
-- -----------------------------------------------------

CALL sp_AgregarAdministracion("Diagonal 6 12-42, Zona 10, Ciudad de Guatemala", "45623589");
CALL sp_AgregarAdministracion("12 Calle 1-25, Zona 10, Ciudad de Guatemala", "79654875");
CALL sp_AgregarAdministracion("9na calle 15-77, Zona 7, Ciudad de Guatemala", "79654875");
CALL sp_AgregarAdministracion("3ra Avenida 8-05, Bosques de San Nicolás, Zona 8, Guatemala", "79654875");

CALL sp_AgregarLocales(5000.00, 7000.00, 1, false, 20000.00, 1000.00);
CALL sp_AgregarLocales(10000.00, 10000.00, 0, false, 5000.00, 1000.00);
CALL sp_AgregarLocales(0.00, 30000.00, 6, false, 5000.00, 1000.00);

CALL sp_AgregarTipoCliente("Bronce");
CALL sp_AgregarTipoCliente("Plata");
CALL sp_AgregarTipoCliente("Oro");
CALL sp_AgregarTipoCliente("Diamante");

CALL sp_AgregarClientes("Jorge", "Pérez", "45236598", "Zona 11", "jperez@gmail.com", 1);
CALL sp_AgregarClientes("Luis", "Canto", "65987465", "Zona 7", "lcanto@gmail.com", 2);
CALL sp_AgregarClientes("Mariela", "Hernández", "75648934", "Zona 14", "mhernandez@gmail.com", 4);

CALL sp_BuscarAdministracion(1);
