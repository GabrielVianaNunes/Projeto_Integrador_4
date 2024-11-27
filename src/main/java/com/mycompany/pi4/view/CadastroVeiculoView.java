/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.*;
import com.mycompany.pi4.entity.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
        setSize(600, 400);
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
        panel.add(placaField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        anoField = new JTextField();
        panel.add(anoField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Quilometragem:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        quilometragemField = new JTextField();
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
        carregarMarcas(); // Chama carregarMarcas para preencher a marca antes de atualizar modelos
    }

    // Método para carregar clientes
    private void carregarClientes() {
        List<Cliente> clientes = clienteController.listarClientes();
        for (Cliente cliente : clientes) {
            clienteComboBox.addItem(cliente);
        }
    }

    // Método para carregar marcas
    private void carregarMarcas() {
        List<Marca> marcas = marcaController.listarMarcas();
        marcaComboBox.removeAllItems();
        for (Marca marca : marcas) {
            marcaComboBox.addItem(marca);
        }
        atualizarModelos(); // Atualiza os modelos somente após carregar marcas
    }

    // Método para atualizar modelos com base na marca selecionada
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

    // Método para cadastrar uma nova marca
    private void cadastrarNovaMarca() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome da nova marca:", "Cadastrar Marca", JOptionPane.PLAIN_MESSAGE);
        if (nome != null && !nome.trim().isEmpty()) {
            Marca novaMarca = new Marca(0, nome.trim());
            marcaController.cadastrarMarca(novaMarca);
            carregarMarcas(); // Atualiza o comboBox
            JOptionPane.showMessageDialog(this, "Nova marca cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para cadastrar um novo modelo
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
            atualizarModelos(); // Atualiza o comboBox
            JOptionPane.showMessageDialog(this, "Novo modelo cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para salvar o veículo
    private void salvarVeiculo() {
        try {
            String placa = placaField.getText();
            int ano = Integer.parseInt(anoField.getText());
            int quilometragem = Integer.parseInt(quilometragemField.getText());
            Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
            Modelo modelo = (Modelo) modeloComboBox.getSelectedItem();

            if (placa.isEmpty() || cliente == null || modelo == null) {
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
}
