/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.ItensServico;
import com.mycompany.pi4.entity.OrdemServico;
import com.mycompany.pi4.entity.Peca;
import com.mycompany.pi4.entity.Servico;
import com.mycompany.pi4.entity.Veiculo;
import com.mycompany.pi4.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoController {
    private List<OrdemServico> ordensServico = new ArrayList<>();
    private VeiculoController veiculoController;
    
    public OrdemServicoController(VeiculoController veiculoController) {
        this.veiculoController = veiculoController;
    }


    // Criar uma nova ordem de serviço
    public int criarOrdemServico(OrdemServico os) {
        String sql = "INSERT INTO OrdemServico (dataInicio, dataFim, status, valorTotal, idVeiculo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(os.getDataInicio().getTime()));
            stmt.setDate(2, os.getDataFim() != null ? new java.sql.Date(os.getDataFim().getTime()) : null);
            stmt.setString(3, os.getStatus());
            stmt.setDouble(4, os.getValorTotal());
            stmt.setInt(5, os.getVeiculo().getIdVeiculo());
            stmt.executeUpdate();

            // Retornar o ID gerado
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 caso algo dê errado
    }



    // Atualizar o status de uma ordem de serviço
    public void atualizarStatus(int idOS, String novoStatus) {
        String sql = "UPDATE OrdemServico SET status = ? WHERE idOS = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idOS);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Status atualizado para: " + novoStatus);
            } else {
                System.out.println("Ordem de serviço não encontrada!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<OrdemServico> listarOrdensServico() {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM OrdemServico";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                os.setIdOS(rs.getInt("idOS"));
                os.setDataInicio(rs.getDate("dataInicio"));
                os.setDataFim(rs.getDate("dataFim"));
                os.setStatus(rs.getString("status"));
                os.setValorTotal(rs.getDouble("valorTotal"));

                // Busque o veículo associado
                Veiculo veiculo = veiculoController.consultarVeiculoPorId(rs.getInt("idVeiculo"));
                os.setVeiculo(veiculo);

                ordens.add(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordens;
    }
    
    private List<ItensServico> consultarItensServicoPorOrdemServico(int idOS) {
        List<ItensServico> itensServico = new ArrayList<>();
        String sql = "SELECT * FROM ItensServico WHERE idOS = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItensServico item = new ItensServico();
                item.setIdItemServico(rs.getInt("idItemServico"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getDouble("precoUnitario"));

                // Busque o serviço associado
                int idServico = rs.getInt("idServico");
                Servico servico = new Servico(); // Crie um método para buscar o serviço completo, se necessário
                servico.setIdServico(idServico);
                servico.setDescricao(rs.getString("descricao"));
                servico.setPrecoUnitario(rs.getDouble("precoUnitario"));
                item.setServico(servico);

                itensServico.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itensServico;
    }
    
    private List<Peca> consultarPecasPorOrdemServico(int idOS) {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM ItensPeca WHERE idOS = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Peca peca = new Peca();
                peca.setIdPeca(rs.getInt("idPeca"));
                peca.setDescricao(rs.getString("descricao"));
                peca.setQuantidade(rs.getInt("quantidade"));
                peca.setPrecoUnitario(rs.getDouble("precoUnitario"));

                pecas.add(peca);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pecas;
    }


    public OrdemServico consultarOrdemServicoPorId(int idOS) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM OrdemServico WHERE idOS = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OrdemServico os = new OrdemServico();
                os.setIdOS(rs.getInt("idOS"));
                os.setDataInicio(rs.getDate("dataInicio"));
                os.setDataFim(rs.getDate("dataFim"));
                os.setStatus(rs.getString("status"));
                os.setValorTotal(rs.getDouble("valorTotal"));

                // Busque o veículo associado
                Veiculo veiculo = veiculoController.consultarVeiculoPorId(rs.getInt("idVeiculo"));
                os.setVeiculo(veiculo);

                // Busque os itens de serviço
                List<ItensServico> itensServico = consultarItensServicoPorOrdemServico(idOS);
                os.setItensServico(itensServico);

                // Busque as peças
                List<Peca> pecas = consultarPecasPorOrdemServico(idOS);
                os.setPecas(pecas);

                return os;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void atualizarStatusOrdemServico(int idOS, String novoStatus) {
        String sql = "UPDATE OrdemServico SET status = ? WHERE idOS = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, idOS);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Status da Ordem de Serviço atualizado para: " + novoStatus);
            } else {
                System.out.println("Ordem de Serviço não encontrada!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void atualizarOrdemServico(OrdemServico os) {
        String sql = "UPDATE OrdemServico SET dataInicio = ?, dataFim = ?, status = ?, valorTotal = ?, idVeiculo = ? WHERE idOS = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(os.getDataInicio().getTime()));
            stmt.setDate(2, os.getDataFim() != null ? new java.sql.Date(os.getDataFim().getTime()) : null);
            stmt.setString(3, os.getStatus());
            stmt.setDouble(4, os.getValorTotal());
            stmt.setInt(5, os.getVeiculo().getIdVeiculo());
            stmt.setInt(6, os.getIdOS());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Ordem de Serviço atualizada com sucesso!");
            } else {
                System.out.println("Nenhuma Ordem de Serviço encontrada para o ID " + os.getIdOS());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao atualizar Ordem de Serviço: " + e.getMessage());
        }
    }

}