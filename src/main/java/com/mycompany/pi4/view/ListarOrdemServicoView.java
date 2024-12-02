package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.OrdemServicoController;
import com.mycompany.pi4.controllers.FuncionarioController;
import com.mycompany.pi4.controllers.EstoqueController;
import com.mycompany.pi4.controllers.ServicoController;
import com.mycompany.pi4.controllers.ClienteController;
import com.mycompany.pi4.controllers.VeiculoController;
import com.mycompany.pi4.entity.ItensServico;
import com.mycompany.pi4.entity.OrdemServico;
import com.mycompany.pi4.entity.Peca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarOrdemServicoView extends JFrame {
    private JTable tabelaOrdensServico;
    private OrdemServicoController ordemServicoController;
    private FuncionarioController funcionarioController;
    private EstoqueController estoqueController;
    private ServicoController servicoController;
    private ClienteController clienteController;
    private VeiculoController veiculoController;

    public ListarOrdemServicoView(
            OrdemServicoController ordemServicoController,
            FuncionarioController funcionarioController,
            EstoqueController estoqueController,
            ServicoController servicoController,
            ClienteController clienteController,
            VeiculoController veiculoController // Adicionado o VeiculoController
    ) {
        this.ordemServicoController = ordemServicoController;
        this.funcionarioController = funcionarioController;
        this.estoqueController = estoqueController;
        this.servicoController = servicoController;
        this.clienteController = clienteController;
        this.veiculoController = veiculoController;

        setTitle("Lista de Ordens de Serviço");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criação da tabela
        DefaultTableModel modeloTabela = new DefaultTableModel(new Object[][]{},
                new String[]{"ID", "Data Início", "Data Fim", "Status", "Valor Total", "Veículo"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }
        };

        tabelaOrdensServico = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaOrdensServico);

        // Preenche a tabela com os dados
        preencherTabela(modeloTabela);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão para abrir a tela de Cadastro de OS
        JButton cadastroOSButton = new JButton("Cadastro de OS");
        cadastroOSButton.addActionListener(e -> {
            CadastroOSView cadastroOSView = new CadastroOSView(
                    funcionarioController,
                    estoqueController,
                    servicoController,
                    veiculoController, // Adicionado o VeiculoController
                    clienteController,
                    ordemServicoController // Adicionado
            );
            cadastroOSView.setVisible(true);
        });
        buttonPanel.add(cadastroOSButton);

        // Botão de fechar
        JButton fecharButton = new JButton("Fechar");
        fecharButton.addActionListener(e -> dispose());
        buttonPanel.add(fecharButton);

        // Adiciona os componentes à janela
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void preencherTabela(DefaultTableModel modeloTabela) {
        List<OrdemServico> ordensServico = ordemServicoController.listarOrdensServico();
        for (OrdemServico os : ordensServico) {
            modeloTabela.addRow(new Object[]{
                    os.getIdOS(),
                    os.getDataInicio(),
                    os.getDataFim(),
                    os.getStatus(),
                    os.getValorTotal(), // Valor total correto
                    os.getVeiculo() != null ? os.getVeiculo().getIdVeiculo() : "N/A"
            });
        }
        tabelaOrdensServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Detecta duplo clique
                    int selectedRow = tabelaOrdensServico.getSelectedRow();
                    if (selectedRow != -1) {
                        int idOS = (int) tabelaOrdensServico.getValueAt(selectedRow, 0); // Pega o ID da OS na coluna 0
                        abrirOrdemServico(idOS); // Chama o método para abrir a tela de edição
                    }
                }
            }
        });
    }

    private void abrirOrdemServico(int idOS) {
        OrdemServico os = ordemServicoController.consultarOrdemServicoPorId(idOS); // Recupera a O.S do banco
        if (os != null) {
            // Recupera as listas de itens de serviço e peças associadas à O.S.
            List<ItensServico> itensServico = ordemServicoController.consultarItensServicoPorOrdemServico(idOS);
            List<Peca> pecas = ordemServicoController.consultarPecasPorOrdemServico(idOS);

            // Passa a O.S, itens de serviço e peças para a tela VisualizarOSView
            VisualizarOSView visualizarOSView = new VisualizarOSView(
                    os, // Passa a O.S como parâmetro
                    itensServico, // Passa os itens de serviço
                    pecas // Passa as peças
            );
            visualizarOSView.setVisible(true); // Exibe a tela de visualização de O.S
        } else {
            JOptionPane.showMessageDialog(this, "Ordem de Serviço não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}