package com.leo.backend.repository;

import com.leo.backend.entity.ParteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParteRepository extends JpaRepository<ParteEntity, String> {
    List<ParteEntity> findAllByOrderByFechaAsc();
}