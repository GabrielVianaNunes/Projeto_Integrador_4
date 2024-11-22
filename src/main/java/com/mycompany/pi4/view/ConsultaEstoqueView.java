/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.view;

import javax.swing.*;
import java.awt.*;

public class ConsultaEstoqueView extends JFrame {
    private JTable tabelaEstoque;
    private JButton atualizarButton;

    public ConsultaEstoqueView() {
        setTitle("Consulta de Estoque");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] colunas = {"ID", "Descrição", "Quantidade", "Preço Unitário"};
        Object[][] dados = {}; // Preencher com dados do controller

        tabelaEstoque = new JTable(dados, colunas);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);

        atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(e -> System.out.println("Atualizando estoque..."));

        add(scrollPane, BorderLayout.CENTER);
        add(atualizarButton, BorderLayout.SOUTH);
    }
}