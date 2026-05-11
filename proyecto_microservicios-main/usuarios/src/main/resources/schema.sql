CREATE TABLE IF NOT EXISTS usuario (
    usuario_id INT NOT NULL AUTO_INCREMENT,
    contrasena VARCHAR(100) NOT NULL,
    correo_electronico VARCHAR(80) NOT NULL,
    direccion VARCHAR(80) NOT NULL,
    nombre VARCHAR(10) NOT NULL,
    fecha_registro DATE,
    rol VARCHAR(20) NOT NULL DEFAULT 'USER',
    telefono VARCHAR(20),
    PRIMARY KEY (usuario_id)
);

SET @rol_exists = (
    SELECT COUNT(*)
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'usuario'
      AND column_name = 'rol'
);

SET @alter_usuario = IF(
    @rol_exists = 0,
    'ALTER TABLE usuario ADD COLUMN rol VARCHAR(20) NOT NULL DEFAULT ''USER''',
    'SELECT 1'
);

PREPARE usuario_stmt FROM @alter_usuario;
EXECUTE usuario_stmt;
DEALLOCATE PREPARE usuario_stmt;

INSERT INTO usuario (contrasena, correo_electronico, direccion, nombre, fecha_registro, rol, telefono)
SELECT 'admin123', 'admin@micro.local', 'Panel central', 'admin', CURDATE(), 'ADMIN', '600000000'
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE nombre = 'admin'
);
