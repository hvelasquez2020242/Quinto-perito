USE IN5BVKINALMALL_HUGODANIELVELASUEZ;


DROP PROCEDURE IF EXISTS sp_AgregarDepartamentos; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarDepartamentos(IN _nombre VARCHAR(45))
BEGIN 
	INSERT INTO Departamentos(nombre)
    VALUES (_nombre);
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarCargos; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarCargos(IN _nombre VARCHAR(45))
BEGIN 
	INSERT INTO Cargos(nombre)
	VALUES (_nombre);
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarHorarios; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarHorarios(IN _horarioEntrada TIME, IN _horarioSalida TIME, IN _lunes BOOLEAN, IN _martes BOOLEAN, IN _miercoles BOOLEAN,
    IN _jueves BOOLEAN, IN _viernes BOOLEAN )
BEGIN 
	INSERT INTO Horarios(horarioEntrada, horarioSalida, lunes, martes, miercoles, jueves, viernes)
    VALUES (_horarioEntrada, _horarioSalida, _lunes, _martes, _miercoles, _jueves, _viernes); 
END $$    
DELIMITER ; 


DROP PROCEDURE IF EXISTS sp_AgregarAdministracion; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarAdministracion(IN _direccion VARCHAR(100), IN _telefono VARCHAR(8))
BEGIN 
	INSERT INTO Administracion(direccion, telefono)
    VALUES (_direccion, _telefono);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_AgregarEmpleados; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarEmpleados(in _nombre VARCHAR (45), IN _apellido VARCHAR (45), IN _email VARCHAR (45), IN _telefono VARCHAR (45),
IN _fechaContratacion DATE, IN _sueldo DECIMAL (11, 2), IN _idDepartamento INT, IN _idCargo INT, IN _idHorario INT, IN _idAdministracion INT)
BEGIN 
	INSERT INTO Empleados(nombre, apellido, email, telefono, fechaContratacion, sueldo, idDepartamento, idCargo, idHorario, idAdministracion)
    VALUES (_nombre, _apellido, _email, _telefono, _fechaContratacion, _sueldo, _idDepartamento, _idCargo, _idHorario, _idAdministracion); 
END $$ 
DELIMITER ; 

 

DROP PROCEDURE IF EXISTS sp_AgreagarTipoCLiente; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarTipoCliente (IN _descripcion VARCHAR (45))
BEGIN 
	INSERT INTO TiploCliente(descripcion)
    VALUES (_descripcion); 
END $$ 
DELIMITER ; 



DROP PROCEDURE IF EXISTS sp_AgregarLocales; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarLocales(IN _saldoFavor DECIMAL (11,2), IN _saldoCOntra DECIMAL (11,2),IN _mesesPendientes INT, IN _disponibilidad BOOLEAN,
IN _valorLocal DECIMAL (11,2), IN _valorAdministracion DECIMAL (11,2))
BEGIN 
	INSERT INTO Locales(saldoFavor, saldoCOntra, mesesPendientes, disponibilidad, valorLocal, valorAdministracion)
    VALUES (_saldoFavor, _saldoCOntra, _mesesPendientes, _disponibilidad, _valorLocal, _valorAdministracion); 
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_AgregarClientes; 
DELIMITER $$ 
CREATE PROCEDURE sp_AgregarClientes(IN _nombre VARCHAR(45), IN _apellidos VARCHAR (45), IN _telefono VARCHAR (8), IN _direccion VARCHAR(60),
IN _email VARCHAR (45), IN _local INT, IN _TipoCliente INT, IN _idAdministracion INT)
BEGIN 
	INSERT INTO Clientes(nombre, apellidos, telefono, direccion, email, idLocal, idTipoCliente, idAdministracion)
	VALUES(_nombre, _apellidos, _telefono, _direccion, _email, _idLocal, _idTipoCliente, _idAdministracion);
END $$ 
DELIMITER ; 


