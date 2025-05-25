-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS taqueria_db;
USE taqueria_db;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'mesero', 'cocinero', 'cajero') NOT NULL
);
INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES
('admin', SHA2('123',256), 'admin'),
('mesero1', SHA2('456',256), 'mesero'),
('cajero1', SHA2('789',256), 'cajero'),
('cocinero1', SHA2('101',256), 'cocinero');

-- Tabla de productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria ENUM('entrada', 'plato', 'bebida', 'postre') NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);
INSERT INTO productos (nombre, categoria, precio, stock) VALUES
('Taco al Pastor', 'Plato', 18.50, 30),
('Taco de Bistec', 'Plato', 20.00, 40),
('Agua de Horchata', 'Bebida', 12.00, 30),
('Agua de Jamaica', 'Bebida', 12.00, 30),
('Guacamole', 'Entrada', 25.00, 24),
('Flan Napolitano', 'Postre', 18.00, 10);

-- Tabla de mesas
CREATE TABLE mesas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    estado ENUM('libre', 'ocupada') NOT NULL DEFAULT 'libre'
);

INSERT INTO mesas (numero, estado) VALUES
(1, 'libre'),
(2, 'libre'),
(3, 'libre'),
(4, 'libre'),
(5, 'libre'),
(6, 'libre'),
(7, 'libre'),
(8, 'libre'),
(9, 'libre'),
(10, 'libre');

-- Tabla de órdenes
CREATE TABLE ordenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_mesero INT,
    id_mesa INT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('abierta', 'cerrada', 'cobrada') NOT NULL DEFAULT 'abierta',
    total DECIMAL(10,2) DEFAULT 0.0,
    FOREIGN KEY (id_mesero) REFERENCES usuarios(id),
    FOREIGN KEY (id_mesa) REFERENCES mesas(id)
);

-- Detalles de productos en una orden
CREATE TABLE orden_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    id_producto INT,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    preparado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_orden) REFERENCES ordenes(id),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);

-- Tabla de pagos
CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    metodo_pago ENUM('efectivo', 'tarjeta', 'qr') NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10, 2),
    FOREIGN KEY (id_orden) REFERENCES ordenes(id)
);

-- Tabla de logs de cocina (opcional)
CREATE TABLE cocina_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_detalle INT,
    fecha_preparado DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_detalle) REFERENCES orden_detalle(id)
);

-- Crear tabla ventas
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL
);

-- Insertar datos de ejemplo recientes para pruebas de reportes semanales y mensuales
INSERT INTO ventas (fecha, total) VALUES 
(CURDATE(), 150.00),
(DATE_SUB(CURDATE(), INTERVAL 1 DAY), 230.00),
(DATE_SUB(CURDATE(), INTERVAL 2 DAY), 180.50),
(DATE_SUB(CURDATE(), INTERVAL 5 DAY), 90.00),
(DATE_SUB(CURDATE(), INTERVAL 7 DAY), 300.00),
(DATE_SUB(CURDATE(), INTERVAL 10 DAY), 120.00),
(DATE_SUB(CURDATE(), INTERVAL 15 DAY), 270.75),
(DATE_SUB(CURDATE(), INTERVAL 20 DAY), 310.40),
(DATE_SUB(CURDATE(), INTERVAL 30 DAY), 500.00);


SELECT *FROM MESAS