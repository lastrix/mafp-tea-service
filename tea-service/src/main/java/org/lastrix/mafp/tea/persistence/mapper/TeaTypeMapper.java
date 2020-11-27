package org.lastrix.mafp.tea.persistence.mapper;

import org.lastrix.mafp.tea.model.dto.TeaTypeDto;
import org.lastrix.mafp.tea.persistence.entity.TeaType;
import org.lastrix.rest.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TeaTypeMapper implements EntityMapper<TeaType, TeaTypeDto> {

}