DROP PROCEDURE IF EXISTS sp_AgregarProveedores;
DELIMITER $$
CREATE PROCEDURE sp_AgregarProveedores(IN _nit VARCHAR(45), IN _servicioPrestado VARCHAR(45), IN _telefono VARCHAR(45), IN _direccion VARCHAR(60),
IN _saldoFavor DECIMAL(11,2), IN _saldoContra DECIMAL(11,2))
BEGIN 
	INSERT INTO Proveedores(nit, servicioPrestado, telefono, direccion, saldoFavor, saldoContra, idAdministracion)
    VALUES(_nit, _servicioPrestado, _telefono, _direccion, _saldoFavor, _saldoContra, _idAdministracion);
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_AgregarCuentasPorCobrar;
DELIMITER $$
CREATE PROCEDURE sp_AgregarCuentasPorCobrar(in _numeroFactura VARCHAR(45), IN _anio YEAR(4), IN _mes INT(2), IN _valorNetoPago DECIMAL(11,2),
IN _estadoPago VARCHAR(45), IN _idCliente INT, IN _idLocal INT, IN _idAdministracion INT)
BEGIN 
	INSERT INTO CuentasPorCobrar(numeroFactura, anio, mes, valorNetoPago, estadoPago, idCliente, idLocal, idAdministracion)
    VALUES(_numeroFactura, _anio, _mes, _valorNetoPago, _estadoPago, _idCliente, _idLocal, _idAdministracion);
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS sp_AgregarCentasPorPagar; 
DELIMITER $$
CREATE PROCEDURE sp_AgregarCentasPorPagar(IN _numeroFactura VARCHAR (45), IN _fechaLimitePago DATE, IN _estadoPago VARCHAR(45), 
IN _valorNetoPago DECIMAL (11,2), in _idAdministracion INT, IN _idProveedores INT)
BEGIN 
	INSERT INTO CentasPorPagar(numeroFactura, fechaLimitePago, estadoPago, valorNetoPago, idAdministracion, idProveedores)
    VALUES (_numeroFactura, _fechaLimitePago, _estadoPago, _valorNetoPago, _idAdministracion, _idProveedores);
END $$
DELIMITER ; 

/*Procesos almacenados para actualizar*/

DROP PROCEDURE IF EXISTS sp_EditarDepartamentos; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarDepartamentos(IN _id INT,IN _nombre VARCHAR(45))
BEGIN 
	UPDATE Departamentos SET nombre = _nombre WHERE id = _id; 
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarCargos;
DELIMITER $$ 
CREATE PROCEDURE sp_EditarCargos(IN _id INT, IN _nombre VARCHAR(45))
BEGIN 
	UPDATE Cargos SET nombre = _nombre WHERE id = _id; 
END $$ 
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EditarHorarios; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarHorarios(IN _id INT ,IN _horarioEntrada TIME, IN _horarioSalida TIME, IN _lunes BOOLEAN, IN _martes BOOLEAN, IN _miercoles BOOLEAN,
    IN _jueves BOOLEAN, IN _viernes BOOLEAN ) 
BEGIN 
	UPDATE Horarios SET horarioEntrada = _horarioEntrada, horarioSalida = _horarioSalida, lunes = _lunes, martes = _martes, miercoles = _miercoles, 
    jueves = _jueves, viernes = _viernes WHERE id = _id; 
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarAdministracion;
DELIMITER $$ 
CREATE PROCEDURE sp_EditarAdministracion(IN id INT,IN _direccion VARCHAR(100), IN _telefono VARCHAR(8))
BEGIN 
	UPDATE Administracion SET direccion = _direccion, telefono = _telefono WHERE id = _id; 
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarEmpleados; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarEmpleados(IN _id INT,in _nombre VARCHAR (45), IN _apellido VARCHAR (45), IN _email VARCHAR (45), IN _telefono VARCHAR (45),
IN _fechaContratacion DATE, IN _sueldo DECIMAL (11, 2), IN _idDepartamento INT, IN _idCargo INT, IN _idHorario INT, IN _idAdministracion INT)
BEGIN 
	UPDATE Empleados SET nombre = _nombre, apellido = _apellido, email = _email, telefono = _telefono, fechaContratacion = _fechaContratacion, 
    sueldo = _sueldo, idDepartamento = _idDepartamento, idCargo = _idCargo, idHorario = _idHorario, idAdministracion = _idAdministracion WHERE id = _id; 
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarTipoCliente; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarTipoCLiente(IN _id INT,IN _descripcion VARCHAR (45))
BEGIN 
	UPDATE TipoCliente SET descripcion = _descripcion WHERE id = _id; 
