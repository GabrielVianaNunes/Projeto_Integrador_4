package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.EstoqueController;
import com.mycompany.pi4.entity.Peca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class ConsultaEstoqueView extends JFrame {
    private JTable tabelaEstoque;
    private JButton atualizarButton, adicionarButton, excluirButton, editarButton;

    private EstoqueController estoqueController;

    public ConsultaEstoqueView(EstoqueController estoqueController) {
        this.estoqueController = estoqueController;

        setTitle("Consulta de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configuração da tabela
        String[] colunas = {"ID", "Descrição", "Quantidade", "Preço Unitário"};
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        tabelaEstoque = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);

        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        adicionarButton = new JButton("Adicionar");
        adicionarButton.addActionListener(e -> adicionarPeca());

        editarButton = new JButton("Editar");
        editarButton.addActionListener(e -> editarPeca());

        excluirButton = new JButton("Excluir");
        excluirButton.addActionListener(e -> excluirPeca());

        atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(e -> atualizarTabela());

        buttonPanel.add(adicionarButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(excluirButton);
        buttonPanel.add(atualizarButton);

        // Adiciona os componentes ao JFrame
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Carregar dados iniciais
        atualizarTabela();
    }

    // Atualiza os dados da tabela
    private void atualizarTabela() {
        DefaultTableModel model = (DefaultTableModel) tabelaEstoque.getModel();
        model.setRowCount(0); // Limpa a tabela

        List<Peca> pecas = estoqueController.listarPecas();
        for (Peca peca : pecas) {
            model.addRow(new Object[]{
                peca.getIdPeca(),
                peca.getDescricao(),
                peca.getQuantidade(),
                peca.getPrecoUnitario()
            });
        }

        // Configura o renderizador para alterar a cor das linhas
        tabelaEstoque.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Define a cor de fundo com base na quantidade
                int quantidade = (int) table.getValueAt(row, 2); // Assume que a quantidade está na coluna 2
                if (quantidade < 3) {
                    c.setBackground(new Color(255, 235, 59)); // Amarelo para alerta
                } else {
                    c.setBackground(Color.WHITE); // Cor padrão
                }

                if (isSelected) {
                    c.setBackground(new Color(184, 207, 229)); // Cor de seleção
                }

                return c;
            }
        });
    }



    // Adiciona uma nova peça
    private void adicionarPeca() {
        try {
            String descricao = JOptionPane.showInputDialog(this, "Descrição da peça:");
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição não pode ser vazia.");
            }

            int quantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantidade:"));
            double precoUnitario = Double.parseDouble(JOptionPane.showInputDialog(this, "Preço Unitário:"));

            if (quantidade < 0 || precoUnitario < 0) {
                throw new IllegalArgumentException("Quantidade e preço não podem ser negativos!");
            }

            Peca novaPeca = new Peca(0, descricao, quantidade, precoUnitario); // ID será gerado pelo banco
            estoqueController.adicionarPeca(novaPeca); // Método salva no banco
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Peça adicionada com sucesso!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valores inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar peça: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Edita a peça selecionada
    private void editarPeca() {
        int selectedRow = tabelaEstoque.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma peça para editar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPeca = (int) tabelaEstoque.getValueAt(selectedRow, 0);
        Peca peca = estoqueController.consultarPecaPorId(idPeca);

        if (peca == null) {
            JOptionPane.showMessageDialog(this, "Peça não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novaDescricao = JOptionPane.showInputDialog(this, "Descrição:", peca.getDescricao());
        int novaQuantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantidade:", peca.getQuantidade()));
        double novoPreco = Double.parseDouble(JOptionPane.showInputDialog(this, "Preço Unitário:", peca.getPrecoUnitario()));

        peca.setDescricao(novaDescricao);
        peca.setQuantidade(novaQuantidade);
        peca.setPrecoUnitario(novoPreco);

        estoqueController.atualizarQuantidade(peca.getIdPeca(), novaQuantidade);
        JOptionPane.showMessageDialog(this, "Peça atualizada com sucesso!");
        atualizarTabela();
    }

    // Exclui a peça selecionada
    private void excluirPeca() {
        int selectedRow = tabelaEstoque.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma peça para excluir!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPeca = (int) tabelaEstoque.getValueAt(selectedRow, 0);
        estoqueController.removerPeca(idPeca);
        JOptionPane.showMessageDialog(this, "Peça removida com sucesso!");
        atualizarTabela();
    }

}
