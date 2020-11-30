package org.lastrix.mafp.tea.web.v1.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lastrix.jwt.Jwt;
import org.lastrix.mafp.roles.client.api.RequireRoles;
import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.mafp.tea.model.dto.TeaTypeDto;
import org.lastrix.mafp.tea.persistence.mapper.TeaCupMapper;
import org.lastrix.mafp.tea.service.TeaCupService;
import org.lastrix.rest.Rest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api/v1/tea")
@RequiredArgsConstructor
public class TeaCupController {
    private final TeaCupService service;
    private final TeaCupMapper mapper;
    private final Jwt jwt;

    @RequireRoles("User")
    @PostMapping
    public Rest<TeaCupDto> drink(@RequestParam Integer amount, @RequestParam String teaType) {
        return Rest.of(service.drink(jwt.getUserId(), amount, teaType), mapper);
    }

    @RequireRoles("User")
    @PostMapping("/dummy")
    public Rest<TeaCupDto> drinkDummy(@RequestParam Integer amount, @RequestParam String teaType) {
        return Rest.of(new TeaCupDto(-1L, jwt.getUserId(), amount, new TeaTypeDto(teaType, teaType), Instant.now()));
    }

    @PostMapping("/dummy/noauth")
    public Rest<TeaCupDto> drinkDummyNoAuth(@RequestParam Integer amount, @RequestParam String teaType) {
        return Rest.of(new TeaCupDto(-1L, jwt.getUserId(), amount, new TeaTypeDto(teaType, teaType), Instant.now()));
    }
}
