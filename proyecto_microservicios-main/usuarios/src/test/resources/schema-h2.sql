CREATE TABLE IF NOT EXISTS usuario (
    usuario_id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(10) NOT NULL,
    correo_electronico VARCHAR(80) NOT NULL,
    direccion VARCHAR(80) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    PRIMARY KEY (usuario_id)
);

INSERT INTO usuario (nombre, correo_electronico, direccion, contrasena, rol)
SELECT 'admin', 'admin@micro.local', 'Panel central', 'admin123', 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE nombre = 'admin'
);
