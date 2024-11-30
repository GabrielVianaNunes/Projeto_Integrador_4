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
    
    private JTextField buscaNomeField, buscaCpfCnpjField;
    private JButton buscarButton;

    public CadastroOSView(FuncionarioController funcionarioController, EstoqueController estoqueController, ServicoController servicoController, ClienteController clienteController) {
    this.funcionarioController = funcionarioController;
    this.estoqueController = estoqueController;
    this.servicoController = servicoController;
    this.clienteController = clienteController;
    this.pecasParaAtualizar = new ArrayList<>();

    setTitle("Cadastro de Ordem de Serviço");
    setSize(900, 700);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    // Layout principal
    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Painel de busca no topo (ajustado para ocupar menos espaço)
    JPanel searchPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Apenas uma linha agora
    searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Cliente"));

    buscaNomeField = new JTextField();
    searchPanel.add(new JLabel("Nome:"));
    searchPanel.add(buscaNomeField);

    buscaCpfCnpjField = new JTextField();
    searchPanel.add(new JLabel("CPF/CNPJ:"));
    searchPanel.add(buscaCpfCnpjField);

    buscarButton = new JButton("Buscar");
    buscarButton.addActionListener(e -> buscarCliente());
    searchPanel.add(buscarButton);

    // Painel de informações gerais
    JPanel infoPanel = new JPanel(new GridLayout(8, 2, 10, 10));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Informações Gerais"));

    clienteComboBox = new JComboBox<>();
    clienteComboBox.addActionListener(e -> preencherCamposCliente());
    infoPanel.add(new JLabel("Cliente:"));
    infoPanel.add(clienteComboBox);

    nomeField = new JTextField();
    nomeField.setEditable(false);
    infoPanel.add(new JLabel("Nome:"));
    infoPanel.add(nomeField);

    cpfField = new JTextField();
    cpfField.setEditable(false);
    infoPanel.add(new JLabel("CPF:"));
    infoPanel.add(cpfField);

    cnpjField = new JTextField();
    cnpjField.setEditable(false);
    infoPanel.add(new JLabel("CNPJ:"));
    infoPanel.add(cnpjField);

    telefoneField = new JTextField();
    telefoneField.setEditable(false);
    infoPanel.add(new JLabel("Telefone:"));
    infoPanel.add(telefoneField);

    emailField = new JTextField();
    emailField.setEditable(false);
    infoPanel.add(new JLabel("Email:"));
    infoPanel.add(emailField);

    enderecoField = new JTextField();
    enderecoField.setEditable(false);
    infoPanel.add(new JLabel("Endereço:"));
    infoPanel.add(enderecoField);

    cepField = new JTextField();
    cepField.setEditable(false);
    infoPanel.add(new JLabel("CEP:"));
    infoPanel.add(cepField);

    veiculoField = new JTextField();
    infoPanel.add(new JLabel("Veículo (Placa):"));
    infoPanel.add(veiculoField);

    descricaoField = new JTextField();
    infoPanel.add(new JLabel("Descrição:"));
    infoPanel.add(descricaoField);

    JPanel tecnicoPanel = new JPanel(new BorderLayout());
    tecnicoComboBox = new JComboBox<>();
    carregarFuncionarios();
    tecnicoPanel.add(tecnicoComboBox, BorderLayout.CENTER);

    novoFuncionarioButton = new JButton("+");
    novoFuncionarioButton.addActionListener(e -> cadastrarNovoFuncionario());
    tecnicoPanel.add(novoFuncionarioButton, BorderLayout.EAST);

    infoPanel.add(new JLabel("Técnico:"));
    infoPanel.add(tecnicoPanel);

    statusField = new JTextField("Aberta");
    infoPanel.add(new JLabel("Status:"));
    infoPanel.add(statusField);

    dataField = new JTextField();
    infoPanel.add(new JLabel("Data:"));
    infoPanel.add(dataField);

    // Painel intermediário para empilhar searchPanel e infoPanel
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
    topPanel.add(searchPanel);
    topPanel.add(infoPanel);

    // Adicionar o painel intermediário ao topo do mainPanel
    mainPanel.add(topPanel, BorderLayout.NORTH);

    // Painel Central: Tabelas de Itens (com ajustes para mais espaço)
    JPanel itensPanel = new JPanel(new BorderLayout(10, 10));
    itensPanel.setBorder(BorderFactory.createTitledBorder("Itens da O.S."));

    // Painel de Serviços
    JPanel servicoPanel = new JPanel(new BorderLayout(10, 10));
    servicoPanel.setBorder(BorderFactory.createTitledBorder("Serviços"));

    itensServicoTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"Descrição", "Quantidade", "Preço Unitário", "Subtotal"}));
    JScrollPane servicoScrollPane = new JScrollPane(itensServicoTable);
    servicoScrollPane.setPreferredSize(new Dimension(850, 200)); // Ajuste de altura
    servicoPanel.add(servicoScrollPane, BorderLayout.CENTER);

    JPanel servicoButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    adicionarServicoButton = new JButton("Adicionar Serviço");
    adicionarServicoButton.addActionListener(e -> adicionarServico());
    excluirServicoButton = new JButton("Excluir Serviço");
    excluirServicoButton.addActionListener(e -> excluirServico());
    servicoButtonsPanel.add(adicionarServicoButton);
    servicoButtonsPanel.add(excluirServicoButton);

    servicoPanel.add(servicoButtonsPanel, BorderLayout.SOUTH);

    // Painel de Peças
    JPanel pecaPanel = new JPanel(new BorderLayout(10, 10));
    pecaPanel.setBorder(BorderFactory.createTitledBorder("Peças"));

    itensPecaTable = new JTable(new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Descrição", "Quantidade", "Preço Unitário", "Subtotal"}));
    JScrollPane pecaScrollPane = new JScrollPane(itensPecaTable);
    pecaScrollPane.setPreferredSize(new Dimension(850, 200)); // Ajuste de altura
    pecaPanel.add(pecaScrollPane, BorderLayout.CENTER);

    JPanel pecaButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    adicionarPecaButton = new JButton("Adicionar Peça");
    adicionarPecaButton.addActionListener(e -> adicionarPeca());
    excluirPecaButton = new JButton("Excluir Peça");
    excluirPecaButton.addActionListener(e -> excluirPeca());
    pecaButtonsPanel.add(adicionarPecaButton);
    pecaButtonsPanel.add(excluirPecaButton);

    pecaPanel.add(pecaButtonsPanel, BorderLayout.SOUTH);

    // Divisor Dinâmico
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, servicoPanel, pecaPanel);
    splitPane.setOneTouchExpandable(true); // Adiciona controles para expandir/retrair
    splitPane.setDividerLocation(350); // Define a posição inicial do divisor
    splitPane.setResizeWeight(0.5); // Prioriza a divisão igual entre os painéis
    splitPane.setContinuousLayout(true); // Suaviza o redimensionamento
    itensPanel.add(splitPane, BorderLayout.CENTER);

    // Adicionar o painel de itens ao centro do mainPanel
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
    
    private void buscarCliente() {
        String nomeBusca = buscaNomeField.getText().trim();
        String cpfCnpjBusca = buscaCpfCnpjField.getText().trim();

        List<Cliente> clientesEncontrados = new ArrayList<>();

        // Busca por nome
        if (!nomeBusca.isEmpty()) {
            clientesEncontrados = clienteController.buscarClientesPorNome(nomeBusca);
        } 
        // Busca por CPF/CNPJ
        else if (!cpfCnpjBusca.isEmpty()) {
            Cliente cliente = clienteController.buscarClienteCPFouCNPJ(cpfCnpjBusca);
            if (cliente != null) {
                clientesEncontrados.add(cliente);
            }
        }

        // Atualiza os dados na ComboBox
        clienteComboBox.removeAllItems();
        for (Cliente cliente : clientesEncontrados) {
            clienteComboBox.addItem(cliente);
        }

        if (clientesEncontrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
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
            telefoneField.setText(clienteSelecionado.getTelefone());
            emailField.setText(clienteSelecionado.getEmail());
            enderecoField.setText(clienteSelecionado.getEndereco());
            cepField.setText(clienteSelecionado.getCep());
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
