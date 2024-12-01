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

    public MenuPrincipalView(Connection connection) {
        setTitle("Menu Principal");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicialização dos controladores
        this.marcaController = new MarcaController(connection);
        this.modeloController = new ModeloController(connection);
        this.clienteController = new ClienteController(connection);
        this.veiculoController = new VeiculoController(connection);
        this.funcionarioController = new FuncionarioController(connection);
        this.estoqueController = new EstoqueController(connection); 
        this.servicoController = new ServicoController(connection); 
        this.ordemServicoController = new OrdemServicoController(veiculoController);

        JPanel panel = new JPanel();
        JButton cadastroClienteButton = new JButton("Cadastro de Cliente");
        JButton cadastroVeiculoButton = new JButton("Cadastro de Veículo");
        JButton consultaEstoqueButton = new JButton("Consulta de Estoque");
        JButton cadastroOSButton = new JButton("Cadastro de OS");
        JButton cadastroServicoButton = new JButton("Cadastro de Serviço"); // Novo botão para cadastro de serviço
        JButton listarOSButton = new JButton("Listar OS");



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

        cadastroOSButton.addActionListener(e -> {
            CadastroOSView cadastroOSView = new CadastroOSView(funcionarioController, estoqueController, servicoController, clienteController);
            cadastroOSView.setVisible(true);
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
                clienteController
            );
            listarView.setVisible(true);
        });


        panel.add(cadastroClienteButton);
        panel.add(cadastroVeiculoButton);
        panel.add(consultaEstoqueButton);
        panel.add(cadastroServicoButton); 
        panel.add(listarOSButton);

        add(panel);
    }
}
