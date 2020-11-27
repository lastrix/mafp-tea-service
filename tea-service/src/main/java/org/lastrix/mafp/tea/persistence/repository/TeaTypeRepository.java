package org.lastrix.mafp.tea.persistence.repository;

import org.lastrix.mafp.tea.model.dto.TeaTypeDto;
import org.lastrix.mafp.tea.persistence.entity.TeaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeaTypeRepository extends JpaRepository<TeaType, TeaTypeDto> {
    @Query("SELECT t FROM TeaType t WHERE t.name = ?1")
    Optional<TeaType> findByName(String teaTypeName);
}
