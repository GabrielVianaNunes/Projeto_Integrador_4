/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pi4;

import com.mycompany.pi4.util.DatabaseConnection;
import com.mycompany.pi4.view.MenuPrincipalView;
import com.mycompany.pi4.view.CadastroClienteView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Configuração inicial
        logger.info("Iniciando o sistema AutoGyn...");

        // Testar a conexão com o banco de dados
        if (testarConexaoBanco()) {
            logger.info("Conexão com o banco de dados bem-sucedida.");
        } else {
            logger.error("Erro ao conectar ao banco de dados. Verifique as configurações.");
            System.exit(1); // Encerra o programa se o banco não estiver acessível
        }

        // Inicializar a interface gráfica
        java.awt.EventQueue.invokeLater(() -> {
            logger.info("Inicializando a interface gráfica...");
            
            // Opção para abrir a tela principal ou diretamente o CadastroClienteView
            boolean abrirCadastroCliente = true; // Alterne para false para abrir o MenuPrincipalView

            if (abrirCadastroCliente) {
                logger.info("Abrindo a janela de cadastro de cliente...");
                new CadastroClienteView().setVisible(true);
            } else {
                logger.info("Abrindo a janela principal...");
                new MenuPrincipalView().setVisible(true);
            }
        });
    }

    /**
     * Testa a conexão com o banco de dados para garantir que o sistema está funcional.
     * @return true se a conexão for bem-sucedida, false caso contrário.
     */
    private static boolean testarConexaoBanco() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            logger.error("Erro ao testar conexão com o banco de dados: {}", e.getMessage());
            return false;
        }
    }
}
