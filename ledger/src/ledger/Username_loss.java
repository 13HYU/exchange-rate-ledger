package ledger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Username_loss {
   Connection con;
   Statement stmt;
   ResultSet rs;
   
   String fullname;
   String email;
   String email_add;
   String full_add;

   public Username_loss(){
      
      JFrame frame = new JFrame("Forgot Username");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      JPanel panel = new JPanel();
      
      JLabel fullnamelabel = new JLabel("Fullname");
      JTextField fullname_text= new JTextField(25);
      
      panel.add(fullnamelabel);
      panel.add(fullname_text);
      
      String[] emailList = {
              "@naver.com",
              "@hanmail.net",
              "@gmail.com",
              "Type directly"
        };
        
        
      JLabel emailLabel = new JLabel("       Email");
      emailLabel.setBounds(10, 40, 80, 25);
      panel.add(emailLabel);
      
      JTextField emailaddr = new JTextField(25);
      panel.add(emailaddr);
      
      
       JComboBox addrBox = new JComboBox(emailList);
      panel.add(addrBox);
      
      JButton submit = new JButton("Find username");
      panel.add(submit);
      submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               fullname = fullname_text.getText();
                email_add = addrBox.getSelectedItem().toString();
               if(email_add.equals("Type directly")){
                 email = emailaddr.getText();
                 full_add = email;
              }
              else{
                 email = emailaddr.getText();
                 full_add = email+email_add;
                 System.out.println(full_add);
              }
              
              find_username();
                             
            }
        });
      
   
      
      frame.add(panel);
      
      frame.setSize(400,200);
      frame.setResizable(false);
      frame.setLocation(700,400);
      frame.setVisible(true);
   }
   
   
   private void find_username(){
      try{
           Class.forName("com.mysql.jdbc.Driver");
           System.out.println("mysql 로딩완료");
           
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
           System.out.println("데이터베이스 연결 성공");
           stmt = con.createStatement();
           String sql = "select username from account where fullname='"+fullname+"' and email='"+full_add+"'";
           rs = stmt.executeQuery(sql);
           if(!rs.next()){
              JOptionPane.showMessageDialog(null, "Can't find matching username.","Failure", JOptionPane.PLAIN_MESSAGE);
           }
           else
              JOptionPane.showMessageDialog(null,"Your username is "+rs.getString("username")+". " ,"Success",JOptionPane.PLAIN_MESSAGE);
        }
        
        catch(ClassNotFoundException ex1){
          System.out.println("mysql드라이버 찾을 수 없음");
          }
       catch(SQLException ex2){
          System.out.println("sql실패");
          }
       catch(Exception ex3){
          System.out.println(ex3.toString());
          }
       finally{
          System.out.println("DB연결 성공!");
       }
      
   }

}