/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class Servico {
    private int idServico;
    private String descricao;
    private double precoUnitario;

    // Construtor vazio
    public Servico() {
    }

    // Construtor completo
    public Servico(int idServico, String descricao, double precoUnitario) {
        this.idServico = idServico;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
    }

    // Getters e Setters
    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public String toString() {
        return "Servico {" +
                "idServico=" + idServico +
                ", descricao='" + descricao + '\'' +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
