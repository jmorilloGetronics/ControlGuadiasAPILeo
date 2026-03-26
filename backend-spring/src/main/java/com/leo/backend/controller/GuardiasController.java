package com.leo.backend.controller;

import com.leo.backend.model.*;
import com.leo.backend.service.GuardiasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class GuardiasController {

    @Autowired
    private GuardiasService guardiasService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("ok", "true");
        response.put("servicio", "Control Guardias API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        String usuario = request != null ? request.getUsuario() : null;
        String password = request != null ? request.getPassword() : null;

        String token = guardiasService.login(usuario, password);
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ok", true);
        response.put("usuario", usuario);
        response.put("token", token);
        response.put("tipo", "Bearer");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> me(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        String token = extraerTokenBearer(authorization);
        String usuario = guardiasService.obtenerUsuarioPorToken(token);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido o ausente");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("ok", true);
        response.put("usuario", usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> config(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        validarToken(authorization);

        Map<String, Object> response = new HashMap<>();
        response.put("centro", guardiasService.getCentro());
        response.put("franjas", guardiasService.getFranjas());
        response.put("fechasDisponibles", guardiasService.getFechasDisponibles());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/partes")
    public ResponseEntity<Map<String, Object>> partes(
        @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        validarToken(authorization);

        Map<String, Object> response = new HashMap<>();
        response.put("centro", guardiasService.getCentro());
        response.put("partes", guardiasService.listarPartes());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parte")
    public ResponseEntity<Map<String, Object>> parteQuery(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestParam(required = false) String fecha
    ) {
        validarToken(authorization);

        Parte parte = guardiasService.obtenerParte(fecha);
        
        if (parte == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontro parte para la fecha solicitada: " + fecha
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("centro", guardiasService.getCentro());
        response.put("franjas", guardiasService.getFranjas());
        response.put("parte", parte);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/parte/{fecha}")
    public ResponseEntity<Map<String, Object>> partePathVariable(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable String fecha
    ) {
        validarToken(authorization);

        Parte parte = guardiasService.obtenerParte(fecha);
        
        if (parte == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No se encontro parte para la fecha solicitada: " + fecha
            );
        }

        Map<String, Object> response = new HashMap<>();
        response.put("centro", guardiasService.getCentro());
        response.put("franjas", guardiasService.getFranjas());
        response.put("parte", parte);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profesores")
    public ResponseEntity<Map<String, Object>> profesores(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestParam(required = false) String fecha
    ) {
        validarToken(authorization);

        String fechaUso = fecha;
        if (fechaUso == null || fechaUso.trim().isEmpty()) {
            fechaUso = guardiasService.getFechasDisponibles().get(0);
        }

        List<GuardiasService.Profesor> profesores = guardiasService.obtenerProfesores(fechaUso);

        Map<String, Object> response = new HashMap<>();
        response.put("fecha", fechaUso);
        response.put("total", profesores.size());
        response.put("profesores", profesores);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ausencias")
    public ResponseEntity<Map<String, Object>> ausencias(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestParam(required = false) String fecha
    ) {
        validarToken(authorization);

        String fechaUso = fecha;
        if (fechaUso == null || fechaUso.trim().isEmpty()) {
            fechaUso = guardiasService.getFechasDisponibles().get(0);
        }

        List<GuardiasService.AusenciaDetalle> ausencias = guardiasService.listarAusencias(fechaUso);

        Map<String, Object> response = new HashMap<>();
        response.put("fecha", fechaUso);
        response.put("total", ausencias.size());
        response.put("ausencias", ausencias);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ausencias")
    public ResponseEntity<Map<String, Object>> crearAusencia(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @RequestBody AusenciaCrudRequest request
    ) {
        validarToken(authorization);

        try {
            GuardiasService.AusenciaDetalle creada = guardiasService.crearAusencia(
                request != null ? request.getFecha() : null,
                request != null ? request.getFranjaId() : null,
                request != null ? request.toAusenciaTarea() : null
            );

            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("ausencia", creada);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PutMapping("/ausencias/{ausenciaId}")
    public ResponseEntity<Map<String, Object>> actualizarAusencia(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable String ausenciaId,
        @RequestBody AusenciaCrudRequest request
    ) {
        validarToken(authorization);

        try {
            GuardiasService.AusenciaDetalle actualizada = guardiasService.actualizarAusencia(
                request != null ? request.getFecha() : null,
                ausenciaId,
                request != null ? request.getFranjaId() : null,
                request != null ? request.toAusenciaTarea() : null
            );

            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("ausencia", actualizada);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/ausencias/{ausenciaId}")
    public ResponseEntity<Map<String, Object>> eliminarAusencia(
        @RequestHeader(value = "Authorization", required = false) String authorization,
        @PathVariable String ausenciaId,
        @RequestParam(required = false) String fecha
    ) {
        validarToken(authorization);

        try {
            boolean eliminada = guardiasService.eliminarAusencia(fecha, ausenciaId);
            if (!eliminada) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe ausencia con id: " + ausenciaId);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("ok", true);
            response.put("eliminada", true);
            response.put("id", ausenciaId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private void validarToken(String authorizationHeader) {
        String token = extraerTokenBearer(authorizationHeader);

        if (!guardiasService.tokenValido(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido o ausente");
        }
    }

    private String extraerTokenBearer(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            return null;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.substring(7).trim();
    }

    public static class LoginRequest {
        private String usuario;
        private String password;

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class AusenciaCrudRequest {
        private String fecha;
        private String franjaId;
        private String profesorAusente;
        private String grupo;
        private String aula;
        private String asignatura;
        private String tarea;

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public String getFranjaId() {
            return franjaId;
        }

        public void setFranjaId(String franjaId) {
            this.franjaId = franjaId;
        }

        public String getProfesorAusente() {
            return profesorAusente;
        }

        public void setProfesorAusente(String profesorAusente) {
            this.profesorAusente = profesorAusente;
        }

        public String getGrupo() {
            return grupo;
        }

        public void setGrupo(String grupo) {
            this.grupo = grupo;
        }

        public String getAula() {
            return aula;
        }

        public void setAula(String aula) {
            this.aula = aula;
        }

        public String getAsignatura() {
            return asignatura;
        }

        public void setAsignatura(String asignatura) {
            this.asignatura = asignatura;
        }

        public String getTarea() {
            return tarea;
        }

        public void setTarea(String tarea) {
            this.tarea = tarea;
        }

        public AusenciaTarea toAusenciaTarea() {
            return new AusenciaTarea(profesorAusente, grupo, aula, asignatura, tarea);
        }
    }
}
