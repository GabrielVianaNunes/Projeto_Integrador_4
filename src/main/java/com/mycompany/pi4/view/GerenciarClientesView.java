package com.mycompany.pi4.view;

import com.mycompany.pi4.entity.Cliente;
import com.mycompany.pi4.entity.PessoaFisica;
import com.mycompany.pi4.entity.PessoaJuridica;
import com.mycompany.pi4.repositories.ClienteRepository;
import com.mycompany.pi4.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GerenciarClientesView extends JFrame {

    private JTable tabelaClientes;
    private ClienteRepository clienteRepository;

    public GerenciarClientesView(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;

        setTitle("Gerenciar Clientes");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel titulo = new JLabel("Gerenciar Clientes", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 102, 204));
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Tabela de clientes
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Nome", "Telefone", "Email", "CPF/CNPJ", "Tipo"});
        tabelaClientes = new JTable(model);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        carregarClientes(model);

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton propriedadesButton = new JButton("Propriedades");
        propriedadesButton.addActionListener(e -> {
            int selectedRow = tabelaClientes.getSelectedRow();
            if (selectedRow != -1) {
                int idCliente = (int) tabelaClientes.getValueAt(selectedRow, 0); // Assume que a coluna 0 é o ID do cliente
                GerenciarVeiculosView gerenciarVeiculosView = new GerenciarVeiculosView(idCliente);
                gerenciarVeiculosView.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um cliente para visualizar as propriedades.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(propriedadesButton);

        JButton editarButton = new JButton("Editar");
        editarButton.setBackground(new Color(0, 153, 0));
        editarButton.setForeground(Color.WHITE);
        editarButton.addActionListener(e -> editarCliente(model));
        buttonPanel.add(editarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBackground(new Color(204, 0, 0));
        excluirButton.setForeground(Color.WHITE);
        excluirButton.addActionListener(e -> excluirCliente(model));
        buttonPanel.add(excluirButton);

        JButton fecharButton = new JButton("Fechar");
        fecharButton.setBackground(new Color(128, 128, 128));
        fecharButton.setForeground(Color.WHITE);
        fecharButton.addActionListener(e -> dispose());
        buttonPanel.add(fecharButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void carregarClientes(DefaultTableModel model) {
        model.setRowCount(0); // Limpa a tabela
        try {
            List<Cliente> clientes = clienteRepository.listarTodos();
            for (Cliente cliente : clientes) {
                String cpfCnpj = cliente instanceof PessoaFisica ? ((PessoaFisica) cliente).getCpf() : ((PessoaJuridica) cliente).getCnpj();
                model.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNome(),
                    cliente.getTelefone(),
                    cliente.getEmail(),
                    cpfCnpj,
                    cliente instanceof PessoaFisica ? "PF" : "PJ"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCliente(DefaultTableModel model) {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection == null || connection.isClosed()) {
                JOptionPane.showMessageDialog(this, "Conexão com o banco de dados não está ativa.", "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ClienteRepository clienteRepository = new ClienteRepository(connection);

            int idCliente = (int) tabelaClientes.getValueAt(selectedRow, 0); // Buscar pelo ID
            Cliente cliente = clienteRepository.buscarPorId(idCliente); // Novo método buscarPorId no repositório
            if (cliente == null) {
                JOptionPane.showMessageDialog(this, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CadastroClienteView cadastroClienteView = new CadastroClienteView();
            cadastroClienteView.setVisible(true);
            cadastroClienteView.preencherCamposComCliente(cliente);

            // Listener para salvar alterações após edição
            cadastroClienteView.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    try {
                        clienteRepository.atualizar(cliente); // Método atualizar para editar cliente no repositório
                        carregarClientes(model); // Atualizar tabela
                        JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso!");
                    } catch (SQLException ex) {
                        // Não exibir mensagem para o usuário, apenas registrar no terminal
                        System.err.println("Erro ao atualizar cliente: " + ex.getMessage());
                    }
                }
            });
        } catch (SQLException ex) {
            // Registrar erro no terminal e não exibir mensagem
            System.err.println("Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    private void excluirCliente(DefaultTableModel model) {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int clienteId = (int) tabelaClientes.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            clienteRepository.excluir(clienteId); // Usa o método de exclusão implementado
            model.removeRow(selectedRow); // Remove a linha da tabela
            JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
        }
    }
}
