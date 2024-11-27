package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.EstoqueController;
import com.mycompany.pi4.controllers.FuncionarioController;
import com.mycompany.pi4.controllers.ServicoController;
import com.mycompany.pi4.entity.Funcionario;
import com.mycompany.pi4.entity.Peca;
import com.mycompany.pi4.entity.Servico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CadastroOSView extends JFrame {
    private JTextField veiculoField, descricaoField, statusField, dataField;
    private JTable itensServicoTable, itensPecaTable;
    private JComboBox<Funcionario> tecnicoComboBox;
    private JButton adicionarServicoButton, adicionarPecaButton, salvarButton, cancelarButton, novoFuncionarioButton;

    private FuncionarioController funcionarioController;
    private EstoqueController estoqueController;
    private ServicoController servicoController;

    // Listas para armazenar alterações temporárias
    private List<Peca> pecasParaAtualizar;
    private List<Servico> servicosSelecionados;

    public CadastroOSView(FuncionarioController funcionarioController, EstoqueController estoqueController, ServicoController servicoController) {
        this.funcionarioController = funcionarioController;
        this.estoqueController = estoqueController;
        this.servicoController = servicoController;
        this.pecasParaAtualizar = new ArrayList<>();
        this.servicosSelecionados = new ArrayList<>();

        setTitle("Cadastro de Ordem de Serviço");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel Superior: Informações gerais da O.S.
        JPanel infoPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações da O.S."));

        infoPanel.add(new JLabel("Veículo (Placa):"));
        veiculoField = new JTextField();
        infoPanel.add(veiculoField);

        infoPanel.add(new JLabel("Descrição:"));
        descricaoField = new JTextField();
        infoPanel.add(descricaoField);

        infoPanel.add(new JLabel("Técnico:"));
        JPanel tecnicoPanel = new JPanel(new BorderLayout());
        tecnicoComboBox = new JComboBox<>();
        carregarFuncionarios();
        tecnicoPanel.add(tecnicoComboBox, BorderLayout.CENTER);

        novoFuncionarioButton = new JButton("+");
        novoFuncionarioButton.addActionListener(e -> cadastrarNovoFuncionario());
        tecnicoPanel.add(novoFuncionarioButton, BorderLayout.EAST);

        infoPanel.add(tecnicoPanel);

        infoPanel.add(new JLabel("Status:"));
        statusField = new JTextField("Aberta");
        infoPanel.add(statusField);

        infoPanel.add(new JLabel("Data:"));
        dataField = new JTextField();
        infoPanel.add(dataField);

        mainPanel.add(infoPanel, BorderLayout.NORTH);

        // Painel Central: Tabelas de Itens (Serviços e Peças)
        JPanel itensPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        itensPanel.setBorder(BorderFactory.createTitledBorder("Itens da O.S."));

        // Tabela de Serviços
        JPanel servicoPanel = new JPanel(new BorderLayout(10, 10));
        servicoPanel.setBorder(BorderFactory.createTitledBorder("Serviços"));

        itensServicoTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"}));
        servicoPanel.add(new JScrollPane(itensServicoTable), BorderLayout.CENTER);

        adicionarServicoButton = new JButton("Adicionar Serviço");
        adicionarServicoButton.addActionListener(e -> adicionarServico());
        servicoPanel.add(adicionarServicoButton, BorderLayout.SOUTH);

        itensPanel.add(servicoPanel);

        // Tabela de Peças
        JPanel pecaPanel = new JPanel(new BorderLayout(10, 10));
        pecaPanel.setBorder(BorderFactory.createTitledBorder("Peças"));

        itensPecaTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Descrição", "Quantidade", "Preço Unitário", "Subtotal"}));
        pecaPanel.add(new JScrollPane(itensPecaTable), BorderLayout.CENTER);

        adicionarPecaButton = new JButton("Adicionar Peça");
        adicionarPecaButton.addActionListener(e -> adicionarPeca());
        pecaPanel.add(adicionarPecaButton, BorderLayout.SOUTH);

        itensPanel.add(pecaPanel);

        mainPanel.add(itensPanel, BorderLayout.CENTER);

        // Painel Inferior: Botões de Ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvarOS());
        actionPanel.add(salvarButton);

        cancelarButton = new JButton("Cancelar");
        cancelarButton.addActionListener(e -> dispose());
        actionPanel.add(cancelarButton);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Adiciona o painel principal à janela
        add(mainPanel);
    }

    private void carregarFuncionarios() {
        tecnicoComboBox.removeAllItems();
        List<Funcionario> funcionarios = funcionarioController.listarFuncionarios();
        for (Funcionario funcionario : funcionarios) {
            tecnicoComboBox.addItem(funcionario);
        }
    }

    private void cadastrarNovoFuncionario() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do novo funcionário:", "Cadastrar Funcionário", JOptionPane.PLAIN_MESSAGE);
        if (nome != null && !nome.trim().isEmpty()) {
            Funcionario novoFuncionario = new Funcionario(0, nome.trim());
            funcionarioController.cadastrarFuncionario(novoFuncionario);
            carregarFuncionarios();
            JOptionPane.showMessageDialog(this, "Novo funcionário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void adicionarServico() {
        List<Servico> servicosDisponiveis = servicoController.listarServicos();

        String[] opcoes = servicosDisponiveis.stream()
                .map(servico -> servico.getIdServico() + " - " + servico.getDescricao() + " (R$ " + servico.getPrecoUnitario() + ")")
                .toArray(String[]::new);

        String selecionada = (String) JOptionPane.showInputDialog(
                this,
                "Selecione um serviço:",
                "Adicionar Serviço",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes.length > 0 ? opcoes[0] : null
        );

        if (selecionada != null) {
            int idServico = Integer.parseInt(selecionada.split(" - ")[0]);
            Servico servico = servicoController.consultarServicoPorId(idServico);

            if (servico != null) {
                int quantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a quantidade do serviço:"));
                double subtotal = quantidade * servico.getPrecoUnitario();

                DefaultTableModel model = (DefaultTableModel) itensServicoTable.getModel();
                model.addRow(new Object[]{servico.getDescricao(), quantidade, servico.getPrecoUnitario(), subtotal});

                servicosSelecionados.add(servico);
            }
        }
    }

    private void adicionarPeca() {
        // Método para adicionar peças (já implementado)
    }

    private void salvarOS() {
        // Atualiza estoque, salva O.S. e outros processos necessários
        JOptionPane.showMessageDialog(this, "Ordem de Serviço salva com sucesso!");
        dispose();
    }
}
