/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class PessoaJuridica extends Cliente {
    private String inscricaoEstadual;
    private String contato;
    
    public PessoaJuridica() {
    }

    public PessoaJuridica(int idCliente, String nome, String telefone, String email, String endereco, String cep, String logradouro, String cnpj, String inscricaoEstadual, String contato) {
        super(idCliente, nome, telefone, email, endereco, cep, logradouro, "PJ", null, cnpj); // "PJ" para tipoCliente e null para cpf
        this.inscricaoEstadual = inscricaoEstadual;
        this.contato = contato;
    }

    // Getters e setters
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

    public String getCnpj() {
        return super.getCnpj();
    }

    public void setCnpj(String cnpj) {
        super.setCnpj(cnpj);
    }
}