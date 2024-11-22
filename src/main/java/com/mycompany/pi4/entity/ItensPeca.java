/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class ItensPeca {
    private int idItemPeca;
    private int quantidade; // Quantidade utilizada na O.S.
    private double valorTotal; // Calculado: quantidade * preco_unitario
    private double precoUnitario; // Preço unitário da peça

    private Peca peca; // Referência à classe Peca

    // Construtor vazio
    public ItensPeca() {
    }

    // Construtor completo
    public ItensPeca(int idItemPeca, int quantidade, double precoUnitario, Peca peca) {
        this.idItemPeca = idItemPeca;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = quantidade * precoUnitario;
        this.peca = peca;
    }

    // Getters e Setters
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
        this.valorTotal = quantidade * this.precoUnitario; // Recalcula o valor total
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // Não há setter para valorTotal, pois ele é calculado

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
        this.valorTotal = this.quantidade * precoUnitario; // Recalcula o valor total
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    @Override
    public String toString() {
        return "ItensPeca {" +
                "idItemPeca=" + idItemPeca +
                ", quantidade=" + quantidade +
                ", valorTotal=" + valorTotal +
                ", precoUnitario=" + precoUnitario +
                ", peca=" + (peca != null ? peca.getDescricao() : "null") +
                '}';
    }
}
