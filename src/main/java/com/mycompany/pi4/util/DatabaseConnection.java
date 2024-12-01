package com.mycompany.pi4.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {

    // Logger para registrar mensagens
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);

    // Configurações do banco de dados PostgreSQL
    private static final String URL = "sua URL"; // Substitua pelo seu URL
    private static final String USER = "seu usuário"; // Substitua pelo seu usuário
    private static final String PASSWORD = "sua senha"; // Substitua pela sua senha


    /**
     * Método para obter conexão com o banco de dados.
     * @return Um objeto Connection para interação com o banco de dados.
     * @throws SQLException Caso ocorra erro na conexão.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexão com o banco de dados estabelecida com sucesso.");
            return connection;
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados: {}", e.getMessage());
            throw e; // Repassa a exceção para tratamento posterior
        }
    }

    /**
     * Método para testar a conexão com o banco de dados.
     *
     * @return true se a conexão for bem-sucedida, false caso contrário.
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.error("Falha ao testar conexão com o banco: {}", e.getMessage());
            return false;
        }
    }
}
