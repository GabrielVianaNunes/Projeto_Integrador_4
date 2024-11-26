/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeiculoController {
    private Connection connection; // Conexão com o banco de dados

    // Construtor para inicializar a conexão
    public VeiculoController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar um novo veículo no banco de dados
    public void cadastrarVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (placa, ano, quilometragem, idcliente, idmodelo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getAno());
            stmt.setInt(3, veiculo.getQuilometragem());
            stmt.setInt(4, veiculo.getCliente().getIdCliente()); // Relacionamento com Cliente
            stmt.setInt(5, veiculo.getModelo().getIdModelo());   // Relacionamento com Modelo

            stmt.executeUpdate();
            System.out.println("Veículo cadastrado com sucesso: " + veiculo.getPlaca());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    // Método para buscar veículo por placa no banco de dados
    public Veiculo buscarVeiculoPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculo WHERE placa = ?";
        Veiculo veiculo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veiculo = new Veiculo();
                    veiculo.setIdVeiculo(rs.getInt("idveiculo"));
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setAno(rs.getInt("ano"));
                    veiculo.setQuilometragem(rs.getInt("quilometragem"));
                    // Supondo que Cliente e Modelo sejam buscados separadamente:
                    // veiculo.setCliente(clienteController.buscarClientePorId(rs.getInt("idcliente")));
                    // veiculo.setModelo(modeloController.buscarModeloPorId(rs.getInt("idmodelo")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar veículo: " + e.getMessage());
        }

        return veiculo;
    }

    // Método para listar todos os veículos
    public List<Veiculo> listarTodosVeiculos() {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = "SELECT * FROM veiculo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Veiculo veiculo = new Veiculo();
                veiculo.setIdVeiculo(rs.getInt("idveiculo"));
                veiculo.setPlaca(rs.getString("placa"));
                veiculo.setAno(rs.getInt("ano"));
                veiculo.setQuilometragem(rs.getInt("quilometragem"));
                // Supondo que Cliente e Modelo sejam buscados separadamente:
                // veiculo.setCliente(clienteController.buscarClientePorId(rs.getInt("idcliente")));
                // veiculo.setModelo(modeloController.buscarModeloPorId(rs.getInt("idmodelo")));

                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar veículos: " + e.getMessage());
        }

        return veiculos;
    }
}
