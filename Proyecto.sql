
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

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('1', 'Juan', '111', 'MEDICO');
INSERT INTO Medico (id, especialidad) VALUES ('1', 'Pediatra');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('2', 'Keneth', '2', 'MEDICO');
INSERT INTO Medico (id, especialidad) VALUES ('2', 'Cardiologo');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('3', 'Ana', '222', 'ADMINISTRADOR');
INSERT INTO Administrador (id) VALUES ('3');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('4', 'Carlos', '333', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('4');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('5', 'Dave', '2', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('5');

INSERT INTO Usuario (id, nombre, clave, tipoUsuario) VALUES ('6', 'Andrey', '33', 'FARMACEUTICO');
INSERT INTO Farmaceutico (id) VALUES ('6');

INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono)
VALUES ('1', 'Maria', '1 de enero de 2000', '88888888');

INSERT INTO Paciente (id, nombre, fechaNacimiento, telefono)
VALUES ('2', 'Keilor', '10 de abril de 2005', '72682583');

INSERT INTO Medicamento (id, nombre, presentacion)
VALUES ('1', 'Paracetamol', '500mg');

INSERT INTO Medicamento (id, nombre, presentacion)
VALUES ('2', 'Jarabe', '1L');

INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('R1', 'Entregada', '10 de septiembre de 2025', '1', '1');

INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('J2D95', 'Lista', '25 de septiembre de 2025', '1', '1');

INSERT INTO Receta (idReceta, estado, fecha, idPaciente, idMedico)
VALUES ('74GXU', 'Entregada', '27 de septiembre de 2025', '2', '1');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('R1', 'Paracetamol', '500mg', '2', 'Tomar después de comer', '5');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('J2D95', 'Paracetamol', '500mg', '1', 'Tomar 1 por dia', '7');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('74GXU', 'Jarabe', '1L', '5', '5 mililitros por dia, durante 1 semana', '7');

INSERT INTO Prescripcion (idReceta, nombre, presentacion, cantidad, indicaciones, duracion)
VALUES ('74GXU', 'Paracetamol', '500mg', '1', 'Cuando le duela el cuerpo', '7');