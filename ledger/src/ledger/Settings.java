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
		        "좋아하는 색깔은 무엇입니까?",
		        "중학교 1학년 담임선생님 이름은?",
		        "내 애완동물 이름은?",
		        "감명깊게 읽은 책은?",
		        "어머니 이름은 무엇입니까?",
		        "아버지 이름은 무엇입니까?",
		        "태어난 고향은 어디입니까?"
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
		  
		  JButton submit = new JButton("수정완료");
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
		        	  JOptionPane.showMessageDialog(null, "비밀번호를 다시 입력해주십시오","비밀번호가 다릅니다", JOptionPane.PLAIN_MESSAGE);
		        	  
		          }
		          
		          if(password.equals(repassword) && !(fullname.equals("")||password.equals("")||repassword.equals("")||email.equals("")||answer.equals("")))
	            	   JOptionPane.showMessageDialog(null, "수정이 완료되었습니다","수정 성공", JOptionPane.PLAIN_MESSAGE);
	               else
	            	   JOptionPane.showMessageDialog(null, "빈 칸을 모두 채워주십시오","빈 칸", JOptionPane.PLAIN_MESSAGE);

		          		        	  
		          
		          
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
