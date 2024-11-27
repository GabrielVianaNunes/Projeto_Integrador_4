package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoController {
    private Connection connection; // Conexão com o banco de dados

    public ServicoController(Connection connection) {
        this.connection = connection;
    }

    // Adicionar um novo serviço
    public void adicionarServico(Servico servico) {
        String sql = "INSERT INTO servico (descricao, precoUnitario) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, servico.getDescricao());
            stmt.setDouble(2, servico.getPrecoUnitario());
            stmt.executeUpdate();

            // Recuperar o ID gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servico.setIdServico(generatedKeys.getInt(1));
                }
            }

            System.out.println("Serviço adicionado: " + servico.getDescricao());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao adicionar serviço: " + e.getMessage());
        }
    }

    // Listar todos os serviços
    public List<Servico> listarServicos() {
        List<Servico> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servico";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Servico servico = new Servico();
                servico.setIdServico(rs.getInt("idServico"));
                servico.setDescricao(rs.getString("descricao"));
                servico.setPrecoUnitario(rs.getDouble("precoUnitario"));

                servicos.add(servico);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar serviços: " + e.getMessage());
        }

        return servicos;
    }

    // Consultar um serviço por ID
    public Servico consultarServicoPorId(int idServico) {
        String sql = "SELECT * FROM servico WHERE idServico = ?";
        Servico servico = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idServico);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    servico = new Servico();
                    servico.setIdServico(rs.getInt("idServico"));
                    servico.setDescricao(rs.getString("descricao"));
                    servico.setPrecoUnitario(rs.getDouble("precoUnitario"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao consultar serviço: " + e.getMessage());
        }

        return servico;
    }

    // Atualizar os dados de um serviço
    public void atualizarServico(int idServico, String novaDescricao, double novoPreco) {
        String sql = "UPDATE servico SET descricao = ?, precoUnitario = ? WHERE idServico = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novaDescricao);
            stmt.setDouble(2, novoPreco);
            stmt.setInt(3, idServico);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Serviço atualizado: " + novaDescricao);
            } else {
                System.out.println("Serviço não encontrado para atualização.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar serviço: " + e.getMessage());
        }
    }

    // Remover um serviço pelo ID
    public void removerServico(int idServico) {
        String sql = "DELETE FROM servico WHERE idServico = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idServico);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Serviço removido com ID: " + idServico);
            } else {
                System.out.println("Serviço não encontrado para remoção.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao remover serviço: " + e.getMessage());
        }
    }
}
