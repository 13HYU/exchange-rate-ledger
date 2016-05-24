package ledger;

import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.util.Vector;

public class Input implements ActionListener{
   public static String username;
   Vector<String> userCol = new Vector<String>();
   Vector<String> userRow = new Vector<String>(); 
   Connection con;
    Statement stmt;
   public int year;
   public int month;
   public int date;
   MemoCalendar memocalendar;
   JTable table1;
   DefaultTableModel model;
   private JScrollPane scrollPane;
   
JLabel inc = new JLabel("  ");
JLabel exp = new JLabel("  ");
JLabel result = new JLabel(" ");
public int incsum, expsum;


   JFrame f;
   

   public Input (String u, int y, int m, int d) {
       this.username = u;
        this.year = y;
        this.month = m;
        this.date = d;
       f= new JFrame("Input Income/Expense");
       f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       JPanel p0 = new JPanel();
       
       JLabel todayLab;
       todayLab = new JLabel(Integer.toString(month+1)+"/"+date+"/"+year);
       JButton New = new JButton("new");
       New.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              DefaultTableModel model = (DefaultTableModel)table1.getModel();
              model.setNumRows(0);
               select();   
           }
       });
       
       Font font = new Font("SansSerif",Font.BOLD,40);
       todayLab.setFont(font);
       p0.setLayout(new BorderLayout());
       p0.add(todayLab, BorderLayout.CENTER);
       p0.add(New, BorderLayout.EAST);

       try {
          Connection con = DriverManager.getConnection(jdbcUrl, userId, userPass); 
          Statement stmt = con.createStatement();
          stmt.close();
          con.close();
       } catch (SQLException e) { 
          System.exit(0); 
          }
     

      userCol.addElement("date"); 
      userCol.addElement("inc/exp"); 
      userCol.addElement("category"); 
      userCol.addElement("details"); 
      userCol.addElement("in foreign currency");
      userCol.addElement("currency"); 
      userCol.addElement("in Won");
      model = (new DefaultTableModel(userCol, 0) {
         /**
       * 
       */
      private static final long serialVersionUID = 1L;

      @Override 
          public boolean isCellEditable(int row, int column) { 
             // 셀의 편집을 막기 위한 부분 
             return false; 
         }
      });
      
      table1 = new JTable(model);

//db 연결   
       JPanel p1 = new JPanel();
//       p1.setLayout(null);
       p1.setLayout(new BorderLayout());
       scrollPane = new JScrollPane(table1);
       scrollPane.setSize(900, 300);
       p1.add(scrollPane, BorderLayout.CENTER);
       select();        
        ///////////////////////////
             
       JPanel pp = new JPanel();
       pp.setLayout(new FlowLayout());
       pp.add(inc);
       pp.add(exp);
       pp.add(result);
       p1.add(pp,  BorderLayout.SOUTH);

       JPanel p2 = new JPanel();
       
       JButton btn1 = new JButton("Income");
       btn1.setPreferredSize(new Dimension(120, 50)); 
//       btn1.setBackground(new Color(50, 100, 200));
       btn1.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                  InputIncome input = new InputIncome(username, year, month, date);

             }
         });
        btn1.setForeground(new Color(50, 100, 200));
        
       JButton btn2 = new JButton("Expense");
       btn2.setPreferredSize(new Dimension(120, 50)); 
//       btn2.setBackground(new Color(50, 100, 200));
        btn2.setForeground(new Color(50, 100, 200));
        btn2.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                  InputExpense input = new InputExpense(username, year, month, date);

   //             getContentPane().removeAll();
    //             getContentPane().add(input2);
     //            revalidate();
      //           repaint();
             }
         });

       p2.add(btn1);
       p2.add(btn2);

       
//       p1.add(table1);
       f.setLayout(new BorderLayout());
       f.add(p0, BorderLayout.NORTH);
       f.add(p1, BorderLayout.CENTER);
       f.add(p2, BorderLayout.SOUTH);



       f.setSize(1000,600);
         f.setVisible(true);    
   }
   
    @Override
    public void actionPerformed(ActionEvent e) {
    }
    String jdbcUrl = "jdbc:mysql://localhost:3306/test_db"; 
    String userId = "root"; 
    String userPass = "0zero6six"; 
    
    public void select() {
       int month2 = month+1;
       try(
             Connection con = DriverManager.getConnection(jdbcUrl, userId, userPass); 
             Statement stmt = con.createStatement();
             
             ResultSet rs = stmt.executeQuery("Select date, inc_or_exp, category, details, sum, currency, wonsum from "+username+" where date like '"+month2+"/"+date+"/"+year+"'");)
              { while(rs.next())
             {
                 userRow = new Vector<String>(); 
                 userRow.addElement(rs.getString(1));
                 userRow.addElement(rs.getString(2)); 
                 userRow.addElement(rs.getString(3)); 
                 userRow.addElement(rs.getString(4)); 
                 userRow.addElement(rs.getString(5)); 
                 userRow.addElement(rs.getString(6)); 
                 userRow.addElement(rs.getString(7)); 
                 model.addRow(userRow);

             }
              stmt.close();
              con.close();
       }catch(Exception e) {
          System.out.println(e.getMessage());
          }
   /////////////
       try(
               Connection con = DriverManager.getConnection(jdbcUrl, userId, userPass); 
               Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("Select sum(wonsum) as inc_sum from "+username+" where inc_or_exp = 'income' and date like '"+month2+"/"+date+"/"+year+"'");)
       { while(rs.next()){
          incsum = rs.getInt("inc_sum");
          inc.setText("총수입: "+incsum+"원  ");
          }
      } catch(Exception e) {
             System.out.println(e.getMessage());
      }
       
       try(
               Connection con = DriverManager.getConnection(jdbcUrl, userId, userPass); 
               Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("Select sum(wonsum) as exp_sum from "+username+" where inc_or_exp = 'expense' and date like '"+month2+"/"+date+"/"+year+"'");)
       { while(rs.next()){
          expsum = rs.getInt("exp_sum");
          exp.setText("총지출: "+expsum+"원   ");
          }
      } catch(Exception e) {
             System.out.println(e.getMessage());
      }
       int all = incsum-expsum;
       result.setText("총액: "+all+"원" );

   
       
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               new MemoCalendar(username);
            }
         });    
    }
}