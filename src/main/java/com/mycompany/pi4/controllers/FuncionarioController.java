/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioController {
    private Connection connection; // Conexão com o banco de dados

    public FuncionarioController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar um novo funcionário
    public void cadastrarFuncionario(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.executeUpdate();
            System.out.println("Funcionário cadastrado com sucesso: " + funcionario.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    // Método para listar todos os funcionários
    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("idfuncionario"));
                funcionario.setNome(rs.getString("nome"));
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return funcionarios;
    }

    // Método para buscar funcionário por ID
    public Funcionario buscarFuncionarioPorId(int id) {
        String sql = "SELECT * FROM funcionario WHERE idfuncionario = ?";
        Funcionario funcionario = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario = new Funcionario();
                    funcionario.setIdFuncionario(rs.getInt("idfuncionario"));
                    funcionario.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar funcionário: " + e.getMessage());
        }

        return funcionario;
    }
}
