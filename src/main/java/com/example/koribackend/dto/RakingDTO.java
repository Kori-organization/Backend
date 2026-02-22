package com.example.koribackend.dto;

public class RakingDTO {

    private int serie;
    private double avarageClass;

    public RakingDTO(int serie, double avarageClass) {
        this.serie = serie;
        this.avarageClass = avarageClass;
    }

    public RakingDTO() {}

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public double getAvarageClass() {
        return avarageClass;
    }

    public void setAvarageClass(double avarageClass) {
        this.avarageClass = avarageClass;
    }

    public String getSerieComplete() {
        return String.format("%dº Ano",this.serie);
    }
}
