/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.entity;


public class PessoaFisica extends Cliente {

    public PessoaFisica(int idCliente, String nome, String telefone, String email, String endereco, String cep, String logradouro, String cpf) {
        super(idCliente, nome, telefone, email, endereco, cep, logradouro, "PF", cpf, null); // "PF" para tipoCliente e null para cnpj
    }

    // Mantemos os métodos para facilitar acessos específicos
    public String getCpf() {
        return super.getCpf();
    }

    public void setCpf(String cpf) {
        super.setCpf(cpf);
    }

    @Override
    public String getTipoCliente() {
        return "Pessoa Física";
    }
}