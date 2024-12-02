package com.mycompany.pi4.repositories;

import com.mycompany.pi4.entity.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioRepository {
    private Connection connection;

    public FuncionarioRepository(Connection connection) {
        this.connection = connection;
    }

    // Salvar um novo funcionário no banco de dados
    public void salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (nome) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.executeUpdate();
            System.out.println("Funcionário cadastrado com sucesso: " + funcionario.getNome());
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    // Listar todos os funcionários
    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM funcionario";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("idfuncionario"));
                funcionario.setNome(rs.getString("nome"));
                funcionarios.add(funcionario);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }

        return funcionarios;
    }

    // Buscar um funcionário pelo ID
    public Funcionario buscarPorId(int id) {
        String sql = "SELECT * FROM funcionario WHERE idfuncionario = ?";
        Funcionario funcionario = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario = new Funcionario();
                    funcionario.setIdFuncionario(rs.getInt("idfuncionario"));
                    funcionario.setNome(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionário por ID: " + e.getMessage());
        }

        return funcionario;
    }
}