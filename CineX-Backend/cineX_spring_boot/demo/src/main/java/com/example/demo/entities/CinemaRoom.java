package com.example.demo.entities;

// Hereda de BaseEntity
public class CinemaRoom extends BaseEntity {
    private String name;
    private int capacity;

    public CinemaRoom() {}

    public CinemaRoom(int id, String name, int capacity) {
        this.id = id; // 'id' viene de BaseEntity
        this.name = name;
        this.capacity = capacity;
    }

    // Getters y Setters (ya no se necesita getId/setId)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
