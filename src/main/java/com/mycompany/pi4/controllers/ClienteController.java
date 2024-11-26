/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private Connection connection; // Conexão com o banco de dados

    public ClienteController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar um novo cliente
    public void cadastrarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, telefone, email, endereco, tipoCliente) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente instanceof PessoaFisica ? "PF" : "PJ");

            stmt.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso: " + cliente.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    // Método para buscar cliente por ID
    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT * FROM cliente WHERE idcliente = ?";
        Cliente cliente = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipoCliente");
                    if ("PF".equalsIgnoreCase(tipo)) {
                        cliente = new PessoaFisica();
                        ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));
                    } else if ("PJ".equalsIgnoreCase(tipo)) {
                        cliente = new PessoaJuridica();
                        ((PessoaJuridica) cliente).setCnpj(rs.getString("cnpj"));
                    }

                    cliente.setIdCliente(rs.getInt("idcliente"));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setEndereco(rs.getString("endereco"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }

        return cliente;
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = null;
                String tipo = rs.getString("tipoCliente");
                if ("PF".equalsIgnoreCase(tipo)) {
                    cliente = new PessoaFisica();
                    ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));
                } else if ("PJ".equalsIgnoreCase(tipo)) {
                    cliente = new PessoaJuridica();
                    ((PessoaJuridica) cliente).setCnpj(rs.getString("cnpj"));
                }

                cliente.setIdCliente(rs.getInt("idcliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEndereco(rs.getString("endereco"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }
}
