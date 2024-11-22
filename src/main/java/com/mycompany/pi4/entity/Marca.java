/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class Marca {
    private int idMarca;
    private String nome;

    public Marca(int idMarca, String nome) {
        this.idMarca = idMarca;
        this.nome = nome;
    }

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
        return "Marca { " +
                "idMarca=" + idMarca +
                ", nome='" + nome + '\'' +
                '}';
    }
}