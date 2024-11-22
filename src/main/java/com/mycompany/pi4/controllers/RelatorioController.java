/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.OrdemServico;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioController {

    // Gerar relatório de O.S. concluídas
    public void gerarRelatorioConcluidas(List<OrdemServico> ordensServico) {
        List<OrdemServico> concluidas = ordensServico.stream()
                .filter(os -> "Concluída".equalsIgnoreCase(os.getStatus()))
                .collect(Collectors.toList());

        System.out.println("=== Relatório de O.S. Concluídas ===");
        concluidas.forEach(os -> System.out.println("ID: " + os.getIdOS() + ", Valor Total: " + os.getValorTotal()));
    }
}