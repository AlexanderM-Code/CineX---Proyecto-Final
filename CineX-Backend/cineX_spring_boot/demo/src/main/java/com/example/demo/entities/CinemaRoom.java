package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Sala del cine. La capacidad limita cuántas sillas se pueden reservar por función. */
@Entity
@Table(name = "cinema_room")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la sala es obligatorio")
    @Size(max = 60, message = "El nombre admite máximo 60 caracteres")
    @Column(nullable = false, unique = true, length = 60)
    private String name;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser de al menos 1 silla")
    @Max(value = 1000, message = "La capacidad máxima es de 1000 sillas")
    @Column(nullable = false)
    private Integer capacity;

    public CinemaRoom() {}

    public CinemaRoom(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
}
