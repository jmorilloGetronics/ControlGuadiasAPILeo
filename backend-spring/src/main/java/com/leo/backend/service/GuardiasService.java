package com.leo.backend.service;

import com.leo.backend.entity.*;
import com.leo.backend.repository.*;
import com.leo.backend.model.*;

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

    private final Map<String, String> sesionesPorToken = new ConcurrentHashMap<>();

    @Autowired private CentroRepository centroRepository;
    @Autowired private FranjaRepository franjaRepository;
    @Autowired private ParteRepository parteRepository;
    @Autowired private ProfesorGuardiaRepository profesorGuardiaRepository;
    @Autowired private AusenciaRepository ausenciaRepository;

    // --- CENTRO ---
    public Centro getCentro() {
        CentroEntity e = centroRepository.findAll().stream()
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No hay centro configurado"));

        Recreo recreo = new Recreo(e.getRecreoInicio(), e.getRecreoFin(), e.getRecreoDuracion());
        Jornada jornada = new Jornada(e.getJornadaInicio(), e.getJornadaFin(), e.getJornadaHoras(), recreo);
        return new Centro(e.getNombre(), jornada);
    }

    // --- FRANJAS ---
    public List<Franja> getFranjas() {
        return franjaRepository.findAllByOrderByOrdenAsc().stream()
            .map(this::toFranja)
            .collect(Collectors.toList());
    }

    private Franja toFranja(FranjaEntity e) {
        return new Franja(e.getId(), e.getEtiqueta(), e.getHoraInicio(), e.getHoraFin(), e.getOrden(), e.getTipo());
    }

    // --- PARTES ---
    public List<String> getFechasDisponibles() {
        return parteRepository.findAllByOrderByFechaAsc().stream()
            .map(ParteEntity::getFecha)
            .collect(Collectors.toList());
    }

    public List<Map<String, Object>> listarPartes() {
        return parteRepository.findAllByOrderByFechaAsc().stream()
            .map(p -> {
                Map<String, Object> m = new HashMap<>();
                m.put("fecha", p.getFecha());
                m.put("diaSemana", p.getDiaSemana());
                return m;
            }).collect(Collectors.toList());
    }

    public Parte obtenerParte(String fecha) {
        String fechaUso = resolverFecha(fecha);
        ParteEntity parteEntity = parteRepository.findById(fechaUso).orElse(null);
        if (parteEntity == null) return null;

        List<FranjaEntity> franjas = franjaRepository.findAllByOrderByOrdenAsc();
        List<ProfesorGuardiaEntity> guardias = profesorGuardiaRepository.findByFecha(fechaUso);
        List<AusenciaEntity> ausencias = ausenciaRepository.findByFecha(fechaUso);

        List<Bloque> bloques = franjas.stream().map(fe -> {
            List<ProfesorGuardia> pG = guardias.stream()
                .filter(g -> g.getFranjaId().equals(fe.getId()))
                .map(g -> new ProfesorGuardia(g.getNombre(), g.getUbicacion()))
                .collect(Collectors.toList());

            List<AusenciaTarea> aT = ausencias.stream()
                .filter(a -> a.getFranjaId().equals(fe.getId()))
                .map(a -> new AusenciaTarea(a.getId(), a.getProfesorAusente(), a.getGrupo(), a.getAula(), a.getAsignatura(), a.getTarea()))
                .collect(Collectors.toList());

            return new Bloque(fe.getId(), pG, aT);
        }).collect(Collectors.toList());

        return new Parte(fechaUso, parteEntity.getDiaSemana(), parteEntity.getJefeEstudios(), bloques);
    }

    // --- LOGIN (MÉTODOS QUE FALTABAN) ---
    public String login(String usuario, String password) {
        if (!LOGIN_USUARIO.equals(usuario) || !LOGIN_PASSWORD.equals(password)) return null;
        String token = UUID.randomUUID().toString();
        sesionesPorToken.put(token, usuario);
        return token;
    }

    public String obtenerUsuarioPorToken(String token) {
        return token == null ? null : sesionesPorToken.get(token);
    }

    public boolean tokenValido(String token) {
        return token != null && sesionesPorToken.containsKey(token);
    }

    // --- AUSENCIAS (CRUD COMPLETO) ---
    public List<AusenciaDetalle> listarAusencias(String fecha) {
        return ausenciaRepository.findByFecha(resolverFecha(fecha)).stream()
            .map(this::construirAusenciaDetalleDesdeEntity).collect(Collectors.toList());
    }

    @Transactional
    public AusenciaDetalle crearAusencia(String fecha, String franjaId, AusenciaTarea ausencia) {
        String id = UUID.randomUUID().toString();
        AusenciaEntity entity = new AusenciaEntity(id, resolverFecha(fecha), franjaId,
            ausencia.getProfesorAusente(), ausencia.getGrupo(), ausencia.getAula(), ausencia.getAsignatura(), ausencia.getTarea());
        ausenciaRepository.save(entity);
        return construirAusenciaDetalleDesdeEntity(entity);
    }

    @Transactional
    public AusenciaDetalle actualizarAusencia(String fecha, String id, String franjaId, AusenciaTarea cambios) {
        AusenciaEntity entity = ausenciaRepository.findById(id).orElseThrow();
        entity.setProfesorAusente(cambios.getProfesorAusente());
        entity.setGrupo(cambios.getGrupo());
        entity.setAula(cambios.getAula());
        entity.setAsignatura(cambios.getAsignatura());
        entity.setTarea(cambios.getTarea());
        if (franjaId != null) entity.setFranjaId(franjaId);
        ausenciaRepository.save(entity);
        return construirAusenciaDetalleDesdeEntity(entity);
    }

    @Transactional
    public boolean eliminarAusencia(String fecha, String id) {
        if (!ausenciaRepository.existsById(id)) return false;
        ausenciaRepository.deleteById(id);
        return true;
    }

    // --- PROFESORES (EL MÉTODO QUE FALTABA) ---
    public List<Profesor> obtenerProfesores(String fecha) {
        List<ProfesorGuardiaEntity> guardias = profesorGuardiaRepository.findByFecha(resolverFecha(fecha));
        Map<String, Profesor> mapa = new HashMap<>();
        for (ProfesorGuardiaEntity g : guardias) {
            mapa.computeIfAbsent(g.getNombre(), n -> new Profesor(generarId(n), n));
        }
        return new ArrayList<>(mapa.values());
    }

    // --- HELPERS ---
    private String resolverFecha(String f) {
        if (f == null || f.trim().isEmpty()) {
            return parteRepository.findAllByOrderByFechaAsc().get(0).getFecha();
        }
        return f.trim();
    }

    private AusenciaDetalle construirAusenciaDetalleDesdeEntity(AusenciaEntity e) {
        return new AusenciaDetalle(e.getId(), e.getFecha(), e.getFranjaId(), "", e.getProfesorAusente(), e.getGrupo(), e.getAula(), e.getAsignatura(), e.getTarea());
    }

    private String generarId(String n) { return n.toLowerCase().replace(" ", "-"); }

    // --- CLASES AUXILIARES ---
    public static class AusenciaDetalle {
        public String id, fecha, franjaId, franja, profesorAusente, grupo, aula, asignatura, tarea;
        public AusenciaDetalle(String id, String fecha, String franjaId, String franja, String profesorAusente, String grupo, String aula, String asignatura, String tarea) {
            this.id = id; this.fecha = fecha; this.franjaId = franjaId; this.franja = franja;
            this.profesorAusente = profesorAusente; this.grupo = grupo; this.aula = aula;
            this.asignatura = asignatura; this.tarea = tarea;
        }
    }

    public static class Profesor {
        public String id, nombre;
        public List<Object> guardias = new ArrayList<>();
        public Profesor(String id, String nombre) { this.id = id; this.nombre = nombre; }
    }
}