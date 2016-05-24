package ledger;

import java.sql.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class LoginView implements ActionListener{
 //MainProcess main;
 JButton button_reset, button_login, button_signup, button_forgot;
 CardLayout cardLayoutSet;
 JPanel cardPanel;
 //JFrame frame;
 Connection con;
 Statement stmt;
 ResultSet rs;

 public LoginView(){}
 
 public Container panel1(){
     JPanel panel = new JPanel();

     JLabel userLabel = new JLabel("Username");
     userLabel.setBounds(10, 10, 80, 25);
     panel.add(userLabel);
    
     JTextField userText = new JTextField(20);
     userText.setBounds(100, 10, 160, 25);
     panel.add(userText);

     JLabel passLabel = new JLabel("Password");
     passLabel.setBounds(10, 40, 80, 25);
     panel.add(passLabel);  
     
     JTextField passText = new JPasswordField(20);
     passText.setBounds(100, 40, 160, 25);
     panel.add(passText);

    
     return panel;

 }
 
 public Container panel2(){

  JPanel panel = new JPanel();

  JLabel userLabel = new JLabel("Username");
  userLabel.setBounds(10, 10, 80, 25);
  panel.add(userLabel);
 
  JTextField userText = new JTextField(20);
  userText.setBounds(100, 10, 160, 25);
  panel.add(userText);

  JLabel passLabel = new JLabel("Password");
  passLabel.setBounds(10, 40, 80, 25);
  panel.add(passLabel);  
  
  JTextField passText = new JPasswordField(20);
  passText.setBounds(100, 40, 160, 25);
  panel.add(passText);
  
  JButton btnInit = new JButton("다시 입력");
  btnInit.setBounds(10, 80, 100, 25);
  panel.add(btnInit);
  btnInit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          userText.setText("");
          passText.setText("");
      }
  });
  
  
  JButton login_submit = new JButton("로그인");
  login_submit.setBounds(10, 80, 100, 25);
  panel.add(login_submit);
  login_submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         String username;
         String password;
          username = userText.getText();
          password = passText.getText();
          String result;
          try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("mysql 로딩완료");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
            System.out.println("데이터베이스 연결 성공");
            stmt = con.createStatement();
            String sql = "select fullname from account where username ='"+username+"' and password='"+password+"'";
            rs = stmt.executeQuery(sql);
            if(!rs.next())
              JOptionPane.showMessageDialog(null, "패스워드 틀렸습니다","로그인 실패", JOptionPane.PLAIN_MESSAGE);
            else{
               result = rs.getString("fullname");
               if(result!="")
                  JOptionPane.showMessageDialog(null, "메인화면으로 이동합니다","로그인 성공", JOptionPane.PLAIN_MESSAGE);
                  userText.setText("");
                  passText.setText("");
                  frame.setVisible(false);
                  MemoCalendar memoCalendar = new MemoCalendar(username);
                  
               }
            rs.close();
             stmt.close();
             con.close();
             
         }
         
        catch(ClassNotFoundException ex1){
           System.out.println("mysql드라이버 찾을 수 없음");
           }
        catch(SQLException ex2){
           System.out.println("sql 실패");
           }
        catch(Exception ex3){
           System.out.println(ex3.toString());
           }
        finally{
           System.out.println("DB연결 성공!");
        }

          
          
      }
  });
 
  return panel;
  
 }

 public Container panel3(){
  JPanel panel = new JPanel();
  
  JLabel fullnameLabel = new JLabel("Fullname ");
  fullnameLabel.setBounds(10, 10, 80, 25);
  panel.add(fullnameLabel);
 
  JTextField fullnameText = new JTextField(20);
  fullnameText.setBounds(100, 10, 160, 25);
  panel.add(fullnameText);

  JLabel userLabel = new JLabel("Username");
  userLabel.setBounds(10, 10, 80, 25);
  panel.add(userLabel);
  
  JTextField userText = new JTextField(20);
  userText.setBounds(100, 10, 160, 25);
  userText.setColumns(15);
  panel.add(userText);

  JButton duplicate = new JButton("중복검사");
  panel.add(duplicate);
  
  JLabel passLabel = new JLabel("Password");
  passLabel.setBounds(10, 40, 80, 25);
  panel.add(passLabel);  
  
  JTextField passText = new JPasswordField(20);
  passText.setBounds(100, 40, 160, 25);
  panel.add(passText);
  
  JLabel repassLabel = new JLabel("PW Again");
  passLabel.setBounds(10, 40, 80, 25);
  panel.add(repassLabel);  
  
  JTextField repassText = new JPasswordField(20);
  repassText.setBounds(100, 40, 160, 25);
  panel.add(repassText);
  
  JLabel emailLabel = new JLabel("Email address");
  emailLabel.setBounds(10, 40, 80, 25);
  panel.add(emailLabel);  
  
  JTextField emailText = new JTextField(20);
  emailText.setBounds(100, 40, 160, 25);
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
  
  
  JLabel questionLabel = new JLabel("Question  ");
  questionLabel.setBounds(10, 40, 80, 25);
  panel.add(questionLabel);  
  
  
  JComboBox questionBox = new JComboBox(questionlist);
  questionBox.setBounds(100, 40, 160, 25);
  panel.add(questionBox);
  
  
  JLabel answerLabel = new JLabel("Answer    ");
  answerLabel.setBounds(10, 40, 80, 25);
  panel.add(answerLabel);  
  
  JTextField answerText = new JTextField(20);
  answerText.setBounds(100, 40, 160, 25);
  panel.add(answerText);
  
  JButton btnInit = new JButton("다시 입력");
  btnInit.setBounds(10, 80, 100, 25);
  panel.add(btnInit);
  
  btnInit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         fullnameText.setText("");
          userText.setText("");
          passText.setText("");
          repassText.setText("");
          emailText.setText("");
          answerText.setText("");
      }
  });
  
  
  JButton submit = new JButton("가입");
  btnInit.setBounds(10, 80, 100, 25);
  panel.add(submit);
  
  submit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         String fullname;
         String username;
         String password;
         String email;
         String question;
         String answer;
         
         fullname = fullnameText.getText();
          username = userText.getText();
          password = passText.getText();
          email = emailText.getText();
          question = questionBox.getSelectedItem().toString();
          answer = answerText.getText();
          
          try{
               Class.forName("com.mysql.jdbc.Driver");
               System.out.println("mysql 로딩완료");
               con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
               System.out.println("데이터베이스 연결 성공");
               stmt = con.createStatement();
               String sql1 = "show tables";
               stmt.executeUpdate(sql1);
               rs = stmt.executeQuery(sql1);
               if(!rs.next()){
                  String sql_account = "create table account(fullname varchar(30) not null, username varchar(20) not null, password varchar(20) not null,email varchar(320) not null,question text not null,answer varchar(30) not null,primary key (username))";
                  stmt.executeUpdate(sql_account);
               }
               
               String sql2 = "insert into account values('"+fullname+"',"+"'"+username+"',"+"'"+password+"',"+"'"+email+"',"+"'"+question+"',"+"'"+answer+"')";
               stmt.executeUpdate(sql2);
               String sql3 = "create table "+username+" (itemnum int not null primary key auto_increment,date varchar(15) not null,inc_or_exp varchar(20) not null,category varchar(30) not null,details varchar(60) not null,rate float not null,currency varchar(10) not null,sum float not null,wonsum float not null)";
               stmt.executeUpdate(sql3);
               
               
               JOptionPane.showMessageDialog(null, "회원가입 성공!!","회원가입성공", JOptionPane.PLAIN_MESSAGE);
               
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
          fullnameText.setText("");
          userText.setText("");
          passText.setText("");
          repassText.setText("");
          emailText.setText("");
          answerText.setText("");          
          
      }
  });
  
  
  return panel;
  
 }
 
 public Container makeCardLayout(){
  cardLayoutSet = new CardLayout();
  cardPanel = new JPanel();
  // 일반 패널인 cardPanel을 카드 레이아웃인 cardLayoutSet으로 설정하였다
  cardPanel.setLayout(cardLayoutSet);
  LoginView test = new LoginView();
  // 이름을 설정
  cardPanel.add("card2", test.panel2());
  cardPanel.add("card1", test.panel1());
  cardPanel.add("card3", test.panel3());
  JPanel panel = new JPanel(new BorderLayout());
  panel.add(cardPanel);
  
  
  
  button_login = new JButton("로그인 화면");
  // button_login.setBounds(10, 80, 100, 25);
  button_signup = new JButton("새 계정");
  //button_signup.setBounds(10, 80, 100, 25);
  //button_forgot = new JButton("아이디/비밀번호 분실");
  //button_forgot.setBounds(10, 80, 100, 25);
  JPanel buttonLayout = new JPanel(new BorderLayout());
  buttonLayout.add(button_login,BorderLayout.BEFORE_FIRST_LINE);
  buttonLayout.add(button_signup,BorderLayout.AFTER_LAST_LINE);
  //buttonLayout.add(button_forgot, BorderLayout.WEST);
  panel.add(buttonLayout, BorderLayout.PAGE_END);
  
  button_login.addActionListener(this);
  button_signup.addActionListener(this);
  panel.add(buttonLayout, BorderLayout.PAGE_END);
  
  return panel;
 }
 JFrame frame= new JFrame("로그인");
 public void createFrame(){
  
  frame.setSize(350, 450);
  frame.setResizable(false);
  frame.setLocation(700, 250);
  frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  frame.add(makeCardLayout());
  frame.setVisible(true);
 }
 
 @Override
 public void actionPerformed(ActionEvent e) {
  // TODO Auto-generated method stub
  // 카드 레이아웃인 부모를 선택하고 카드 레이아웃으로 설정한 자식을 선택하고,
  // 자식의 자식인 불러올 패널의 이름을 적는다
  
  if(e.getSource() == button_login)
   cardLayoutSet.show(cardPanel, "card2");
  else if(e.getSource() == button_signup)
   cardLayoutSet.show(cardPanel, "card3");;
 }
 
 public static void main(String[] args) {
  // TODO Auto-generated method stub
  LoginView test = new LoginView();
  
  test.createFrame();
 }


 
}