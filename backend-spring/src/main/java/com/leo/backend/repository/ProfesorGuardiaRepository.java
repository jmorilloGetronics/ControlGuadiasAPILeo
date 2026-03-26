package com.leo.backend.repository;

import com.leo.backend.entity.ProfesorGuardiaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfesorGuardiaRepository extends JpaRepository<ProfesorGuardiaEntity, String> {
    List<ProfesorGuardiaEntity> findByFecha(String fecha);
    List<ProfesorGuardiaEntity> findByFechaAndFranjaId(String fecha, String franjaId);
}