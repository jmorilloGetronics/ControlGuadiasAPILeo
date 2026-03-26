package com.leo.backend.service;

import com.leo.backend.model.*;
import com.leo.backend.repository.AusenciaEntity;
import com.leo.backend.repository.AusenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GuardiasService {

    private static final String LOGIN_USUARIO = "usuario";
    private static final String LOGIN_PASSWORD = "usuario";

    private final Centro centro;
    private final List<Franja> franjas;
    private final Map<String, Parte> partesPorFecha;
    private final List<String> fechasDisponibles;
    private final Map<String, String> sesionesPorToken;

    @Autowired
    private AusenciaRepository ausenciaRepository;

    public GuardiasService() {
        this.centro = crearCentro();
        this.franjas = crearFranjas();
        this.partesPorFecha = crearPartes();
        normalizarColeccionesMutables();
        this.fechasDisponibles = new ArrayList<>(partesPorFecha.keySet());
        Collections.sort(fechasDisponibles);
        this.sesionesPorToken = new ConcurrentHashMap<>();
    }

    private Centro crearCentro() {
        Recreo recreo = new Recreo("11:15", "11:45", 30);
        Jornada jornada = new Jornada("08:15", "14:45", 6, recreo);
        return new Centro("IES Alixar", jornada);
    }

    private List<Franja> crearFranjas() {
        return Arrays.asList(
            new Franja("h1", "Hora 1", "08:15", "09:15", 1),
            new Franja("h2", "Hora 2", "09:15", "10:15", 2),
            new Franja("h3", "Hora 3", "10:15", "11:15", 3),
            new Franja("recreo", "Recreo", "11:15", "11:45", 4, "descanso"),
            new Franja("h4", "Hora 4", "11:45", "12:45", 5),
            new Franja("h5", "Hora 5", "12:45", "13:45", 6),
            new Franja("h6", "Hora 6", "13:45", "14:45", 7)
        );
    }

    private Map<String, Parte> crearPartes() {
        Map<String, Parte> partes = new HashMap<>();
        
        // Parte del 10/02/2026
        Parte parte1 = new Parte(
            "2026-02-10",
            "martes",
            "Navarro Romero, Pablo",
            Arrays.asList(
                new Bloque("h1",
                    Arrays.asList(
                        new ProfesorGuardia("Dominguez Molina, Beatriz", "Edif 2, Planta Baja"),
                        new ProfesorGuardia("Ellas Olivencia, Maria Carmen", "Edif 2, Planta Alta"),
                        new ProfesorGuardia("Garcia Ales, Patricia", "Edif 2, Pasillo Sur"),
                        new ProfesorGuardia("Ruiz Bernal, Jesus", "Edif 1, Planta Baja"),
                        new ProfesorGuardia("Vega Garcia, Lucia", "Edif 3, Patio")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Abad Diaz, Joaquin", "1B", "Apoyo-04", "ALCT", "Lectura libre y actividades de la pagina 22"),
                        new AusenciaTarea("Moreno Toril, Maria Josefa", "E1-A", "6A.E1AE", "GeH", "Fichas dejadas en la carpeta"),
                        new AusenciaTarea("Garcia Rufino, Carmen Maria", "2LCB", "TICE 1", "IPII1", "Repasar esquema de unidad")
                    )
                ),
                new Bloque("h2",
                    Arrays.asList(
                        new ProfesorGuardia("Blazquez Lopez, Elena", "Edif 2, Planta Baja"),
                        new ProfesorGuardia("Paramio Gonzalez, Abraham", "Edif 2, Planta Alta"),
                        new ProfesorGuardia("Ramirez Lopez, Carmen", "Edif 1, Planta Baja"),
                        new ProfesorGuardia("de la Cruz Moran, Isabel", "Edif 1, Patio"),
                        new ProfesorGuardia("Valle Linero, Maria Magdalena", "Edif 3, Pasillo"),
                        new ProfesorGuardia("Vega Garcia, Lucia", "Edif 2, Pasillo Norte")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Fernandez Cordero, Julio Jose", "1F", "A12", "ApoE1", "Trabajo guiado con la tutora"),
                        new AusenciaTarea("Garcia Delgado, Maria Angeles", "B1-C", "17-A.E4A", "ByG", "No salen del aula por excursion"),
                        new AusenciaTarea("Moreno Toril, Maria Josefa", "B1-B", "B2.04", "TIPLANTIA", "Tema movimiento obrero"),
                        new AusenciaTarea("Garcia Rufino, Carmen Maria", "2QSA", "QSA-01", "IPII1", "Despachos administrativos")
                    )
                ),
                new Bloque("h3",
                    Arrays.asList(
                        new ProfesorGuardia("Molina Perez, Sara", "Edif 2, Planta Baja"),
                        new ProfesorGuardia("Pastor Leon, Alberto", "Edif 3, Laboratorio"),
                        new ProfesorGuardia("Guerrero Navas, Pilar", "Edif 1, Pasillo Central")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Cabello Ortiz, Eva", "2B", "14-B", "Lengua", "Comentario de texto pagina 61")
                    )
                ),
                new Bloque("h4",
                    Arrays.asList(
                        new ProfesorGuardia("Martin Rios, Daniel", "Edif 1, Planta Baja"),
                        new ProfesorGuardia("Serrano Vera, Ana", "Edif 2, Planta Alta"),
                        new ProfesorGuardia("Vega Garcia, Lucia", "Edif 3, Patio")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Lopez Camacho, Javier", "3A", "09-C", "Fisica y Quimica", "Resolver hoja de ejercicios 4")
                    )
                ),
                new Bloque("h5",
                    Arrays.asList(
                        new ProfesorGuardia("Naranjo Solis, Marta", "Edif 2, Planta Baja"),
                        new ProfesorGuardia("Roldan Flores, Victor", "Edif 1, Pasillo")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Munoz Cardenas, Lucia", "4C", "12-D", "Ingles", "Reading + workbook"),
                        new AusenciaTarea("Santos Vega, Ines", "2A", "11-A", "Matematicas", "Taller de problemas")
                    )
                ),
                new Bloque("h6",
                    Arrays.asList(
                        new ProfesorGuardia("Campos Rivas, Miguel", "Edif 3, Pasillo"),
                        new ProfesorGuardia("Prieto Navas, Elena", "Edif 2, Planta Alta")
                    ),
                    Arrays.asList(
                        new AusenciaTarea("Romero Castillo, Alberto", "1C", "08-B", "Historia", "Mapa conceptual de la unidad")
                    )
                )
            )
        );
        
        partes.put("2026-02-10", parte1);

        // Parte del 11/02/2026 (miércoles) - con variaciones
        Parte parte2 = new Parte(
            "2026-02-11",
            "miercoles",
            "Navarro Romero, Pablo",
            parte1.getBloques().stream().map(bloque -> {
                List<AusenciaTarea> ausenciasActualizadas = bloque.getAusenciasTareas().stream()
                    .map(aus -> new AusenciaTarea(
                        aus.getProfesorAusente(),
                        aus.getGrupo(),
                        aus.getAula(),
                        aus.getAsignatura(),
                        aus.getTarea() + " (actualizado)"
                    ))
                    .collect(Collectors.toList());
                return new Bloque(bloque.getFranjaId(), bloque.getProfesoresGuardia(), ausenciasActualizadas);
            }).collect(Collectors.toList())
        );
        
        partes.put("2026-02-11", parte2);
        
        return partes;
    }

    private void normalizarColeccionesMutables() {
        for (Parte parte : partesPorFecha.values()) {
            if (parte.getBloques() == null) {
                parte.setBloques(new ArrayList<>());
                continue;
            }

            parte.setBloques(new ArrayList<>(parte.getBloques()));
            for (Bloque bloque : parte.getBloques()) {
                if (bloque.getAusenciasTareas() == null) {
                    bloque.setAusenciasTareas(new ArrayList<>());
                } else {
                    bloque.setAusenciasTareas(new ArrayList<>(bloque.getAusenciasTareas()));
                }
            }
        }
    }

    public Centro getCentro() {
        return centro;
    }

    public List<Franja> getFranjas() {
        return franjas;
    }

    public List<String> getFechasDisponibles() {
        return fechasDisponibles;
    }

    public List<Map<String, Object>> listarPartes() {
        return fechasDisponibles.stream()
            .map(fecha -> {
                Parte parte = partesPorFecha.get(fecha);
                Map<String, Object> mapa = new HashMap<>();
                mapa.put("fecha", parte.getFecha());
                mapa.put("diaSemana", parte.getDiaSemana());
                return mapa;
            })
            .collect(Collectors.toList());
    }

    public Parte obtenerParte(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return partesPorFecha.get(fechasDisponibles.get(0));
        }
        return partesPorFecha.get(fecha);
    }

    public String login(String usuario, String password) {
        if (!LOGIN_USUARIO.equals(usuario) || !LOGIN_PASSWORD.equals(password)) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        sesionesPorToken.put(token, usuario);
        return token;
    }

    public String obtenerUsuarioPorToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        return sesionesPorToken.get(token);
    }

    public boolean tokenValido(String token) {
        return obtenerUsuarioPorToken(token) != null;
    }

    public List<AusenciaDetalle> listarAusencias(String fecha) {
        String fechaUso = resolverFecha(fecha);
        return ausenciaRepository.findByFecha(fechaUso).stream()
            .map(this::construirAusenciaDetalleDesdeEntity)
            .collect(Collectors.toList());
    }

    @Transactional
    public AusenciaDetalle crearAusencia(String fecha, String franjaId, AusenciaTarea ausencia) {
        String fechaUso = resolverFecha(fecha);
        String franjaUso = normalizarCampo(franjaId, "franjaId");
        Franja franja = franjas.stream()
            .filter(f -> f.getId().equals(franjaUso))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No existe franja: " + franjaUso));

        AusenciaTarea nueva = normalizarAusenciaEntrada(ausencia);
        String id = UUID.randomUUID().toString();

        AusenciaEntity entity = new AusenciaEntity(
            id, fechaUso, franjaUso,
            nueva.getProfesorAusente(), nueva.getGrupo(), nueva.getAula(),
            nueva.getAsignatura(), nueva.getTarea()
        );
        ausenciaRepository.save(entity);

        return new AusenciaDetalle(id, fechaUso, franjaUso, franja.getEtiqueta(),
            nueva.getProfesorAusente(), nueva.getGrupo(), nueva.getAula(),
            nueva.getAsignatura(), nueva.getTarea());
    }

    @Transactional
    public AusenciaDetalle actualizarAusencia(String fecha, String ausenciaId, String franjaId, AusenciaTarea cambios) {
        String ausenciaIdUso = normalizarCampo(ausenciaId, "ausenciaId");
        AusenciaEntity entity = ausenciaRepository.findById(ausenciaIdUso)
            .orElseThrow(() -> new NoSuchElementException("No existe ausencia con id: " + ausenciaIdUso));

        AusenciaTarea cambiosNormalizados = normalizarAusenciaEntrada(cambios);
        entity.setProfesorAusente(cambiosNormalizados.getProfesorAusente());
        entity.setGrupo(cambiosNormalizados.getGrupo());
        entity.setAula(cambiosNormalizados.getAula());
        entity.setAsignatura(cambiosNormalizados.getAsignatura());
        entity.setTarea(cambiosNormalizados.getTarea());

        String franjaDestinoId = (franjaId == null || franjaId.trim().isEmpty())
            ? entity.getFranjaId()
            : franjaId.trim();
        entity.setFranjaId(franjaDestinoId);
        ausenciaRepository.save(entity);

        Franja franja = franjas.stream()
            .filter(f -> f.getId().equals(franjaDestinoId))
            .findFirst()
            .orElse(null);

        return new AusenciaDetalle(
            entity.getId(), entity.getFecha(), entity.getFranjaId(),
            franja != null ? franja.getEtiqueta() : entity.getFranjaId(),
            entity.getProfesorAusente(), entity.getGrupo(), entity.getAula(),
            entity.getAsignatura(), entity.getTarea()
        );
    }

    @Transactional
    public boolean eliminarAusencia(String fecha, String ausenciaId) {
        String ausenciaIdUso = normalizarCampo(ausenciaId, "ausenciaId");
        if (!ausenciaRepository.existsById(ausenciaIdUso)) {
            return false;
        }
        ausenciaRepository.deleteById(ausenciaIdUso);
        return true;
    }

    public List<Profesor> obtenerProfesores(String fecha) {
        Parte parte = obtenerParte(fecha);
        if (parte == null) {
            return new ArrayList<>();
        }

        Map<String, Profesor> mapaProfes = new HashMap<>();

        for (Bloque bloque : parte.getBloques()) {
            Franja franja = franjas.stream()
                .filter(f -> f.getId().equals(bloque.getFranjaId()))
                .findFirst()
                .orElse(null);

            for (ProfesorGuardia guardia : bloque.getProfesoresGuardia()) {
                mapaProfes.computeIfAbsent(guardia.getNombre(), nombre ->
                    new Profesor(
                        generarId(nombre),
                        nombre,
                        new ArrayList<>(),
                        new ArrayList<>()
                    )
                ).getGuardias().add(new GuardiaInfo(
                    bloque.getFranjaId(),
                    franja != null ? franja.getEtiqueta() : bloque.getFranjaId(),
                    guardia.getUbicacion()
                ));
            }
        }

        return new ArrayList<>(mapaProfes.values());
    }

    private String generarId(String nombre) {
        return nombre.toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
    }

    private String resolverFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            if (fechasDisponibles.isEmpty()) {
                throw new NoSuchElementException("No hay fechas disponibles");
            }
            return fechasDisponibles.get(0);
        }
        return fecha.trim();
    }

    private String normalizarCampo(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio");
        }
        return valor.trim();
    }

    private AusenciaTarea normalizarAusenciaEntrada(AusenciaTarea ausencia) {
        if (ausencia == null) {
            throw new IllegalArgumentException("La ausencia no puede ser nula");
        }

        return new AusenciaTarea(
            ausencia.getId(),
            normalizarCampo(ausencia.getProfesorAusente(), "profesorAusente"),
            normalizarCampo(ausencia.getGrupo(), "grupo"),
            normalizarCampo(ausencia.getAula(), "aula"),
            normalizarCampo(ausencia.getAsignatura(), "asignatura"),
            normalizarCampo(ausencia.getTarea(), "tarea")
        );
    }

    private Bloque buscarBloque(Parte parte, String franjaId) {
        return parte.getBloques().stream()
            .filter(bloque -> franjaId.equals(bloque.getFranjaId()))
            .findFirst()
            .orElse(null);
    }

    private void asegurarId(AusenciaTarea ausencia) {
        if (ausencia.getId() == null || ausencia.getId().trim().isEmpty()) {
            ausencia.setId(UUID.randomUUID().toString());
        }
    }

    private AusenciaDetalle construirAusenciaDetalle(String fecha, Bloque bloque, AusenciaTarea ausencia) {
        Franja franja = franjas.stream()
            .filter(f -> f.getId().equals(bloque.getFranjaId()))
            .findFirst()
            .orElse(null);

        return new AusenciaDetalle(
            ausencia.getId(),
            fecha,
            bloque.getFranjaId(),
            franja != null ? franja.getEtiqueta() : bloque.getFranjaId(),
            ausencia.getProfesorAusente(),
            ausencia.getGrupo(),
            ausencia.getAula(),
            ausencia.getAsignatura(),
            ausencia.getTarea()
        );
    }

    private AusenciaDetalle construirAusenciaDetalleDesdeEntity(AusenciaEntity entity) {
        Franja franja = franjas.stream()
            .filter(f -> f.getId().equals(entity.getFranjaId()))
            .findFirst()
            .orElse(null);

        return new AusenciaDetalle(
            entity.getId(), entity.getFecha(), entity.getFranjaId(),
            franja != null ? franja.getEtiqueta() : entity.getFranjaId(),
            entity.getProfesorAusente(), entity.getGrupo(), entity.getAula(),
            entity.getAsignatura(), entity.getTarea()
        );
    }


    // Clases auxiliares
    public static class Profesor {
        private String id;
        private String nombre;
        private List<GuardiaInfo> guardias;
        private List<AusenciaCubiertas> ausenciasCubiertas;

        public Profesor(String id, String nombre, List<GuardiaInfo> guardias, List<AusenciaCubiertas> ausenciasCubiertas) {
            this.id = id;
            this.nombre = nombre;
            this.guardias = guardias;
            this.ausenciasCubiertas = ausenciasCubiertas;
        }

        public String getId() { return id; }
        public String getNombre() { return nombre; }
        public List<GuardiaInfo> getGuardias() { return guardias; }
        public List<AusenciaCubiertas> getAusenciasCubiertas() { return ausenciasCubiertas; }
    }

    public static class GuardiaInfo {
        private String franjaId;
        private String franja;
        private String ubicacion;

        public GuardiaInfo(String franjaId, String franja, String ubicacion) {
            this.franjaId = franjaId;
            this.franja = franja;
            this.ubicacion = ubicacion;
        }

        public String getFranjaId() { return franjaId; }
        public String getFranja() { return franja; }
        public String getUbicacion() { return ubicacion; }
    }

    public static class AusenciaCubiertas {
        private String franjaId;
        private String profesorAusente;
        private String grupo;

        public AusenciaCubiertas(String franjaId, String profesorAusente, String grupo) {
            this.franjaId = franjaId;
            this.profesorAusente = profesorAusente;
            this.grupo = grupo;
        }

        public String getFranjaId() { return franjaId; }
        public String getProfesorAusente() { return profesorAusente; }
        public String getGrupo() { return grupo; }
    }

    public static class AusenciaDetalle {
        private String id;
        private String fecha;
        private String franjaId;
        private String franja;
        private String profesorAusente;
        private String grupo;
        private String aula;
        private String asignatura;
        private String tarea;

        public AusenciaDetalle(
            String id,
            String fecha,
            String franjaId,
            String franja,
            String profesorAusente,
            String grupo,
            String aula,
            String asignatura,
            String tarea
        ) {
            this.id = id;
            this.fecha = fecha;
            this.franjaId = franjaId;
            this.franja = franja;
            this.profesorAusente = profesorAusente;
            this.grupo = grupo;
            this.aula = aula;
            this.asignatura = asignatura;
            this.tarea = tarea;
        }

        public String getId() { return id; }
        public String getFecha() { return fecha; }
        public String getFranjaId() { return franjaId; }
        public String getFranja() { return franja; }
        public String getProfesorAusente() { return profesorAusente; }
        public String getGrupo() { return grupo; }
        public String getAula() { return aula; }
        public String getAsignatura() { return asignatura; }
        public String getTarea() { return tarea; }
    }
}
