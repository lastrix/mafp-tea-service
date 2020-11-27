package org.lastrix.mafp.tea.client.impl;

import org.lastrix.mafp.tea.model.dto.TeaCupDto;
import org.lastrix.rest.Rest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequestMapping("/srv/v1/tea")
public interface TeaClient {
    @PostMapping(value = "/{userId}")
    Rest<TeaCupDto> drink(@PathVariable UUID userId, @RequestParam Integer amount, @RequestParam String teaType);
}
