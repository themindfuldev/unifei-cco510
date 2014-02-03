import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConsultaBanco extends JFrame implements ActionListener {
    Statement stmt;
    JComboBox comboCodigo, comboNome;
    JTextArea dadosAluno;
    
    public ConsultaBanco() {
        super("Consulta Banco de Dados");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection c = DriverManager.getConnection
                ("jdbc:mysql://localhost/Alunos", "teste", "teste1234");
            stmt = c.createStatement();
        } catch (Exception e){
            System.err.println(e);
        }
    }
    
    private void geraInterface(){
        ResultSet rs;
        comboCodigo = new JComboBox();
        try{
            rs = stmt.executeQuery("SELECT codigo FROM Aluno");
            while(rs.next())
                comboCodigo.addItem(rs.getInt(1));
            comboCodigo.addActionListener(this);
            comboNome = new JComboBox();
            rs = stmt.executeQuery("SELECT nome FROM Aluno");
            while(rs.next())
                comboNome.addItem(rs.getString(1));   
            comboNome.addActionListener(this);
        }catch(Exception e){
            e.printStackTrace();
        }

        JPanel pCombo = new JPanel();
        pCombo.setLayout(new FlowLayout(FlowLayout.CENTER, 10,  30));        
        pCombo.add(new JLabel("Codigo:"));
        pCombo.add(comboCodigo);
        pCombo.add(new JLabel("                   "));
        pCombo.add(new JLabel("Nome:"));
        pCombo.add(comboNome);
        dadosAluno = new JTextArea(7, 40);        
        String query = new String("SELECT * FROM Aluno WHERE codigo = '" + 
                                       comboCodigo.getSelectedItem() + "'");
        doQuery(query);                
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(pCombo, BorderLayout.PAGE_START);
        contentPane.add(dadosAluno, BorderLayout.PAGE_END);
        pack();
        setVisible(true);        
    }
    
    private void doQuery(String query){
        ResultSet rs;
        try{
            rs = stmt.executeQuery(query);
            while(rs.next()){
                dadosAluno.setText("\nCodigo: " + rs.getObject(1).toString() + "\n" +
                                  "Nome: " + rs.getObject(2) + "\n" +
                                  "RG: " + rs.getObject(3) + "\n" +
                                  "Nasc: " + rs.getObject(4).toString() + "\n" +
                                  "Fone: " + rs.getObject(5));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }    
    }
    
    
    public void actionPerformed(ActionEvent e){
        ResultSet rs;
        String query;
        if (e.getSource() == comboCodigo){
            comboNome.setSelectedIndex(comboCodigo.getSelectedIndex());
            query = new String("SELECT * FROM Aluno WHERE codigo = '" + 
                                       comboCodigo.getSelectedItem() + "'");
            doQuery(query);
        }
        else {
            comboCodigo.setSelectedIndex(comboNome.getSelectedIndex());
            query = new String("SELECT * FROM Aluno WHERE nome = '" + 
                                       comboNome.getSelectedItem() + "'");
            doQuery(query);            
        }
    }    
    
    public static void main(String args[]){    
        ConsultaBanco consulta = new ConsultaBanco();
        consulta.geraInterface();    
        
    }
    
}
