/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class Veiculo {
    private int idVeiculo;
    private String placa;
    private int ano;
    private int quilometragem;
    private Cliente cliente;
    private Modelo modelo;

    // Construtor vazio
    public Veiculo() {
    }

    // Construtor completo
    public Veiculo(int idVeiculo, String placa, int ano, int quilometragem, Cliente cliente, Modelo modelo) {
        this.idVeiculo = idVeiculo;
        this.placa = placa;
        this.ano = ano;
        this.quilometragem = quilometragem;
        this.cliente = cliente;
        this.modelo = modelo;
    }

    // Getters e Setters
    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
}
