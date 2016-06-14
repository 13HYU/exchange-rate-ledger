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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Settings {
   String username;
   Connection con;
    Statement stmt;
   ResultSet rs;
   String fullname;
   String password;
   String email;
   String question;
   String answer;

   
   public Settings(String u){
      this.username = u;
      try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("mysql 로딩완료");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
            System.out.println("데이터베이스 연결 성공");
            stmt = con.createStatement();
            String sql = "select * from account where username='"+username+"'";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
               fullname = rs.getString("fullname");
               password = rs.getString("password");
               email = rs.getString("email");
               question = rs.getString("question");
               answer = rs.getString("answer");
            }
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
    
      
      
      JFrame frame = new JFrame();
       JPanel panel = new JPanel();
        
        JLabel fullnameLabel = new JLabel("Fullname");
        panel.add(fullnameLabel);
       
        JTextField fullnameText = new JTextField(20);
        fullnameText.setText(fullname);
        panel.add(fullnameText);

        JLabel passLabel = new JLabel("Password");
        panel.add(passLabel);  
        
        JTextField passText = new JPasswordField(20);
        passText.setText(password);
        panel.add(passText);
        
        JLabel repassLabel = new JLabel("PW Again");
        panel.add(repassLabel);  
        
        JTextField repassText = new JPasswordField(20);
        repassText.setBounds(100, 40, 160, 25);
        repassText.setText(password);
        panel.add(repassText);
        
        JLabel emailLabel = new JLabel("Email address");
        emailLabel.setBounds(10, 40, 80, 25);
        panel.add(emailLabel);  
        
        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 40, 160, 25);
        emailText.setText(email);
        panel.add(emailText);
        
        String[] questionlist = {
                "What's your favorite color?",
                "What's the name of your middle-school teacher?",
                "What's the name of your pet?",
                "What's the name of the most impressive book?",
                "What's your mother's name?",
                "Whta's your father's name?",
                "Where were you born?"
        };
        
        
        JLabel questionLabel = new JLabel("Question");
        panel.add(questionLabel);  
        
        
        JComboBox questionBox = new JComboBox(questionlist);
        questionBox.setSelectedItem(question);
        panel.add(questionBox);
        
        
        JLabel answerLabel = new JLabel("Answer");
        panel.add(answerLabel);  
        
        JTextField answerText = new JTextField(20);
        answerText.setText(answer);
        panel.add(answerText);
        
        JButton submit = new JButton("Save");
        panel.add(submit);
        
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String fullname;
               String password;
               String email;
               String question;
               String answer;
               String repassword;
               
               fullname = fullnameText.getText();
               password = passText.getText();
               repassword = repassText.getText();
               
               email = emailText.getText();
               question = questionBox.getSelectedItem().toString();
               answer = answerText.getText();
                
                if(!password.equals(repassword)){
                   passText.setText("");
                   repassText.setText("");
                   passText.hasFocus();
                   JOptionPane.showMessageDialog(null, "Passwords didn't match","Wrong Password", JOptionPane.PLAIN_MESSAGE);
                   
                }
                
                if(password.equals(repassword) && !(fullname.equals("")||password.equals("")||repassword.equals("")||email.equals("")||answer.equals("")))
                     JOptionPane.showMessageDialog(null, "Modifications are saved","Success", JOptionPane.PLAIN_MESSAGE);
                  else
                     JOptionPane.showMessageDialog(null, "Please fill all the blanks","Null", JOptionPane.PLAIN_MESSAGE);

                                   
                
                
                try{
                     Class.forName("com.mysql.jdbc.Driver");
                     System.out.println("mysql 로딩완료");
                     con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
                     System.out.println("데이터베이스 연결 성공");
                     stmt = con.createStatement();
                     String sql = "update account set fullname='"+fullname+"' , password='"+password+"', email='"+email+"', question='"+question+"', answer=+'"+answer+"' where username='"+username+"'";
                     stmt.executeUpdate(sql);
                     
                    
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
        });
        
        frame.add(panel);
        frame.setSize(380, 450);
        frame.setVisible(true);
        
        

   }
}