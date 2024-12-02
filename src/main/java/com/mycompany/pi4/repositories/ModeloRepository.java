package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Marca;
import com.mycompany.pi4.entity.Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloRepository {
    private Connection connection;

    public ModeloRepository(Connection connection) {
        this.connection = connection;
    }

    // Método para salvar um novo modelo no banco de dados
    public void salvar(Modelo modelo) {
        String sql = "INSERT INTO modelo (nome, idmarca) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, modelo.getNome());
            stmt.setInt(2, modelo.getMarca().getIdMarca());

            stmt.executeUpdate();
            System.out.println("Modelo cadastrado com sucesso: " + modelo.getNome());
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar modelo: " + e.getMessage());
        }
    }

    // Método para buscar um modelo pelo ID
    public Modelo buscarPorId(int id) {
        String sql = "SELECT * FROM modelo WHERE idmodelo = ?";
        Modelo modelo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    modelo = new Modelo();
                    modelo.setIdModelo(rs.getInt("idmodelo"));
                    modelo.setNome(rs.getString("nome"));

                    // Buscar a marca associada ao modelo
                    MarcaRepository marcaRepository = new MarcaRepository(connection);
                    Marca marca = marcaRepository.buscarPorId(rs.getInt("idmarca"));
                    modelo.setMarca(marca);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar modelo por ID: " + e.getMessage());
        }

        return modelo;
    }

    // Método para listar todos os modelos associados a uma marca específica
    public List<Modelo> listarPorMarca(int idMarca) {
        List<Modelo> modelos = new ArrayList<>();
        String sql = "SELECT * FROM modelo WHERE idmarca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMarca);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Modelo modelo = new Modelo();
                    modelo.setIdModelo(rs.getInt("idmodelo"));
                    modelo.setNome(rs.getString("nome"));

                    // Associar a marca ao modelo
                    MarcaRepository marcaRepository = new MarcaRepository(connection);
                    Marca marca = marcaRepository.buscarPorId(idMarca);
                    modelo.setMarca(marca);

                    modelos.add(modelo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar modelos por marca: " + e.getMessage());
        }

        return modelos;
    }

    // Método para listar todos os modelos
    public List<Modelo> listarTodos() {
        List<Modelo> modelos = new ArrayList<>();
        String sql = "SELECT * FROM modelo";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Modelo modelo = new Modelo();
                modelo.setIdModelo(rs.getInt("idmodelo"));
                modelo.setNome(rs.getString("nome"));

                // Buscar a marca associada ao modelo
                MarcaRepository marcaRepository = new MarcaRepository(connection);
                Marca marca = marcaRepository.buscarPorId(rs.getInt("idmarca"));
                modelo.setMarca(marca);

                modelos.add(modelo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os modelos: " + e.getMessage());
        }

        return modelos;
    }
}