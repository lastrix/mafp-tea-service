package org.lastrix.mafp.tea.web.v1.srv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lastrix.jwt.UserType;
import org.lastrix.mafp.roles.client.api.RequireRoles;
import org.lastrix.mafp.roles.client.api.RoleClientService;
import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.mafp.tea.persistence.mapper.TeaCupMapper;
import org.lastrix.mafp.tea.service.TeaCupService;
import org.lastrix.rest.Rest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/srv/v1/tea")
@RequiredArgsConstructor
public class SrvTeaCupController {
    private final TeaCupService service;
    private final TeaCupMapper mapper;
    private final RoleClientService roleClientService;

    @RequireRoles(value = "TeaDealer", userTypes = UserType.SRV)
    @PostMapping("/{userId}")
    public Rest<TeaCupDto> drink(@PathVariable UUID userId, @RequestParam Integer amount, @RequestParam String teaType) {
        if (!roleClientService.hasRoles(userId, List.of("User")))
            throw new IllegalStateException("Person not allowed to drink tea");
        return Rest.of(service.drink(userId, amount, teaType), mapper);
    }
}
