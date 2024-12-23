package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Peca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueRepository {
    private Connection connection;

    public EstoqueRepository(Connection connection) {
        this.connection = connection;
    }

    // Adicionar uma nova peça ao estoque
    public void salvar(Peca peca) {
        String sql = "INSERT INTO Peca (descricao, quantidade, precoUnitario) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, peca.getDescricao());
            stmt.setInt(2, peca.getQuantidade());
            stmt.setDouble(3, peca.getPrecoUnitario());
            stmt.executeUpdate();
            System.out.println("Peça adicionada ao estoque: " + peca.getDescricao());
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar peça: " + e.getMessage());
        }
    }

    // Atualizar a quantidade de uma peça
    public void atualizarQuantidade(int idPeca, int novaQuantidade) {
        String sql = "UPDATE Peca SET quantidade = ? WHERE idPeca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, idPeca);
            stmt.executeUpdate();
            System.out.println("Quantidade atualizada para: " + novaQuantidade);
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar quantidade: " + e.getMessage());
        }
    }

    // Listar todas as peças disponíveis no estoque
    public List<Peca> listarTodos() {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM Peca";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Peca peca = new Peca(
                    rs.getInt("idPeca"),
                    rs.getString("descricao"),
                    rs.getInt("quantidade"),
                    rs.getDouble("precoUnitario")
                );
                pecas.add(peca);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar peças: " + e.getMessage());
        }
        return pecas;
    }

    // Consultar detalhes de uma peça específica
    public Peca consultarPorId(int idPeca) {
        String sql = "SELECT * FROM Peca WHERE idPeca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPeca);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Peca(
                        rs.getInt("idPeca"),
                        rs.getString("descricao"),
                        rs.getInt("quantidade"),
                        rs.getDouble("precoUnitario")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar peça: " + e.getMessage());
        }
        return null;
    }

    // Verificar se há quantidade suficiente de uma peça
    public boolean verificarDisponibilidade(int idPeca, int quantidadeDesejada) {
        Peca peca = consultarPorId(idPeca);
        return peca != null && peca.getQuantidade() >= quantidadeDesejada;
    }

    // Remover uma peça do estoque
    public void remover(int idPeca) {
        String sql = "DELETE FROM Peca WHERE idPeca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPeca);
            stmt.executeUpdate();
            System.out.println("Peça removida com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao remover peça: " + e.getMessage());
        }
    }

    // Editar os detalhes de uma peça
    public void editar(int idPeca, String novaDescricao, int novaQuantidade, double novoPreco) {
        String sql = "UPDATE Peca SET descricao = ?, quantidade = ?, precoUnitario = ? WHERE idPeca = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, novaDescricao);
            stmt.setInt(2, novaQuantidade);
            stmt.setDouble(3, novoPreco);
            stmt.setInt(4, idPeca);
            stmt.executeUpdate();
            System.out.println("Peça atualizada com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao editar peça: " + e.getMessage());
        }
    }
}