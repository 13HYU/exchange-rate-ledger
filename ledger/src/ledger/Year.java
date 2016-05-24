package ledger;

import java.sql.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;

public class Year extends JFrame {
   public static String username;
   public int year;
   Connection con;
   Statement stmt;
   ResultSet rs = null;
   int int_kor_Minc;
   int int_kor_Mexp;
   int Msum;
   
   public void showMemo(){
       main1Frame.setVisible(false); // 테스트프레임 오픈
    }      
   
   JFrame main1Frame;
   
   JPanel ratePanel;
   
   JPanel YearPanel;
   JLabel curYear;
   JButton pYearBut;
   JButton nYearBut;
   
   JPanel Month;
   JLabel Months[];
   
   JPanel ResultPanel;
   JLabel inc_exp_sum[];
   JLabel Result[][];
   
   JPanel rateSubPanel;
   JButton detailBut; 
   JButton testBut;
   
   final String title = "메모 달력 ver 1.0";
   
   public static void main(String[] args){
      SwingUtilities.invokeLater(new Runnable(){
         public void run(){
            new MemoCalendar(username);
         } 
      });
      
      
   }
   
   public Year(String u, int y){
     this.username = u;
     this.year = y;
     //System.out.println(year);
      main1Frame = new JFrame(title);
      main1Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      main1Frame.setSize(500,700);
      main1Frame.setLocationRelativeTo(null);
      main1Frame.setResizable(false);
      
      ratePanel=new JPanel();
        ratePanel.setBorder(BorderFactory.createTitledBorder("일년내역"));        

        rateSubPanel=new JPanel();        
        testBut = new JButton("테스트");
        detailBut = new JButton("main page로 돌아가기");
        detailBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemo();
            }
        });
        
        rateSubPanel.add(detailBut);
        rateSubPanel.add(testBut);
        
        YearPanel = new JPanel();
        curYear = new JLabel("<html><table width=100><tr><th><font size=5>"+year+"</th></tr></table></html>");
        pYearBut = new JButton("<<");
        pYearBut.setToolTipText("Previous Year");
        pYearBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                year = year-1;
                curYear.setText("<html><table width=100><tr><th><font size=5>"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        nYearBut = new JButton(">>");
        nYearBut.setToolTipText("Next Year");
        nYearBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                year = year+1;
                curYear.setText("<html><table width=100><tr><th><font size=5>"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        YearPanel.setLayout(new FlowLayout());
        YearPanel.add(pYearBut);
        YearPanel.add(curYear);        
        YearPanel.add(nYearBut);
        
        
        Month = new JPanel();
        Months = new JLabel[14];
        Dimension WestSize = Month.getPreferredSize();
        WestSize.width = 100;
        Month.setPreferredSize(WestSize);
        for(int i=0 ; i<14 ; i++){
           int j;
           String fontColor="white";
           if (i==0){
              Months[i]=new JLabel("");
            
               Months[i].setOpaque(true);
               //Months[i].setBackground(new Color(150, 150, 150)); 
           }
           else if (i != 0 && i != 13){
              j = i;              
              Months[i]=new JLabel();
              Months[i].setText("<html><table width=100><tr><th><font size=6><font color="+fontColor+">"+j+"</font></th></tr></table></html>");
              Months[i].setOpaque(true);
              Months[i].setBackground(new Color(150, 150, 150));              
           }
           else {
              Months[i]=new JLabel();
              Months[i].setText("<html><table width=100><tr><th><font size=6><font color="+fontColor+">Sum</font></th></tr></table></html>");
              Months[i].setOpaque(true);
              Months[i].setBackground(new Color(200, 50, 50));
           }
           //Month.setLayout(new BoxLayout(Month, BoxLayout.Y_AXIS));
           Months[i].setForeground(Color.WHITE);
           Month.setLayout(new GridLayout(0,1,13,2));
           Month.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
           //Months[i].setSize(100,100);
           Month.add((Months)[i]);
         }
        
        ResultPanel = new JPanel();
        inc_exp_sum = new JLabel[3];
        for(int i=0 ; i<3 ; i++){
           inc_exp_sum[i]=new JLabel();
           inc_exp_sum[i].setOpaque(true);
           if (i==0){
              inc_exp_sum[i].setText("<html><table width=100><tr><th><font size=6><font color=white>Income</font></th></tr></table></html>");
              inc_exp_sum[i].setOpaque(true);
              inc_exp_sum[i].setBackground(new Color(150, 150, 150)); 
           }
           else if (i==1){
              inc_exp_sum[i].setText("<html><table width=100><tr><th><font size=6><font color=white>Expense</font></th></tr></table></html>");
              inc_exp_sum[i].setOpaque(true);
              inc_exp_sum[i].setBackground(new Color(150, 150, 150));
           }
           else {
              inc_exp_sum[i].setText("<html><table width=100><tr><th><font size=6><font color=white>Sum</font></th></tr></table></html>");
              inc_exp_sum[i].setOpaque(true);
              inc_exp_sum[i].setBackground(new Color(200, 50, 50));
           }
            
            ResultPanel.add(inc_exp_sum[i]);
         }
        Result = new JLabel[13][3];
        for(int i=0 ; i<13 ; i++){
            for(int j=0 ; j<3 ; j++){
               Result[i][j]=new JLabel("");
               Result[i][j].setOpaque(true);
               Result[i][j].setBackground(Color.WHITE);
               
               ResultPanel.add(Result[i][j]);
            }
         }
         ResultPanel.setLayout(new GridLayout(0,3,14,2));
         ResultPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
         showResult();
    
        ratePanel.setLayout(new BorderLayout());
        ratePanel.add(YearPanel, BorderLayout.NORTH);
        ratePanel.add(Month, BorderLayout.WEST);
        ratePanel.add(ResultPanel, BorderLayout.CENTER);
        ratePanel.add(rateSubPanel,BorderLayout.SOUTH);
        
        main1Frame.add(ratePanel);
        main1Frame.setVisible(true);
   }
   
   private void showResult(){
      try{
            Class.forName("com.mysql.jdbc.Driver");
            //System.out.println("mysql 로딩완료");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
            //System.out.println("데이터베이스 연결 성공");
            stmt = con.createStatement();
            
            for(int i=0;i<13;i++){
               for(int j=0;j<3;j++){
                  if (j==0){                
                     int a = i+1;  
                     if(i != 12){
                        String sql = "Select sum(wonsum) as inc_kor from "+username+" where inc_or_exp = 'income' and date like '"+a+"/%/"+year+"'";
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                           int_kor_Minc = rs.getInt("inc_kor");
                           Result[i][j].setText("<html><table width=100><tr><th><font size=6><font color=black>"+int_kor_Minc+"</font></th></tr></table></html>");
                        }
                     }
                     else {
                        String sql = "Select sum(wonsum) as inc_kor from "+username+" where inc_or_exp = 'income' and date like '%"+year+"'";
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                           int_kor_Minc = rs.getInt("inc_kor");
                           Result[i][j].setText("<html><table width=100><tr><th><font size=6><font color=black>"+int_kor_Minc+"</font></th></tr></table></html>");
                        }
                     }
                  }   
                  else if(j==1){
                     int a = i+1;  
                     if(i != 12){
                        String sql = "Select sum(wonsum) as exp_kor from "+username+" where inc_or_exp = 'expense' and date like '"+a+"/%/"+year+"'";
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                           int_kor_Mexp = rs.getInt("exp_kor");
                           Result[i][j].setText("<html><table width=100><tr><th><font size=6><font color=black>"+int_kor_Mexp+"</font></th></tr></table></html>");
                        }
                     }
                     else {
                        String sql = "Select sum(wonsum) as exp_kor from "+username+" where inc_or_exp = 'expense' and date like '%"+year+"'";
                        rs = stmt.executeQuery(sql);
                        while(rs.next())
                        {
                           int_kor_Mexp = rs.getInt("exp_kor");
                           Result[i][j].setText("<html><table width=100><tr><th><font size=6><font color=black>"+int_kor_Mexp+"</font></th></tr></table></html>");
                        }
                     }
                  }
                  else {
                     Msum = int_kor_Minc - int_kor_Mexp;
                     Result[i][j].setText("<html><table width=100><tr><th><font size=6><font color=black>"+Msum+"</font></th></tr></table></html>");
                  }
                  
                  Result[i][j].removeAll();
               }
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
          //System.out.println("DB연결 성공!");
       }
   }
}
      
    