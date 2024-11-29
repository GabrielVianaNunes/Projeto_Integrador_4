package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.ClienteController;
import com.mycompany.pi4.controllers.EstoqueController;
import com.mycompany.pi4.controllers.FuncionarioController;
import com.mycompany.pi4.controllers.ServicoController;
import com.mycompany.pi4.entity.Cliente;
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
    private JButton adicionarServicoButton, excluirServicoButton, adicionarPecaButton, excluirPecaButton, salvarButton, cancelarButton, novoFuncionarioButton;

    private FuncionarioController funcionarioController;
    private EstoqueController estoqueController;
    private ServicoController servicoController;
    private ClienteController clienteController;

    private List<Peca> pecasParaAtualizar;
    private JComboBox<Cliente> clienteComboBox;
    private JTextField nomeField, cpfField, cnpjField, telefoneField, emailField, enderecoField, cepField;


    public CadastroOSView(FuncionarioController funcionarioController, EstoqueController estoqueController, ServicoController servicoController, ClienteController clienteController) {
        this.funcionarioController = funcionarioController;
        this.estoqueController = estoqueController;
        this.servicoController = servicoController;
        this.clienteController = clienteController;
        this.pecasParaAtualizar = new ArrayList<>();

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
        
        infoPanel.add(new JLabel("Selecionar Cliente:"));
        clienteComboBox = new JComboBox<>();
        carregarClientes();
        clienteComboBox.addActionListener(e -> preencherCamposCliente());
        infoPanel.add(clienteComboBox);
        
        infoPanel.add(new JLabel("Nome:"));
        nomeField = new JTextField();
        nomeField.setEditable(false);
        infoPanel.add(nomeField);

        infoPanel.add(new JLabel("CPF:"));
        cpfField = new JTextField();
        cpfField.setEditable(false);
        infoPanel.add(cpfField);

        infoPanel.add(new JLabel("CNPJ:"));
        cnpjField = new JTextField();
        cnpjField.setEditable(false);
        infoPanel.add(cnpjField);

        infoPanel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField();
        telefoneField.setEditable(false);
        infoPanel.add(telefoneField);

        infoPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        emailField.setEditable(false);
        infoPanel.add(emailField);

        infoPanel.add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        enderecoField.setEditable(false);
        infoPanel.add(enderecoField);

        infoPanel.add(new JLabel("CEP:"));
        cepField = new JTextField();
        cepField.setEditable(false);
        infoPanel.add(cepField);

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

        JPanel servicoButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adicionarServicoButton = new JButton("Adicionar Serviço");
        adicionarServicoButton.addActionListener(e -> adicionarServico());
        excluirServicoButton = new JButton("Excluir Serviço");
        excluirServicoButton.addActionListener(e -> excluirServico());
        servicoButtonsPanel.add(adicionarServicoButton);
        servicoButtonsPanel.add(excluirServicoButton);

        servicoPanel.add(servicoButtonsPanel, BorderLayout.SOUTH);

        itensPanel.add(servicoPanel);

        // Tabela de Peças
        JPanel pecaPanel = new JPanel(new BorderLayout(10, 10));
        pecaPanel.setBorder(BorderFactory.createTitledBorder("Peças"));

        itensPecaTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Descrição", "Quantidade", "Preço Unitário", "Subtotal"}));
        pecaPanel.add(new JScrollPane(itensPecaTable), BorderLayout.CENTER);

        JPanel pecaButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adicionarPecaButton = new JButton("Adicionar Peça");
        adicionarPecaButton.addActionListener(e -> adicionarPeca());
        excluirPecaButton = new JButton("Excluir Peça");
        excluirPecaButton.addActionListener(e -> excluirPeca());
        pecaButtonsPanel.add(adicionarPecaButton);
        pecaButtonsPanel.add(excluirPecaButton);

        pecaPanel.add(pecaButtonsPanel, BorderLayout.SOUTH);

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
        if (funcionarios != null) {
            for (Funcionario funcionario : funcionarios) {
                tecnicoComboBox.addItem(funcionario);
            }
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
    
    private void carregarClientes() {
        clienteComboBox.removeAllItems();
        List<Cliente> clientes = clienteController.listarClientes();
        if (clientes != null) {
            for (Cliente cliente : clientes) {
                clienteComboBox.addItem(cliente);
            }
        }
    }

    
    private void preencherCamposCliente() {
        Cliente clienteSelecionado = (Cliente) clienteComboBox.getSelectedItem();
        if (clienteSelecionado != null) {
            nomeField.setText(clienteSelecionado.getNome());
            cpfField.setText(clienteSelecionado.getCpf() != null ? clienteSelecionado.getCpf() : "");
            cnpjField.setText(clienteSelecionado.getCnpj() != null ? clienteSelecionado.getCnpj() : "");
            telefoneField.setText(formatarTelefone(clienteSelecionado.getTelefone()));
            emailField.setText(clienteSelecionado.getEmail());
            enderecoField.setText(clienteSelecionado.getEndereco());
            cepField.setText(formatarCep(clienteSelecionado.getCep()));
        }
    }
    
    private String formatarTelefone(String telefone) {
        return telefone.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    private String formatarCep(String cep) {
        return cep.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
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

        if (selecionada != null && selecionada.contains(" - ")) {
            int idServico = Integer.parseInt(selecionada.split(" - ")[0]);
            Servico servico = servicoController.consultarServicoPorId(idServico);

            if (servico != null) {
                int quantidade = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a quantidade:"));
                double subtotal = quantidade * servico.getPrecoUnitario();
                DefaultTableModel model = (DefaultTableModel) itensServicoTable.getModel();
                model.addRow(new Object[]{
                        servico.getDescricao(),
                        quantidade,
                        servico.getPrecoUnitario(),
                        subtotal
                });
            }
        }
    }

    private void excluirServico() {
        DefaultTableModel model = (DefaultTableModel) itensServicoTable.getModel();
        int selectedRow = itensServicoTable.getSelectedRow();

        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um serviço para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void excluirPeca() {
        DefaultTableModel model = (DefaultTableModel) itensPecaTable.getModel();
        int selectedRow = itensPecaTable.getSelectedRow();

        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma peça para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void adicionarPeca() {
        List<Peca> pecasDisponiveis = estoqueController.listarPecas();

        String[] opcoes = pecasDisponiveis.stream()
                .map(peca -> peca.getIdPeca() + " - " + peca.getDescricao() + " (Qtd: " + peca.getQuantidade() + ")")
                .toArray(String[]::new);

        String selecionada = (String) JOptionPane.showInputDialog(
                this,
                "Selecione uma peça:",
                "Adicionar Peça",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes.length > 0 ? opcoes[0] : null
        );

        if (selecionada != null) {
            int idPeca = Integer.parseInt(selecionada.split(" - ")[0]);
            Peca peca = estoqueController.consultarPecaPorId(idPeca);

            if (peca != null) {
                int quantidadeDesejada = Integer.parseInt(JOptionPane.showInputDialog(this, "Digite a quantidade:"));

                if (estoqueController.verificarDisponibilidade(idPeca, quantidadeDesejada)) {
                    double subtotal = quantidadeDesejada * peca.getPrecoUnitario();
                    DefaultTableModel model = (DefaultTableModel) itensPecaTable.getModel();
                    model.addRow(new Object[]{
                            peca.getIdPeca(),
                            peca.getDescricao(),
                            quantidadeDesejada,
                            peca.getPrecoUnitario(),
                            subtotal
                    });

                    Peca pecaParaAtualizar = new Peca(peca.getIdPeca(), peca.getDescricao(), quantidadeDesejada, peca.getPrecoUnitario());
                    pecasParaAtualizar.add(pecaParaAtualizar);
                } else {
                    JOptionPane.showMessageDialog(this, "Quantidade insuficiente no estoque!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void salvarOS() {
        try {
            String veiculo = veiculoField.getText();
            String descricao = descricaoField.getText();
            Funcionario tecnico = (Funcionario) tecnicoComboBox.getSelectedItem();
            String status = statusField.getText();
            String data = dataField.getText();

            if (veiculo.isEmpty() || descricao.isEmpty() || tecnico == null) {
                throw new IllegalArgumentException("Todos os campos devem ser preenchidos!");
            }

            for (Peca peca : pecasParaAtualizar) {
                Peca pecaEstoque = estoqueController.consultarPecaPorId(peca.getIdPeca());
                estoqueController.atualizarQuantidade(peca.getIdPeca(), pecaEstoque.getQuantidade() - peca.getQuantidade());
            }

            JOptionPane.showMessageDialog(this, "Ordem de Serviço salva com sucesso!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar OS: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