END $$ 
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EditarLocales; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarLocales(IN _id INT,IN _saldoFavor DECIMAL (11,2), IN _saldoCOntra DECIMAL (11,2),IN _mesesPendientes INT, IN _disponibilidad BOOLEAN,
IN _valorLocal DECIMAL (11,2), IN _valorAdministracion DECIMAL (11,2)) 
BEGIN 
	UPDATE Locales SET saldoFavor = _saldoFavor, saldoContra = _saldoContra, mesesPendientes = _mesesPendientes, disponibilidad = _disponibilidad,
    valorLocal = _valorLocal, valorAdministracion = _valorAdministracion WHERE id = _id;
END $$ 
DELIMITER ;     

DROP PROCEDURE IF EXISTS sp_EditarClientes; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarClientes(IN _id INT,IN _nombre VARCHAR(45), IN _apellidos VARCHAR (45), IN _telefono VARCHAR (8), IN _direccion VARCHAR(60),
IN _email VARCHAR (45), IN _local INT, IN _TipoCliente INT, IN _idAdministracion INT)
BEGIN 
	UPDATE Clientes SET nombre = _nombre, apellidos = _apellidos, telefono = _telefono, direccion = _direccion, email = _email, idLocal = _local, idTipoCliente = _TipoCLiente, idAdministracion = _idAdministracion 
    WHERE id = _id; 
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarProveedores;
DELIMITER $$ 
CREATE PROCEDURE sp_EditarProveedores(IN _id INT,IN _nit VARCHAR(45), IN _servicioPrestado VARCHAR(45), IN _telefono VARCHAR(45), IN _direccion VARCHAR(60),
IN _saldoFavor DECIMAL(11,2), IN _saldoContra DECIMAL(11,2))
BEGIN 
	UPDATE Proveedores SET nit = _nit, servicioPrestado = _servicioPrestado, telefono = _telefono, direccion = _direccion,
    salfoFavor = _saldoFavor, saldoContra = _saldoContra WHERE id = _id; 
END $$ 
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarCuentasPorCobrar;
DELIMITER $$ 
CREATE PROCEDURE sp_EditarCuentasPorCobrar(IN _id INT,in _numeroFactura VARCHAR(45), IN _anio YEAR(4), IN _mes INT(2), IN _valorNetoPago DECIMAL(11,2),
IN _estadoPago VARCHAR(45), IN _idCliente INT, IN _idLocal INT, IN _idAdministracion INT)
BEGIN 
	UPDATE CuentasPorCobrar SET numeroFactura = _numeroFactura, anio = _anio, mes = _mes, valorNetoPago = _valorNetoPago, 
    estadoPago = _estadoPago, idCliente = _idCliente, idLocal = _idLocal, idAdministracion = _idAdministracion WHERE id = _id;
END $$
DELIMITER ; 

DROP PROCEDURE IF EXISTS sp_EditarCuentasPorPagar; 
DELIMITER $$ 
CREATE PROCEDURE sp_EditarCuentasPorPagar(IN _numeroFactura VARCHAR (45), IN _fechaLimitePago DATE, IN _estadoPago VARCHAR(45), 
IN _valorNetoPago DECIMAL (11,2), in _idAdministracion INT, IN _idProveedores INT)
BEGIN 
	UPDATE CentasPorPagar SET numeroFactura = _numeroFactura, fechaLimitePago = _fechaLimitePago, estadoPago = _estadoPago,
    valorNetoPago = _valorNetoPago, idAdministracion = _idAdministracion, idProveedores = _idProveedores WHERE id = _id; 
END $$
DELIMITER ;

/*Procesos almacenados para eliminar*/

