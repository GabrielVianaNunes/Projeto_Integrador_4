/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Marca;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarcaController {
    private Connection connection; // Conexão com o banco de dados

    // Construtor para inicializar a conexão
    public MarcaController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar uma nova marca
    public void cadastrarMarca(Marca marca) {
        String sql = "INSERT INTO marca (nome) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, marca.getNome());
            stmt.executeUpdate();
            System.out.println("Marca cadastrada com sucesso: " + marca.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar marca: " + e.getMessage());
        }
    }

    // Método para buscar uma marca por ID
    public Marca buscarMarcaPorId(int id) {
        String sql = "SELECT * FROM marca WHERE idmarca = ?";
        Marca marca = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    marca = new Marca();
                    marca.setIdMarca(rs.getInt("idmarca"));
                    marca.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar marca: " + e.getMessage());
        }

        return marca;
    }

    // Método para listar todas as marcas
    public List<Marca> listarMarcas() {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM marca";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Marca marca = new Marca();
                marca.setIdMarca(rs.getInt("idmarca"));
                marca.setNome(rs.getString("nome"));
                marcas.add(marca);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar marcas: " + e.getMessage());
        }

        return marcas;
    }
}

