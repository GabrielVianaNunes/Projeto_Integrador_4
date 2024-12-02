package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.ItensServico;
import com.mycompany.pi4.entity.OrdemServico;
import com.mycompany.pi4.entity.Peca;
import com.mycompany.pi4.entity.Servico;
import com.mycompany.pi4.entity.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoRepository {
    private final Connection connection;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;

    public OrdemServicoRepository(Connection connection, VeiculoRepository veiculoRepository, ServicoRepository servicoRepository) {
        this.connection = connection;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
    }

    public int salvar(OrdemServico os) {
        String sql = "INSERT INTO OrdemServico (dataInicio, dataFim, status, valorTotal, idVeiculo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao salvar Ordem de Serviço: " + e.getMessage());
        }
        return -1;
    }

    public void atualizar(OrdemServico os) {
        String sql = "UPDATE OrdemServico SET dataInicio = ?, dataFim = ?, status = ?, valorTotal = ?, idVeiculo = ? WHERE idOS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(os.getDataInicio().getTime()));
            stmt.setDate(2, os.getDataFim() != null ? new java.sql.Date(os.getDataFim().getTime()) : null);
            stmt.setString(3, os.getStatus());
            stmt.setDouble(4, os.getValorTotal());
            stmt.setInt(5, os.getVeiculo().getIdVeiculo());
            stmt.setInt(6, os.getIdOS());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao atualizar Ordem de Serviço: " + e.getMessage());
        }
    }

    public OrdemServico buscarPorId(int idOS) {
        String sql = "SELECT * FROM OrdemServico WHERE idOS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrdemServico os = new OrdemServico();
                os.setIdOS(rs.getInt("idOS"));
                os.setDataInicio(rs.getDate("dataInicio"));
                os.setDataFim(rs.getDate("dataFim"));
                os.setStatus(rs.getString("status"));
                os.setValorTotal(rs.getDouble("valorTotal"));

                Veiculo veiculo = buscarVeiculoPorId(rs.getInt("idVeiculo"));
                os.setVeiculo(veiculo);

                os.setItensServico(consultarItensServicoPorOrdemServico(idOS));
                os.setPecas(consultarPecasPorOrdemServico(idOS));

                return os;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar Ordem de Serviço por ID: " + e.getMessage());
        }
        return null;
    }

    public List<OrdemServico> listarTodas() {
        List<OrdemServico> ordens = new ArrayList<>();
        String sql = "SELECT * FROM OrdemServico";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                os.setIdOS(rs.getInt("idOS"));
                os.setDataInicio(rs.getDate("dataInicio"));
                os.setDataFim(rs.getDate("dataFim"));
                os.setStatus(rs.getString("status"));
                os.setValorTotal(rs.getDouble("valorTotal"));

                Veiculo veiculo = buscarVeiculoPorId(rs.getInt("idVeiculo"));
                os.setVeiculo(veiculo);

                ordens.add(os);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar todas as Ordens de Serviço: " + e.getMessage());
        }
        return ordens;
    }

    private List<ItensServico> consultarItensServicoPorOrdemServico(int idOS) {
        List<ItensServico> itensServico = new ArrayList<>();
        String sql = "SELECT * FROM ItensServico WHERE idOS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idOS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ItensServico item = new ItensServico();
                item.setIdItemServico(rs.getInt("idItemServico"));
                item.setQuantidade(rs.getInt("quantidade"));
                item.setPrecoUnitario(rs.getDouble("precoUnitario"));

                Servico servico = buscarServicoPorId(rs.getInt("idServico"));
                item.setServico(servico);

                itensServico.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao consultar Itens de Serviço: " + e.getMessage());
        }
        return itensServico;
    }

    private List<Peca> consultarPecasPorOrdemServico(int idOS) {
        List<Peca> pecas = new ArrayList<>();
        String sql = "SELECT * FROM ItensPeca WHERE idOS = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao consultar Peças: " + e.getMessage());
        }
        return pecas;
    }

    private Veiculo buscarVeiculoPorId(int idVeiculo) {
        return veiculoRepository.buscarPorId(idVeiculo);
    }

    private Servico buscarServicoPorId(int idServico) {
        return servicoRepository.buscarPorId(idServico);
    }
}