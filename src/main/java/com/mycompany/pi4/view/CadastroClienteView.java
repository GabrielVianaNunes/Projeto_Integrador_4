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
    private JButton salvarButton, cancelarButton;

    public CadastroClienteView() {
        setTitle("Cadastro de Cliente");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com layout compacto
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel central com formulário
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 5, 5)); // 9 linhas, 2 colunas
        formPanel.add(new JLabel("ID Cliente:"));
        idClienteField = new JTextField();
        idClienteField.setText(gerarIdCliente()); // Gerar automaticamente com o formato
        idClienteField.setEditable(false); // ID não editável
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
        cnpjField.setEnabled(false); // CNPJ desabilitado por padrão
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
                ajustarBackspaceDelete(e); // Ajusta comportamento do backspace e delete
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String texto = telefoneField.getText().replaceAll("[^\\d]", ""); // Remove tudo que não for número
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(this::onSalvar);
        buttonPanel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Adicionar painel principal à janela
        add(mainPanel);
    }

    private String gerarIdCliente() {
        int idAleatorio = (int) (Math.random() * 10000); // ID aleatório (entre 0 e 9999)
        return "#" + String.format("%04d", idAleatorio); // Formata o ID para #XXXX
    }

    private void onTipoClienteChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            String tipoSelecionado = (String) tipoClienteComboBox.getSelectedItem();
            if ("Pessoa Física".equals(tipoSelecionado)) {
                cpfField.setEnabled(true);
                cnpjField.setEnabled(false);
                cnpjField.setText(""); // Limpa o campo
            } else if ("Pessoa Jurídica".equals(tipoSelecionado)) {
                cpfField.setEnabled(false);
                cnpjField.setEnabled(true);
                cpfField.setText(""); // Limpa o campo
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
        // Captura o texto atual e a posição do cursor
        String textoAtual = telefoneField.getText();
        int posicaoCursor = telefoneField.getCaretPosition();

        // Tratamento do backspace
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && posicaoCursor > 0) {
            // Remove o caractere antes do cursor, ignorando a formatação
            StringBuilder sb = new StringBuilder(textoAtual);
            sb.deleteCharAt(posicaoCursor - 1);
            textoAtual = sb.toString();
            posicaoCursor--;
        } // Tratamento do delete
        else if (e.getKeyCode() == KeyEvent.VK_DELETE && posicaoCursor < textoAtual.length()) {
            // Remove o caractere na posição do cursor, ignorando a formatação
            StringBuilder sb = new StringBuilder(textoAtual);
            sb.deleteCharAt(posicaoCursor);
            textoAtual = sb.toString();
        }

        // Remove caracteres de formatação para recalcular a posição
        String textoNumerico = textoAtual.replaceAll("[^\\d]", "");

        // Atualiza o texto formatado no campo
        telefoneField.setText(formatarTexto(textoNumerico));

        // Ajusta a posição do cursor no texto formatado
        telefoneField.setCaretPosition(Math.max(0, Math.min(posicaoCursor, telefoneField.getText().length())));
    }

    private String formatarTexto(String texto) {
        StringBuilder sb = new StringBuilder(texto);

        // Adiciona o parêntese no DDD
        if (texto.length() > 2) {
            sb.insert(0, "(").insert(3, ")");
        }
        // Adiciona o hífen no número
        if (texto.length() > 6) {
            sb.insert(9, '-');
        } else if (texto.length() > 2) {
            sb.insert(6, '-');
        }

        return sb.toString();
    }

    private void onSalvar(ActionEvent event) {
        try {
            String idCliente = idClienteField.getText().trim();
            String nome = nomeField.getText().trim();
            String tipoCliente = (String) tipoClienteComboBox.getSelectedItem();
            String cpf = cpfField.isEnabled() ? cpfField.getText().trim() : null;
            String cnpj = cnpjField.isEnabled() ? cnpjField.getText().trim() : null;
            String telefone = telefoneField.getText().trim();
            String email = emailField.getText().trim();
            String endereco = enderecoField.getText().trim();
            String cep = cepField.getText().trim();

            if (nome.isEmpty() || (cpf == null && cnpj == null) || telefone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}