/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;

public class ItensServico {
    private int idItemServico;
    private int quantidade; // Quantidade do serviço realizado
    private double valorTotal; // Calculado: quantidade * precoUnitario
    private double precoUnitario; // Preço unitário do serviço

    private Servico servico; // Relacionamento com a classe Servico

    // Construtor vazio
    public ItensServico() {
    }

    // Construtor completo
    public ItensServico(int idItemServico, int quantidade, double precoUnitario, Servico servico) {
        this.idItemServico = idItemServico;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = quantidade * precoUnitario;
        this.servico = servico;
    }

    // Getters e Setters
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
        this.valorTotal = quantidade * this.precoUnitario; // Recalcula o valor total
    }

    public double getValorTotal() {
        return valorTotal;
    }

    // Não há setter para valorTotal, pois ele é calculado automaticamente

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
        this.valorTotal = this.quantidade * precoUnitario; // Recalcula o valor total
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @Override
    public String toString() {
        return "ItensServico {" +
                "idItemServico=" + idItemServico +
                ", quantidade=" + quantidade +
                ", valorTotal=" + valorTotal +
                ", precoUnitario=" + precoUnitario +
                ", servico=" + (servico != null ? servico.getDescricao() : "null") +
                '}';
    }
}
