/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CadastroClienteView extends JFrame {
    private JTextField nomeField, telefoneField, emailField;
    private JButton salvarButton, cancelarButton;

    public CadastroClienteView() {
        setTitle("Cadastro de Cliente");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        panel.add(nomeField);

        panel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        panel.add(telefoneField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(this::onSalvar);
        panel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        panel.add(cancelarButton);

        add(panel);
    }

    private void onSalvar(ActionEvent event) {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();

        // Chamada ao controller para salvar os dados (exemplo)
        System.out.println("Salvando cliente: " + nome + ", " + telefone + ", " + email);
        dispose();
    }
}