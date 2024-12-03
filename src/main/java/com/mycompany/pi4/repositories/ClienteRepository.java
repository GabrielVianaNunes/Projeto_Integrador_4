package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;
import com.mycompany.pi4.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    private Connection connection;

    public ClienteRepository(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, telefone, email, endereco, logradouro, cep, tipoCliente, cpf, cnpj) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getLogradouro());
            stmt.setString(6, normalizarCEP(cliente.getCep()));
            stmt.setString(7, cliente instanceof PessoaFisica ? "PF" : "PJ");
            stmt.setString(8, cliente instanceof PessoaFisica ? normalizarCPF(cliente.getCpf()) : null);
            stmt.setString(9, cliente instanceof PessoaJuridica ? normalizarCNPJ(cliente.getCnpj()) : null);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE idcliente = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarCliente(rs);
                }
            }
        }
        return null;
    }

    public void atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ?, endereco = ?, logradouro = ?, cep = ? WHERE idcliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getLogradouro());
            stmt.setString(6, normalizarCEP(cliente.getCep()));
            stmt.setInt(7, cliente.getIdCliente());

            stmt.executeUpdate();
        }
    }

    public Cliente buscarPorCpfOuCnpj(String cpfOuCnpj) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null || connection.isClosed()) {
            throw new SQLException("A conexão com o banco de dados não está ativa.");
        }

        String sql = "SELECT * FROM cliente WHERE cpf = ? OR cnpj = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpfOuCnpj);
            stmt.setString(2, cpfOuCnpj);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarCliente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por CPF ou CNPJ: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(criarCliente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    public List<Cliente> buscarPorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE LOWER(nome) LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome.toLowerCase() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(criarCliente(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar clientes por nome: " + e.getMessage());
        }

        return clientes;
    }

    public int obterProximoId() {
        String sql = "SELECT nextval('cliente_idcliente_seq')";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao obter o próximo ID de cliente: " + e.getMessage());
        }
        return -1;
    }

    private Cliente criarCliente(ResultSet rs) throws SQLException {
        Cliente cliente = null;
        String tipo = rs.getString("tipoCliente");
        if ("PF".equalsIgnoreCase(tipo)) {
            cliente = new PessoaFisica();
            ((PessoaFisica) cliente).setCpf(rs.getString("cpf"));
        } else if ("PJ".equalsIgnoreCase(tipo)) {
            cliente = new PessoaJuridica();
            ((PessoaJuridica) cliente).setCnpj(rs.getString("cnpj"));
        }

        cliente.setIdCliente(rs.getInt("idcliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setLogradouro(rs.getString("logradouro"));
        cliente.setCep(rs.getString("cep"));

        return cliente;
    }

    private String normalizarCPF(String cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.replaceAll("[^\\d]", "");
    }

    private String normalizarCNPJ(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.replaceAll("[^\\d]", "");
    }

    private String normalizarCEP(String cep) {
        if (cep == null) {
            return null;
        }
        return cep.replaceAll("[^\\d]", "");
    }

    public void excluir(int idCliente) {
        String sql = "DELETE FROM cliente WHERE idcliente = ?";

        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cliente com ID " + idCliente + " excluído com sucesso.");
            } else {
                System.out.println("Nenhum cliente encontrado com o ID " + idCliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }
}
