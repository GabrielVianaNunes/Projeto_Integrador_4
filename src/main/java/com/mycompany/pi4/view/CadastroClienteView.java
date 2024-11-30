package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.ClienteController;
import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class CadastroClienteView extends JFrame {

    private JTextField idClienteField, nomeField, telefoneField, emailField, enderecoField, cepField, cpfField, cnpjField, logradouroField;
    private JComboBox<String> tipoClienteComboBox;
    private JButton salvarButton, cancelarButton, limparButton;

    public CadastroClienteView() {
        setTitle("Cadastro de Cliente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245)); // Cor de fundo suave

        JLabel titulo = new JLabel("Cadastro de Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 102, 204));
        mainPanel.add(titulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        formPanel.setBackground(new Color(245, 245, 245));

        formPanel.add(new JLabel("ID Cliente:"));
        idClienteField = new JTextField();
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
                if (texto.length() > 11) {
                    texto = texto.substring(0, 11);
                }
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
        
        formPanel.add(new JLabel("Logradouro:"));
        logradouroField = new JTextField(); // Novo campo para logradouro
        formPanel.add(logradouroField);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        salvarButton = new JButton("Salvar");
        salvarButton.setBackground(new Color(0, 102, 204));
        salvarButton.setForeground(Color.WHITE);
        salvarButton.addActionListener(this::onSalvar);
        buttonPanel.add(salvarButton);

        limparButton = new JButton("Limpar");
        limparButton.setBackground(new Color(0, 153, 0));
        limparButton.setForeground(Color.WHITE);
        limparButton.addActionListener(this::onLimpar);
        buttonPanel.add(limparButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.setBackground(new Color(204, 0, 0));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
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
        String textoAtual = telefoneField.getText().replaceAll("[^\\d]", "");
        int posicaoCursor = telefoneField.getCaretPosition();

        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && posicaoCursor > 0) {
            if (textoAtual.length() > 0) {
                textoAtual = textoAtual.substring(0, textoAtual.length() - 1);
            }
        }

        String formatado = formatarTexto(textoAtual);
        telefoneField.setText(formatado);
        telefoneField.setCaretPosition(formatado.length());
    }

    private String formatarTexto(String texto) {
        StringBuilder sb = new StringBuilder();
        if (texto.length() > 0) sb.append("(").append(texto.substring(0, Math.min(texto.length(), 2))).append(") ");
        if (texto.length() > 2) sb.append(texto.substring(2, Math.min(texto.length(), 7))).append("-");
        if (texto.length() > 7) sb.append(texto.substring(7));
        return sb.toString();
    }

    private void onLimpar(ActionEvent e) {
        idClienteField.setText("");
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
        Cliente cliente = new Cliente();
        cliente.setNome(nomeField.getText());
        cliente.setTelefone(telefoneField.getText());
        cliente.setEmail(emailField.getText());
        cliente.setEndereco(enderecoField.getText());
        cliente.setLogradouro(logradouroField.getText()); // Captura o valor do novo campo
        cliente.setCep(cepField.getText());
        cliente.setTipoCliente((String) tipoClienteComboBox.getSelectedItem());
        cliente.setCpf(cpfField.getText());
        cliente.setCnpj(cnpjField.getText());

        try (Connection connection = DatabaseConnection.getConnection()) {
            ClienteController clienteController = new ClienteController(connection);
            clienteController.cadastrarCliente(cliente);

            if (cliente.getIdCliente() > 0) {
                idClienteField.setText(String.valueOf(cliente.getIdCliente()));
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso! ID: " + cliente.getIdCliente());
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
    }


}
