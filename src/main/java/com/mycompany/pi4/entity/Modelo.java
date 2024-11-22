/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class Modelo {
    private int idModelo;
    private String nome;
    private Marca marca; // Relacionamento com Marca

    // Construtor vazio
    public Modelo() {
    }

    // Construtor completo
    public Modelo(int idModelo, String nome, Marca marca) {
        this.idModelo = idModelo;
        this.nome = nome;
        this.marca = marca;
    }

    // Getters e Setters
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
        return "Modelo {" +
                "idModelo=" + idModelo +
                ", nome='" + nome + '\'' +
                ", marca=" + (marca != null ? marca.getNome() : "null") +
                '}';
    }
}

