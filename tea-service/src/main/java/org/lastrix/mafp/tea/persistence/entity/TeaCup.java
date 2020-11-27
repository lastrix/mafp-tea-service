package org.lastrix.mafp.tea.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tea_cup")
@Data
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class TeaCup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tea_cup_tea_cup_id_auto_gen")
    @SequenceGenerator(name = "tea_cup_tea_cup_id_auto_gen", sequenceName = "tea_cup_seq", allocationSize = 1)
    @Column(name = "tea_cup_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Positive
    @Column(name = "amount", nullable = false, updatable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tea_type_id", nullable = false)
    private TeaType type;

    @CreatedDate
    @Column(name = "stamp", nullable = false, updatable = false)
    private Instant stamp;
}
