/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.util.List;

public class Veiculo {
    private int idVeiculo;
    private String chassi;
    private int quilometragem;
    private int anoFabricacao;
    private int anoModelo;
    private String placa;

    private Modelo modelo;
    private List<OrdemServico> ordensServico;

    public Veiculo(int idVeiculo, String chassi, int quilometragem, int anoFabricacao, int anoModelo, String placa, Modelo modelo) {
        this.idVeiculo = idVeiculo;
        this.chassi = chassi;
        this.quilometragem = quilometragem;
        this.anoFabricacao = anoFabricacao;
        this.anoModelo = anoModelo;
        this.placa = placa;
        this.modelo = modelo;
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public int getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(int quilometragem) {
        this.quilometragem = quilometragem;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public int getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(int anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public List<OrdemServico> getOrdensServico() {
        return ordensServico;
    }

    public void setOrdensServico(List<OrdemServico> ordensServico) {
        this.ordensServico = ordensServico;
    }

    @Override
    public String toString() {
        return "Veiculo { " +
                "idVeiculo=" + idVeiculo +
                ", chassi='" + chassi + '\'' +
                ", quilometragem=" + quilometragem +
                ", anoFabricacao=" + anoFabricacao +
                ", anoModelo=" + anoModelo +
                ", placa='" + placa + '\'' +
                ", modelo=" + (modelo != null ? modelo.getNome() : "null") +
                '}';
    }
}