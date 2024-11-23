/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.util.Date;

public class OrdemServico {

    private int idOS;
    private Date dataInicio;
    private Date dataFim;
    private String status;
    private double valorTotal;
    private Veiculo veiculo;

    public OrdemServico(int idOS, Date dataInicio, Date dataFim, String status, double valorTotal, Veiculo veiculo) {
        this.idOS = idOS;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.valorTotal = valorTotal;
        this.veiculo = veiculo;
    }

    // Getters e setters
    public int getIdOS() {
        return idOS;
    }

    public void setIdOS(int idOS) {
        this.idOS = idOS;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
}
