package org.lastrix.mafp.tea.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.mafp.tea.model.dto.TeaTypeDto;

import java.time.Instant;
import java.util.UUID;

@Slf4j
public class TeaClientServiceMock implements TeaClientService {
    @Override
    public TeaCupDto drink(UUID userId, int amount, String teaType) {
        return new TeaCupDto(1L, userId, amount, new TeaTypeDto(teaType, teaType), Instant.now());
    }
}
