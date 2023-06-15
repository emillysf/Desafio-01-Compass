import Controle.Conexao;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Conexao con = new Conexao();
        Produtos prod = new Produtos(con);

        int op;

        do {
            op = Integer.parseInt(JOptionPane.showInputDialog("""
                    Digite a opção:
                     1- Listar Produto
                     2- Cadastrar Produto
                     3- Atualizar Produto
                     4- Excluir Produto
                     0- Sair"""));
            switch (op) {
                case 1:
                    List<Produtos> lista = prod.listarProdutos();
                    StringBuilder sb = new StringBuilder();
                    for (Produtos produto : lista) {
                        sb.append("ID: ").append(produto.getId()).append("\n");
                        sb.append("Nome: ").append(produto.getNome()).append("\n");
                        sb.append("Preço: ").append(produto.getPreco()).append("\n");
                        sb.append("Quantidade: ").append(produto.getQuantidade()).append("\n");
                        sb.append("\n");
                    }
                    JOptionPane.showMessageDialog(null, sb.toString());
                    break;
                case 2:
                    prod.cadastrarProduto();
                    break;
                case 3:
                    prod.atualizarProduto();
                    break;
                case 4:
                    prod.excluirProduto();
                    break;
                case 0:
                    JOptionPane.showMessageDialog(null, "Finalizando o programa");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção Inválida!");
                    break;
            }
        } while (op != 0);
    }


}