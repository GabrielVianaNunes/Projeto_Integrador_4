package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.OrdemServico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RelatorioRepository {
    private final Connection connection;

    public RelatorioRepository(Connection connection) {
        this.connection = connection;
    }

    // Gerar relatório de O.S. concluídas
    public List<OrdemServico> listarOSConcluidas() {
        List<OrdemServico> ordensConcluidas = new ArrayList<>();
        String sql = "SELECT * FROM OrdemServico WHERE status = 'Concluída'";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                os.setIdOS(rs.getInt("idOS"));
                os.setDataInicio(rs.getDate("dataInicio"));
                os.setDataFim(rs.getDate("dataFim"));
                os.setStatus(rs.getString("status"));
                os.setValorTotal(rs.getDouble("valorTotal"));
                // Caso tenha um Veiculo associado, implemente o método para buscar e definir o Veiculo aqui
                ordensConcluidas.add(os);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar O.S. concluídas: " + e.getMessage());
        }

        return ordensConcluidas;
    }

    // Gerar relatório baseado no status especificado
    public List<OrdemServico> listarOSPorStatus(String status) {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM OrdemServico WHERE status = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrdemServico os = new OrdemServico();
                    os.setIdOS(rs.getInt("idOS"));
                    os.setDataInicio(rs.getDate("dataInicio"));
                    os.setDataFim(rs.getDate("dataFim"));
                    os.setStatus(rs.getString("status"));
                    os.setValorTotal(rs.getDouble("valorTotal"));
                    ordens.add(os);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar O.S. por status: " + e.getMessage());
        }

        return ordens;
    }
}