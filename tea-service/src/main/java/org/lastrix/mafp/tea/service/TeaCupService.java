package org.lastrix.mafp.tea.service;

import org.lastrix.mafp.tea.persistence.entity.TeaCup;

import java.util.UUID;

public interface TeaCupService {
    TeaCup drink(UUID userId, Integer amount, String teaType);
}
