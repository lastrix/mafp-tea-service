package org.lastrix.mafp.tea.web.v1.api;

import lombok.RequiredArgsConstructor;
import org.lastrix.mafp.roles.client.api.RequireRoles;
import org.lastrix.mafp.tea.model.dto.TeaTypeDto;
import org.lastrix.mafp.tea.persistence.mapper.TeaTypeMapper;
import org.lastrix.mafp.tea.service.TeaTypeService;
import org.lastrix.rest.Pagination;
import org.lastrix.rest.Rest;
import org.lastrix.rest.ValidGroup;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tea/type")
public class TeaTypeController {
    private final TeaTypeService service;
    private final TeaTypeMapper mapper;

    @RequireRoles("TeaTypeManager")
    @PostMapping
    public Rest<TeaTypeDto> create(@RequestBody @Validated(ValidGroup.Create.class) TeaTypeDto teaType) {
        return Rest.of(service.create(mapper.fromDto(teaType)), mapper);
    }

    @RequireRoles("TeaTypeManager")
    @PutMapping
    public Rest<TeaTypeDto> update(@RequestBody @Validated(ValidGroup.Create.class) TeaTypeDto teaType) {
        return Rest.of(service.update(mapper.fromDto(teaType)), mapper);
    }

    @RequireRoles("TeaTypeManager")
    @GetMapping("/{teaType}")
    public Rest<TeaTypeDto> getRole(@PathVariable String teaType) {
        return Rest.of(service.getByName(teaType), mapper);
    }

    @RequireRoles("TeaTypeManager")
    @GetMapping("/page")
    public Rest<TeaTypeDto> page(@ModelAttribute @Validated(ValidGroup.Read.class) Pagination pagination) {
        return Rest.of(service.page(pagination),
                pagination,
                mapper
        );
    }

    @RequireRoles("TeaTypeManager")
    @PostMapping("/page")
    public Rest<TeaTypeDto> pagePost(@RequestBody @Validated(ValidGroup.Read.class) Pagination pagination) {
        return Rest.of(service.page(pagination),
                pagination,
                mapper
        );
    }
}
