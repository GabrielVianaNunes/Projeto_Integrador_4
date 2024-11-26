/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class ItensServico {

    private int idItemServico;
    private OrdemServico ordemServico;
    private Servico servico;
    private Funcionario funcionario;
    private int quantidade;
    private double precoUnitario;
    
    public ItensServico() {
    }

    public ItensServico(int idItemServico, OrdemServico ordemServico, Servico servico, Funcionario funcionario, int quantidade, double precoUnitario) {
        this.idItemServico = idItemServico;
        this.ordemServico = ordemServico;
        this.servico = servico;
        this.funcionario = funcionario;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters e setters
    public int getIdItemServico() {
        return idItemServico;
    }

    public void setIdItemServico(int idItemServico) {
        this.idItemServico = idItemServico;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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
