/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class PessoaJuridica extends Cliente {
    private String cnpj;
    private String inscricaoEstadual;
    private String contato;

    // Construtor
    public PessoaJuridica(int idCliente, String nome, String telefone, String email, String endereco, String logradouro, String rua, String cep, String setor, String cnpj, String inscricaoEstadual, String contato) {
        super(idCliente, nome, telefone, email, endereco, logradouro, rua, cep, setor);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.contato = contato;
    }

    // Getters e Setters
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Jurídica";
    }
}
