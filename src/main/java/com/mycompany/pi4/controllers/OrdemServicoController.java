/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.ItensServico;
import com.mycompany.pi4.entity.OrdemServico;
import com.mycompany.pi4.entity.Peca;
import com.mycompany.pi4.entity.Servico;
import com.mycompany.pi4.entity.Veiculo;
import com.mycompany.pi4.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
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
        String sql = """
            SELECT os.idOS, os.dataInicio, os.dataFim, os.status, os.valorTotal, v.placa
            FROM OrdemServico os
            JOIN Veiculo v ON os.idVeiculo = v.idVeiculo
        """;

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

                // Atribuir a placa do veículo
                Veiculo veiculo = new Veiculo();
                veiculo.setPlaca(rs.getString("placa"));
                os.setVeiculo(veiculo);

                ordens.add(os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordens;
    }

    
    public List<ItensServico> consultarItensServicoPorOrdemServico(int idOS) {
        List<ItensServico> itensServico = new ArrayList<>();
        String sql = """
            SELECT iserv.idItemServico, iserv.quantidade, iserv.precoUnitario, 
                   s.idServico, s.descricao AS servicoDescricao, s.precoUnitario AS precoServico
            FROM ItensServico iserv
            JOIN Servico s ON iserv.idServico = s.idServico
            WHERE iserv.idOS = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ItensServico item = new ItensServico();
                item.setIdItemServico(rs.getInt("idItemServico"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getDouble("precoUnitario"));

                Servico servico = new Servico();
                servico.setIdServico(rs.getInt("idServico"));
                servico.setDescricao(rs.getString("servicoDescricao"));
                servico.setPrecoUnitario(rs.getDouble("precoServico"));
                item.setServico(servico);

                itensServico.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itensServico;
    }


    
    public List<Peca> consultarPecasPorOrdemServico(int idOS) {
        List<Peca> pecas = new ArrayList<>();
        String sql = """
            SELECT ip.idItemPeca, ip.quantidade, ip.precoUnitario, 
                   p.idPeca, p.descricao AS pecaDescricao, p.precoUnitario AS precoPeca
            FROM ItensPeca ip
            JOIN Peca p ON ip.idPeca = p.idPeca
            WHERE ip.idOS = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Peca peca = new Peca();
                peca.setIdPeca(rs.getInt("idPeca"));
                peca.setDescricao(rs.getString("pecaDescricao"));
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
        if (idOS <= 0) {
            System.err.println("ID inválido fornecido: " + idOS);
            return null;
        }

        String sql = """
            SELECT os.idOS, os.dataInicio, os.dataFim, os.status, os.valorTotal, os.idVeiculo,
                   v.placa, c.nome AS clienteNome
            FROM OrdemServico os
            JOIN Veiculo v ON os.idVeiculo = v.idVeiculo
            JOIN Cliente c ON v.idCliente = c.idCliente
            WHERE os.idOS = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, idOS);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OrdemServico os = new OrdemServico();
                    os.setIdOS(rs.getInt("idOS"));
                    os.setDataInicio(rs.getDate("dataInicio"));
                    os.setDataFim(rs.getDate("dataFim"));
                    os.setStatus(rs.getString("status"));
                    os.setValorTotal(rs.getDouble("valorTotal"));

                    // Veículo
                    Veiculo veiculo = new Veiculo();
                    veiculo.setIdVeiculo(rs.getInt("idVeiculo"));
                    veiculo.setPlaca(rs.getString("placa"));
                    os.setVeiculo(veiculo);

                    // Cliente
                    Cliente cliente = new Cliente();
                    cliente.setNome(rs.getString("clienteNome"));
                    os.setCliente(cliente);

                    // Carregar serviços e peças
                    System.out.println("Carregando itens de serviço para a OS: " + idOS);
                    os.setItensServico(consultarItensServicoPorOrdemServico(idOS));

                    System.out.println("Carregando peças para a OS: " + idOS);
                    os.setPecas(consultarPecasPorOrdemServico(idOS));

                    System.out.println("Consulta concluída com sucesso para OS: " + idOS);
                    return os;
                } else {
                    System.err.println("Nenhuma Ordem de Serviço encontrada com o ID: " + idOS);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao consultar a Ordem de Serviço com ID: " + idOS);
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
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void salvarItemServico(int idOS, ItensServico item) {
        if (item.getServico() == null || item.getServico().getIdServico() <= 0) {
            throw new IllegalArgumentException("ID do serviço inválido: " + 
                (item.getServico() != null ? item.getServico().getIdServico() : "null"));
        }

        String sql = "INSERT INTO ItensServico (idOS, idServico, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idOS);
            stmt.setInt(2, item.getServico().getIdServico());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoUnitario());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar item de serviço: " + e.getMessage());
        }
    }



    public void salvarItemPeca(int idOS, Peca peca) {
        String sql = "INSERT INTO ItensPeca (idOS, idPeca, quantidade, precoUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Validação para evitar ID inválido
            if (peca == null || peca.getIdPeca() <= 0) {
                throw new IllegalArgumentException("ID da peça inválido: " + (peca != null ? peca.getIdPeca() : "null"));
            }

            stmt.setInt(1, idOS);
            stmt.setInt(2, peca.getIdPeca());
            stmt.setInt(3, peca.getQuantidade());
            stmt.setDouble(4, peca.getPrecoUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar item de peça: " + e.getMessage());
        }
    }

}