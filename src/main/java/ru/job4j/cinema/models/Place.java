package ru.job4j.cinema.models;

public class Place {
    private int id;
    private int row;
    private int cell;
    private boolean isBooked;

    public Place(int id, int row, int cell, boolean isBooked) {
        this.id = id;
        this.row = row;
        this.cell = cell;
        this.isBooked = isBooked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
