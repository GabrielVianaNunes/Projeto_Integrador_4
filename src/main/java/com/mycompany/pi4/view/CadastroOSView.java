/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;

public class CadastroOSView extends JFrame {
    private JTextField veiculoField, descricaoField;
    private JButton salvarButton, cancelarButton;

    public CadastroOSView() {
        setTitle("Cadastro de Ordem de Serviço");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Veículo (Placa):"));
        veiculoField = new JTextField();
        panel.add(veiculoField);

        panel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        panel.add(descricaoField);

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> {
            String veiculo = veiculoField.getText();
            String descricao = descricaoField.getText();
            System.out.println("OS cadastrada: Veículo " + veiculo + ", Descrição " + descricao);
            dispose();
        });
        panel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        panel.add(cancelarButton);

        add(panel);
    }
}