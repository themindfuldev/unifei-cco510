import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Matricula extends JFrame implements ActionListener {
	
    Statement stmt;
    //ResultSet rs;
    JTextField codAluno, codAlunoCons, codDiscip;
    JButton bMat, bCons1, bCons2;
    //JComboBox comboCodAluno, comboNomeAluno, comboCodDiscip, comboNomeDisp;
    JTextArea disciplinas, alunos;
	
    public Matricula(){
        super("Trabalhando com chaves extrangeiras");      
        String hostname, database, user, pass;
	hostname = "localhost";
	database = "escola";
	user	 = "root";
	pass	 = "mysqlbal";
	
	try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection c;        
            c = DriverManager.getConnection ("jdbc:mysql://"+hostname+"/"+database, user, pass);
            stmt = c.createStatement();
        }        
        catch (Exception e){
        	JOptionPane.showMessageDialog(null,"Aplicativo não conectado ao banco"
            	+" de dados.");
        }		
    }
        
    private void geraInterface(){
                        
        JPanel pMat = new JPanel();
        pMat.setSize(10,30);
        pMat.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 80));
        pMat.add(new JLabel("Código do Aluno: "));
        pMat.add(codAluno = new JTextField(5));
        pMat.add(new JLabel("Código da Disciplina: "));
        pMat.add(codDiscip = new JTextField(5)); 
        pMat.add(bMat = new JButton("Matricula"));     
        bMat.addActionListener(this);    
        
        JPanel pCons1 = new JPanel();
        pCons1.setSize(10,30);
        pCons1.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30));
        pCons1.add(new JLabel("Código do Aluno: "));
        pCons1.add(codAlunoCons = new JTextField(5));
        pCons1.add(bCons1 = new JButton("Consulta"));   
        pCons1.add(disciplinas = new JTextArea(12,40));
        bCons1.addActionListener(this);        
        
        
        JPanel pCons2 = new JPanel();
        
	JTabbedPane tab = new JTabbedPane( JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);			
	tab.addTab( "Matrícula", pMat );
        tab.addTab( "Consulta Aluno", pCons1 );
        tab.addTab( "Consulta Disciplina", pCons2 );
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	getContentPane().setLayout(new BorderLayout());
	setSize(500,400);
	setLocation(400,250);
	setResizable(false);
	setDefaultLookAndFeelDecorated(true);
        getContentPane().add(tab, BorderLayout.CENTER);        		
        setVisible(true);	
    }

    private String getNomeDiscip(Object cod) throws Exception{
        ResultSet rs;
        rs = stmt.executeQuery("SELECT nome FROM Discip WHERE codigo=" + cod.toString() + ";");
        if(rs.next())
            return rs.getString(1);
        return "";
    }
    
    public void actionPerformed(ActionEvent e){
        ResultSet rs;
        if((JButton)e.getSource() == bMat)
            try{               
                stmt.executeUpdate("INSERT INTO Matricula VALUES (" + codAluno.getText() + "," + codDiscip.getText() +");");                
                JOptionPane.showMessageDialog(null, "Aluno matriculado!");
                codAluno.setText("");
                codDiscip.setText("");                
            }catch (SQLException ex){
                switch(ex.getErrorCode()){                    
                    case 1062: JOptionPane.showMessageDialog(null, "O aluno já está matriculado nesta disciplina");
                               break;
                    case 1216: JOptionPane.showMessageDialog(null, "Aluno ou disciplina inexistente");
                               break;
                    case 1064: JOptionPane.showMessageDialog(null, "Não é permitido deixar campos em branco");
                               break;
                    default:   JOptionPane.showMessageDialog(null, "Oooops! Erro inesperado");                    
                }
            }
        else
            if((JButton)e.getSource() == bCons1)
                try{
                    rs = stmt.executeQuery("SELECT * FROM Aluno WHERE codigo=" + codAluno.getText() + ";");
                    if(!rs.next())
                        disciplinas.setText("Aluno " + codAlunoCons.getText() + " inexistente");
                    else{
                        
                        rs = stmt.executeQuery("SELECT codDiscip FROM Matricula WHERE codAluno=" + codAlunoCons.getText() + ";");
                        String strDiscip = "";
                        Vector vCod = new Vector();
                        while(rs.next())
                            vCod.add(rs.getInt(1));
                        Iterator i = vCod.iterator();
                        while(i.hasNext())
                            strDiscip = strDiscip + getNomeDiscip(i.next()) + "\n";
                        if(strDiscip.equals(""))
                            disciplinas.setText("Aluno " + codAlunoCons.getText() + " sem nenhuma matrícula em disciplinas");                        
                        else
                            disciplinas.setText("Aluno se encontra matriculado nas seguintes disciplinas:\n" + strDiscip);
                            
                    }                                                
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }                                
    }
	
	
    public static void main(String args[]){		
        Matricula prog = new Matricula();		
	prog.geraInterface();	
    }
}	
