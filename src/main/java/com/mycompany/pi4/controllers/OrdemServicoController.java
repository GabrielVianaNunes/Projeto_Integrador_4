/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.OrdemServico;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoController {
    private List<OrdemServico> ordensServico = new ArrayList<>();

    // Criar uma nova ordem de serviço
    public void criarOrdemServico(OrdemServico os) {
        ordensServico.add(os);
        System.out.println("Ordem de serviço criada: " + os.getIdOS());
    }

    // Atualizar o status de uma ordem de serviço
    public void atualizarStatus(int idOS, String novoStatus) {
        for (OrdemServico os : ordensServico) {
            if (os.getIdOS() == idOS) {
                os.setStatus(novoStatus);
                System.out.println("Status atualizado para: " + novoStatus);
                return;
            }
        }
        System.out.println("Ordem de serviço não encontrada!");
    }
}