package org.lastrix.mafp.tea.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lastrix.mafp.tea.persistence.entity.TeaType;
import org.lastrix.mafp.tea.persistence.repository.TeaTypeRepository;
import org.lastrix.mafp.tea.service.TeaTypeService;
import org.lastrix.rest.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeaTypeServiceImpl implements TeaTypeService {
    private final TeaTypeRepository repository;

    @Transactional
    @Override
    public TeaType create(TeaType teaType) {
        var r = new TeaType();
        r.setName(teaType.getName());
        r.setDescription(teaType.getDescription());
        return repository.save(r);
    }

    @Transactional
    @Override
    public TeaType update(TeaType teaType) {
        var r = getByName(teaType.getName());
        if (teaType.getDescription() != null) {
            r.setDescription(teaType.getDescription());
        }
        return repository.save(r);
    }

    @Transactional(readOnly = true)
    @Override
    public TeaType getByName(String teaTypeName) {
        return repository.findByName(teaTypeName).orElseThrow(() -> new IllegalArgumentException("No teaType: " + teaTypeName));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TeaType> page(Pagination pagination) {
        return repository.findAll(pagination.toPageable(Sort.by("name")));
    }
}
