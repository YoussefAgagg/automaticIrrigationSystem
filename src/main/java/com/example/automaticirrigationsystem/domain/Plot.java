package com.example.automaticirrigationsystem.domain;

import com.example.automaticirrigationsystem.domain.enumeration.CropType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Plot.
 */
@Entity
@Table(name = "plot")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Plot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "length")
    private Double length;

    @Column(name = "width")
    private Double width;

    @Column(name = "is_irrigated")
    private Boolean isIrrigated;

    @Column(name = "tries_count")
    private Integer triesCount;

    @Column(name = "has_alert")
    private Boolean hasAlert;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_type")
    private CropType cropType;

    @OneToOne
    @JoinColumn(unique = true, name = "sensor_id")
    private Sensor sensor;

    @OneToMany(mappedBy = "plot")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ToString.Exclude
    private Set<Slot> slots = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Plot plot = (Plot) o;
        return id != null && Objects.equals(id, plot.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
