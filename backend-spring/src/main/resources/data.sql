-- =============================================================================
--  data.sql — Script de Inicialización Completo (IES Alixar)
-- =============================================================================

-- 1. LIMPIEZA TOTAL (Para poder reiniciar la App sin errores de duplicados)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE profesor_guardia;
TRUNCATE TABLE ausencia;
TRUNCATE TABLE franja;
TRUNCATE TABLE parte;
TRUNCATE TABLE centro;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. CONFIGURACIÓN DEL CENTRO
INSERT INTO centro (nombre, jornada_inicio, jornada_fin, jornada_horas, recreo_inicio, recreo_fin, recreo_duracion)
VALUES ('IES Alixar', '08:15', '14:45', 6, '11:15', '11:45', 30);

-- 3. FRANJAS HORARIAS (Ordenadas)
INSERT INTO franja (id, etiqueta, hora_inicio, hora_fin, orden, tipo) VALUES
('h1',     '1ª Hora', '08:15', '09:15', 1, 'LECTIVA'),
('h2',     '2ª Hora', '09:15', '10:15', 2, 'LECTIVA'),
('h3',     '3ª Hora', '10:15', '11:15', 3, 'LECTIVA'),
('recreo', 'Recreo',  '11:15', '11:45', 4, 'RECREO'),
('h4',     '4ª Hora', '11:45', '12:45', 5, 'LECTIVA'),
('h5',     '5ª Hora', '12:45', '13:45', 6, 'LECTIVA'),
('h6',     '6ª Hora', '13:45', '14:45', 7, 'LECTIVA');

-- 4. PARTES DIARIOS
INSERT INTO parte (fecha, dia_semana, jefe_estudios) VALUES
('2026-02-10', 'Martes',    'Navarro Romero, Pablo'),
('2026-02-11', 'Miércoles', 'Navarro Romero, Pablo');

-- 5. PROFESORES DE GUARDIA (Asignados por tramo horario el 10/02/2026)
INSERT INTO profesor_guardia (id, fecha, franja_id, nombre, ubicacion) VALUES
(UUID(), '2026-02-10', 'h1', 'Dominguez Molina, Beatriz',       'Edif 2, Planta Baja'),
(UUID(), '2026-02-10', 'h1', 'Ellas Olivencia, Maria Carmen',   'Edif 2, Planta Alta'),
(UUID(), '2026-02-10', 'h2', 'Blazquez Lopez, Elena',           'Edif 2, Planta Baja'),
(UUID(), '2026-02-10', 'h2', 'Paramio Gonzalez, Abraham',       'Edif 2, Planta Alta'),
(UUID(), '2026-02-10', 'h3', 'Molina Perez, Sara',              'Edif 2, Planta Baja'),
(UUID(), '2026-02-10', 'h4', 'Martin Rios, Daniel',             'Edif 1, Planta Baja'),
(UUID(), '2026-02-10', 'h5', 'Naranjo Solis, Marta',            'Edif 2, Planta Baja'),
(UUID(), '2026-02-10', 'h6', 'Campos Rivas, Miguel',            'Edif 3, Pasillo');

-- 6. AUSENCIAS Y TAREAS PARA EL DÍA 10/02/2026
INSERT INTO ausencia (id, fecha, franja_id, profesor_ausente, grupo, aula, asignatura, tarea) VALUES
-- Tramo h1
(UUID(), '2026-02-10', 'h1', 'Abad Diaz, Joaquin',          '1º ESO B', 'Apoyo-04',  'Lengua',     'Lectura libre y actividades de la página 22'),
(UUID(), '2026-02-10', 'h1', 'Moreno Toril, Maria Josefa',  '1º Bach A', '6A.E1AE',   'Historia',   'Fichas de la Revolución Industrial en carpeta roja'),
(UUID(), '2026-02-10', 'h1', 'Garcia Rufino, Carmen Maria', '2º CFGS',   'TICE 1',    'Sistemas',   'Repasar esquema unidad 4 para examen viernes'),

-- Tramo h2
(UUID(), '2026-02-10', 'h2', 'Fernandez Cordero, Julio',    '1º ESO F',  'A12',       'Mates',      'Trabajo guiado con la tutora (págs 110-112)'),
(UUID(), '2026-02-10', 'h2', 'Garcia Delgado, Mª Angeles',  '1º Bach C', 'Lab-Bio',   'Biología',   'Ver documental "El origen de la vida"'),

-- Tramo h3
(UUID(), '2026-02-10', 'h3', 'Cabello Ortiz, Eva',          '2º ESO B',  '14-B',      'Lengua',     'Comentario de texto página 61 del libro'),

-- Tramo h5
(UUID(), '2026-02-10', 'h5', 'Munoz Cardenas, Lucia',       '4º ESO C',  '12-D',      'Inglés',     'Reading unit 5 + workbook página 40'),
(UUID(), '2026-02-10', 'h5', 'Santos Vega, Ines',           '2º ESO A',  '11-A',      'Mates',      'Taller de problemas de geometría'),

-- Tramo h6
(UUID(), '2026-02-10', 'h6', 'Romero Castillo, Alberto',    '1º ESO C',  '08-B',      'Geografía',  'Terminar mapa conceptual de la unidad 3');

-- 7. AUSENCIAS PARA EL DÍA 11/02/2026 (Para probar cambio de día)
INSERT INTO ausencia (id, fecha, franja_id, profesor_ausente, grupo, aula, asignatura, tarea) VALUES
(UUID(), '2026-02-11', 'h1', 'Abad Diaz, Joaquin',          '1º ESO B', 'Apoyo-04',  'Lengua',     'Continuar actividades página 23'),
(UUID(), '2026-02-11', 'h4', 'Martin Rios, Daniel',         '4º ESO B', 'A-21',      'Música',     'Ensayo de flauta en grupos pequeños');