package com.leo.backend.repository;

import com.leo.backend.entity.AusenciaEntity; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // <--- Nueva
import java.util.List;

@Repository
public interface AusenciaRepository extends JpaRepository<AusenciaEntity, String> {

    List<AusenciaEntity> findByFecha(String fecha);

    List<AusenciaEntity> findByFechaAndFranjaId(String fecha, String franjaId);

    @Transactional // <--- ¡Añade esto para que el borrado funcione!
    void deleteByFechaAndId(String fecha, String id);
}