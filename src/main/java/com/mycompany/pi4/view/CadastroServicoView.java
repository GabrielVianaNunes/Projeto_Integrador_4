/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.ServicoController;
import com.mycompany.pi4.entity.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CadastroServicoView extends JFrame {
    private JTable tabelaServicos;
    private JButton adicionarButton, atualizarButton, removerButton;
    private ServicoController servicoController;

    public CadastroServicoView(ServicoController servicoController) {
        this.servicoController = servicoController;

        setTitle("Gerenciamento de Serviços");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabela de serviços
        tabelaServicos = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Descrição", "Preço Unitário"}));
        atualizarTabela();
        JScrollPane scrollPane = new JScrollPane(tabelaServicos);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarServico());
        buttonPanel.add(adicionarButton);

        atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(e -> atualizarServico());
        buttonPanel.add(atualizarButton);

        removerButton = new JButton("Remover");
        removerButton.addActionListener(e -> removerServico());
        buttonPanel.add(removerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tabelaServicos.getModel();
        model.setRowCount(0); // Limpa os dados antigos
        List<Servico> servicos = servicoController.listarServicos();
        for (Servico servico : servicos) {
            model.addRow(new Object[]{servico.getIdServico(), servico.getDescricao(), servico.getPrecoUnitario()});
        }
    }

    private void adicionarServico() {
        try {
            String descricao = JOptionPane.showInputDialog(this, "Digite a descrição do serviço:");
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição não pode ser vazia.");
            }

            double precoUnitario = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o preço unitário:"));
            int idServico = servicoController.listarServicos().size() + 1; // Gera o ID incremental
            Servico novoServico = new Servico(idServico, descricao, precoUnitario);
            servicoController.adicionarServico(novoServico);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Serviço adicionado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar serviço: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarServico() {
        try {
            int linhaSelecionada = tabelaServicos.getSelectedRow();
            if (linhaSelecionada == -1) {
                throw new IllegalArgumentException("Selecione um serviço para atualizar.");
            }

            int idServico = (int) tabelaServicos.getValueAt(linhaSelecionada, 0);
            String novaDescricao = JOptionPane.showInputDialog(this, "Digite a nova descrição do serviço:");
            if (novaDescricao == null || novaDescricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição não pode ser vazia.");
            }

            double novoPreco = Double.parseDouble(JOptionPane.showInputDialog(this, "Digite o novo preço unitário:"));
            servicoController.atualizarServico(idServico, novaDescricao, novoPreco);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Serviço atualizado com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar serviço: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerServico() {
        try {
            int linhaSelecionada = tabelaServicos.getSelectedRow();
            if (linhaSelecionada == -1) {
                throw new IllegalArgumentException("Selecione um serviço para remover.");
            }

            int idServico = (int) tabelaServicos.getValueAt(linhaSelecionada, 0);
            servicoController.removerServico(idServico);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Serviço removido com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover serviço: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
