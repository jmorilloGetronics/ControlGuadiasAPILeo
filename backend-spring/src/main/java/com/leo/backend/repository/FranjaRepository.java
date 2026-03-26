package com.leo.backend.repository;

import com.leo.backend.entity.FranjaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FranjaRepository extends JpaRepository<FranjaEntity, String> {
    // Esto ordenará las horas (1ª hora, 2ª hora...) automáticamente
    List<FranjaEntity> findAllByOrderByOrdenAsc();
}