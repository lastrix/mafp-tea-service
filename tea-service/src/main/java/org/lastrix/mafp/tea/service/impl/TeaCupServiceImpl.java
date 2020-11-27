package org.lastrix.mafp.tea.service.impl;

import lombok.RequiredArgsConstructor;
import org.lastrix.mafp.tea.persistence.entity.TeaCup;
import org.lastrix.mafp.tea.persistence.repository.TeaCupRepository;
import org.lastrix.mafp.tea.service.TeaCupService;
import org.lastrix.mafp.tea.service.TeaTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeaCupServiceImpl implements TeaCupService {
    private final TeaCupRepository repository;
    private final TeaTypeService typeService;

    @Transactional
    @Override
    public TeaCup drink(UUID userId, Integer amount, String teaType) {
        var tc = new TeaCup();
        tc.setAmount(amount);
        tc.setUserId(userId);
        tc.setType(typeService.getByName(teaType));
        return repository.save(tc);
    }
}
