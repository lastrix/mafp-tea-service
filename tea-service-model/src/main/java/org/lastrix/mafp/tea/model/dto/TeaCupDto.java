package org.lastrix.mafp.tea.model.dto;

import lombok.*;
import org.lastrix.rest.ValidGroup;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class TeaCupDto {
    @Null(groups = ValidGroup.Create.class)
    private Long id;

    @NotNull(groups = ValidGroup.Create.class)
    private UUID userId;

    @NotNull(groups = ValidGroup.Create.class)
    @Positive
    private Integer amount;

    @Valid
    @NotNull
    private TeaTypeDto type;

    @Null(groups = ValidGroup.Create.class)
    private Instant stamp;
}
