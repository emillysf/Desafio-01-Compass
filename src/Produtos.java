import Controle.Conexao;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Produtos {

    private Conexao con;

    public Produtos(Conexao conexao) {
        this.con = conexao;
    }
    private int Id;
    private String Nome;
    private double Preco;
    private int Quantidade;

    public Produtos() {
        this(0,"",0.0,0);
    }

    public Produtos(int id, String nome, double preco, int quantidade) {
        Id = id;
        Nome = nome;
        Preco = preco;
        Quantidade = quantidade;
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
        String idStr = JOptionPane.showInputDialog("Informe o ID do produto:");
        int id = Integer.parseInt(idStr);
        setId(id);

        String nome = JOptionPane.showInputDialog("Informe o nome do produto:");
        setNome(nome);

        String precoStr = JOptionPane.showInputDialog("Informe o preço do produto:");
        double preco = Double.parseDouble(precoStr);
        setPreco(preco);

        String quantidadeStr = JOptionPane.showInputDialog("Informe a quantidade do produto:");
        int quantidade = Integer.parseInt(quantidadeStr);
        setQuantidade(quantidade);

        String sql = "INSERT INTO Produtos(Id, Nome, Preco, Quantidade) VALUES " +
                "(" + getId() +", '" + getNome() + "', " + getPreco() + ", " + getQuantidade() + " )";
        con.executeSQL(sql);
        JOptionPane.showMessageDialog(null, "Produto Cadastrado com Sucesso!");
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
        String sql = "DELETE FROM Produtos WHERE Id =" + getId();
        con.executeSQL(sql);
        JOptionPane.showMessageDialog(null, "Produto excluido com sucesso!");
    }




}
