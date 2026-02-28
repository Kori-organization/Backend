package com.example.koribackend.dto;

/**
 * Data Transfer Object used to represent class performance rankings.
 * It carries the grade level (serie) and the calculated class average.
 */
public class RakingDTO {

    private int serie;
    private double avarageClass;

    // Full constructor to initialize the DTO with data from the database or service
    public RakingDTO(int serie, double avarageClass) {
        this.serie = serie;
        this.avarageClass = avarageClass;
    }

    // Default no-argument constructor required for some frameworks and serialization
    public RakingDTO() {}

    // Getter for the numeric grade level
    public int getSerie() {
        return serie;
    }

    // Setter for the numeric grade level
    public void setSerie(int serie) {
        this.serie = serie;
    }

    // Getter for the calculated class average
    public double getAvarageClass() {
        return avarageClass;
    }

    // Setter for the calculated class average
    public void setAvarageClass(double avarageClass) {
        this.avarageClass = avarageClass;
    }

    /**
     * Helper method to return a formatted string of the grade level.
     * Useful for displaying "1º Ano", "2º Ano", etc., directly in the JSP views.
     */
    public String getSerieComplete() {
        return String.format("%dº Ano",this.serie);
    }
}