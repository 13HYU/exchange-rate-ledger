package ledger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

class RandomString {

     private static final char[] symbols ;

     static {
       StringBuilder tmp = new StringBuilder();
       for (char ch = '0'; ch <= '9'; ++ch)
         tmp.append(ch);
       for (char ch = 'a'; ch <= 'z'; ++ch)
         tmp.append(ch);
       symbols = tmp.toString().toCharArray();
     }   

     private final Random random = new Random();

     public final char[] buf;

     public RandomString(int length) {
       if (length < 1)
         throw new IllegalArgumentException("length < 1: " + length);
       buf = new char[length];
     }

     public String nextString() {
       for (int idx = 0; idx < buf.length; ++idx) 
         buf[idx] = symbols[random.nextInt(symbols.length)];
       return new String(buf);
     }
   }



public class Password_loss {
   Connection con;
   Statement stmt;
   ResultSet rs;
   String username;
   String email;
   String email_add;
   String full_add;
   String question;
   String answer;
   
   String mailServer = null;
   Message message = null;
   String sender = null;
   String senderName = null;
   String subject = null;
   String content = null;
   String receiver = null;
   
   Address senderAddress = null;
   Address[] receiverAddress = null;
   
   public Password_loss(){}

   public Password_loss(String mailServer){
      
      setMailServer(mailServer);
      
      JFrame frame = new JFrame("Find Password");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
      JPanel panel = new JPanel();
      
      
      JLabel username_label = new JLabel("Username");
      panel.add(username_label);
      JTextField username_text = new JTextField(20);
      panel.add(username_text);
      
      
      
      String[] questionlist = {
              "What's your favorite color?",
              "What's the name of your middle-school teacher?",
              "What's the name of your pet?",
              "What's the name of the most impressive book?",
              "What's your mother's name?",
              "Whta's your father's name?",
              "Where were you born?"
        };
        
        
        JLabel questionLabel = new JLabel("Question  ");
        questionLabel.setBounds(10, 40, 80, 25);
        panel.add(questionLabel);
        
        
        JComboBox questionBox = new JComboBox(questionlist);
        questionBox.setBounds(100, 40, 160, 25);
        panel.add(questionBox);
        
        JLabel answerLabel = new JLabel(" Answer   ");
        answerLabel.setBounds(10, 40, 80, 25);
        panel.add(answerLabel);
        
        JTextField answerText = new JTextField(20);
        answerText.setBounds(100, 40, 160, 25);
        panel.add(answerText);
      
        
      String[] emailList = {
              "@naver.com",
              "@hanmail.net",
              "@gmail.com",
              "Type directly"
        };
        
        
      JLabel emailLabel = new JLabel("       Email");
      emailLabel.setBounds(10, 40, 80, 25);
      panel.add(emailLabel);
      
      JTextField emailaddr = new JTextField(15);
      panel.add(emailaddr);
      
      JComboBox addrBox = new JComboBox(emailList);
      panel.add(addrBox);
      
      JButton submit = new JButton("Find Password");
      panel.add(submit);
      
      submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               username = username_text.getText();
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
             question = questionBox.getSelectedItem().toString();
              answer= answerText.getText();
              find_password();
                       
            }
        });
   
      
      
      frame.add(panel);
      
      frame.setSize(350,200);
      frame.setResizable(false);
      frame.setLocation(700,400);
      frame.setVisible(true);
   }
   
   private void find_password(){
      try{
           Class.forName("com.mysql.jdbc.Driver");
           System.out.println("mysql 로딩완료");
           
           con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
           System.out.println("데이터베이스 연결 성공");
           stmt = con.createStatement();
           String sql = "select username from account where username='"+username+"' and email='"+full_add+"' and question = '"+question+"' and answer='"+answer+"'";
           rs = stmt.executeQuery(sql);
           if(!rs.next()){
              JOptionPane.showMessageDialog(null, "Can't find your password","Find Failure", JOptionPane.PLAIN_MESSAGE);
           }
           else{
              this.setMailServer("smtp.gmail.com");
             this.setSender("hyledger@gmail.com");
             this.setSenderName("My Ledger");
             this.setReceiver(full_add);
             this.setSubject("Guide Password");
             RandomString rand = new RandomString(8);
             String new_pw = rand.buf.toString().substring(3);
             this.setContent("Your password is "+new_pw+".");
             String sql2 = "update account set password = '"+new_pw+"' where username = '"+username+"'";
             stmt.executeUpdate(sql2);
             try{
                this.SendMail();
             }
             catch(UnsupportedEncodingException e){
                e.printStackTrace();
             }
             catch(MessagingException e){
                e.printStackTrace();
             }
             JOptionPane.showMessageDialog(null, "Temporary password has sent to your email.","Temporary Password", JOptionPane.PLAIN_MESSAGE);
              
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
      
         
   }
   public void SendMail() throws UnsupportedEncodingException, MessagingException{
      if(sender==null || subject == null || content==null|| receiver==null)
         throw new NullPointerException("sender, subject, content, receiver is null");
      
      initializeMailServer();
      initializeSender();
      initializeReceiver();
      
      Send();
   }
   
   private void initializeMailServer(){
      Properties properties = new Properties();
      properties.put("mail.smtp.host", mailServer);
      properties.put("mail.transport.protocol", "smtp");
      properties.put("mail.smtp.starttls.enable", "true");
      properties.put("mail.smtp.host","smtp.gmail.com");
      properties.put("mail.smtp.port", "25");
      properties.put("mail.smtp.auth", "true");
      javax.mail.Authenticator authenticator = new javax.mail.Authenticator(){
       protected javax.mail.PasswordAuthentication getPasswordAuthentication() 
           {
           return new javax.mail.PasswordAuthentication("hyledger", "ledgerledger");
       }
       };
      message = new MimeMessage(Session.getInstance(properties, authenticator));
   }
   
   private void initializeSender() throws UnsupportedEncodingException{
      if(senderName == null)
         senderName = sender;
      senderAddress = new InternetAddress(sender, MimeUtility.encodeText(senderName, "UTF-8", "B"));
   }
   
   private void initializeReceiver() throws AddressException{
      ArrayList<String> receiverList = new ArrayList<String>();
      StringTokenizer stMailAddress = new StringTokenizer(receiver, ";");
      while(stMailAddress.hasMoreTokens()){
         receiverList.add(stMailAddress.nextToken());
      }
      
      receiverAddress = new Address[receiverList.size()];
      for(int i = 0; i < receiverList.size(); i++){
         receiverAddress[i] = new InternetAddress(receiverList.get(i));
      }
   }
   
   private void Send() throws MessagingException, UnsupportedEncodingException{
      message.setHeader("content-type", "text/html;charset=UTF-8");
      message.setFrom(senderAddress);
      message.setRecipients(Message.RecipientType.TO, receiverAddress);
      message.setSubject(MimeUtility.encodeText(subject, "UTF-8","B"));
      message.setContent(content, "text/html;charset=UTF-8");
      message.setSentDate(new java.util.Date());
      
      Transport.send(message);
   }
   
   public void setMailServer(String mailServer){
      this.mailServer = mailServer;
   }
   
   public void setSender(String sender){
      this.sender = sender;
   }
   
   public void setSubject(String subject){
      this.subject = subject;
   }
   
   public void setContent(String content){
      this.content = content;
   }
   
   public void setReceiver(String receiver){
      this.receiver = receiver;
   }
   
   public void setSenderName(String senderName){
      this.senderName = senderName;
   }
}