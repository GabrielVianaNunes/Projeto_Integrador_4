package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;

public class MenuPrincipalView extends JFrame {

    private VeiculoController veiculoController;
    private ClienteController clienteController;
    private ModeloController modeloController;
    private MarcaController marcaController;
    private FuncionarioController funcionarioController;
    private EstoqueController estoqueController;
    private ServicoController servicoController;
    private OrdemServicoController ordemServicoController;

    // Botões para ajuste de tamanho
    private JButton cadastroClienteButton;
    private JButton cadastroVeiculoButton;
    private JButton consultaEstoqueButton;
    private JButton cadastroServicoButton;
    private JButton listarOSButton;

    public MenuPrincipalView(Connection connection) {
        setTitle("Menu Principal");
        setSize(600, 400); // Tamanho inicial correto
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicialização dos controladores
        this.marcaController = new MarcaController(connection);
        this.modeloController = new ModeloController(connection);
        this.clienteController = new ClienteController(connection);
        this.veiculoController = new VeiculoController(connection, clienteController, modeloController);
        this.funcionarioController = new FuncionarioController(connection);
        this.estoqueController = new EstoqueController(connection);
        this.servicoController = new ServicoController(connection);
        this.ordemServicoController = new OrdemServicoController(veiculoController);

        // Painel principal com BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Painel estático superior
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titulo = new JLabel("Mecânica Viana", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        headerPanel.add(titulo);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Painel central para organizar os botões verticalmente
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);

        // Inicializar botões
        cadastroClienteButton = new JButton("Cadastro de Cliente");
        cadastroVeiculoButton = new JButton("Cadastro de Veículo");
        consultaEstoqueButton = new JButton("Consulta de Estoque");
        cadastroServicoButton = new JButton("Cadastro de Serviço");
        listarOSButton = new JButton("Listar OS");

        // Alinhamento central dos botões
        cadastroClienteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastroVeiculoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        consultaEstoqueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cadastroServicoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        listarOSButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adicionar botões ao painel com espaçamento
        buttonPanel.add(Box.createVerticalGlue()); // Espaçamento superior
        buttonPanel.add(cadastroClienteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento entre botões
        buttonPanel.add(cadastroVeiculoButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(consultaEstoqueButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(cadastroServicoButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(listarOSButton);
        buttonPanel.add(Box.createVerticalGlue()); // Espaçamento inferior

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Configuração dos botões
        cadastroClienteButton.addActionListener(e -> new CadastroClienteView().setVisible(true));

        cadastroVeiculoButton.addActionListener(e -> {
            CadastroVeiculoView cadastroVeiculoView = new CadastroVeiculoView(
                    veiculoController,
                    clienteController,
                    modeloController,
                    marcaController
            );
            cadastroVeiculoView.setVisible(true);
        });

        consultaEstoqueButton.addActionListener(e -> {
            ConsultaEstoqueView consultaEstoqueView = new ConsultaEstoqueView(estoqueController);
            consultaEstoqueView.setVisible(true);
        });

        cadastroServicoButton.addActionListener(e -> {
            CadastroServicoView cadastroServicoView = new CadastroServicoView(servicoController);
            cadastroServicoView.setVisible(true);
        });

        listarOSButton.addActionListener(e -> {
            ListarOrdemServicoView listarView = new ListarOrdemServicoView(
                    ordemServicoController,
                    funcionarioController,
                    estoqueController,
                    servicoController,
                    clienteController,
                    veiculoController
            );
            listarView.setVisible(true);
        });

        // Ajustar tamanho inicial dos botões
        ajustarTamanhoDosBotoes(false);

        // Listener para ajustar o tamanho dos botões ao redimensionar
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ajustarTamanhoDosBotoes(isTelaCheia());
            }
        });
    }

    // Método para verificar se a tela está cheia
    private boolean isTelaCheia() {
        return getExtendedState() == JFrame.MAXIMIZED_BOTH;
    }

    // Método ajustado para redimensionar os botões com base no estado da tela
    private void ajustarTamanhoDosBotoes(boolean telaCheia) {
        Dimension tamanho = telaCheia ? new Dimension(350, 60) : new Dimension(200, 40);
        JButton[] botoes = {cadastroClienteButton, cadastroVeiculoButton, consultaEstoqueButton, cadastroServicoButton, listarOSButton};
        for (JButton botao : botoes) {
            botao.setPreferredSize(tamanho);
            botao.setMinimumSize(tamanho);
            botao.setMaximumSize(tamanho);
            botao.revalidate();
        }
        revalidate();
        repaint();
    }
}
