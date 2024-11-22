/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrdemServico {
    private int idOS;
    private String status;
    private Date dataInicio;
    private Veiculo veiculo;
    private List<ItensServico> itensServico;
    private List<ItensPeca> itensPeca;

    public OrdemServico(int idOS, String status, Date dataInicio, Veiculo veiculo, List<ItensServico> itensServico, List<ItensPeca> itensPeca) {
        this.idOS = idOS;
        this.status = status;
        this.dataInicio = dataInicio;
        this.veiculo = veiculo;
        this.itensServico = itensServico;
        this.itensPeca = itensPeca;
    }

    public int getIdOS() {
        return idOS;
    }

    public void setIdOS(int idOS) {
        this.idOS = idOS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItensServico> getItensServico() {
        return itensServico;
    }

    public void setItensServico(List<ItensServico> itensServico) {
        this.itensServico = itensServico;
    }

    public List<ItensPeca> getItensPeca() {
        return itensPeca;
    }

    public void setItensPeca(List<ItensPeca> itensPeca) {
        this.itensPeca = itensPeca;
    }

    public BigDecimal getValorTotal() {
        BigDecimal totalServico = itensServico.stream()
                .map(ItensServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPeca = itensPeca.stream()
                .map(ItensPeca::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalServico.add(totalPeca);
    }
}