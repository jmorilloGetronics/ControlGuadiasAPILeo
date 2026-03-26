package com.leo.backend.repository;

import com.leo.backend.entity.CentroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroRepository extends JpaRepository<CentroEntity, Long> {
}