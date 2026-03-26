package com.leo.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AusenciaRepository extends JpaRepository<AusenciaEntity, String> {

    List<AusenciaEntity> findByFecha(String fecha);

    List<AusenciaEntity> findByFechaAndFranjaId(String fecha, String franjaId);

    void deleteByFechaAndId(String fecha, String id);
}
