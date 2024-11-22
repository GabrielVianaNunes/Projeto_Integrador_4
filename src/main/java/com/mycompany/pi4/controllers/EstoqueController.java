/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Peca;
import java.util.HashMap;
import java.util.Map;

public class EstoqueController {
    private Map<Integer, Peca> estoque = new HashMap<>();

    // Adicionar uma nova peça ao estoque
    public void adicionarPeca(Peca peca) {
        estoque.put(peca.getIdPeca(), peca);
        System.out.println("Peça adicionada ao estoque: " + peca.getDescricao());
    }

    // Atualizar quantidade de uma peça
    public void atualizarQuantidade(int idPeca, int novaQuantidade) {
        Peca peca = estoque.get(idPeca);
        if (peca != null) {
            peca.setQuantidade(novaQuantidade);
            System.out.println("Quantidade atualizada para: " + novaQuantidade);
        } else {
            System.out.println("Peça não encontrada!");
        }
    }
}