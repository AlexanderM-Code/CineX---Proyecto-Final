package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Compra de sillas para una función. */
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La función es obligatoria")
    @ManyToOne(optional = false)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 120, message = "El correo admite máximo 120 caracteres")
    @Column(name = "customer_email", nullable = false, length = 120)
    private String customerEmail;

    @NotNull(message = "El número de sillas es obligatorio")
    @Min(value = 1, message = "Se debe reservar al menos 1 silla")
    @Max(value = 20, message = "Se pueden reservar máximo 20 sillas por compra")
    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void alCrear() {
        createdAt = LocalDateTime.now();
    }

    public Booking() {}

    public Booking(Showtime showtime, String customerEmail, Integer numberOfSeats) {
        this.showtime = showtime;
        this.customerEmail = customerEmail;
        this.numberOfSeats = numberOfSeats;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public Integer getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(Integer numberOfSeats) { this.numberOfSeats = numberOfSeats; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
