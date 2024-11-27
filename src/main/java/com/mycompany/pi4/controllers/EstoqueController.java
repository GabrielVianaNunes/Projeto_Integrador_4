package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Peca;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class EstoqueController {
    private Map<Integer, Peca> estoque = new HashMap<>();

    // Adicionar uma nova peça ao estoque
    public void adicionarPeca(Peca peca) {
        if (estoque.containsKey(peca.getIdPeca())) {
            System.out.println("Peça já existente! Atualize a quantidade, se necessário.");
        } else {
            estoque.put(peca.getIdPeca(), peca);
            System.out.println("Peça adicionada ao estoque: " + peca.getDescricao());
        }
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

    // Listar todas as peças disponíveis no estoque
    public List<Peca> listarPecas() {
        return new ArrayList<>(estoque.values());
    }

    // Consultar detalhes de uma peça específica
    public Peca consultarPecaPorId(int idPeca) {
        return estoque.get(idPeca);
    }

    // Verificar se há quantidade suficiente de uma peça
    public boolean verificarDisponibilidade(int idPeca, int quantidadeDesejada) {
        Peca peca = estoque.get(idPeca);
        return peca != null && peca.getQuantidade() >= quantidadeDesejada;
    }

    // Remover uma peça do estoque
    public void removerPeca(int idPeca) {
        if (estoque.containsKey(idPeca)) {
            estoque.remove(idPeca);
            System.out.println("Peça removida com sucesso!");
        } else {
            System.out.println("Peça não encontrada!");
        }
    }

    // Editar os detalhes de uma peça
    public void editarPeca(int idPeca, String novaDescricao, int novaQuantidade, double novoPreco) {
        Peca peca = estoque.get(idPeca);
        if (peca != null) {
            peca.setDescricao(novaDescricao);
            peca.setQuantidade(novaQuantidade);
            peca.setPrecoUnitario(novoPreco);
            System.out.println("Peça atualizada com sucesso!");
        } else {
            System.out.println("Peça não encontrada!");
        }
    }
}
