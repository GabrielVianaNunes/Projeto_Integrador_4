package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;

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
            stmt.setString(8, normalizarCPF(cliente.getCpf()));
            stmt.setString(9, normalizarCNPJ(cliente.getCnpj()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public Cliente buscarPorCpfOuCnpj(String cpfOuCnpj) {
        String sql = "SELECT * FROM cliente WHERE cpf = ? OR cnpj = ?";
        Cliente cliente = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String valorNormalizado = cpfOuCnpj.replaceAll("[^\\d]", "");
            stmt.setString(1, valorNormalizado);
            stmt.setString(2, valorNormalizado);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = criarCliente(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar cliente por CPF ou CNPJ: " + e.getMessage());
        }

        return cliente;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
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
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
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
        if (cpf == null) return null;
        return cpf.replaceAll("[^\\d]", "");
    }

    private String normalizarCNPJ(String cnpj) {
        if (cnpj == null) return null;
        return cnpj.replaceAll("[^\\d]", "");
    }

    private String normalizarCEP(String cep) {
        if (cep == null) return null;
        return cep.replaceAll("[^\\d]", "");
    }
}