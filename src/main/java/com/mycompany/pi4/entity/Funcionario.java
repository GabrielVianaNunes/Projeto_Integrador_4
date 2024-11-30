/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class Funcionario {

    private int idFuncionario;
    private String nome;
    
    public Funcionario() {
    }

    public Funcionario(int idFuncionario, String nome) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
    }

    // Getters e setters
    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    @Override
    public String toString() {
        return this.nome; // Certifique-se de que "nome" é um atributo da classe Funcionario
    }

}
