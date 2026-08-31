CREATE DATABASE IF NOT EXISTS logitrack_db;
USE logitrack_db;

-- ==========================================
-- 1. TABLAS PRINCIPALES Y SEGURIDAD
-- ==========================================

-- Catálogo de Roles
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE -- 'ROLE_ADMIN', 'ROLE_EMPLEADO'
);

-- Usuarios del sistema / Empleados (JWT)
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Catálogo de Categorías de Productos
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(150) NULL
);

-- Bodegas
CREATE TABLE bodegas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(150) NOT NULL,
    capacidad INT NOT NULL CHECK (capacidad >= 0),
    encargado_id INT,
    FOREIGN KEY (encargado_id) REFERENCES usuarios(id) ON DELETE SET NULL
);

-- Productos (Ahora enlazado a categorias)
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria_id INT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio >= 0),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- Stock de Productos por Bodega (Relación M:N)
CREATE TABLE bodega_producto (
    bodega_id INT NOT NULL,
    producto_id INT NOT NULL,
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    PRIMARY KEY (bodega_id, producto_id),
    FOREIGN KEY (bodega_id) REFERENCES bodegas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE
);

-- Encabezado de Movimientos de Inventario
CREATE TABLE movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id INT NOT NULL,
    bodega_origen_id INT NULL,   -- Obligatorio para SALIDA y TRANSFERENCIA
    bodega_destino_id INT NULL,  -- Obligatorio para ENTRADA y TRANSFERENCIA
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (bodega_origen_id) REFERENCES bodegas(id),
    FOREIGN KEY (bodega_destino_id) REFERENCES bodegas(id)
);

-- Detalle de Productos por Movimiento
CREATE TABLE detalle_movimiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    FOREIGN KEY (movimiento_id) REFERENCES movimientos(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Tabla de Auditoría
CREATE TABLE auditorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    entidad VARCHAR(50) NOT NULL,
    entidad_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario VARCHAR(50) NOT NULL,
    valores_anteriores JSON NULL,
    valores_nuevos JSON NULL
);


-- ==========================================
-- TRIGGERS DE AUDITORÍA 
-- ==========================================

-- PRODUCTOS: INSERT
CREATE TRIGGER trg_auditoria_productos_insert
AFTER INSERT ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('INSERT', 'Producto', NEW.id, NOW(), CURRENT_USER(),
        NULL,
        JSON_OBJECT('nombre', NEW.nombre, 'categoria_id', NEW.categoria_id, 'precio', NEW.precio));
END //

-- PRODUCTOS: DELETE
CREATE TRIGGER trg_auditoria_productos_delete
AFTER DELETE ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('DELETE', 'Producto', OLD.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('nombre', OLD.nombre, 'categoria_id', OLD.categoria_id, 'precio', OLD.precio),
        NULL);
END //

-- BODEGAS: INSERT
CREATE TRIGGER trg_auditoria_bodegas_insert
AFTER INSERT ON bodegas
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('INSERT', 'Bodega', NEW.id, NOW(), CURRENT_USER(),
        NULL,
        JSON_OBJECT('nombre', NEW.nombre, 'ubicacion', NEW.ubicacion, 'capacidad', NEW.capacidad, 'encargado_id', NEW.encargado_id));
END //

-- BODEGAS: UPDATE
CREATE TRIGGER trg_auditoria_bodegas_update
AFTER UPDATE ON bodegas
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('UPDATE', 'Bodega', NEW.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('nombre', OLD.nombre, 'ubicacion', OLD.ubicacion, 'capacidad', OLD.capacidad, 'encargado_id', OLD.encargado_id),
        JSON_OBJECT('nombre', NEW.nombre, 'ubicacion', NEW.ubicacion, 'capacidad', NEW.capacidad, 'encargado_id', NEW.encargado_id));
END //

