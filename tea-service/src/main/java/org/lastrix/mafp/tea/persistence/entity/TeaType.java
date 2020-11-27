package org.lastrix.mafp.tea.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "tea_type")
@Data
@ToString
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class TeaType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tea_type_tea_type_id_auto_gen")
    @SequenceGenerator(name = "tea_type_tea_type_id_auto_gen", sequenceName = "tea_type_seq", allocationSize = 1)
    @Column(name = "tea_type_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;
}
