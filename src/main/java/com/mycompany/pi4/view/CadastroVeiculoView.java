package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.*;
import com.mycompany.pi4.entity.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class CadastroVeiculoView extends JFrame {
    private JTextField placaField, anoField, quilometragemField;
    private JComboBox<Cliente> clienteComboBox;
    private JComboBox<Marca> marcaComboBox;
    private JComboBox<Modelo> modeloComboBox;
    private JButton salvarButton, cancelarButton;

    private VeiculoController veiculoController;
    private ClienteController clienteController;
    private ModeloController modeloController;
    private MarcaController marcaController;

    public CadastroVeiculoView(VeiculoController veiculoController, ClienteController clienteController,
                                ModeloController modeloController, MarcaController marcaController) {
        this.veiculoController = veiculoController;
        this.clienteController = clienteController;
        this.modeloController = modeloController;
        this.marcaController = marcaController;

        setTitle("Cadastro de Veículo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos para entrada de dados
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        placaField = new JTextField();
        placaField.setInputVerifier(new PlacaInputVerifier());
        panel.add(placaField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        anoField = new JTextField();
        anoField.setInputVerifier(new AnoInputVerifier()); // Verificador para validar o intervalo de anos
        panel.add(anoField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Quilometragem:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        quilometragemField = new JTextField();
        quilometragemField.setInputVerifier(new QuilometragemInputVerifier()); // Verificador para validar a quilometragem
        panel.add(quilometragemField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        clienteComboBox = new JComboBox<>();
        panel.add(clienteComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        panel.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        marcaComboBox = new JComboBox<>();
        panel.add(marcaComboBox, gbc);

        gbc.gridx = 2;
        JButton novaMarcaButton = new JButton("+");
        novaMarcaButton.setPreferredSize(new Dimension(40, 25));
        panel.add(novaMarcaButton, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        panel.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        modeloComboBox = new JComboBox<>();
        panel.add(modeloComboBox, gbc);

        gbc.gridx = 2;
        JButton novoModeloButton = new JButton("+");
        novoModeloButton.setPreferredSize(new Dimension(40, 25));
        panel.add(novoModeloButton, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        salvarButton = new JButton("Salvar");
        panel.add(salvarButton, gbc);

        gbc.gridx = 1;
        cancelarButton = new JButton("Cancelar");
        panel.add(cancelarButton, gbc);

        add(panel);

        // Ações e carregamento de dados
        salvarButton.addActionListener(e -> salvarVeiculo());
        cancelarButton.addActionListener(e -> dispose());
        novaMarcaButton.addActionListener(e -> cadastrarNovaMarca());
        novoModeloButton.addActionListener(e -> cadastrarNovoModelo());
        marcaComboBox.addActionListener(e -> atualizarModelos());

        carregarClientes();
        carregarMarcas();
    }

    private void carregarClientes() {
        List<Cliente> clientes = clienteController.listarClientes();
        for (Cliente cliente : clientes) {
            clienteComboBox.addItem(cliente);
        }
    }

    private void carregarMarcas() {
        List<Marca> marcas = marcaController.listarMarcas();
        marcaComboBox.removeAllItems();
        for (Marca marca : marcas) {
            marcaComboBox.addItem(marca);
        }
        atualizarModelos();
    }

    private void atualizarModelos() {
        if (modeloComboBox == null) return;

        modeloComboBox.removeAllItems();
        Marca marcaSelecionada = (Marca) marcaComboBox.getSelectedItem();
        if (marcaSelecionada != null) {
            List<Modelo> modelos = modeloController.listarModelosPorMarca(marcaSelecionada.getIdMarca());
            for (Modelo modelo : modelos) {
                modeloComboBox.addItem(modelo);
            }
        }
    }

    private void cadastrarNovaMarca() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome da nova marca:", "Cadastrar Marca", JOptionPane.PLAIN_MESSAGE);
        if (nome != null && !nome.trim().isEmpty()) {
            Marca novaMarca = new Marca(0, nome.trim());
            marcaController.cadastrarMarca(novaMarca);
            carregarMarcas();
            JOptionPane.showMessageDialog(this, "Nova marca cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void cadastrarNovoModelo() {
        Marca marcaSelecionada = (Marca) marcaComboBox.getSelectedItem();
        if (marcaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma marca antes de cadastrar um modelo.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do novo modelo:", "Cadastrar Modelo", JOptionPane.PLAIN_MESSAGE);
        if (nome != null && !nome.trim().isEmpty()) {
            Modelo novoModelo = new Modelo(0, nome.trim(), marcaSelecionada);
            modeloController.cadastrarModelo(novoModelo);
            atualizarModelos();
            JOptionPane.showMessageDialog(this, "Novo modelo cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void salvarVeiculo() {
        try {
            String placa = placaField.getText();
            if (!Pattern.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$", placa)) {
                throw new IllegalArgumentException("A placa deve estar no formato LLLNLNN (com letras maiúsculas).");
            }

            int ano = Integer.parseInt(anoField.getText());
            if (ano < 1920 || ano > 2025) {
                throw new IllegalArgumentException("O ano deve estar entre 1920 e 2025.");
            }

            int quilometragem = Integer.parseInt(quilometragemField.getText());
            if (quilometragem < 0 || quilometragem > 1_000_000_000) {
                throw new IllegalArgumentException("A quilometragem deve estar entre 0 e 1.000.000.000.");
            }

            Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
            Modelo modelo = (Modelo) modeloComboBox.getSelectedItem();

            if (cliente == null || modelo == null) {
                throw new IllegalArgumentException("Todos os campos devem ser preenchidos!");
            }

            Veiculo veiculo = new Veiculo(0, placa, ano, quilometragem, cliente, modelo);
            veiculoController.cadastrarVeiculo(veiculo);

            JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar veículo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class PlacaInputVerifier extends InputVerifier {
        private static final Pattern PLACA_PATTERN = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

        @Override
        public boolean verify(JComponent input) {
            String text = ((JTextField) input).getText();
            if (!PLACA_PATTERN.matcher(text).matches()) {
                JOptionPane.showMessageDialog(input, "A placa deve estar no formato LLLNLNN.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
    }

    private static class AnoInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                int ano = Integer.parseInt(((JTextField) input).getText());
                if (ano < 1920 || ano > 2025) {
                    JOptionPane.showMessageDialog(input, "O ano deve estar entre 1920 e 2025.", "Ano inválido", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(input, "O ano deve ser um número válido.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    private static class QuilometragemInputVerifier extends InputVerifier {
        @Override
        public boolean verify(JComponent input) {
            try {
                int quilometragem = Integer.parseInt(((JTextField) input).getText());
                if (quilometragem < 0 || quilometragem > 1_000_000_000) {
                    JOptionPane.showMessageDialog(input, "A quilometragem deve estar entre 0 e 1.000.000.000.", "Quilometragem inválida", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(input, "A quilometragem deve ser um número válido.", "Formato inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
}