package org.lastrix.mafp.tea.persistence.mapper;

import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.mafp.tea.persistence.entity.TeaCup;
import org.lastrix.rest.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TeaCupMapper implements EntityMapper<TeaCup, TeaCupDto> {

}
