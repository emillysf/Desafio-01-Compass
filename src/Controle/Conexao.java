package Controle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class Conexao {
    final private String driver = "com.mysql.jdbc.Driver";

    final private String url= "jdbc:mysql://localhost/ecommerce";

    final private String usuario="root";
    final private String senha="";
    private Connection conexao;// objeto que faz conexao com o banco
    public Statement statement;// objeto que abre caminho até o banco
    public ResultSet resultset;// objeto que armazena os comandos sql

    public boolean conecta() {
        boolean result = true;

        try {
            Class.forName(driver);
            conexao = (Connection) DriverManager.getConnection(url,usuario,senha);
            //JOptionPane.showMessageDialog(null,"Conectou com o Banco de Dados");

        } catch(ClassNotFoundException Driver){
            JOptionPane.showMessageDialog(null,"Driver nao localizado: "+Driver);
            result = false;
        }catch(SQLException Fonte) {
            JOptionPane.showMessageDialog(null,"Erro na conexão com a fonte de dados: "+Fonte);
            result = false;
        }
        return result;
    }

    public void desconecta (){
        boolean result = true;
        try
        {
            conexao.close();
            //JOptionPane.showMessageDialog(null,"Banco fechado");
        }
        catch(SQLException fecha)
        {
            JOptionPane.showMessageDialog(null,"Não foi possivel fechar o banco de dados"+ fecha);
            result = false;
        }
    }

    public ResultSet executeSQL(String sql){
    //chamada do metodo conecta para abrir a conexão com o db
        conecta();
        try{

            statement = (Statement) conexao.createStatement();

            statement.execute(sql);
            //desconecta()
        }catch(SQLException sqle){
            JOptionPane.showMessageDialog(null, "Driver não encontrado1" + sqle.getMessage());
        }
        return null;
    }

    public ResultSet RetornarResultset(String sql){
        ResultSet resultSet = null;
        conecta();
        try{
            statement = (Statement) conexao.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao retornar resultset"+e.getMessage());
        }
        return resultSet;
    }
}

