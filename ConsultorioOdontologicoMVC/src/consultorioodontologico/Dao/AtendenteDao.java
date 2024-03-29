package consultorioodontologico.Dao;

import consultorioodontologico.Model.Atendente;
import consultorioodontologico.Model.Pessoa;
import consultorioodontologico.View.JanelaPrincipal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AtendenteDao extends Pessoa {

    private String login, senha;
    private double salario;
    private int idAtendente;

    public AtendenteDao() {
        super("", "", "", "", "");
    }

    public AtendenteDao(String nome, String cpf, String celular, String email, String endereco, double salario, String login, String senha) {
        super(nome, cpf, celular, email, endereco);
        this.senha = senha;
        this.salario = salario;
        this.login = login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setIdAtendente(int idAtendente) {
        this.idAtendente = idAtendente;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public double getSalario() {
        return salario;
    }

    public int getIdAtendente() {
        return idAtendente;
    }

    public void cadastrar(Atendente atendente) {
        try {
            atendente.cadastrarPessoa();
            atendente.pegaUltimoIdPessoa();

            String sql = "INSERT INTO atendente (salario,senha,login,cod_pessoa) VALUES ("
                    + "  " + atendente.getSalario() + ", "
                    + " '" + atendente.getSenha() + "', "
                    + " '" + atendente.getLogin() + "', "
                    + "  " + atendente.getIdPessoa() + "); ";

            ModuloConexao.executar(sql);
            
            JOptionPane.showMessageDialog(null, "Atendente cadastrado com sucesso.");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }

    }

    public void excluir(int idAtendente) {
        try {

            String sql = "DELETE FROM atendente WHERE idAtendente = " + idAtendente;
            ModuloConexao.executar(sql);

            this.excluirPessoa(this.getIdPessoa());

            JOptionPane.showMessageDialog(null, "Atendente excluiído com sucesso");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    public void editar(Atendente atendente) {
        try {
            atendente.editarPessoa();

            String sql = "UPDATE atendente SET "
                    + " salario = " + atendente.getSalario() + " , "
                    + " senha = '" + atendente.getSenha() + "',  "
                    + " login = '" + atendente.getLogin() + "'  "
                    + " Where idAtendente = " + atendente.getIdAtendente() + ";  ";
            ModuloConexao.executar(sql);
            JOptionPane.showMessageDialog(null, "Atendente atualizado com sucesso");
        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
        }
    }

    public ArrayList<Atendente> getAtendentes() {
        ArrayList<Atendente> lista = new ArrayList<>();
        String sql = "SELECT pes.nome,cpf,email,celular,endereco,aten.idAtendente,salario,login,senha,cod_pessoa FROM Pessoa pes INNER JOIN Atendente aten ON pes.idPessoa = aten.cod_pessoa";
        ResultSet rs = ModuloConexao.consultar(sql);
        if (rs != null) {
            try {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    String email = rs.getString("email");
                    String celular = rs.getString("celular");
                    String endereco = rs.getString("endereco");
                    double salario = rs.getDouble("salario");
                    String login = rs.getString("login");
                    String senha = rs.getString("senha");

                    Atendente Atend = new Atendente(nome, cpf, celular, email, endereco, salario, login, senha);
                    Atend.setIdAtendente(rs.getInt("idAtendente"));
                    Atend.setIdPessoa(rs.getInt("cod_pessoa"));
                    lista.add(Atend);
                }
            } catch (Exception e) {

            }
        }
        return lista;
    }

    public boolean logar(String user, String senha) {
        Connection conexao = null;
        conexao = ModuloConexao.conector();
        PreparedStatement pst = null;
        ResultSet rs = null;

        boolean verificador = false;
        String sql = "SELECT * FROM atendente WHERE login = ? AND senha = ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, senha);

            rs = pst.executeQuery();

            if (rs.next()) {
                verificador = true;
            } else {
                JOptionPane.showMessageDialog(null, "Usuário e/ou senha inválido(s)");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return verificador;
    }
    
    public ArrayList<Atendente> Pesquisar(String pesquisa) {
        ArrayList<Atendente> lista = new ArrayList<>();
            String sql = "Select P.nome, P.cpf, P.celular, P.email, P.endereco, P.endereco, A.salario, A.login, A.senha FROM Atendente A INNER JOIN Pessoa P on A.cod_pessoa = P.idPessoa WHERE P.nome LIKE '" + pesquisa + "%'"; 
        ResultSet rs = ModuloConexao.consultar(sql);
        if (rs != null) {
            try {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String cpf = rs.getString("cpf");
                    String celular = rs.getString("celular");
                    String email = rs.getString("email");
                    String endereco = rs.getString("endereco");
                    double salario = rs.getDouble("salario");
                    String login = rs.getString("login");
                    String senha = rs.getString("senha");

                    Atendente Aten = new Atendente(nome, cpf, celular, email, endereco, salario, login, senha);
                    Aten.setIdAtendente(rs.getInt("idAtendente"));
                    Aten.setIdPessoa(rs.getInt("cod_Pessoa"));
                    lista.add(Aten);
                }
            } catch (Exception e) {

            }
        }
        return lista;
    }
}
