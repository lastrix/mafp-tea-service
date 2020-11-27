package org.lastrix.mafp.tea.service;

import org.lastrix.mafp.tea.persistence.entity.TeaType;
import org.lastrix.rest.Pagination;
import org.springframework.data.domain.Page;

public interface TeaTypeService {
    TeaType create(TeaType teaType);

    TeaType update(TeaType teaType);

    Page<TeaType> page(Pagination pagination);

    TeaType getByName(String teaTypeName);
}
