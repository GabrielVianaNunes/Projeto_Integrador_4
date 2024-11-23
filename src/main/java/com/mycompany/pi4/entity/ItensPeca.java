/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class ItensPeca {

    private int idItemPeca;
    private OrdemServico ordemServico;
    private Peca peca;
    private int quantidade;
    private double precoUnitario;

    public ItensPeca(int idItemPeca, OrdemServico ordemServico, Peca peca, int quantidade, double precoUnitario) {
        this.idItemPeca = idItemPeca;
        this.ordemServico = ordemServico;
        this.peca = peca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e setters
    public int getIdItemPeca() {
        return idItemPeca;
    }

    public void setIdItemPeca(int idItemPeca) {
        this.idItemPeca = idItemPeca;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
