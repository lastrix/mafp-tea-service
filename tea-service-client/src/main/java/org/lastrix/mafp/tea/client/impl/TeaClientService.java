package org.lastrix.mafp.tea.client.impl;

import org.lastrix.mafp.tea.model.dto.TeaCupDto;

import java.util.UUID;

public interface TeaClientService {
    TeaCupDto drink(UUID userId, int amount, String teaType);
}