DROP PROCEDURE IF EXISTS sp_EliminarDepartamentos; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarDepartamentos(in _id INT)
BEGIN 
	DELETE FROM Departamentos WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarCargos;
DELIMITER $$
CREATE PROCEDURE sp_EliminarCargos(IN _id INT)
BEGIN 
	DELETE FROM Cargos WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarHorarios;
DELIMITER $$
CREATE PROCEDURE sp_EliminarHorarios(IN _id INT)
BEGIN 
	DELETE FROM Horarios WHERE id = _id; 
END $$ 
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarAdministracion;
DELIMITER $$  
CREATE PROCEDURE sp_EliminarAdministracion(IN _id INT)
BEGIN 
	DELETE FROM Administracion WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarEmpleados; 
DELIMITER $$ 
CREATE PROCEDURE sp_EliminarEmpleados(IN _id INT)
BEGIN
	DELETE FROM Empleados WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarTipoCliente; 
DELIMITER $$ 
CREATE PROCEDURE sp_EliminarTipoCliente(IN _id INT)
BEGIN 
	DELETE FROM TipoCliente WHERE id = _id; 
END $$
DELIMITER ; 


DROP PROCEDURE IF EXISTS sp_EliminarLocales; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarLocales(IN _id INT)
BEGIN 
	DELETE FROM Locales WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarClientes; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarClientes(IN _id INT)
BEGIN 
	DELETE FROM Clientes WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarProveedores; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarProveedores(IN _id INT)
BEGIN 
	DELETE FROM Proveedores WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorCobrar; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarCuentasPorCobrar(IN _id INT)
BEGIN 
	DELETE FROM CuentasPorCobrar WHERE id = _id; 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_EliminarCuentasPorPagar; 
DELIMITER $$
CREATE PROCEDURE sp_EliminarCuentasPorPagar(IN _id INT)
BEGIN 
	DELETE FROM CentasPorPagar WHERE id = _id; 
END $$
DELIMITER ;

/*Procesos almacenados para buscar*/

DROP PROCEDURE IF EXISTS sp_BuscarDepartamentos; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarDepartamentos(IN _id INT)
BEGIN
SELECT *FROM Departamentos
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarCargos; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarCargos(IN _id INT)
BEGIN
SELECT *FROM Cargos
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarDepartamentos; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarHorarios(IN _id INT)
BEGIN
SELECT *FROM Horarios
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarAdministracion; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarAdministracion(IN _id INT)
BEGIN
SELECT *FROM Administracion
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarEmpleados; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarEmpleados(IN _id INT)
BEGIN
SELECT *FROM Empleados
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarTipoCliente; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarTipoCliente(IN _id INT)
BEGIN
SELECT *FROM TipoCliente
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarLocales; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarLocales(IN _id INT)
BEGIN
SELECT *FROM Locales
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarClientes; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarClientes(IN _id INT)
BEGIN
SELECT *FROM Clientes
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarProveedores; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarProveedores(IN _id INT)
BEGIN
SELECT *FROM Proveedores
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarCuentasPorCobrar; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarCuentasPorCobrar(IN _id INT)
BEGIN
SELECT *FROM CuentasPorCobrar
WHERE id = _id;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS sp_BuscarCentasPorPagar; 
DELIMITER $$
CREATE PROCEDURE sp_BuscarCentasPorPagar(IN _id INT)
BEGIN
SELECT *FROM CentasPorPagar
WHERE id = _id;
END $$
DELIMITER ;


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

CALL  sp_ListarAdministracion();


CALL sp_AgregarAdministracion("23 calle, Condado Naranjo 14-50, Cdad. de Guatemala 01057", "54859668");
CALL sp_AgregarAdministracion("Edificio Reforma 10, Avenida La Reforma 9-55 Z 10, Cdad. de Guatemala 01010", "84965874");
CALL sp_AgregarAdministracion("8 Calle, Cdad. de Guatemala", "87523652");
CALL sp_AgregarAdministracion("12 Calle 1-25, Cdad. de Guatemala", "26254635");
CALL sp_AgregarAdministracion("5A Calle 4-33, Cdad. de Guatemala", "54855632");





