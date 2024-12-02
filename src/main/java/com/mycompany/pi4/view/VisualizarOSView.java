package com.mycompany.pi4.view;

import com.mycompany.pi4.entity.OrdemServico;
import com.mycompany.pi4.entity.ItensServico;
import com.mycompany.pi4.entity.Peca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VisualizarOSView extends JFrame {
    private OrdemServico ordemServico;
    private List<ItensServico> itensServico;
    private List<Peca> pecas;

    private JTable servicosTable; // Declaração da variável para a tabela de serviços
    private JTable pecasTable; // Declaração da variável para a tabela de peças

    public VisualizarOSView(OrdemServico os, List<ItensServico> itensServico, List<Peca> pecas) {
        this.ordemServico = os;
        this.itensServico = itensServico;
        this.pecas = pecas;

        setTitle("Visualizar Ordem de Serviço");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Informações Gerais
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações Gerais"));

        infoPanel.add(new JLabel("Cliente:"));
        infoPanel.add(new JLabel(ordemServico.getCliente().getNome()));

        infoPanel.add(new JLabel("Veículo:"));
        infoPanel.add(new JLabel(ordemServico.getVeiculo().getPlaca()));

        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(new JLabel(ordemServico.getStatus()));

        infoPanel.add(new JLabel("Data Início:"));
        infoPanel.add(new JLabel(ordemServico.getDataInicio().toString()));

        infoPanel.add(new JLabel("Data Fim:"));
        infoPanel.add(new JLabel(ordemServico.getDataFim() != null ? ordemServico.getDataFim().toString() : "N/A"));

        // Calcular e exibir o valor total da O.S.
        double valorTotal = calcularValorTotal();
        infoPanel.add(new JLabel("Valor Total:"));
        JLabel totalLabel = new JLabel(String.format("R$ %.2f", valorTotal));
        infoPanel.add(totalLabel); // Atualiza o valor total na interface


        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Tabelas de Serviços e Peças
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Tabela de Serviços
        JPanel servicosPanel = new JPanel(new BorderLayout());
        servicosPanel.setBorder(BorderFactory.createTitledBorder("Serviços"));
        servicosTable = new JTable(); // Inicializa a tabela de serviços
        DefaultTableModel servicosModel = new DefaultTableModel(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"});
        servicosTable.setModel(servicosModel);
        preencherTabelaServicos(servicosModel);
        servicosPanel.add(new JScrollPane(servicosTable), BorderLayout.CENTER);

        // Tabela de Peças
        JPanel pecasPanel = new JPanel(new BorderLayout());
        pecasPanel.setBorder(BorderFactory.createTitledBorder("Peças"));
        pecasTable = new JTable(); // Inicializa a tabela de peças
        DefaultTableModel pecasModel = new DefaultTableModel(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"});
        pecasTable.setModel(pecasModel);
        preencherTabelaPecas(pecasModel);
        pecasPanel.add(new JScrollPane(pecasTable), BorderLayout.CENTER);

        tablesPanel.add(servicosPanel);
        tablesPanel.add(pecasPanel);

        mainPanel.add(tablesPanel, BorderLayout.CENTER);

        // Botão Fechar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener(e -> dispose());
        buttonPanel.add(fecharButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Calcular o valor total considerando os itens de serviço e peças
    private double calcularValorTotal() {
        double total = 0;

        // Calcula o total para os itens de serviço
        for (ItensServico item : itensServico) {
            total += item.getQuantidade() * item.getServico().getPrecoUnitario();
        }

        // Calcula o total para as peças
        for (Peca peca : pecas) {
            total += peca.getQuantidade() * peca.getPrecoUnitario();
        }

        return total;
    }

    // Preencher tabela de serviços
    private void preencherTabelaServicos(DefaultTableModel model) {
        if (itensServico != null && !itensServico.isEmpty()) {
            for (ItensServico item : itensServico) {
                model.addRow(new Object[]{
                    item.getServico().getDescricao(),
                    item.getQuantidade(),
                    item.getServico().getPrecoUnitario(),
                    item.getQuantidade() * item.getServico().getPrecoUnitario()
                });
            }
        }
    }

    // Preencher tabela de peças
    private void preencherTabelaPecas(DefaultTableModel model) {
        if (pecas != null && !pecas.isEmpty()) {
            for (Peca peca : pecas) {
                model.addRow(new Object[] {
                    peca.getDescricao(),
                    peca.getQuantidade(),
                    peca.getPrecoUnitario(),
                    peca.getQuantidade() * peca.getPrecoUnitario()
                });
            }
        }
    }
}
