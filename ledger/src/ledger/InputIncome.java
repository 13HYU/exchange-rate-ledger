package ledger;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;

import javax.swing.*;

public class InputIncome implements ActionListener {
   public static String username;
   Connection con;
    Statement stmt;
    ResultSet rs = null;
   public int year;
   public int month;
   public int date;
   MemoCalendar memocalendar;
   
   public void showMemo(){
      f.setVisible(false); // 테스트프레임 오픈
      }      
      
   static Calendar today = Calendar.getInstance();
   static JLabel todayLab;
   JFrame f;
   JButton detailBut;
   
   
   public static void main(String[] args){
      SwingUtilities.invokeLater(new Runnable(){
         public void run(){
            new MemoCalendar(username);
         }
      });
   }
   
   public InputIncome(String u, int y, int m, int d) {
       this.username = u;
        this.year = y;
        this.month = m;
        this.date = d;
        f= new JFrame("Input Income");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      //프레임
      
      JPanel p0 = new JPanel();
      todayLab = new JLabel(Integer.toString(month+1)+"/"+date+"/"+year);

      Font font = new Font("SansSerif",Font.BOLD,40);
      todayLab.setFont(font);
      
      JPanel p1 = new JPanel();

      Choice income = new Choice();
      income.add("Salary");
      income.add("Allowance");
      income.add("Balance from last month");
      income.add("Other");
      
      income.setSize(200, 150);
      income.setLocation(500, 300);
      
   
      JPanel p2 = new JPanel();
      
      Label lsum = new Label("Amount ", Label.RIGHT);
      TextArea tsum= new TextArea("", 1,16,TextArea.SCROLLBARS_NONE);
      tsum.setEditable(true);


      Choice currency = new Choice();
      currency.add("KRW");
      currency.add("USD");
      currency.add("EUR");
      currency.add("JPY");
      currency.add("CNY");
      currency.add("AUD");
      currency.add("NZD");
      currency.add("GBP");
      currency.add("CAD");
      currency.add("PHP");
      currency.add("HKD");
      currency.add("THB");
      currency.add("SGD");
      currency.add("INR");
      currency.add("BRL");
      currency.add("TWD");
      currency.add("MYR");
      currency.add("CHF");
      currency.add("VND");
      currency.add("RUB");
      currency.add("IDR");
      currency.add("BDT");
      currency.add("SEK");
      currency.add("NOK");
      currency.add("ZAR");
      
      JPanel p3 = new JPanel();
      
      TextArea ta= new TextArea("Details of your income", 7,30,TextArea.SCROLLBARS_NONE);
      ta.setEditable(true);
         //입력한 내용 반환
      JButton btn2 = new JButton("add");
      btn2.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e){
             System.out.println("입력된 돈은 "+tsum.getText()+", 단위는 "+ currency.getSelectedItem()+" 가(이) 입력되었습니다.");
             System.out.println("카테고리는 "+income.getSelectedItem()+"가(이) 선택되었습니다.");
             System.out.println("세부사항으로는 "+ta.getText()+" 가(이) 입력되었습니다.");
             
             try{
                 Class.forName("com.mysql.jdbc.Driver");
                 System.out.println("mysql 로딩완료");
                 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
                 System.out.println("데이터베이스 연결 성공");
                 stmt = con.createStatement();
                 int month1=month+1;
                 String selectedcurr = currency.getSelectedItem();
                 UrlDownload urldownload = new UrlDownload(year, month1, date);
                 ExchangeRate exchangerate = new ExchangeRate(selectedcurr ,year, month1, date);
                 //exchangerate.KRW 
                 float rate;
                 if(exchangerate.currency.equals("USD")){
                    rate = exchangerate.won;                    
                 }
                 else if(exchangerate.currency.equals("KRW")){
                    rate = 1;
                 }
                 else {
                    rate = exchangerate.won/exchangerate.value;                    
                 }                    
                    
                 float wonsum = rate * Integer.parseInt(tsum.getText());
                 //float wonsum = rate * Integer.parseInt(tsum.getText());
                 String sql2 = "insert into "+username+" values(null,'"+month1+"/"+date+"/"+year+"', 'income','"+income.getSelectedItem()+"', '"+ta.getText()+"','"+rate+"', '"+currency.getSelectedItem()+"', '"+tsum.getText()+"','"+wonsum+"')";
                 stmt.executeUpdate(sql2);

                             
                 JOptionPane.showMessageDialog(null, "Success","Input Successed", JOptionPane.PLAIN_MESSAGE);
                 
             
                 }catch(Exception ex) {
              System.out.println(ex.getMessage());
           }finally {
              try{          
                 rs.close();
                 stmt.close();
                 con.close();
                 
              }catch(Exception e2) {}
           }

        
          }
      });
      
      f.setLayout(new BorderLayout());
      f.add(p0,BorderLayout.NORTH   );
      f.add(p1, BorderLayout.WEST);
      f.add(p2, BorderLayout.CENTER);
      f.add(p3, BorderLayout.SOUTH);
      

      
      p1.setLayout(new GridBagLayout());
      p2.setLayout(new GridBagLayout());
      p3.setLayout(new BorderLayout(10,10));

      JLabel memo = new JLabel("Memo/Notes", Label.LEFT);
      p3.add(memo, BorderLayout.WEST);
      p3.add(ta, BorderLayout.CENTER);
      p3.add(btn2, BorderLayout.EAST);

      
      p0.setLayout(new BorderLayout());
      Label null_ = new Label("  ");
      p0.add(null_, BorderLayout.NORTH);
      p0.add(todayLab, BorderLayout.CENTER);
      todayLab.setHorizontalAlignment(SwingConstants.CENTER);
      todayLab.setVerticalAlignment(SwingConstants.CENTER);

      
      GridBagConstraints calOpGC = new GridBagConstraints();
      calOpGC.gridx = 1;
      calOpGC.gridy = 2;
      calOpGC.gridwidth = 1;
      calOpGC.gridheight = 2;
      calOpGC.weightx = 1;
      calOpGC.weighty = 1;
      calOpGC.insets = new Insets(5,5,0,0);
      calOpGC.anchor = GridBagConstraints.WEST;
      calOpGC.fill = GridBagConstraints.NONE;
   
      p1.add(income, calOpGC);
      
      calOpGC.gridx = 1;
      calOpGC.gridy = 3;
      p2.add(lsum,calOpGC);
      calOpGC.gridx = 2;

      p2.add(tsum,calOpGC);
      
      calOpGC.gridx = 3;
      calOpGC.gridy = 3;
      p2.add(currency,calOpGC);
      
     
      detailBut = new JButton("Back to input page");

      detailBut.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              showMemo();
          }
      });
      detailBut.setBackground(new Color(200, 50, 50));

      p3.add(detailBut,BorderLayout.SOUTH);
      
      f.setSize(600, 400);

      f.setVisible(true);
   }

@Override
public void actionPerformed(ActionEvent e) {
   // TODO Auto-generated method stub
   
}
}