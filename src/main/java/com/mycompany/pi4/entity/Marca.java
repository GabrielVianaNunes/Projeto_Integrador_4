package com.mycompany.pi4.entity;

public class Marca {

    private int idMarca;
    private String nome;

    public Marca() {
    }

    public Marca(int idMarca, String nome) {
        this.idMarca = idMarca;
        this.nome = nome;
    }

    // Getters e setters
    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome; // Retorna apenas o nome da marca
    }
}
