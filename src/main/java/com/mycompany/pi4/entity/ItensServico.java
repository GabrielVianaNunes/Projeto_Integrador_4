/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.math.BigDecimal;

public class ItensServico {
    private int idItemServico;
    private int quantidade;
    private BigDecimal valorTotal;
    private BigDecimal precoUnitario;

    private Servico servico;

    public ItensServico(int idItemServico, int quantidade, BigDecimal precoUnitario, Servico servico) {
        this.idItemServico = idItemServico;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        this.servico = servico;
    }

    public int getIdItemServico() {
        return idItemServico;
    }

    public void setIdItemServico(int idItemServico) {
        this.idItemServico = idItemServico;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = this.precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        this.valorTotal = precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }
}