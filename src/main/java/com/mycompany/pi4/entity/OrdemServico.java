/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.util.Date;
import java.util.List;

public class OrdemServico {
    private int idOS;
    private String status; // Status da O.S. (Aberta, Concluída, Cancelada, etc.)
    private Date dataInicio;

    private Veiculo veiculo;           
    private List<ItensServico> itensServico; 
    private List<ItensPeca> itensPeca;

    // Construtor vazio
    public OrdemServico() {
    }

    // Construtor completo
    public OrdemServico(int idOS, String status, Date dataInicio, Veiculo veiculo, List<ItensServico> itensServico, List<ItensPeca> itensPeca) {
        this.idOS = idOS;
        this.status = status;
        this.dataInicio = dataInicio;
        this.veiculo = veiculo;
        this.itensServico = itensServico;
        this.itensPeca = itensPeca;
    }

    // Getters e Setters
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

    // Calcula dinamicamente a quantidade de serviços
    public int getQuantidadeServico() {
        return itensServico != null ? itensServico.size() : 0;
    }

    // Calcula dinamicamente a quantidade de itens
    public int getQuantidadeItens() {
        return itensPeca != null ? itensPeca.size() : 0;
    }

    // Calcula dinamicamente o valor total da O.S.
    public double getValorTotal() {
        double totalServico = itensServico != null
                ? itensServico.stream().mapToDouble(ItensServico::getValorTotal).sum()
                : 0;
        double totalPeca = itensPeca != null
                ? itensPeca.stream().mapToDouble(ItensPeca::getValorTotal).sum()
                : 0;
        return totalServico + totalPeca;
    }

    @Override
    public String toString() {
        return "OrdemServico {" +
                "idOS=" + idOS +
                ", quantidadeServico=" + getQuantidadeServico() +
                ", quantidadeItens=" + getQuantidadeItens() +
                ", status='" + status + '\'' +
                ", dataInicio=" + dataInicio +
                ", valorTotal=" + getValorTotal() +
                ", veiculo=" + (veiculo != null ? veiculo.getPlaca() : "null") +
                '}';
    }
}
