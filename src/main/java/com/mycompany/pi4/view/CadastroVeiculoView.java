/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;


public class CadastroVeiculoView extends JFrame {
    private JTextField placaField, modeloField, anoField;
    private JButton salvarButton, cancelarButton;

    public CadastroVeiculoView() {
        setTitle("Cadastro de Veículo");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Placa:"));
        placaField = new JTextField();
        panel.add(placaField);

        panel.add(new JLabel("Modelo:"));
        modeloField = new JTextField();
        panel.add(modeloField);

        panel.add(new JLabel("Ano:"));
        anoField = new JTextField();
        panel.add(anoField);

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> {
            String placa = placaField.getText();
            String modelo = modeloField.getText();
            String ano = anoField.getText();
            System.out.println("Veículo cadastrado: Placa " + placa + ", Modelo " + modelo + ", Ano " + ano);
            dispose();
        });
        panel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        panel.add(cancelarButton);

        add(panel);
    }
}