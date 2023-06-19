import Controle.Conexao;
import java.sql.PreparedStatement;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class Produtos {
    private int Id;
    private String Nome;
    private double Preco;
    private int Quantidade;
    private Conexao con;
    public Produtos(Conexao conexao) {
        this.con = conexao;
    }

    public Produtos() {
        this(0,"",0.0,0);
    }

    public Produtos(int id, String nome, double preco, int quantidade) {
        this.Id = id;
        this.Nome = nome;
        this.Preco = preco;
        this.Quantidade = quantidade;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public double getPreco() {
        return Preco;
    }

    public void setPreco(double preco) {
        this.Preco = preco;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.Quantidade = quantidade;
    }

    public void cadastrarProduto() {
        try {
            String idStr = JOptionPane.showInputDialog("Informe o ID do produto:");
            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                setId(id);
            } else {
                JOptionPane.showMessageDialog(null, "ID inválido.");
                return;
            }

            String nome = JOptionPane.showInputDialog("Informe o nome do produto:");
            if (nome == null || nome.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nome inválido.");
                return;
            }
            setNome(nome);

            String precoStr = JOptionPane.showInputDialog("Informe o preço do produto:");
            if (precoStr != null && !precoStr.isEmpty()) {
                double preco = Double.parseDouble(precoStr);
                setPreco(preco);
            } else {
                JOptionPane.showMessageDialog(null, "Preço inválido.");
                return;
            }

            String quantidadeStr = JOptionPane.showInputDialog("Informe a quantidade do produto:");
            if (quantidadeStr != null && !quantidadeStr.isEmpty()) {
                int quantidade = Integer.parseInt(quantidadeStr);
                setQuantidade(quantidade);
            } else {
                JOptionPane.showMessageDialog(null, "Quantidade inválida.");
                return;
            }

            int option = JOptionPane.showConfirmDialog(null, "Deseja cadastrar o produto?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String sql = "INSERT INTO Produtos(Id, Nome, Preco, Quantidade) VALUES " +
                        "(" + getId() + ", '" + getNome() + "', " + getPreco() + ", " + getQuantidade() + ")";
                con.executeSQL(sql);
                JOptionPane.showMessageDialog(null, "Produto Cadastrado com Sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valor inválido. Fonreça um número válido para ID, Preço e Quantidade.");
        }
    }

    public void atualizarProduto() {
        String idStr = JOptionPane.showInputDialog("Informe o ID do produto que deseja atualizar:");
        int id = Integer.parseInt(idStr);

        String nome = JOptionPane.showInputDialog("Informe o novo nome do produto:");
        setNome(nome);

        String precoStr = JOptionPane.showInputDialog("Informe o novo preço do produto:");
        double preco = Double.parseDouble(precoStr);
        setPreco(preco);

        String quantidadeStr = JOptionPane.showInputDialog("Informe a nova quantidade do produto:");
        int quantidade = Integer.parseInt(quantidadeStr);
        setQuantidade(quantidade);

        String sql = "UPDATE Produtos SET Nome = '" + getNome() + "', Preco = " + getPreco() +
                ", Quantidade = " + getQuantidade() + " WHERE Id = " + id;
        con.executeSQL(sql);
        JOptionPane.showMessageDialog(null, "Produto Atualizado com Sucesso!");
    }

    public List<Produtos> listarProdutos() {
        List<Produtos> listaProdutos = new ArrayList<>();

        String sql = "SELECT * FROM Produtos";

        ResultSet resultado = con.RetornarResultset(sql);
        if (resultado != null) {
            try {
                while (resultado.next()) {
                    int id = resultado.getInt("Id");
                    String nome = resultado.getString("Nome");
                    double preco = resultado.getDouble("Preco");
                    int quantidade = resultado.getInt("Quantidade");

                    Produtos produto = new Produtos(id, nome, preco, quantidade);
                    listaProdutos.add(produto);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return listaProdutos;
    }

    public void excluirProduto(){
        String idStr = JOptionPane.showInputDialog("Informe o ID do produto que deseja excluir:");
        int id = Integer.parseInt(idStr);

        String sql = "DELETE FROM Produtos WHERE Id =" + id;
        con.executeSQL(sql);
        JOptionPane.showMessageDialog(null, "Produto excluido com sucesso!");
    }


    public int obterQuantidadeDisponivel(int id) {
        String sql = "SELECT Quantidade FROM Produtos WHERE Id=" + id;
        ResultSet resultSet = con.RetornarResultset(sql);

        int quantidade = 0;

        try {
            if (resultSet.next()) {
                quantidade = resultSet.getInt("Quantidade");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return quantidade;
    }



}
