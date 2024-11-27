/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pi4;

import com.mycompany.pi4.util.DatabaseConnection;
import com.mycompany.pi4.view.MenuPrincipalView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        // Configuração inicial
        logger.info("Iniciando o sistema AutoGyn...");

        // Testar a conexão com o banco de dados
        Connection connection = DatabaseConnection.getConnection();
        if (connection == null) {
            logger.error("Erro ao conectar ao banco de dados. Verifique as configurações.");
            System.exit(1); // Encerra o programa se o banco não estiver acessível
        } else {
            logger.info("Conexão com o banco de dados bem-sucedida.");
        }

        // Inicializar a interface gráfica
        java.awt.EventQueue.invokeLater(() -> {
            logger.info("Inicializando a interface gráfica...");
            new MenuPrincipalView(connection).setVisible(true); // Passa a conexão para a tela principal
        });
    }
}
