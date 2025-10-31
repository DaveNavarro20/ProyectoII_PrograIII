CREATE DATABASE Hospital;
USE Hospital;

-- Tabla base para usuarios
CREATE TABLE Usuario (
id VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
clave VARCHAR(50) NOT NULL,
tipoUsuario VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
);

-- Tabla para Médicos
CREATE TABLE Medico (
id VARCHAR(10) NOT NULL,
especialidad VARCHAR(50) NOT NULL,
PRIMARY KEY (id),
 FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- Tabla para Administradores
CREATE TABLE Administrador (
id VARCHAR(10) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- Tabla para Farmacéuticos
CREATE TABLE Farmaceutico (
id VARCHAR(10) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (id) REFERENCES Usuario(id) ON DELETE CASCADE
);

-- Tabla Paciente
CREATE TABLE Paciente (
id VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
fechaNacimiento VARCHAR(30) NOT NULL,
telefono VARCHAR(15) NOT NULL,
PRIMARY KEY (id)
);

-- Tabla Medicamento
CREATE TABLE Medicamento (
id VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
presentacion VARCHAR(50) NOT NULL,
PRIMARY KEY (id)
);

-- Tabla Receta
CREATE TABLE Receta (
idReceta VARCHAR(10) NOT NULL,
estado VARCHAR(20) NOT NULL,
fecha VARCHAR(30) NOT NULL,
idPaciente VARCHAR(10) NOT NULL,
idMedico VARCHAR(10) NOT NULL,
PRIMARY KEY (idReceta),
FOREIGN KEY (idPaciente) REFERENCES Paciente(id),
FOREIGN KEY (idMedico) REFERENCES Medico(id)
);

-- Tabla Prescripción
CREATE TABLE Prescripcion (
idPrescripcion INT AUTO_INCREMENT NOT NULL,
idReceta VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
presentacion VARCHAR(50) NOT NULL,
cantidad VARCHAR(10) NOT NULL,
indicaciones VARCHAR(200) NOT NULL,
duracion VARCHAR(20) NOT NULL,
PRIMARY KEY (idPrescripcion),
FOREIGN KEY (idReceta) REFERENCES Receta(idReceta) ON DELETE CASCADE
);

-- Médicos
INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1001', 'Dave Navarro', '1001', 'MEDICO');
INSERT INTO Medico (id, especialidad) VALUES ('1001', 'Pediatría');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1002', 'Laura Gómez', '1002', 'MEDICO');
INSERT INTO Medico (id, especialidad) VALUES ('1002', 'Cardiología');

-- Administrador
INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1003', 'Keneth Jara', '1003', 'ADMINISTRADOR');
INSERT INTO Administrador (id) VALUES ('1003');

-- Farmacéuticos
INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1004', 'Ana Torres', '1004', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('1004');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1005', 'Angel Ramirez', '1005', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('1005');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1006', 'Kendall Vargas', '1006', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('1006');

-- Pacientes
INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono)
VALUES ('2001', 'María Fernández', '1998-01-12', '8888-2244');

INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono)
VALUES ('2002', 'Keilor Guillen', '2005-04-10', '7268-2583');

-- Medicamentos
INSERT INTO Medicamento (id, nombre, presentacion)
VALUES ('3001', 'Paracetamol', 'Tabletas 500 mg');

INSERT INTO Medicamento (id, nombre, presentacion)
VALUES ('3002', 'Amoxicilina', 'Cápsulas 500 mg');

INSERT INTO Medicamento (id, nombre, presentacion)
VALUES ('3003', 'Ibuprofeno', 'Tabletas 400 mg');

-- Recetas
INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('R001', 'Entregada', '2025-09-10', '2001', '1001');

INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('R002', 'Pendiente', '2025-09-25', '2001', '1002');

INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('R003', 'Entregada', '2025-09-27', '2002', '1001');

-- Prescripciones
INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('R001', 'Paracetamol', 'Tabletas 500 mg', '2', 'Tomar una tableta cada 8 horas después de comer', '5 días');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('R002', 'Amoxicilina', 'Cápsulas 500 mg', '3', 'Tomar una cápsula cada 8 horas con agua', '7 días');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('R003', 'Ibuprofeno', 'Tabletas 400 mg', '2', 'Tomar cada 12 horas para el dolor', '5 días');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('R003', 'Paracetamol', 'Tabletas 500 mg', '1', 'Tomar solo si tiene fiebre', '3 días');