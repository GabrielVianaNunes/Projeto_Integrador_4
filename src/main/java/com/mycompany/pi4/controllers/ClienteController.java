/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private List<Cliente> clientes = new ArrayList<>();

    // Método para cadastrar um novo cliente
    public void cadastrarCliente(Cliente cliente) {
        clientes.add(cliente);
        System.out.println("Cliente cadastrado com sucesso: " + cliente.getNome());
    }

    // Método para buscar cliente por ID
    public Cliente buscarClientePorId(int id) {
        return clientes.stream()
                .filter(cliente -> cliente.getIdCliente() == id)
                .findFirst()
                .orElse(null);
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        return clientes;
    }
}