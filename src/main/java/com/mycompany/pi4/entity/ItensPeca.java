/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

import java.math.BigDecimal;

public class ItensPeca {
    private int idItemPeca;
    private int quantidade;
    private BigDecimal valorTotal;
    private BigDecimal precoUnitario;

    private Peca peca;

    public ItensPeca(int idItemPeca, int quantidade, BigDecimal precoUnitario, Peca peca) {
        this.idItemPeca = idItemPeca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        this.peca = peca;
    }

    public int getIdItemPeca() {
        return idItemPeca;
    }

    public void setIdItemPeca(int idItemPeca) {
        this.idItemPeca = idItemPeca;
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

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }
}