/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;

public class CadastroOSView extends JFrame {
    private JTextField veiculoField, descricaoField, tecnicoField, statusField, dataField;
    private JTable itensServicoTable, itensPecaTable;
    private JButton adicionarServicoButton, adicionarPecaButton, salvarButton, cancelarButton;

    public CadastroOSView() {
        setTitle("Cadastro de Ordem de Serviço");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel Superior: Informações gerais da O.S.
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações da O.S."));

        infoPanel.add(new JLabel("Veículo (Placa):"));
        veiculoField = new JTextField();
        infoPanel.add(veiculoField);

        infoPanel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        infoPanel.add(descricaoField);

        infoPanel.add(new JLabel("Técnico:"));
        tecnicoField = new JTextField();
        infoPanel.add(tecnicoField);

        infoPanel.add(new JLabel("Status:"));
        statusField = new JTextField("Aberta");
        infoPanel.add(statusField);

        infoPanel.add(new JLabel("Data:"));
        dataField = new JTextField();
        infoPanel.add(dataField);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Painel Central: Tabelas de Itens (Serviços e Peças)
        JPanel itensPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        itensPanel.setBorder(BorderFactory.createTitledBorder("Itens da O.S."));

        // Tabela de Serviços
        JPanel servicoPanel = new JPanel(new BorderLayout(10, 10));
        servicoPanel.setBorder(BorderFactory.createTitledBorder("Serviços"));

        itensServicoTable = new JTable(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"});
        servicoPanel.add(new JScrollPane(itensServicoTable), BorderLayout.CENTER);

        adicionarServicoButton = new JButton("Adicionar Serviço");
        servicoPanel.add(adicionarServicoButton, BorderLayout.SOUTH);

        itensPanel.add(servicoPanel);

        // Tabela de Peças
        JPanel pecaPanel = new JPanel(new BorderLayout(10, 10));
        pecaPanel.setBorder(BorderFactory.createTitledBorder("Peças"));

        itensPecaTable = new JTable(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"});
        pecaPanel.add(new JScrollPane(itensPecaTable), BorderLayout.CENTER);

        adicionarPecaButton = new JButton("Adicionar Peça");
        pecaPanel.add(adicionarPecaButton, BorderLayout.SOUTH);

        itensPanel.add(pecaPanel);

        mainPanel.add(itensPanel, BorderLayout.CENTER);

        // Painel Inferior: Botões de Ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> {
            String veiculo = veiculoField.getText();
            String descricao = descricaoField.getText();
            String tecnico = tecnicoField.getText();
            String status = statusField.getText();
            String data = dataField.getText();
            System.out.println("OS salva: Veículo " + veiculo + ", Descrição " + descricao +
                               ", Técnico " + tecnico + ", Status " + status + ", Data " + data);
            dispose();
        });
        actionPanel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        actionPanel.add(cancelarButton);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

}
