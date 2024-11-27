/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CadastroClienteView extends JFrame {

    private JTextField idClienteField, nomeField, telefoneField, emailField, enderecoField, cepField, logradouroField, cpfField, cnpjField;
    private JComboBox<String> tipoClienteComboBox;
    private JButton salvarButton, cancelarButton, limparButton;

    public CadastroClienteView() {
        setTitle("Cadastro de Cliente");
        setSize(500, 600);  // Aumentei o tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com layout compacto
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245)); // Cor de fundo suave

        // Título
        JLabel titulo = new JLabel("Cadastro de Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));  // Tamanho da fonte maior
        titulo.setForeground(new Color(0, 102, 204));  // Cor do título
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Painel central com formulário
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // 10 linhas, 2 colunas
        formPanel.setBackground(new Color(245, 245, 245));

        formPanel.add(new JLabel("ID Cliente:"));
        idClienteField = new JTextField();
        idClienteField.setText(gerarIdCliente());
        idClienteField.setEditable(false);
        formPanel.add(idClienteField);

        formPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        formPanel.add(nomeField);

        formPanel.add(new JLabel("Tipo Cliente:"));
        tipoClienteComboBox = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        tipoClienteComboBox.addItemListener(this::onTipoClienteChange);
        formPanel.add(tipoClienteComboBox);

        formPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        cpfField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCPF();
            }
        });
        formPanel.add(cpfField);

        formPanel.add(new JLabel("CNPJ:"));
        cnpjField = new JTextField();
        cnpjField.setEnabled(false);
        cnpjField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCNPJ();
            }
        });
        formPanel.add(cnpjField);

        formPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        telefoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                ajustarBackspaceDelete(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = telefoneField.getText().replaceAll("[^\\d]", "");
                String formatado = formatarTexto(texto);
                telefoneField.setText(formatado);
                telefoneField.setCaretPosition(formatado.length());
            }
        });
        formPanel.add(telefoneField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        formPanel.add(enderecoField);

        formPanel.add(new JLabel("CEP:"));
        cepField = new JTextField();
        cepField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarCEP();
            }
        });
        formPanel.add(cepField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Painel inferior com botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245)); // Cor de fundo dos botões

        salvarButton = new JButton("Salvar");
        salvarButton.setBackground(new Color(0, 102, 204)); // Cor de fundo azul
        salvarButton.setForeground(Color.WHITE); // Cor da fonte
        salvarButton.addActionListener(this::onSalvar);
        buttonPanel.add(salvarButton);

        limparButton = new JButton("Limpar");
        limparButton.setBackground(new Color(0, 153, 0)); // Cor de fundo verde
        limparButton.setForeground(Color.WHITE);
        limparButton.addActionListener(this::onLimpar);
        buttonPanel.add(limparButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(new Color(204, 0, 0)); // Cor de fundo vermelho
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adicionar painel principal à janela
        add(mainPanel);
    }

    private String gerarIdCliente() {
        int idAleatorio = (int) (Math.random() * 10000);
        return "#" + String.format("%04d", idAleatorio);
    }

    private void onTipoClienteChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            String tipoSelecionado = (String) tipoClienteComboBox.getSelectedItem();
            if ("Pessoa Física".equals(tipoSelecionado)) {
                cpfField.setEnabled(true);
                cnpjField.setEnabled(false);
                cnpjField.setText("");
            } else if ("Pessoa Jurídica".equals(tipoSelecionado)) {
                cpfField.setEnabled(false);
                cnpjField.setEnabled(true);
                cpfField.setText("");
            }
        }
    }

    private void formatarCPF() {
        String texto = cpfField.getText().replaceAll("[^\\d]", "");
        if (texto.length() > 11) {
            texto = texto.substring(0, 11);
        }
        StringBuilder sb = new StringBuilder(texto);

        if (texto.length() > 9) {
            sb.insert(3, '.').insert(7, '.').insert(11, '-');
        } else if (texto.length() > 6) {
            sb.insert(3, '.').insert(7, '.');
        } else if (texto.length() > 3) {
            sb.insert(3, '.');
        }

        if (!sb.toString().equals(cpfField.getText())) {
            cpfField.setText(sb.toString());
        }
    }

    private void formatarCNPJ() {
        String texto = cnpjField.getText().replaceAll("[^\\d]", "");

        if (texto.length() > 14) {
            texto = texto.substring(0, 14);
        }

        StringBuilder sb = new StringBuilder(texto);

        if (texto.length() > 12) {
            sb.insert(12, '-');
        }
        if (texto.length() > 8) {
            sb.insert(8, '/');
        }
        if (texto.length() > 6) {
            sb.insert(6, '.');
        }
        if (texto.length() > 2) {
            sb.insert(2, '.');
        }

        if (!sb.toString().equals(cnpjField.getText())) {
            cnpjField.setText(sb.toString());
            cnpjField.setCaretPosition(sb.length());
        }
    }

    private void formatarCEP() {
        String texto = cepField.getText().replaceAll("[^\\d]", "");

        if (texto.length() > 8) {
            texto = texto.substring(0, 8);
        }

        StringBuilder sb = new StringBuilder(texto);
        if (texto.length() > 5) {
            sb.insert(5, '-');
        }

        if (!sb.toString().equals(cepField.getText())) {
            cepField.setText(sb.toString());
            cepField.setCaretPosition(sb.length());
        }
    }

    private void ajustarBackspaceDelete(KeyEvent e) {
        String textoAtual = telefoneField.getText();
        int posicaoCursor = telefoneField.getCaretPosition();

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && posicaoCursor > 0) {
            StringBuilder sb = new StringBuilder(textoAtual);
            sb.deleteCharAt(posicaoCursor - 1);
            textoAtual = sb.toString();
            posicaoCursor--;
        } else if (e.getKeyCode() == KeyEvent.VK_DELETE && posicaoCursor < textoAtual.length()) {
            StringBuilder sb = new StringBuilder(textoAtual);
            sb.deleteCharAt(posicaoCursor);
            textoAtual = sb.toString();
        }

        telefoneField.setText(textoAtual);
        telefoneField.setCaretPosition(posicaoCursor);
    }

    private String formatarTexto(String texto) {
        StringBuilder sb = new StringBuilder();
        if (texto.length() > 0) sb.append("(").append(texto.substring(0, Math.min(texto.length(), 2))).append(") ");
        if (texto.length() > 2) sb.append(texto.substring(2, Math.min(texto.length(), 7))).append(" ");
        if (texto.length() > 7) sb.append(texto.substring(7, Math.min(texto.length(), 11)));
        if (texto.length() > 11) sb.append("-").append(texto.substring(11, Math.min(texto.length(), 15)));
        return sb.toString();
    }

    private void onLimpar(ActionEvent e) {
        idClienteField.setText(gerarIdCliente());
        nomeField.setText("");
        tipoClienteComboBox.setSelectedIndex(0);
        cpfField.setText("");
        cnpjField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        cepField.setText("");
    }

    private void onSalvar(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
    }
}