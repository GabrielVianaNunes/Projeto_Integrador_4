package com.mycompany.pi4.entity;

public class Modelo {

    private int idModelo;
    private String nome;
    private Marca marca;

    public Modelo() {
    }

    public Modelo(int idModelo, String nome, Marca marca) {
        this.idModelo = idModelo;
        this.nome = nome;
        this.marca = marca;
    }

    // Getters e setters
    public int getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return nome; // Retorna apenas o nome do modelo
    }
}
