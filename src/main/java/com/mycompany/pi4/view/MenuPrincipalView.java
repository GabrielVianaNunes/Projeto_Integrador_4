/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;

public class MenuPrincipalView extends JFrame {
    public MenuPrincipalView() {
        setTitle("Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JButton cadastroClienteButton = new JButton("Cadastro de Cliente");
        JButton cadastroVeiculoButton = new JButton("Cadastro de Veículo");
        JButton consultaEstoqueButton = new JButton("Consulta de Estoque");
        JButton cadastroOSButton = new JButton("Cadastro de OS");

        cadastroClienteButton.addActionListener(e -> new CadastroClienteView().setVisible(true));
        cadastroVeiculoButton.addActionListener(e -> new CadastroVeiculoView().setVisible(true));
        consultaEstoqueButton.addActionListener(e -> new ConsultaEstoqueView().setVisible(true));
        cadastroOSButton.addActionListener(e -> new CadastroOSView().setVisible(true));

        panel.add(cadastroClienteButton);
        panel.add(cadastroVeiculoButton);
        panel.add(consultaEstoqueButton);
        panel.add(cadastroOSButton);

        add(panel);
    }
}