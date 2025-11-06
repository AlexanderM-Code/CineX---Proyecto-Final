package com.example.demo.entities;

// Hereda de BaseEntity
public class Booking extends BaseEntity {
    private int showtimeId;
    private String customerEmail;
    private int numberOfSeats;

    public Booking() {}

    public Booking(int id, int showtimeId, String customerEmail, int numberOfSeats) {
        this.id = id; // 'id' viene de BaseEntity
        this.showtimeId = showtimeId;
        this.customerEmail = customerEmail;
        this.numberOfSeats = numberOfSeats;
    }

    // Getters y Setters (ya no se necesita getId/setId)
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public int getNumberOfSeats() { return numberOfSeats; }
    public void setNumberOfSeats(int numberOfSeats) { this.numberOfSeats = numberOfSeats; }
}
