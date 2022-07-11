package com.example.automaticirrigationsystem.domain;


import com.example.automaticirrigationsystem.domain.enumeration.Status;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Slot.
 */
@Entity
@Table(name = "slot")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Slot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "plot_id")
    private Plot plot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Slot slot = (Slot) o;
        return id != null && Objects.equals(id, slot.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
