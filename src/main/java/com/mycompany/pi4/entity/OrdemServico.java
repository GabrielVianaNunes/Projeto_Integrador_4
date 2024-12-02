package com.mycompany.pi4.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdemServico {

    private int idOS;
    private Date dataInicio;
    private Date dataFim;
    private String status;
    private double valorTotal;
    private Veiculo veiculo;
    private String descricao;
    private Funcionario funcionario;
    private Cliente cliente; 
    private List<ItensServico> itensServico = new ArrayList<>(); 
    private List<Peca> pecas = new ArrayList<>(); 

    public OrdemServico() {
    }

    public OrdemServico(int idOS, Date dataInicio, Date dataFim, String status, double valorTotal, Veiculo veiculo, List<ItensServico> itensServico, List<Peca> pecas) {
        this.idOS = idOS;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.valorTotal = valorTotal;
        this.veiculo = veiculo;
        this.itensServico = itensServico;
        this.pecas = pecas;
    }

    // Getters e setters
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
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

    public List<ItensServico> getItensServico() {
        return itensServico;
    }

    public void setItensServico(List<ItensServico> itensServico) {
        this.itensServico = itensServico;
    }

    public List<Peca> getPecas() {
        return pecas;
    }

    public void setPecas(List<Peca> pecas) {
        this.pecas = pecas;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
    
}
