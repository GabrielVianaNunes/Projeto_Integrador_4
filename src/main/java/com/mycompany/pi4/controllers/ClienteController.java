package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {
    private Connection connection; // Conexão com o banco de dados

    public ClienteController(Connection connection) {
        this.connection = connection;
    }

    // Método para cadastrar um novo cliente
    public void cadastrarCliente(Cliente cliente) {
        // Normalizar os campos CPF, CNPJ e CEP
        String cpfNormalizado = normalizarCPF(cliente.getCpf());
        String cnpjNormalizado = normalizarCNPJ(cliente.getCnpj());
        String cepNormalizado = normalizarCEP(cliente.getCep());

        String sql = "INSERT INTO cliente (nome, telefone, email, endereco, logradouro, cep, tipoCliente, cpf, cnpj) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getLogradouro());
            stmt.setString(6, cepNormalizado); // CEP normalizado
            stmt.setString(7, cliente instanceof PessoaFisica ? "PF" : "PJ");
            stmt.setString(8, cpfNormalizado); // CPF normalizado
            stmt.setString(9, cnpjNormalizado); // CNPJ normalizado

            stmt.executeUpdate();

            // Recuperar o ID gerado pelo banco
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getInt(1));
                    System.out.println("Cliente cadastrado com ID: " + cliente.getIdCliente());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }


    // Método para buscar cliente por CPF ou CNPJ
    public Cliente buscarClienteCPFouCNPJ(String cpfOuCnpj) {
        String sql = "SELECT * FROM cliente WHERE cpf = ? OR cnpj = ?";
        Cliente cliente = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Normalizar o CPF ou CNPJ fornecido
            String valorNormalizado = cpfOuCnpj.replaceAll("[^\\d]", "");
            stmt.setString(1, valorNormalizado); // Comparação com CPF
            stmt.setString(2, valorNormalizado); // Comparação com CNPJ

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar cliente por CPF ou CNPJ: " + e.getMessage());
        }

        return cliente;
    }


    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
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
                cliente.setLogradouro(rs.getString("logradouro")); // Campo logradouro adicionado
                cliente.setCep(rs.getString("cep")); // Campo cep adicionado

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }

    // Método para obter o próximo ID de cliente
    public int obterProximoIdCliente() {
        String sql = "SELECT nextval('cliente_idcliente_seq')"; // Exemplo para PostgreSQL
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao obter o próximo ID de cliente: " + e.getMessage());
        }
        return -1; // Retorna -1 em caso de erro
    }

    // Métodos auxiliares para normalização
    private String normalizarCPF(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^\\d]", ""); // Remove pontos e traços
    }

    private String normalizarCNPJ(String cnpj) {
        if (cnpj == null) return null;
        return cnpj.replaceAll("[^\\d]", ""); // Remove pontos, barras e traços
    }

    private String normalizarCEP(String cep) {
        if (cep == null) return null;
        return cep.replaceAll("[^\\d]", ""); // Remove traços
    }
    
    public List<Cliente> buscarClientesPorNome(String nome) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE LOWER(nome) LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Adiciona o caractere '%' para busca parcial
            stmt.setString(1, "%" + nome.toLowerCase() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
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

                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao buscar clientes por nome: " + e.getMessage());
        }

        return clientes;
    }

}