-- BODEGAS: DELETE
CREATE TRIGGER trg_auditoria_bodegas_delete
AFTER DELETE ON bodegas
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('DELETE', 'Bodega', OLD.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('nombre', OLD.nombre, 'ubicacion', OLD.ubicacion, 'capacidad', OLD.capacidad, 'encargado_id', OLD.encargado_id),
        NULL);
END //

-- USUARIOS: INSERT
CREATE TRIGGER trg_auditoria_usuarios_insert
AFTER INSERT ON usuarios
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('INSERT', 'Usuario', NEW.id, NOW(), CURRENT_USER(),
        NULL,
        JSON_OBJECT('username', NEW.username, 'email', NEW.email, 'rol_id', NEW.rol_id));
END //

-- USUARIOS: UPDATE
CREATE TRIGGER trg_auditoria_usuarios_update
AFTER UPDATE ON usuarios
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('UPDATE', 'Usuario', NEW.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('username', OLD.username, 'email', OLD.email, 'rol_id', OLD.rol_id),
        JSON_OBJECT('username', NEW.username, 'email', NEW.email, 'rol_id', NEW.rol_id));
END //

-- USUARIOS: DELETE
CREATE TRIGGER trg_auditoria_usuarios_delete
AFTER DELETE ON usuarios
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('DELETE', 'Usuario', OLD.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('username', OLD.username, 'email', OLD.email, 'rol_id', OLD.rol_id),
        NULL);
END //

-- MOVIMIENTOS: INSERT
CREATE TRIGGER trg_auditoria_movimientos_insert
AFTER INSERT ON movimientos
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('INSERT', 'Movimiento', NEW.id, NOW(), CURRENT_USER(),
        NULL,
        JSON_OBJECT('tipo', NEW.tipo, 'usuario_id', NEW.usuario_id, 'bodega_origen_id', NEW.bodega_origen_id, 'bodega_destino_id', NEW.bodega_destino_id));
END //

-- MOVIMIENTOS: UPDATE
CREATE TRIGGER trg_auditoria_movimientos_update
AFTER UPDATE ON movimientos
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('UPDATE', 'Movimiento', NEW.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('tipo', OLD.tipo, 'usuario_id', OLD.usuario_id, 'bodega_origen_id', OLD.bodega_origen_id, 'bodega_destino_id', OLD.bodega_destino_id),
        JSON_OBJECT('tipo', NEW.tipo, 'usuario_id', NEW.usuario_id, 'bodega_origen_id', NEW.bodega_origen_id, 'bodega_destino_id', NEW.bodega_destino_id));
END //

-- MOVIMIENTOS: DELETE
CREATE TRIGGER trg_auditoria_movimientos_delete
AFTER DELETE ON movimientos
FOR EACH ROW
BEGIN
    INSERT INTO auditorias (operacion, entidad, entidad_id, fecha_hora, usuario, valores_anteriores, valores_nuevos)
    VALUES ('DELETE', 'Movimiento', OLD.id, NOW(), CURRENT_USER(),
        JSON_OBJECT('tipo', OLD.tipo, 'usuario_id', OLD.usuario_id, 'bodega_origen_id', OLD.bodega_origen_id, 'bodega_destino_id', OLD.bodega_destino_id),
        NULL);
END //





-- ================================================================================================================



INSERT INTO roles (nombre) VALUES ('ADMIN'), ('EMPLEADO');

INSERT INTO usuarios (username, email, password, rol_id)
VALUES (
    'admin',
    'admin@logitrack.com',
    '$2b$10$TpsHT3bN2YmmypxPKXN78.pZ7Zah2onh.eAhrd.5uV0dFhTgSsVCK',
    1 
)
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- 1. TABLA FORÁNEA: CATEGORÍAS (10 Categorías de Tecnología)
INSERT INTO categorias (id, nombre, descripcion) VALUES
(1, 'Computadores y Laptops', 'Equipos portátiles y de escritorio'),
(2, 'Monitores y Pantallas', 'Monitores IPS, OLED y pantallas gamer'),
(3, 'Componentes PC', 'Procesadores, tarjetas de video y memorias RAM'),
(4, 'Almacenamiento', 'Discos SSD NVMe, discos duros y memorias USB'),
(5, 'Periféricos', 'Teclados mecánicos, mouses y auriculares'),
(6, 'Redes y Conectividad', 'Routers Wi-Fi 6, switches y adaptadores'),
(7, 'Impresión y Escaneo', 'Impresoras multifuncionales y consumibles'),
(8, 'Accesorios y Cables', 'Cables HDMI, adaptadores USB-C y hubs'),
(9, 'Audio y Video', 'Micrófonos USB, webcams 4K y parlantes'),
(10, 'Servidores y Rack', 'Equipos de servidor y gabinetes rack')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), descripcion = VALUES(descripcion);

