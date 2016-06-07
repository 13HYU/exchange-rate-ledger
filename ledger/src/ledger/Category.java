package ledger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Category extends JFrame{
   public static String username;
   public int year;
   public int month;
   
   Connection con;
   Statement stmt;
   ResultSet rs = null;
      
   public void showMemo(){
          cateFrame.setVisible(false); // 테스트프레임 오픈
       }  
   
   JFrame cateFrame;
   
   JPanel AllP;
   
   JPanel MMYY;
   JButton Pyear;
   JButton Pmonth;
   JLabel curMY;
   JButton Nmonth;
   JButton Nyear;
   
   JPanel Inc_Exp_P;
   
   JPanel IncomeP;
   JPanel IncomeCateP;
   JPanel IncomeResultP;
   JLabel IncomeCate[];
   JLabel IncomeResult[];
   JPanel IncomeGraphP;
   //그래프추가
   
   JPanel ExpenseP;
   JPanel ExpenseCateP;
   JPanel ExpenseResultP;
   JLabel ExpenseCate[];
   JLabel ExpenseResult[];
   JPanel ExpenseGraphP;
   //그래프추가
   
   JPanel BottomP;
   JButton CloseBut;
   
   final String title = "메모 달력 ver 1.0";
   
   public static void main(String[] args){
      SwingUtilities.invokeLater(new Runnable(){
         public void run(){
            new MemoCalendar(username);
            }
         });
      }
   
   public Category(String u, int y, int m) {
      this.username = u;
      this.year = y;
      this.month = m+1;  
      
      cateFrame = new JFrame(title);
       cateFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       cateFrame.setSize(700,700);
       cateFrame.setLocationRelativeTo(null);
       cateFrame.setResizable(false);
        
        MMYY = new JPanel();
        curMY = new JLabel("<html><table width=100><tr><th><font size=5>"+month+"/"+year+"</th></tr></table></html>");
        Pyear = new JButton("<<");
        Pyear.setToolTipText("Previous Year");
        Pyear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                year = year-1;
                curMY.setText("<html><table width=100><tr><th><font size=5>"+month+"/"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        Nyear = new JButton(">>");
        Nyear.setToolTipText("Next Year");
        Nyear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                year = year+1;
                curMY.setText("<html><table width=100><tr><th><font size=5>"+month+"/"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        
        Pmonth = new JButton("<");
        Pmonth.setToolTipText("Previous Month");
        Pmonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                month = month-1;
                curMY.setText("<html><table width=100><tr><th><font size=5>"+month+"/"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        Nmonth = new JButton(">");
        Nmonth.setToolTipText("Next Month");
        Nmonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                month = month+1;
                curMY.setText("<html><table width=100><tr><th><font size=5>"+month+"/"+year+"</th></tr></table></html>");
                showResult();
            }
        });
        
        MMYY.setLayout(new FlowLayout());
        MMYY.add(Pyear);
        MMYY.add(Pmonth);
        MMYY.add(curMY); 
        MMYY.add(Nmonth);
        MMYY.add(Nyear);
        
        Inc_Exp_P = new JPanel();
        Inc_Exp_P.setLayout(new BoxLayout(Inc_Exp_P, BoxLayout.Y_AXIS));
        
        IncomeP = new JPanel();
        IncomeP.setBorder(BorderFactory.createTitledBorder("Income"));
        Dimension IncomeSize = IncomeP.getPreferredSize();
        IncomeSize.height = 200;
        IncomeP.setPreferredSize(IncomeSize);
        
        IncomeCateP = new JPanel();
        IncomeCateP.setLayout(new GridLayout(0,1,4,2));
        IncomeCateP.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        
        IncomeResultP = new JPanel();
        IncomeResultP.setLayout(new GridLayout(0,1,4,2));
        IncomeResultP.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        
        IncomeCate = new JLabel[4];
                
        for(int i=0; i<4; i++){
           IncomeCate[i] = new JLabel();
           IncomeCate[i].setOpaque(true);
           IncomeCate[i].setBackground(new Color(150, 150, 150));    
           
           IncomeCateP.add(IncomeCate[i]);
        }
        
        IncomeCate[0].setText("<html><table width=100><tr><th><font size=4><font color=white>Salary</font></th></tr></table></html>");
        IncomeCate[1].setText("<html><table width=100><tr><th><font size=4><font color=white>Allowance</font></th></tr></table></html>");
        IncomeCate[2].setText("<html><table width=100><tr><th><font size=4><font color=white>Balance from last month</font></th></tr></table></html>");
        IncomeCate[3].setText("<html><table width=100><tr><th><font size=4><font color=white>Others</font></th></tr></table></html>");
       
        IncomeResult = new JLabel[4];
        
        for(int i=0; i<4; i++){
           IncomeResult[i] = new JLabel();
           IncomeResult[i].setOpaque(true);
           IncomeResult[i].setBackground(Color.white);

           IncomeResultP.add(IncomeResult[i]);
        }
        
        
        IncomeGraphP = new JPanel(); //나중에 그래프 붙이기
        
        
        IncomeP.setLayout(new BorderLayout());
        IncomeP.add(IncomeCateP, BorderLayout.WEST);
        IncomeP.add(IncomeResultP, BorderLayout.CENTER);
        IncomeP.add(IncomeGraphP, BorderLayout.EAST);
        
        //////////////////////////
        
        ExpenseP = new JPanel();
        ExpenseP.setBorder(BorderFactory.createTitledBorder("Expense"));
        Dimension ExpenseSize = ExpenseP.getPreferredSize();
        ExpenseSize.height = 200;
        ExpenseP.setPreferredSize(ExpenseSize);
        
        ExpenseCateP = new JPanel();
        ExpenseCateP.setLayout(new GridLayout(0,1,9,2));
        ExpenseCateP.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        
        ExpenseResultP = new JPanel();
        ExpenseResultP.setLayout(new GridLayout(0,1,9,2));
        ExpenseResultP.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        
        ExpenseCate = new JLabel[9];
                
        for(int i=0; i<9; i++){
           ExpenseCate[i] = new JLabel();
           ExpenseCate[i].setOpaque(true);
           ExpenseCate[i].setBackground(new Color(150, 150, 150));    
           
           ExpenseCateP.add(ExpenseCate[i]);
        }
        
        ExpenseCate[0].setText("<html><table width=100><tr><th><font size=4><font color=white>Food</font></th></tr></table></html>");
        ExpenseCate[1].setText("<html><table width=100><tr><th><font size=4><font color=white>Transportation</font></th></tr></table></html>");
        ExpenseCate[2].setText("<html><table width=100><tr><th><font size=4><font color=white>Residence fee</font></th></tr></table></html>");
        ExpenseCate[3].setText("<html><table width=100><tr><th><font size=4><font color=white>Clothing</font></th></tr></table></html>");
        ExpenseCate[4].setText("<html><table width=100><tr><th><font size=4><font color=white>Telecommunication</font></th></tr></table></html>");
        ExpenseCate[5].setText("<html><table width=100><tr><th><font size=4><font color=white>Education</font></th></tr></table></html>");
        ExpenseCate[6].setText("<html><table width=100><tr><th><font size=4><font color=white>Culture</font></th></tr></table></html>");
        ExpenseCate[7].setText("<html><table width=100><tr><th><font size=4><font color=white>Family events</font></th></tr></table></html>");
        ExpenseCate[8].setText("<html><table width=100><tr><th><font size=4><font color=white>Others</font></th></tr></table></html>");
        
        ExpenseResult = new JLabel[9];
        
        for(int i=0; i<9; i++){
           ExpenseResult[i] = new JLabel();
           ExpenseResult[i].setOpaque(true);
           ExpenseResult[i].setBackground(Color.white);

           ExpenseResultP.add(ExpenseResult[i]);
        }
        
         //showResult();
        
        ExpenseGraphP = new JPanel(); //나중에 그래프 붙이기
        
        ExpenseP.setLayout(new BorderLayout());
        ExpenseP.add(ExpenseCateP, BorderLayout.WEST);
        ExpenseP.add(ExpenseResultP, BorderLayout.CENTER);
        ExpenseP.add(ExpenseGraphP, BorderLayout.EAST);
        
        Inc_Exp_P.add(IncomeP);
        Inc_Exp_P.add(ExpenseP);

        showResult();
        
        BottomP = new JPanel();
        CloseBut = new JButton("Close");
        CloseBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemo();
            }
        });        
        BottomP.add(CloseBut);
        
        AllP = new JPanel();
        AllP.setLayout(new BorderLayout());
        AllP.add(MMYY, BorderLayout.NORTH);
        AllP.add(Inc_Exp_P, BorderLayout.CENTER);
        AllP.add(BottomP, BorderLayout.SOUTH);
        
        cateFrame.add(AllP);
        cateFrame.setVisible(true);
      }
   
    private void showResult(){
         try{
               Class.forName("com.mysql.jdbc.Driver");
               //System.out.println("mysql 로딩완료");
               con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
               //System.out.println("데이터베이스 연결 성공");
               stmt = con.createStatement();
               String categ;
               for(int i=0;i<4;i++){
                  
               if(i == 0){categ = "Salary"; }         
               else if(i == 1) { categ = "Allowance"; }
                else if(i == 2) { categ = "Balance from last month"; }
                else {categ = "Other";}
               String sql = "Select category, sum(wonsum) as catesum from "+username+" where inc_or_exp = 'income' and date like '"+month+"/%/"+year+"' and category = '"+categ+"'";
                   rs = stmt.executeQuery(sql);
               while(rs.next()){
                      IncomeResult[i].setText("<html><table width=100><tr><th><font size=4><font color=black>"+rs.getInt("catesum")+"KRW</font></th></tr></table></html>");
                }              
               IncomeResult[i].removeAll();
               }
               for(int i=0;i<9;i++){
               if(i == 0){ categ = "Food"; }
               else if(i == 1) { categ = "Transportation"; }
               else if(i == 2) { categ = "Residence fee"; }
               else if(i == 3) { categ = "Clothing"; }
               else if(i == 4) { categ = "Telecommunication"; }
               else if(i == 5) { categ = "Education"; }
               else if(i == 6) { categ = "Culture"; }
               else if(i == 7) { categ = "Family events"; }
                else { categ = "Others"; }
               String sql2 = "Select category, sum(wonsum) as catesum from "+username+" where inc_or_exp = 'expense' and date like '"+month+"/%/"+year+"' and category = '"+categ+"'";
                  rs = stmt.executeQuery(sql2);
               while(rs.next()){
                  ExpenseResult[i].setText("<html><table width=100><tr><th><font size=4><font color=black>"+rs.getInt("catesum")+"KRW</font></th></tr></table></html>");
               }
               ExpenseResult[i].removeAll();
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