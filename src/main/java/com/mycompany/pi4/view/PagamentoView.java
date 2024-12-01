package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.OrdemServicoController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PagamentoView extends JFrame {

    private JComboBox<String> formaPagamentoComboBox;
    private JTextField descontoField, numeroParcelasField, diasEntreParcelasField, vencimentoPrimeiraParcelaField;
    private JTable tabelaParcelas;
    private JLabel totalLabel;

    private double totalOriginal;
    private int idOS; // ID da O.S. associada
    private OrdemServicoController ordemServicoController; // Controlador para atualizar a O.S.

    public PagamentoView(double total, int idOS, OrdemServicoController ordemServicoController) {
        this.totalOriginal = total;
        this.idOS = idOS;
        this.ordemServicoController = ordemServicoController;

        setTitle("Pagamento");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada de dados
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Forma de Pagamento"));

        // Formas de pagamento
        inputPanel.add(new JLabel("Forma de Pagamento:"));
        formaPagamentoComboBox = new JComboBox<>(new String[]{"À vista", "Cartão de Crédito", "Cartão de Débito", "Pix", "Boleto"});
        formaPagamentoComboBox.addActionListener(e -> atualizarCampos());
        inputPanel.add(formaPagamentoComboBox);

        // Desconto
        inputPanel.add(new JLabel("Desconto (%):"));
        descontoField = new JTextField();
        descontoField.addActionListener(e -> atualizarTotal());
        inputPanel.add(descontoField);

        // Número de parcelas (somente cartão de crédito)
        inputPanel.add(new JLabel("Número de Parcelas:"));
        numeroParcelasField = new JTextField();
        numeroParcelasField.setEnabled(false);
        inputPanel.add(numeroParcelasField);

        // Dias entre parcelas
        inputPanel.add(new JLabel("Dias entre Parcelas:"));
        diasEntreParcelasField = new JTextField();
        diasEntreParcelasField.setEnabled(false);
        inputPanel.add(diasEntreParcelasField);

        // Data da primeira parcela
        inputPanel.add(new JLabel("Vencimento 1ª Parcela:"));
        vencimentoPrimeiraParcelaField = new JTextField();
        vencimentoPrimeiraParcelaField.setEnabled(false);
        inputPanel.add(vencimentoPrimeiraParcelaField);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Tabela de parcelas
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBorder(BorderFactory.createTitledBorder("Tabela de Parcelas"));

        DefaultTableModel tabelaModel = new DefaultTableModel(new Object[][]{}, new String[]{"Nº Parcela", "Vencimento", "Valor"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaParcelas = new JTable(tabelaModel);
        JScrollPane scrollPane = new JScrollPane(tabelaParcelas);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tabelaPanel, BorderLayout.CENTER);

        // Painel inferior com total e botões
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: R$ " + String.format("%.2f", totalOriginal));
        bottomPanel.add(totalLabel);

        JButton gerarParcelasButton = new JButton("Gerar Parcelas");
        gerarParcelasButton.addActionListener(e -> gerarParcelas(tabelaModel));
        bottomPanel.add(gerarParcelasButton);

        // Botão para confirmar o pagamento
        JButton confirmarPagamentoButton = new JButton("Confirmar Pagamento");
        confirmarPagamentoButton.addActionListener(e -> {
            try {
                // Atualizar o status da O.S. para "Fechada"
                ordemServicoController.atualizarStatus(idOS, "Fechada");
                JOptionPane.showMessageDialog(this, "Pagamento realizado com sucesso!");
                dispose(); // Fecha a janela após o pagamento
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar pagamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        bottomPanel.add(confirmarPagamentoButton);

        JButton fecharButton = new JButton("Cancelar");
        fecharButton.addActionListener(e -> dispose());
        bottomPanel.add(fecharButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void atualizarCampos() {
        String formaPagamento = (String) formaPagamentoComboBox.getSelectedItem();
        boolean habilitarParcelamento = "Cartão de Crédito".equals(formaPagamento);

        numeroParcelasField.setEnabled(habilitarParcelamento);
        diasEntreParcelasField.setEnabled(habilitarParcelamento);
        vencimentoPrimeiraParcelaField.setEnabled(habilitarParcelamento);
    }

    private void atualizarTotal() {
        try {
            double desconto = Double.parseDouble(descontoField.getText());
            double totalComDesconto = totalOriginal - (totalOriginal * desconto / 100);
            totalLabel.setText("Total: R$ " + String.format("%.2f", totalComDesconto));
        } catch (NumberFormatException e) {
            totalLabel.setText("Total: R$ " + String.format("%.2f", totalOriginal));
        }
    }

    private void gerarParcelas(DefaultTableModel tabelaModel) {
        tabelaModel.setRowCount(0);

        try {
            int numParcelas = Integer.parseInt(numeroParcelasField.getText());
            int diasEntreParcelas = Integer.parseInt(diasEntreParcelasField.getText());
            double desconto = Double.parseDouble(descontoField.getText());
            double totalComDesconto = totalOriginal - (totalOriginal * desconto / 100);
            double valorParcela = totalComDesconto / numParcelas;

            for (int i = 1; i <= numParcelas; i++) {
                tabelaModel.addRow(new Object[]{
                        i,
                        calcularVencimento(vencimentoPrimeiraParcelaField.getText(), diasEntreParcelas * (i - 1)),
                        String.format("R$ %.2f", valorParcela)
                });
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preencha corretamente os campos para gerar as parcelas!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String calcularVencimento(String dataInicial, int dias) {
        try {
            LocalDate data = LocalDate.parse(dataInicial, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate dataVencimento = data.plusDays(dias);
            return dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Data inicial inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
            return dataInicial; // Retorna a data original se houver erro.
        }
    }

}
