package com.mycompany.pi4.view;

import com.mycompany.pi4.entity.Veiculo;
import com.mycompany.pi4.repositories.VeiculoRepository;
import com.mycompany.pi4.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class EditarVeiculoView extends JFrame {
    private JTextField placaField, anoField, quilometragemField;
    private Veiculo veiculo;

    public EditarVeiculoView(Veiculo veiculo) {
        this.veiculo = veiculo;
        setTitle("Editar Veículo");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        panel.add(new JLabel("Placa:"));
        placaField = new JTextField(veiculo.getPlaca());
        panel.add(placaField);

        panel.add(new JLabel("Ano:"));
        anoField = new JTextField(String.valueOf(veiculo.getAno()));
        panel.add(anoField);

        panel.add(new JLabel("Quilometragem:"));
        quilometragemField = new JTextField(String.valueOf(veiculo.getQuilometragem()));
        panel.add(quilometragemField);

        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvarVeiculo());
        panel.add(salvarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        panel.add(cancelarButton);

        add(panel);
    }

    private void salvarVeiculo() {
        String placa = placaField.getText().trim();
        int ano;
        int quilometragem;

        try {
            ano = Integer.parseInt(anoField.getText().trim());
            quilometragem = Integer.parseInt(quilometragemField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano e Quilometragem devem ser números.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        veiculo.setPlaca(placa);
        veiculo.setAno(ano);
        veiculo.setQuilometragem(quilometragem);

        try (Connection connection = DatabaseConnection.getConnection()) {
            VeiculoRepository veiculoRepository = new VeiculoRepository(connection);
            veiculoRepository.atualizar(veiculo);
            JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!");
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar veículo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
