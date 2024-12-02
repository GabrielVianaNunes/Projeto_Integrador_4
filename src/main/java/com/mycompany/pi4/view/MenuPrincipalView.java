package com.mycompany.pi4.view;

import com.mycompany.pi4.controllers.ClienteController;
import com.mycompany.pi4.controllers.ModeloController;
import com.mycompany.pi4.controllers.VeiculoController;
import com.mycompany.pi4.controllers.MarcaController;
import com.mycompany.pi4.controllers.FuncionarioController;
import com.mycompany.pi4.controllers.EstoqueController;
import com.mycompany.pi4.controllers.ServicoController;
import com.mycompany.pi4.controllers.OrdemServicoController;

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
    private JButton cadastroOSButton;
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

        // Painel principal para organizar os botões verticalmente
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Define layout vertical

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
        panel.add(cadastroClienteButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        panel.add(cadastroVeiculoButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        panel.add(consultaEstoqueButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        panel.add(cadastroServicoButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento vertical
        panel.add(listarOSButton);

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
            ConsultaEstoqueView consultaEstoqueView = new ConsultaEstoqueView(estoqueController); // Passando EstoqueController
            consultaEstoqueView.setVisible(true);
        });


        cadastroServicoButton.addActionListener(e -> {
            CadastroServicoView cadastroServicoView = new CadastroServicoView(servicoController); // Passando ServicoController
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

        add(panel);

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
        Dimension tamanho = telaCheia ? new Dimension(350, 60) : new Dimension(150, 40); // Tamanhos ajustados
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