-- 2. TABLA PRINCIPAL: PRODUCTOS (10 Productos asociados a sus Categorías)
INSERT INTO productos (id, nombre, categoria_id, precio) VALUES
(1, 'Laptop Asus ROG Strix i9 32GB', 1, 8500000.00),
(2, 'MacBook Pro 16 M3 Max', 1, 14200000.00),
(3, 'Monitor Dell UltraSharp 27 4K', 2, 2800000.00),
(4, 'Monitor Samsung Odyssey OLED 34', 2, 4900000.00),
(5, 'Tarjeta de Video Nvidia RTX 4080 Super', 3, 5600000.00),
(6, 'Procesador AMD Ryzen 9 7950X', 3, 2950000.00),
(7, 'SSD NVMe Samsung 990 Pro 2TB', 4, 980000.00),
(8, 'Teclado Mecánico Logitech G Pro X', 5, 620000.00),
(9, 'Router TP-Link Archer AX90 Wi-Fi 6', 6, 890000.00),
(10, 'Webcam Logitech Brio 4K', 9, 750000.00)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), categoria_id = VALUES(categoria_id), precio = VALUES(precio);

-- ==========================================
-- BODEGAS
-- ==========================================
INSERT INTO bodegas (id, nombre, ubicacion, capacidad, encargado_id) VALUES
(1, 'Bodega Central', 'Bogotá', 500, 1),
(2, 'Bodega Norte', 'Medellín', 300, 1),
(3, 'Bodega Sur', 'Cali', 200, 1)
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- ==========================================
-- STOCK INICIAL (mezcla de stock normal y bajo, para probar /stock-bajo)
-- ==========================================
INSERT INTO bodega_producto (bodega_id, producto_id, stock) VALUES
(1, 1, 15),   -- Laptop Asus en Central
(1, 2, 3),    -- MacBook en Central -> stock bajo
(1, 3, 20),   -- Monitor Dell en Central
(1, 10, 18),  -- Webcam en Central
(2, 3, 8),    -- Monitor Dell en Norte -> stock bajo
(2, 5, 12),   -- RTX 4080 en Norte
(2, 6, 7),    -- Ryzen 9 en Norte -> stock bajo
(2, 7, 40),   -- SSD en Norte
(3, 8, 5),    -- Teclado en Sur -> stock bajo
(3, 9, 25)    -- Router en Sur
ON DUPLICATE KEY UPDATE stock = VALUES(stock);

-- ==========================================
-- MOVIMIENTOS (disparan los triggers: ajustan stock y generan auditoría)
-- ==========================================
INSERT INTO movimientos (id, tipo, usuario_id, bodega_origen_id, bodega_destino_id) VALUES
(1, 'ENTRADA', 1, NULL, 1),
(2, 'SALIDA', 1, 2, NULL),
(3, 'TRANSFERENCIA', 1, 1, 3);

INSERT INTO detalle_movimiento (movimiento_id, producto_id, cantidad) VALUES
(1, 1, 5),   -- Entra: 5 Laptops Asus a Central (stock 15 -> 20)
(2, 7, 10),  -- Sale: 10 SSD de Norte (stock 40 -> 30)
(3, 10, 5);  -- Transferencia: 5 Webcams de Central a Sur (Central 18->13, Sur queda con 5)

