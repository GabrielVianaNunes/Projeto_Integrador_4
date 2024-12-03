package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.ClienteController;
import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;
import com.mycompany.pi4.repositories.ClienteRepository;
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
        mainPanel.setBackground(new Color(245, 245, 245));

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
        nomeField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                formatarNome();
            }
        });
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
            public void keyReleased(KeyEvent e) {
                formatarTelefone();
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
        logradouroField = new JTextField();
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

        // Adicione esse botão no painel de botões
        JButton gerenciarClientesButton = new JButton("Gerenciar Clientes");
        gerenciarClientesButton.setBackground(new Color(0, 102, 204));
        gerenciarClientesButton.setForeground(Color.WHITE);
        gerenciarClientesButton.addActionListener(e -> {
            try (Connection connection = DatabaseConnection.getConnection()) {
                ClienteRepository clienteRepository = new ClienteRepository(connection);
                GerenciarClientesView gerenciarClientesView = new GerenciarClientesView(clienteRepository);
                gerenciarClientesView.setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir a tela de Gerenciar Clientes: " + ex.getMessage(), "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(gerenciarClientesButton);

        salvarButton = new JButton("Salvar");
        salvarButton.setBackground(new Color(0, 102, 204));
        salvarButton.setForeground(Color.WHITE);
        salvarButton.addActionListener(e -> {
            String tipoCliente = tipoClienteComboBox.getSelectedItem().toString().equals("Pessoa Física") ? "PF" : "PJ";

            Cliente cliente = tipoCliente.equals("PF") ? new PessoaFisica() : new PessoaJuridica();
            cliente.setNome(nomeField.getText().trim());
            cliente.setTelefone(telefoneField.getText().trim());
            cliente.setEmail(emailField.getText().trim());
            cliente.setEndereco(enderecoField.getText().trim());
            cliente.setLogradouro(logradouroField.getText().trim());
            cliente.setCep(cepField.getText().trim());

            if (cliente instanceof PessoaFisica) {
                ((PessoaFisica) cliente).setCpf(cpfField.getText().trim());
            } else if (cliente instanceof PessoaJuridica) {
                ((PessoaJuridica) cliente).setCnpj(cnpjField.getText().trim());
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                ClienteRepository clienteRepository = new ClienteRepository(connection);
                clienteRepository.salvar(cliente); // Usa o método corrigido
                JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
                dispose(); // Fecha a janela após salvar
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

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

    private void limparCampos() {
        idClienteField.setText("");
        nomeField.setText("");
        tipoClienteComboBox.setSelectedIndex(0);
        cpfField.setText("");
        cnpjField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        logradouroField.setText("");
        cepField.setText("");
    }

    private void formatarNome() {
        String texto = nomeField.getText();
        String textoFormatado = texto.replaceAll("[^a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÊÍÏÓÔÕÖÚÇÑ\\s]", "");
        textoFormatado = textoFormatado.replaceAll("\\s{2,}", " ");

        // Atualiza o campo apenas se houver alteração
        if (!textoFormatado.equals(texto)) {
            nomeField.setText(textoFormatado);
            nomeField.setCaretPosition(textoFormatado.length());
        }
    }

    private void formatarCPF() {
        String texto = cpfField.getText().replaceAll("[^\\d]", "");
        if (texto.length() > 11) {
            texto = texto.substring(0, 11);
        }
        String formatado = texto.length() > 9
                ? texto.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3-$4")
                : texto.length() > 6
                ? texto.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1.$2.$3")
                : texto.length() > 3
                ? texto.replaceFirst("(\\d{3})(\\d+)", "$1.$2")
                : texto;
        cpfField.setText(formatado);
    }

    private void formatarCNPJ() {
        // Salva a posição atual do cursor
        int posicaoCursor = cnpjField.getCaretPosition();

        // Remove todos os caracteres que não são dígitos
        String texto = cnpjField.getText().replaceAll("[^\\d]", "");

        // Se o texto tem exatamente 8 dígitos, solicita a identificação (Matriz ou Filial)
        if (texto.length() == 8 && !texto.contains("0001") && !texto.contains("0002")) {
            Object[] options = {"Matriz (0001)", "Filial (0002)"};
            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "Este CNPJ pertence a uma Matriz ou a uma Filial?",
                    "Identificação de CNPJ",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            // Adiciona o identificador com base na escolha do usuário
            if (escolha == JOptionPane.YES_OPTION) {
                texto += "0001"; // Matriz
            } else if (escolha == JOptionPane.NO_OPTION) {
                texto += "0002"; // Filial
            }
        }

        // Limitar o número total de dígitos a 14 (CNPJ completo)
        if (texto.length() > 14) {
            texto = texto.substring(0, 14);
        }

        // Aplica a formatação adequada
        String formatado = texto.replaceFirst(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{0,2})",
                "$1.$2.$3/$4-$5"
        );
        cnpjField.setText(formatado);

        // Ajusta a posição do cursor
        posicaoCursor = Math.min(posicaoCursor, formatado.length()); // Garante que o cursor não passe o final do texto
        cnpjField.setCaretPosition(posicaoCursor);
    }

    private void formatarTelefone() {
        String texto = telefoneField.getText().replaceAll("[^\\d]", "");
        if (texto.length() > 11) {
            texto = texto.substring(0, 11);
        }
        String formatado = texto.length() > 6
                ? texto.replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1) $2-$3")
                : texto.length() > 2
                ? texto.replaceFirst("(\\d{2})(\\d+)", "($1) $2")
                : texto;
        telefoneField.setText(formatado);
    }

    private void formatarCEP() {
        String texto = cepField.getText().replaceAll("[^\\d]", "");
        if (texto.length() > 8) {
            texto = texto.substring(0, 8);
        }
        cepField.setText(texto.replaceFirst("(\\d{5})(\\d+)", "$1-$2"));
    }

    private void onTipoClienteChange(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            if ("Pessoa Física".equals(tipoClienteComboBox.getSelectedItem())) {
                cpfField.setEnabled(true);
                cnpjField.setEnabled(false);
                cnpjField.setText("");
            } else {
                cpfField.setEnabled(false);
                cnpjField.setEnabled(true);
                cpfField.setText("");
            }
        }
    }

    public void preencherCamposComCliente(Cliente cliente) {
        idClienteField.setText(String.valueOf(cliente.getIdCliente()));
        nomeField.setText(cliente.getNome());
        tipoClienteComboBox.setSelectedItem(cliente instanceof PessoaFisica ? "Pessoa Física" : "Pessoa Jurídica");

        // Desabilitar os campos CPF/CNPJ
        if (cliente instanceof PessoaFisica) {
            cpfField.setText(((PessoaFisica) cliente).getCpf());
            cpfField.setEnabled(false); // Desabilitar o campo
            cnpjField.setText("");
            cnpjField.setEnabled(false); // Garantir que CNPJ também esteja desabilitado
        } else if (cliente instanceof PessoaJuridica) {
            cnpjField.setText(((PessoaJuridica) cliente).getCnpj());
            cnpjField.setEnabled(false); // Desabilitar o campo
            cpfField.setText("");
            cpfField.setEnabled(false); // Garantir que CPF também esteja desabilitado
        }

        telefoneField.setText(cliente.getTelefone());
        emailField.setText(cliente.getEmail());
        enderecoField.setText(cliente.getEndereco());
        logradouroField.setText(cliente.getLogradouro());
        cepField.setText(cliente.getCep());
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
        logradouroField.setText("");
        cepField.setText("");
    }

    private void onSalvar(ActionEvent e) {
        String tipoCliente = "Pessoa Física".equals(tipoClienteComboBox.getSelectedItem()) ? "PF" : "PJ";
        Cliente cliente = new Cliente();
        cliente.setNome(nomeField.getText());
        cliente.setTelefone(telefoneField.getText());
        cliente.setEmail(emailField.getText());
        cliente.setEndereco(enderecoField.getText());
        cliente.setLogradouro(logradouroField.getText());
        cliente.setCep(cepField.getText());
        cliente.setTipoCliente(tipoCliente);

        if ("PF".equals(tipoCliente) && (cpfField.getText().isEmpty() || cpfField.getText().length() != 14)) {
            JOptionPane.showMessageDialog(this, "CPF inválido!");
            return;
        }
        if ("PJ".equals(tipoCliente) && (cnpjField.getText().isEmpty() || cnpjField.getText().length() != 18)) {
            JOptionPane.showMessageDialog(this, "CNPJ inválido!");
            return;
        }

        cliente.setCpf(cpfField.getText());
        cliente.setCnpj(cnpjField.getText());

        try (Connection connection = DatabaseConnection.getConnection()) {
            ClienteController clienteController = new ClienteController(connection);
            clienteController.cadastrarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente: " + ex.getMessage());
        }
    }
}
