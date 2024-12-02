package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.Marca;
import com.mycompany.pi4.entity.Modelo;
import com.mycompany.pi4.entity.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeiculoController {
    private Connection connection; // Conexão com o banco de dados
    private ClienteController clienteController;
    private ModeloController modeloController;

    // Construtor para inicializar a conexão e os controladores
    public VeiculoController(Connection connection, ClienteController clienteController, ModeloController modeloController) {
        this.connection = connection;
        this.clienteController = clienteController;
        this.modeloController = modeloController;
    }

    // Método para cadastrar um novo veículo no banco de dados
    public void cadastrarVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO veiculo (placa, ano, quilometragem, idcliente, idmodelo) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, veiculo.getPlaca());
            stmt.setInt(2, veiculo.getAno());
            stmt.setInt(3, veiculo.getQuilometragem());
            stmt.setInt(4, veiculo.getCliente().getIdCliente()); // Relacionamento com Cliente
            stmt.setInt(5, veiculo.getModelo().getIdModelo());   // Relacionamento com Modelo

            stmt.executeUpdate();
            System.out.println("Veículo cadastrado com sucesso: " + veiculo.getPlaca());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar veículo: " + e.getMessage());
        }
    }

    // Método para buscar veículo por placa no banco de dados
    public Veiculo buscarVeiculoPorPlaca(String placa) {
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
                    veiculo = new Veiculo();
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
                    veiculo.setModelo(modelo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar veículo: " + e.getMessage());
        }

        return veiculo;
    }

    // Método para listar todos os veículos
    public List<Veiculo> listarTodosVeiculos() {
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
                veiculo.setModelo(modelo);

                veiculos.add(veiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar veículos: " + e.getMessage());
        }

        return veiculos;
    }

    // Método para buscar veículo por ID no banco de dados
    public Veiculo consultarVeiculoPorId(int idVeiculo) {
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
                    veiculo = new Veiculo();
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
                    veiculo.setModelo(modelo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar veículo por ID: " + e.getMessage());
        }

        return veiculo;
    }
    
    public List<Veiculo> buscarVeiculosPorCliente(int idCliente) {
        String sql = """
            SELECT v.idVeiculo, v.placa, v.ano, v.quilometragem, 
                   m.idModelo, m.nome AS modelo_nome, 
                   ma.idMarca, ma.nome AS marca_nome
            FROM veiculo v
            INNER JOIN modelo m ON v.idModelo = m.idModelo
            INNER JOIN marca ma ON m.idMarca = ma.idMarca
            WHERE v.idCliente = ?
        """;

        List<Veiculo> veiculos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Instancia o veículo
                    Veiculo veiculo = new Veiculo();
                    veiculo.setIdVeiculo(rs.getInt("idVeiculo"));
                    veiculo.setPlaca(rs.getString("placa"));
                    veiculo.setAno(rs.getInt("ano"));
                    veiculo.setQuilometragem(rs.getInt("quilometragem"));

                    // Instancia o modelo
                    Modelo modelo = new Modelo();
                    modelo.setIdModelo(rs.getInt("idModelo"));
                    modelo.setNome(rs.getString("modelo_nome"));

                    // Instancia a marca e associa ao modelo
                    Marca marca = new Marca();
                    marca.setIdMarca(rs.getInt("idMarca"));
                    marca.setNome(rs.getString("marca_nome"));
                    modelo.setMarca(marca);

                    // Associa o modelo ao veículo
                    veiculo.setModelo(modelo);

                    veiculos.add(veiculo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar veículos do cliente: " + e.getMessage());
        }

        return veiculos;
    }


}
