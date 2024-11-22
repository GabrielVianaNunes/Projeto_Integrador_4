/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Configurações do banco de dados PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/AutoGyn";
    private static final String USER = "postgres"; // Substitua pelo usuário do seu PostgreSQL
    private static final String PASSWORD = "sua_senha"; // Substitua pela senha do seu PostgreSQL

    // Método para obter conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e; // Repassa a exceção para tratamento posterior
        }
    }
}