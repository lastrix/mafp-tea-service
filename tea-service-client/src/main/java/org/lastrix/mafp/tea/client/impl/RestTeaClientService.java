package org.lastrix.mafp.tea.client.impl;

import lombok.RequiredArgsConstructor;
import org.lastrix.http.client.api.AbstractRestService;
import org.lastrix.mafp.tea.model.dto.TeaCupDto;

import java.util.UUID;

@RequiredArgsConstructor
public class RestTeaClientService extends AbstractRestService implements TeaClientService {
    private final TeaClient client;

    @Override
    public TeaCupDto drink(UUID userId, int amount, String teaType) {
        return singleResult(() -> client.drink(userId, amount, teaType));
    }
}
