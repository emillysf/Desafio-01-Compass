import Controle.Conexao;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Conexao con = new Conexao();
        Produtos prod = new Produtos(con);
        Carrinho car = new Carrinho();

        int func;
        int op;

        do {
            func = Integer.parseInt(JOptionPane.showInputDialog(
                    "Digite a opção:\n" +
                    " 1- Gestão de Produtos\n" +
                    " 2- Compras\n" +
                    " 0- Sair"));
            switch (func){
                case 1:
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
                                    sb.append("ID: ").append(produto.getId()).append("|");
                                    sb.append(" ").append(produto.getNome()).append(" - ");
                                    sb.append("R$").append(produto.getPreco()).append("\n");
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
                    break;
                case 2:
                    do {
                        op = Integer.parseInt(JOptionPane.showInputDialog("""
                                Digite a opção:
                                 1- Comprar produto
                                 2- Itens no meu carrinho
                                 3- Remover itens do carrinho
                                 4- Concluir compra 
                                 0- Sair"""));
                        switch(op) {
                            case 1:
                                car.AdicionarItem();
                                break;
                            case 2:
                                car.listarItens();
                                break;
                            case 3:
                                car.Removeritem();
                                break;
                            case 4:
                                car.concluirCompra();
                                break;
                            case 0:
                                JOptionPane.showMessageDialog(null, "Finalizando o programa");
                                break;
                        }
                    } while (op != 0);
            }
        }while (func != 0);
    }


}