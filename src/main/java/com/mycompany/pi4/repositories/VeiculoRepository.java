package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.Marca;
import com.mycompany.pi4.entity.Modelo;
import com.mycompany.pi4.entity.Veiculo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoRepository {
    private final Connection connection;

    public VeiculoRepository(Connection connection) {
        this.connection = connection;
    }

    // Método para salvar um novo veículo
    public void salvar(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (placa, ano, quilometragem, idcliente, idmodelo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getAno());
            stmt.setInt(3, veiculo.getQuilometragem());
            stmt.setInt(4, veiculo.getCliente().getIdCliente());
            stmt.setInt(5, veiculo.getModelo().getIdModelo());

            stmt.executeUpdate();
            System.out.println("Veículo cadastrado com sucesso: " + veiculo.getPlaca());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao salvar veículo: " + e.getMessage());
        }
    }

    // Método para buscar veículo por placa
    public Veiculo buscarPorPlaca(String placa) {
        String sql = """
            SELECT v.*, c.nome AS nomeCliente, m.nome AS nomeModelo
            FROM Veiculo v
            JOIN Cliente c ON v.idCliente = c.idCliente
            JOIN Modelo m ON v.idModelo = m.idModelo
            WHERE v.placa = ?
        """;
        Veiculo veiculo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veiculo = montarVeiculo(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar veículo por placa: " + e.getMessage());
        }

        return veiculo;
    }

    // Método para buscar veículo por ID
    public Veiculo buscarPorId(int idVeiculo) {
        String sql = """
            SELECT v.*, c.nome AS nomeCliente, m.nome AS nomeModelo
            FROM Veiculo v
            JOIN Cliente c ON v.idCliente = c.idCliente
            JOIN Modelo m ON v.idModelo = m.idModelo
            WHERE v.idVeiculo = ?
        """;
        Veiculo veiculo = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idVeiculo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    veiculo = montarVeiculo(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar veículo por ID: " + e.getMessage());
        }

        return veiculo;
    }

    // Método para listar todos os veículos
    public List<Veiculo> listarTodos() {
        List<Veiculo> veiculos = new ArrayList<>();
        String sql = """
            SELECT v.*, c.nome AS nomeCliente, m.nome AS nomeModelo
            FROM Veiculo v
            JOIN Cliente c ON v.idCliente = c.idCliente
            JOIN Modelo m ON v.idModelo = m.idModelo
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                veiculos.add(montarVeiculo(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao listar veículos: " + e.getMessage());
        }

        return veiculos;
    }

    // Método auxiliar para montar o objeto Veículo a partir do ResultSet
    private Veiculo montarVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(rs.getInt("idVeiculo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setQuilometragem(rs.getInt("quilometragem"));

        // Cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNome(rs.getString("nomeCliente"));
        veiculo.setCliente(cliente);

        // Modelo
        Modelo modelo = new Modelo();
        modelo.setIdModelo(rs.getInt("idModelo"));
        modelo.setNome(rs.getString("nomeModelo"));

        // Marca
        Marca marca = new Marca();
        if (rs.getString("nomeMarca") != null) {
            marca.setIdMarca(rs.getInt("idMarca"));
            marca.setNome(rs.getString("nomeMarca"));
        }
        modelo.setMarca(marca);

        veiculo.setModelo(modelo);

        return veiculo;
    }

    
    // Método para buscar veículos por cliente
    public List<Veiculo> buscarPorCliente(int idCliente) {
        String sql = """
            SELECT v.*, c.nome AS nomeCliente, m.nome AS nomeModelo, ma.idMarca, ma.nome AS nomeMarca
            FROM Veiculo v
            JOIN Cliente c ON v.idCliente = c.idCliente
            JOIN Modelo m ON v.idModelo = m.idModelo
            LEFT JOIN Marca ma ON m.idMarca = ma.idMarca -- LEFT JOIN para incluir modelos sem marca
            WHERE v.idCliente = ?
        """;
        List<Veiculo> veiculos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    veiculos.add(montarVeiculo(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao buscar veículos por cliente: " + e.getMessage());
        }

        return veiculos;
    }

    
    // Método para excluir veículo por ID
    public void excluirPorId(int idVeiculo) {
        String sql = "DELETE FROM veiculo WHERE idVeiculo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idVeiculo);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Veículo excluído com sucesso. ID: " + idVeiculo);
            } else {
                System.err.println("Nenhum veículo encontrado com o ID: " + idVeiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao excluir veículo: " + e.getMessage());
        }
    }
    
    public void excluirPorPlaca(String placa) {
        String sql = "DELETE FROM veiculo WHERE placa = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, placa);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Veículo excluído com sucesso. Placa: " + placa);
            } else {
                System.err.println("Nenhum veículo encontrado com a placa: " + placa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao excluir veículo por placa: " + e.getMessage());
        }
    }

    
    // Método para editar os dados de um veículo
    public void atualizar(Veiculo veiculo) {
        String sql = "UPDATE veiculo SET placa = ?, ano = ?, quilometragem = ?, idModelo = ? WHERE idVeiculo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getAno());
            stmt.setInt(3, veiculo.getQuilometragem());
            stmt.setInt(4, veiculo.getModelo().getIdModelo());
            stmt.setInt(5, veiculo.getIdVeiculo());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Veículo atualizado com sucesso. ID: " + veiculo.getIdVeiculo());
            } else {
                System.err.println("Nenhum veículo encontrado para atualizar. ID: " + veiculo.getIdVeiculo());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao atualizar veículo: " + e.getMessage());
        }
    }

}