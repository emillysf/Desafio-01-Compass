import Controle.Conexao;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {
    private int idProduto;
    private String Nome;
    private double Preco;
    private int Quantidade;
    private double Total;
    private Conexao con;
    private Produtos produtos;

    public Carrinho() {
        this(0,"",0.0,0,0.0);
        con = new Conexao();
        produtos = new Produtos(con);
    }

    public Carrinho(int idProduto, String nome, double preco, int quantidade, double total) {
        this.idProduto = idProduto;
        this.Nome = nome;
        this.Preco = preco;
        this.Quantidade = quantidade;
        this.Total = total;
    }

    public int getId() {
        return idProduto;
    }

    public void setId(int id) {
        this.idProduto = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public double getPreco() {
        return Preco;
    }

    public void setPreco(double preco) {
        Preco = preco;
    }

    public int getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(int quantidade) {
        Quantidade = quantidade;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }


    public void AdicionarItem() {
        List<Produtos> listaProdutos = produtos.listarProdutos();
        JComboBox<String> comboBox = new JComboBox<>();
        for (Produtos produto : listaProdutos) {
            comboBox.addItem("ID: " + produto.getId() + " | Nome: " + produto.getNome() + " | " + produto.getPreco());
        }
        Object[] message = {"Produtos Disponíveis:", comboBox};
        int option = JOptionPane.showConfirmDialog(null, message, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            int selectedIndex = comboBox.getSelectedIndex();
            Produtos produtoSelecionado = listaProdutos.get(selectedIndex);

            String quantidadeStr = JOptionPane.showInputDialog("Informe a quantidade do produto:");

            if (quantidadeStr != null) {
                try {
                    int quantidade = Integer.parseInt(quantidadeStr);

                    int estoque = produtoSelecionado.getQuantidade();

                    if (quantidade <= estoque) {
                        setId(produtoSelecionado.getId());
                        setNome(produtoSelecionado.getNome());
                        setPreco(produtoSelecionado.getPreco());
                        setQuantidade(quantidade);
                        setTotal(produtoSelecionado.getPreco() * quantidade);

                        String sql = "INSERT INTO Carrinho (IdProd, Quantidade) VALUES (" + produtoSelecionado.getId() + ", " + quantidade + ")";
                        con.executeSQL(sql);
                        JOptionPane.showMessageDialog(null, "Produto adicionado ao carrinho");
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantidade indisponível");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Quantidade inválida. Digite um valor numérico inteiro.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
            }
        }
    }

    public void listarItens() {
        String sql = "SELECT c.IdProd, p.nome, p.preco, c.quantidade FROM produtos p INNER JOIN carrinho c ON p.Id = c.IdProd";
        ResultSet resultado = null;
        double valorTotal = 0.0;

        try {
            resultado = con.RetornarResultset(sql);

            StringBuilder sb = new StringBuilder();
            sb.append("Itens do Carrinho:\n");

            while (resultado.next()) {
                int idProduto = resultado.getInt("IdProd");
                int quantidade = resultado.getInt("quantidade");
                String nomeProduto = resultado.getString("nome");
                double preco = resultado.getDouble("preco");
                double valorItem = preco * quantidade;
                valorTotal += valorItem;

                StringBuilder itemCarrinho = new StringBuilder();
                itemCarrinho.append("Produto: ").append(nomeProduto).append("\n");
                itemCarrinho.append("Quantidade: ").append(quantidade).append("\n");
                itemCarrinho.append("Valor Item: ").append(valorItem).append("\n");
                itemCarrinho.append("\n");

                sb.append(itemCarrinho.toString());
            }

            if (sb.length() > 0) {
                sb.append("Valor Total do Carrinho: ").append(valorTotal).append("\n");
                JOptionPane.showMessageDialog(null, sb.toString());
            } else {
                JOptionPane.showMessageDialog(null, "O carrinho está vazio.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Removeritem(){
        String idStr = JOptionPane.showInputDialog("Informe o ID do produto que deseja excluir:");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                int option = JOptionPane.showConfirmDialog(null, "Deseja excluir o produto?", "Confirmação", JOptionPane.OK_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    String sql = "DELETE FROM Carrinho WHERE IdProd = " + id;
                    con.executeSQL(sql);
                    JOptionPane.showMessageDialog(null, "Item excluído do carrinho!");
                } else {
                    JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Valor inválido. Forneça um número válido para o ID do produto.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário.");
        }
    }

    public void concluirCompra() {
        String sql = "SELECT COUNT(*) FROM Carrinho";
        ResultSet resultado = null;

        try {
            resultado = con.RetornarResultset(sql);

            if (resultado.next()) {
                int count = resultado.getInt(1);

                if (count == 0) {
                    JOptionPane.showMessageDialog(null, "O carrinho está vazio. Adicione itens antes de concluir a compra.");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        double valorTotal = calcularValorTotalCarrinho();

        String mensagem = "Deseja concluir a compra?\nValor Total: " + valorTotal;
        int option = JOptionPane.showConfirmDialog(null, mensagem, "Confirmação de Compra", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            List<Carrinho> itensCarrinho = obterItensCarrinho();
            for (Carrinho item : itensCarrinho) {
                int idProduto = item.getId();
                int novaQuantidade = item.getQuantidade();

                atualizarEstoque(idProduto, novaQuantidade);
            }

            limparCarrinho();

            JOptionPane.showMessageDialog(null, "Compra concluída com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Compra cancelada!");
        }
    }

    private double calcularValorTotalCarrinho() {
        String sql = "SELECT SUM(p.preco * c.quantidade) AS valorTotal FROM produtos p INNER JOIN carrinho c ON p.Id = c.IdProd";
        ResultSet resultado = null;
        double valorTotal = 0.0;

        try {
            resultado = con.RetornarResultset(sql);

            if (resultado.next()) {
                valorTotal = resultado.getDouble("valorTotal");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return valorTotal;
    }

    private List<Carrinho> obterItensCarrinho() {
        String sql = "SELECT c.IdProd, p.nome, p.preco, c.quantidade FROM produtos p INNER JOIN carrinho c ON p.Id = c.IdProd";
        ResultSet resultado = null;
        List<Carrinho> itensCarrinho = new ArrayList<>();

        try {
            resultado = con.RetornarResultset(sql);

            while (resultado.next()) {
                int idProduto = resultado.getInt("IdProd");
                int quantidade = resultado.getInt("quantidade");
                String nomeProduto = resultado.getString("nome");

                Carrinho item = new Carrinho(idProduto, nomeProduto, 0, quantidade, 0);
                itensCarrinho.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultado != null) {
                try {
                    resultado.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return itensCarrinho;
    }

    private void limparCarrinho() {
        String sql = "DELETE FROM Carrinho";
        con.executeSQL(sql);
    }

    public void atualizarEstoque(int idProduto, int novaQuantidade) {
        String sql = "UPDATE produtos SET Quantidade = ? WHERE id = ?";

        try {
            boolean conexaoEstabelecida = con.conecta();

            if (conexaoEstabelecida) {
                PreparedStatement stmt = con.statement.getConnection().prepareStatement(sql);
                stmt.setInt(1, novaQuantidade);
                stmt.setInt(2, idProduto);
                stmt.executeUpdate();
                con.desconecta();
            } else {
                JOptionPane.showMessageDialog(null,"Não foi possível estabelecer a conexão com o banco de dados.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
