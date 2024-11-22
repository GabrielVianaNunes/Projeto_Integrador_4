/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class PessoaFisica extends Cliente {
    private String cpf;

    // Construtor
    public PessoaFisica(int idCliente, String nome, String telefone, String email, String endereco, String logradouro, String rua, String cep, String setor, String cpf) {
        super(idCliente, nome, telefone, email, endereco, logradouro, rua, cep, setor);
        this.cpf = cpf;
    }

    // Getter e Setter
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Física";
    }
}
