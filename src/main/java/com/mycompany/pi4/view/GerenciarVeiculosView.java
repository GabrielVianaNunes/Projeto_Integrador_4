package com.mycompany.pi4.view;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.Marca;
import com.mycompany.pi4.entity.Modelo;
import com.mycompany.pi4.repositories.VeiculoRepository;
import com.mycompany.pi4.entity.Veiculo;
import com.mycompany.pi4.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GerenciarVeiculosView extends JFrame {
    private JTable veiculosTable;
    private int idCliente;

    public GerenciarVeiculosView(int idCliente) {
        this.idCliente = idCliente;

        setTitle("Gerenciar Veículos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Veículos do Cliente", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titulo, BorderLayout.NORTH);

        veiculosTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Placa", "Ano", "Quilometragem", "Modelo", "Marca"}));
        JScrollPane scrollPane = new JScrollPane(veiculosTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton excluirButton = new JButton("Excluir");

        buttonPanel.add(excluirButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        carregarVeiculos(idCliente);
    }

    private void carregarVeiculos(int idCliente) {
        DefaultTableModel model = (DefaultTableModel) veiculosTable.getModel();
        model.setRowCount(0); // Limpa a tabela

        try (Connection connection = DatabaseConnection.getConnection()) {
            VeiculoRepository veiculoRepository = new VeiculoRepository(connection);
            List<Veiculo> veiculos = veiculoRepository.buscarPorCliente(idCliente);

            for (Veiculo veiculo : veiculos) {
                model.addRow(new Object[]{
                    veiculo.getPlaca(),
                    veiculo.getAno(),
                    veiculo.getQuilometragem(),
                    veiculo.getModelo().getNome(),
                    veiculo.getModelo().getMarca() != null ? veiculo.getModelo().getMarca().getNome() : "Sem Marca"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar veículos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirVeiculo() {
        int selectedRow = veiculosTable.getSelectedRow();
        if (selectedRow != -1) {
            String placa = (String) veiculosTable.getValueAt(selectedRow, 0); // Assume que a placa está na coluna 0
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este veículo?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    VeiculoRepository veiculoRepository = new VeiculoRepository(connection);
                    veiculoRepository.excluirPorPlaca(placa); // Usa o método criado no repositório
                    carregarVeiculos(idCliente); // Atualiza a tabela após exclusão
                    JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir veículo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um veículo para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private Veiculo montarVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setIdVeiculo(rs.getInt("idVeiculo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setQuilometragem(rs.getInt("quilometragem"));

        // Cliente
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setNome(rs.getString("nomeCliente"));
        veiculo.setCliente(cliente);

        // Modelo
        Modelo modelo = new Modelo();
        modelo.setIdModelo(rs.getInt("idModelo"));
        modelo.setNome(rs.getString("nomeModelo"));

        // Marca
        Marca marca = new Marca();
        if (rs.getString("nomeMarca") != null) {
            marca.setIdMarca(rs.getInt("idMarca"));
            marca.setNome(rs.getString("nomeMarca"));
        }
        modelo.setMarca(marca);

        veiculo.setModelo(modelo);

        return veiculo;
    }


}
