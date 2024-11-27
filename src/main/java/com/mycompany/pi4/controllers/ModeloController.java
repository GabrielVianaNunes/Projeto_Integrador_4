/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Marca;
import com.mycompany.pi4.entity.Modelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloController {
    private Connection connection; // Conexão com o banco de dados

    public ModeloController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar um novo modelo
    public void cadastrarModelo(Modelo modelo) {
        String sql = "INSERT INTO modelo (nome, idmarca) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo.getNome());
            stmt.setInt(2, modelo.getMarca().getIdMarca());

            stmt.executeUpdate();
            System.out.println("Modelo cadastrado com sucesso: " + modelo.getNome());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar modelo: " + e.getMessage());
        }
    }

    // Método para buscar modelo por ID
    public Modelo buscarModeloPorId(int id) {
        String sql = "SELECT * FROM modelo WHERE idmodelo = ?";
        Modelo modelo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    modelo = new Modelo(); // Utilize a variável já declarada fora do bloco
                    modelo.setIdModelo(rs.getInt("idmodelo"));
                    modelo.setNome(rs.getString("nome"));
                    // Integração com MarcaController
                    MarcaController marcaController = new MarcaController(connection); 
                    modelo.setMarca(marcaController.buscarMarcaPorId(rs.getInt("idmarca")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar modelo: " + e.getMessage());
        }

        return modelo;
    }
    
    // Método para listar todos os modelos associados a uma marca específica
    public List<Modelo> listarModelosPorMarca(int idMarca) {
        List<Modelo> modelos = new ArrayList<>();
        String sql = "SELECT * FROM modelo WHERE idmarca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMarca);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Modelo modelo = new Modelo();
                    modelo.setIdModelo(rs.getInt("idmodelo"));
                    modelo.setNome(rs.getString("nome"));

                    // Associa a marca ao modelo
                    MarcaController marcaController = new MarcaController(connection);
                    modelo.setMarca(marcaController.buscarMarcaPorId(idMarca));

                    modelos.add(modelo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar modelos por marca: " + e.getMessage());
        }

        return modelos;
    }



    // Método para listar todos os modelos
    public List<Modelo> listarModelos() {
        List<Modelo> modelos = new ArrayList<>();
        String sql = "SELECT * FROM modelo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Modelo modelo = new Modelo();
                modelo.setIdModelo(rs.getInt("idmodelo"));
                modelo.setNome(rs.getString("nome"));
                // Supondo que o controlador de Marca seja usado:
                // modelo.setMarca(marcaController.buscarMarcaPorId(rs.getInt("idmarca")));

                modelos.add(modelo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar modelos: " + e.getMessage());
        }

        return modelos;
    }
}

