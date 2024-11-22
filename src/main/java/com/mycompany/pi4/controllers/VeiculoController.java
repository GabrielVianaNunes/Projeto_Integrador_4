/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pi4.controllers;

import com.mycompany.pi4.entity.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class VeiculoController {
    private List<Veiculo> veiculos = new ArrayList<>();

    // Método para cadastrar um novo veículo
    public void cadastrarVeiculo(Veiculo veiculo) {
        veiculos.add(veiculo);
        System.out.println("Veículo cadastrado: " + veiculo.getPlaca());
    }

    // Método para buscar veículo por placa
    public Veiculo buscarVeiculoPorPlaca(String placa) {
        return veiculos.stream()
                .filter(veiculo -> veiculo.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }
}