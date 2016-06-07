package ledger;
import java.sql.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import org.jdatepicker.impl.*;
import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;



class CalendarDataManager{ // 6*7배열에 나타낼 달력 값을 구하는 class
   static final int CAL_WIDTH = 7;
   final static int CAL_HEIGHT = 6;
   int calDates[][] = new int[CAL_HEIGHT][CAL_WIDTH];
   int calYear;
   int calMonth;
   int calDayOfMon;
   final int calLastDateOfMonth[]={31,28,31,30,31,30,31,31,30,31,30,31};
   int calLastDate;
   Calendar today = Calendar.getInstance();
   Calendar cal;
   JMenuItem version, info, logout;
  
   DefaultTableModel model;

   Vector<String> userCol = new Vector<String>();
   Vector<String> userRow = new Vector<String>(); 
   JTable table1;
   
   int incsum, expsum;
   
   UtilDateModel model2;
   JDatePanelImpl datePanel;
   JDatePickerImpl datePicker;
   
   public CalendarDataManager(){ 
      setToday(); 
   }
   public void setToday(){
      calYear = today.get(Calendar.YEAR); 
      calMonth = today.get(Calendar.MONTH);
      calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
      makeCalData(today);
   }
   private void makeCalData(Calendar cal){
      // 1일의 위치와 마지막 날짜를 구함 
      int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7;
      if(calMonth == 1) calLastDate = calLastDateOfMonth[calMonth] + leapCheck(calYear);
      else calLastDate = calLastDateOfMonth[calMonth];
      // 달력 배열 초기화
      for(int i = 0 ; i<CAL_HEIGHT ; i++){
         for(int j = 0 ; j<CAL_WIDTH ; j++){
            calDates[i][j] = 0;
         }
      }
      // 달력 배열에 값 채워넣기
      for(int i = 0, num = 1, k = 0 ; i<CAL_HEIGHT ; i++){
         if(i == 0) k = calStartingPos;
         else k = 0;
         for(int j = k ; j<CAL_WIDTH ; j++){
            if(num <= calLastDate) calDates[i][j]=num++;
         }
      }
   }
   private int leapCheck(int year){ // 윤년인지 확인하는 함수
      if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
      else return 0;
   }
   public void moveMonth(int mon){ // 현재달로 부터 n달 전후를 받아 달력 배열을 만드는 함수(1년은 +12, -12달로 이동 가능)
      calMonth += mon;
      if(calMonth>11) while(calMonth>11){
         calYear++;
         calMonth -= 12;
      } else if (calMonth<0) while(calMonth<0){
         calYear--;
         calMonth += 12;
      }
      cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
      makeCalData(cal);
   }
}

public class MemoCalendar extends CalendarDataManager{ 
	public static String username;
   Connection con;
    Statement stmt;
    ResultSet rs = null;
    int int_for_inc;
    int int_for_exp;
    int int_kor_inc;
    int int_kor_exp;
    int result_sum1;
    int result_sum2;
    String cur;
    String a = " ";
    
    int year2 = calYear;
    int month2 = calMonth+1;

   Category category;
   
    public void showCategory(){
       this.category = new Category(username, calYear, calMonth); 
    }
    
    Year year;
    
    public void showYear(){
        this.year = new Year(username,calYear);
    } 
    
   JFrame mainFrame;
      ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
   JMenuBar menuBar = new JMenuBar();
   JMenu fileMenu = new JMenu("SETTINGS");
   JMenu helpMenu = new JMenu("HELP");
   
   JPanel calOpPanel;
      JButton todayBut;
      JLabel todayLab;
      JSlider Monslider;
      JButton lYearBut;
      JButton lMonBut;
      JLabel curMMYYYYLab;
      JButton nMonBut;
      JButton nYearBut;
      ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();
   
   JPanel calPanel;
      JButton weekDaysName[];
      JButton dateButs[][] = new JButton[6][7];
      listenForDateButs lForDateButs = new listenForDateButs(); 
      
   JPanel infoPanel;
      JLabel infoClock;
   
   JPanel ratePanel;
      JPanel rateSub2Panel;
      JPanel RatedateP;
      TextField RatedateY;
      TextField RatedateM;
      TextField RatedateD;
      JLabel M;
      JLabel D;
      JLabel Y;
      Choice Curr;
      JLabel Resultrate;
      JPanel rateSubPanel;
      JButton testBut;
      
   JPanel statePanel;
       JPanel stateSubPanel;
       JLabel WL[];
       JPanel statesPanel;
       JLabel NL[];
       JLabel CL[][];
       JButton YearBUT;
       JButton CateBut;
   
       JLabel inc;
       JLabel exp;
       JLabel result;
       JLabel todayLab2;
       
   JPanel frameBottomPanel;
      JLabel bottomInfo = new JLabel("Welcome to Rate Ledger!");
      
   //상수, 메세지
   final String WEEK_DAY_NAME[] = { "SUN", "MON", "TUE", "WED", "THR", "FRI", "SAT" };
   final String title = "Rate Ledger ver 1.0";

   public static void main(String[] args){
      SwingUtilities.invokeLater(new Runnable(){
         public void run(){
            new MemoCalendar(username);
         }
      });
   }
   
   //InputIncome input;
   
   ActionListener action = new ActionListener() {
       public void actionPerformed(ActionEvent e) {
              Object obj = e.getSource();
              if (obj == version) {
                     JOptionPane.showMessageDialog(null, "Ledger Version v.01", "Version", JOptionPane.INFORMATION_MESSAGE);
                    
              } else if (obj == info) {
                  JOptionPane.showMessageDialog(null, "Made by students of Hanyang univ. \n As a Software Engineering course    project", "Info", JOptionPane.INFORMATION_MESSAGE);

              } else if (obj == logout) {
                 mainFrame.dispose();
                      
              } else {
              }
       }
 };
   
   public MemoCalendar(String u){ 
      this.username = u;
      mainFrame = new JFrame(title);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainFrame.setSize(1000,700);
      mainFrame.setLocationRelativeTo(null);
      mainFrame.setResizable(false);
      mainFrame.setIconImage(icon.getImage());
      try{
         UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//LookAndFeel Windows 스타일 적용
         SwingUtilities.updateComponentTreeUI(mainFrame) ;
      }catch(Exception e){
         bottomInfo.setText("ERROR : LookAndFeel setting failed");
      }
      logout = new JMenuItem("로그아웃");
      version = new JMenuItem("버전");
      info = new JMenuItem("정보");
      
      //fileMenu.add(new JMenuItem("환경설정"));
      fileMenu.add(logout);
      helpMenu.add(version);
      helpMenu.add(info);
      
      version.addActionListener(action);
      info.addActionListener(action);
      logout.addActionListener(action);
      
      JMenuItem settings = new JMenuItem("환경설정");
      fileMenu.add(settings);
      settings.addActionListener(new ActionListener(){
    	  @Override
          public void actionPerformed(ActionEvent e){
    		  Settings settings = new Settings(username);
    	  }
      });
      
      
      JMenuItem excelfile = new JMenuItem("엑셀로 저장하기");
      fileMenu.add(excelfile);
      excelfile.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              Excel excel = new Excel(username); 
          }
      });      
      
      menuBar.add(fileMenu);
      menuBar.add(helpMenu);
      mainFrame.setJMenuBar(menuBar);
      
      calOpPanel = new JPanel();
         todayBut = new JButton("Today");
         todayBut.setToolTipText("Today");
         todayBut.addActionListener(lForCalOpButtons);
         todayLab = new JLabel(today.get(Calendar.MONTH)+1+"/"+today.get(Calendar.DAY_OF_MONTH)+"/"+today.get(Calendar.YEAR));
         ////label추가 slider
         Monslider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100); 
         Monslider.setPaintTicks(true);
         Monslider.setPaintTrack(true);
         Monslider.setMajorTickSpacing(50);
         Monslider.setMinorTickSpacing(10);
         lYearBut = new JButton("<<");
         lYearBut.setToolTipText("Previous Year");
         lYearBut.addActionListener(lForCalOpButtons);
         lYearBut.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 year2 = year2-1;
                 showState();
             }
         });
         lMonBut = new JButton("<");
         lMonBut.setToolTipText("Previous Month");
         lMonBut.addActionListener(lForCalOpButtons);
         lMonBut.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 month2 = month2-1;
                 showState();
             }
         });
         curMMYYYYLab = new JLabel("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
         nMonBut = new JButton(">");
         nMonBut.setToolTipText("Next Month");
         nMonBut.addActionListener(lForCalOpButtons);
         nMonBut.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 month2 = month2+1;
                 showState();
             }
         });
         nYearBut = new JButton(">>");
         nYearBut.setToolTipText("Next Year");
         nYearBut.addActionListener(lForCalOpButtons);
         nYearBut.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 year2 = year2+1;
                 showState();
             }
         });
         calOpPanel.setLayout(new GridBagLayout());
         GridBagConstraints calOpGC = new GridBagConstraints();
         calOpGC.gridx = 1;
         calOpGC.gridy = 1;
         calOpGC.gridwidth = 2;
         calOpGC.gridheight = 1;
         calOpGC.weightx = 1;
         calOpGC.weighty = 1;
         calOpGC.insets = new Insets(5,5,0,0);
         calOpGC.anchor = GridBagConstraints.WEST;
         calOpGC.fill = GridBagConstraints.NONE;
         calOpPanel.add(todayBut,calOpGC);
         calOpGC.gridwidth = 3;
         calOpGC.gridx = 2;
         calOpGC.gridy = 1;
         calOpPanel.add(todayLab,calOpGC);
         calOpGC.gridwidth = 3;
         calOpGC.gridx = 3;
         calOpGC.gridy = 1;
         calOpPanel.add(Monslider,calOpGC);
         calOpGC.anchor = GridBagConstraints.CENTER;
         calOpGC.gridwidth = 1;
         calOpGC.gridx = 1;
         calOpGC.gridy = 2;
         calOpPanel.add(lYearBut,calOpGC);
         calOpGC.gridwidth = 1;
         calOpGC.gridx = 2;
         calOpGC.gridy = 2;
         calOpPanel.add(lMonBut,calOpGC);
         calOpGC.gridwidth = 2;
         calOpGC.gridx = 3;
         calOpGC.gridy = 2;
         calOpPanel.add(curMMYYYYLab,calOpGC);
         calOpGC.gridwidth = 1;
         calOpGC.gridx = 5;
         calOpGC.gridy = 2;
         calOpPanel.add(nMonBut,calOpGC);
         calOpGC.gridwidth = 1;
         calOpGC.gridx = 6;
         calOpGC.gridy = 2;
         calOpPanel.add(nYearBut,calOpGC);
      
      calPanel=new JPanel();
         weekDaysName = new JButton[7];
         for(int i=0 ; i<CAL_WIDTH ; i++){
            weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
            weekDaysName[i].setBorderPainted(false);
            weekDaysName[i].setContentAreaFilled(false);
            weekDaysName[i].setForeground(Color.WHITE);
            if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
            else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
            else weekDaysName[i].setBackground(new Color(150, 150, 150));
            weekDaysName[i].setOpaque(true);
            weekDaysName[i].setFocusPainted(false);
            calPanel.add(weekDaysName[i]);
         }
         for(int i=0 ; i<CAL_HEIGHT ; i++){
            for(int j=0 ; j<CAL_WIDTH ; j++){
               dateButs[i][j]=new JButton();
               dateButs[i][j].setBorderPainted(false);
               dateButs[i][j].setContentAreaFilled(false);
               dateButs[i][j].setBackground(Color.WHITE);
               dateButs[i][j].setOpaque(true);
               dateButs[i][j].addActionListener(lForDateButs);
               dateButs[i][j].addActionListener(new ActionListener() {
                   @Override
                   public void actionPerformed(ActionEvent e) {
                      }
                   });
               calPanel.add(dateButs[i][j]);
            }
         }
         calPanel.setLayout(new GridLayout(0,7,2,2));
         calPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
         showCal(); // 달력을 표시
                  
      infoPanel = new JPanel();
         infoPanel.setLayout(new BorderLayout());
         infoClock = new JLabel("", SwingConstants.RIGHT);
         infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
         infoPanel.add(infoClock, BorderLayout.NORTH);                         
     
         ratePanel=new JPanel();
            ratePanel.setBorder(BorderFactory.createTitledBorder("Exchange Rate"));
            rateSub2Panel = new JPanel();
            
            RatedateP = new JPanel();
           
            model2 = new UtilDateModel();
            //model.setDate(20,04,2014);
            Properties p = new Properties();
            p.put("text.today", "Today");
            p.put("text.month", "Month");
            p.put("text.year", "Year");
            datePanel = new JDatePanelImpl(model2, p);
            datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

    	   
            RatedateP.add(datePicker);
            /*
            Font font = new Font("arian", Font.BOLD, 20);
            RatedateY = new TextField(5);
            RatedateY.setFont(font);
            RatedateM  = new TextField(2);
            RatedateM.setFont(font);
            RatedateD  = new TextField(2);
            RatedateD.setFont(font);
            
            M = new JLabel("M:");
            D = new JLabel("D:");
            Y = new JLabel("Y:");
            
            //RatedateP.setLayout(new BoxLayout(RatedateP, BoxLayout.X_AXIS));
            RatedateP.setLayout(new FlowLayout());*/
            Dimension rateSize = RatedateP.getPreferredSize();
            rateSize.width = 300;
    
            RatedateP.setPreferredSize(rateSize);
    /*        RatedateP.add(M);
            RatedateP.add(RatedateM);
            RatedateP.add(D);
            RatedateP.add(RatedateD);
            RatedateP.add(Y);
            RatedateP.add(RatedateY);
      */      
            Curr = new Choice();
            Curr.add("USD");
            Curr.add("EUR");
            Curr.add("JPY");
            Curr.add("CNY");
            Curr.add("AUD");
            Curr.add("NZD");
            Curr.add("GBP");
            Curr.add("CAD");
            Curr.add("PHP");
            Curr.add("HKD");
            Curr.add("THB");
            Curr.add("SGD");
            Curr.add("INR");
            Curr.add("TWD");
            Curr.add("CHF");
            Curr.add("VND");
            Curr.add("BDT");
            Curr.add("SEK");
            Curr.add("DKK");
            
            Resultrate = new JLabel("");
            
            rateSub2Panel.setLayout(new BorderLayout());
            rateSub2Panel.add(RatedateP, BorderLayout.NORTH);
            rateSub2Panel.add(Curr, BorderLayout.CENTER);
            rateSub2Panel.add(Resultrate, BorderLayout.SOUTH);
            rateSubPanel=new JPanel();
            testBut = new JButton("환율 보기");
            testBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String getdate = datePicker.getJFormattedTextField().getText();
					String parts[] = getdate.split("-");
                	String p1 = parts[0]; //year
                	String p2 = parts[1]; //month
                	String p3 = parts[2]; //day
                	String selectedcurr = Curr.getSelectedItem();
                    UrlDownload urldownload = new UrlDownload(Integer.parseInt(p1), Integer.parseInt(p2), Integer.parseInt(p3));
              
                    ExchangeRate exchangerate = new ExchangeRate(selectedcurr ,Integer.parseInt(p1), Integer.parseInt(p2), Integer.parseInt(p3));
                    if(exchangerate.currency.equals("USD")){
                    	Resultrate.setText("<html><tr><th><font size=4>"+exchangerate.won + "원</th></tr></table></html>");
                    }
                    else	
                    	Resultrate.setText("<html><tr><th><font size=4>"+exchangerate.won/exchangerate.value + "원</th></tr></table></html>");
                }
            });
            
            rateSubPanel.add(testBut);
            ratePanel.setLayout(new BorderLayout());
            ratePanel.add(rateSub2Panel,BorderLayout.CENTER);
            ratePanel.add(rateSubPanel,BorderLayout.SOUTH);
            
            
            statePanel = new JPanel();
         
            stateSubPanel = new JPanel();
            WL = new JLabel[4];
            Dimension stateSize = statePanel.getPreferredSize();
            stateSize.height = 300;
            statePanel.setPreferredSize(stateSize);
            for(int i=0 ; i<4 ; i++){
             if (i==0){
          	   WL[i]=new JLabel();
          	   WL[i].setText("<html><table width=100><tr><th><font size=4><font color=black>State</font></th></tr></table></html>");
               WL[i].setOpaque(true);
             }
             else if (i==1){
                WL[i]=new JLabel();
                WL[i].setText("<html><table width=100><tr><th><font size=4><font color=white>Income</font></th></tr></table></html>");
                WL[i].setOpaque(true);
                WL[i].setBackground(new Color(150, 150, 150));
             }
             else if (i==2){
            	 WL[i]=new JLabel();
                 WL[i].setText("<html><table width=100><tr><th><font size=4><font color=white>Expense</font></th></tr></table></html>");
                 WL[i].setOpaque(true);
                 WL[i].setBackground(new Color(150, 150, 150));
             }
             else {
            	 WL[i]=new JLabel();
                 WL[i].setText("<html><table width=100><tr><th><font size=4><font color=white>Sum</font></th></tr></table></html>");
                 WL[i].setOpaque(true);
                 WL[i].setBackground(new Color(200, 50, 50));
             }
             WL[i].setForeground(Color.WHITE);
             stateSubPanel.setLayout(new GridLayout(0,1,4,2));
             stateSubPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
             stateSubPanel.add((WL)[i]);
           }
          
          statesPanel = new JPanel();
          NL = new JLabel[2];
          for(int i=0 ; i<2 ; i++){
          	NL[i]=new JLabel();
          	NL[i].setOpaque(true);
          	if (i==0){
          		NL[i].setText("<html><table width=100><tr><th><font size=4><font color=white>Foreign Currency</font></th></tr></table></html>");
          		NL[i].setOpaque(true);
          		NL[i].setBackground(new Color(50, 100, 200)); 
          	}
          	else {
          		NL[i].setText("<html><table width=100><tr><th><font size=4><font color=white>Korea Currency</font></th></tr></table></html>");
          		NL[i].setOpaque(true);
          		NL[i].setBackground(new Color(50, 100, 200));
          	}
              
              statesPanel.add(NL[i]);
           }
          CL = new JLabel[3][2];
          for(int i=0 ; i<3 ; i++){
              for(int j=0 ; j<2 ; j++){
                 CL[i][j]=new JLabel("");
                 CL[i][j].setOpaque(true);
                 CL[i][j].setBackground(Color.WHITE);
                 
                 statesPanel.add(CL[i][j]);
              }
           }
           statesPanel.setLayout(new GridLayout(0,2,4,2));
           statesPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
           showState();
           JPanel Bottom = new JPanel();
           Bottom.setLayout(new FlowLayout());
           JButton NEW = new JButton("Refresh");
           NEW.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   showState();
               }
           });
           YearBUT = new JButton("일년보기");
           YearBUT.addActionListener(new ActionListener() {			
               @Override
               public void actionPerformed(ActionEvent e) {
                   showYear();
               }
           });
           CateBut = new JButton("분류별 보기");
           CateBut.addActionListener(new ActionListener() {			
               @Override
               public void actionPerformed(ActionEvent e) {
                   showCategory();
               }
           });
           Bottom.add(NEW);
           Bottom.add(YearBUT);
           Bottom.add(CateBut);
           
      //calOpPanel, calPanel을  frameSubPanelWest에 배치
          statePanel.setLayout(new BorderLayout());
          statePanel.add(stateSubPanel, BorderLayout.WEST);
          statePanel.add(statesPanel, BorderLayout.CENTER);
          statePanel.add(Bottom, BorderLayout.SOUTH);
          
          JPanel f = new JPanel();
          JPanel p0 = new JPanel();
          
          JScrollPane scrollPane = new JScrollPane();
          
          inc = new JLabel("  ");
          exp = new JLabel("  ");
          result = new JLabel(" ");
          

          
          todayLab2 = new JLabel(Integer.toString(calMonth+1)+"/"+calDayOfMon+"/"+calYear);
          JButton New = new JButton("new");
          New.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                 DefaultTableModel model = (DefaultTableModel)table1.getModel();
                 model.setNumRows(0);
                  select();   
                  showState();
              }
          });
          
          Font font1 = new Font("SansSerif",Font.BOLD,30);
          todayLab2.setFont(font1);
          p0.setLayout(new BorderLayout());
          p0.add(todayLab2, BorderLayout.CENTER);
          p0.add(New, BorderLayout.EAST);

          try {
        	  String jdbcUrl = "jdbc:mysql://localhost:3306/test_db"; 
              String userId = "root"; 
              String userPass = "0zero6six"; 
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
//          p1.setLayout(null);
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
//          btn1.setBackground(new Color(50, 100, 200));
          btn1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                     InputIncome input = new InputIncome(username, thisyear, thismonth, thisdate);

                }
            });
           btn1.setForeground(new Color(50, 100, 200));
           
          JButton btn2 = new JButton("Expense");
          
          btn2.setPreferredSize(new Dimension(120, 50)); 
//          btn2.setBackground(new Color(50, 100, 200));
           btn2.setForeground(new Color(50, 100, 200));
           btn2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                     InputExpense input = new InputExpense(username, thisyear, thismonth, thisdate);

                }
            });

           JButton btn3 = new JButton("Delete");
           btn3.setPreferredSize(new Dimension(120, 50));
           btn3.setForeground(new Color(50, 100, 200));
           btn3.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  int selection = table1.getSelectedRow();
                  try{
                       Class.forName("com.mysql.jdbc.Driver");
                       System.out.println("mysql 로딩완료");
                       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
                       System.out.println("데이터베이스 연결 성공");
                       stmt = con.createStatement();

                       String query = "delete from "+username+" where date= '"+(String) model.getValueAt(selection, 0)+"' and category= '"+(String) model.getValueAt(selection, 2)+"' and details='"+(String) model.getValueAt(selection,3)+"'";
                       stmt.executeUpdate(query); 

                  } catch (Exception eeee) {eeee.getMessage();
                  } finally{
                     System.out.println("DB연결 성공!");
                         model.removeRow(selection);
                         table1.updateUI();
                         showState();
                      }
                  }
               }
           );

           

          p2.add(btn1);
          p2.add(btn2);
          p2.add(btn3);
          
          f.setLayout(new BorderLayout());
          f.add(p0, BorderLayout.NORTH);
          f.add(p1, BorderLayout.CENTER);
          f.add(p2, BorderLayout.SOUTH);
          
         
          
          
      JPanel frameSubPanelWest = new JPanel();
      Dimension calOpPanelSize = calOpPanel.getPreferredSize();
      calOpPanelSize.height = 90;
      calOpPanel.setPreferredSize(calOpPanelSize);
      
      Dimension fSize = f.getPreferredSize();
      fSize.height = 300;
      f.setPreferredSize(fSize);
      
      frameSubPanelWest.setLayout(new BorderLayout());
      frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
      frameSubPanelWest.add(calPanel,BorderLayout.CENTER);
      frameSubPanelWest.add(f, BorderLayout.SOUTH);

      //infoPanel, memoPanel을  frameSubPanelEast에 배치
      JPanel frameSubPanelEast = new JPanel();
      Dimension infoPanelSize=infoPanel.getPreferredSize();
      infoPanelSize.height = 65;
      infoPanel.setPreferredSize(infoPanelSize);
      
      frameSubPanelEast.setLayout(new BoxLayout(frameSubPanelEast, BoxLayout.Y_AXIS));
      frameSubPanelEast.add(ratePanel);
      frameSubPanelEast.add(statePanel);

      Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
      frameSubPanelWestSize.width = 600;
      frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);
      
      //뒤늦게 추가된 bottom Panel..
      frameBottomPanel = new JPanel();
      frameBottomPanel.add(bottomInfo);
      
      //frame에 전부 배치
      mainFrame.setLayout(new BorderLayout());
      mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
      mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
      mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
      mainFrame.setVisible(true);

      focusToday(); //현재 날짜에 focus를 줌 (mainFrame.setVisible(true) 이후에 배치해야함)
      
      //Thread 작동(시계, bottomMsg 일정시간후 삭제)
      ThreadConrol threadCnl = new ThreadConrol();
      threadCnl.start();   
   }
   private void focusToday(){
      if(today.get(Calendar.DAY_OF_WEEK) == 1)
         dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
      else
         dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
       }
   public int thisdate;
   public int thismonth;
   public int thisyear;

   private void showState(){
	      try{
	            Class.forName("com.mysql.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
	            stmt = con.createStatement();
	            
	            for (int i=0; i<3; i++) {
	            		if (i == 0){
	            			   String sql = "Select currency, sum(sum) as inc_for_sum from "+username+" where inc_or_exp = 'income' and date like '"+month2+"/%/"+year2+"' group by currency";
	            			    rs = stmt.executeQuery(sql);
	            			    while(rs.next()){
	            			    	 cur = rs.getString("currency");
	            			    	 int_for_inc = rs.getInt("inc_for_sum");
	            			    	 a = a + int_for_inc + cur + "<br>";
	            			    }
	            			    CL[i][0].setText("<html><table width=100><tr><th><font size=3><font color=black>"+a+"</font></th></tr></table></html>");
	            			    String sql2 = "Select sum(wonsum) as inc_kor_sum from "+username+" where inc_or_exp = 'income' and date like '"+month2+"/%/"+year2+"'";
	            			    rs = stmt.executeQuery(sql2);
	            			    while(rs.next()){
	            			    	int_kor_inc = rs.getInt("inc_kor_sum");
	            			    	CL[i][1].setText("<html><table width=100><tr><th><font size=4><font color=black>"+int_kor_inc+"</font></th></tr></table></html>");
	            			    }
	            			}
	            		else if (i == 1){
	            			a = " ";
	            			String sql = "Select currency, sum(sum) as exp_for_sum from "+username+" where inc_or_exp = 'expense' and date like '"+month2+"/%/"+year2+"' group by currency";
            			    rs = stmt.executeQuery(sql);
            			    while(rs.next()){
            			    	cur = rs.getString("currency");
           			    	    int_for_exp = rs.getInt("exp_for_sum");
            			    	a = a + int_for_exp + cur + "<br>";
           			        }
            			    CL[i][0].setText("<html><table width=100><tr><th><font size=3><font color=black>"+a+"</font></th></tr></table></html>");
            			    String sql2 = "Select sum(wonsum) as exp_kor_sum from "+username+" where inc_or_exp = 'expense' and date like '"+month2+"/%/"+year2+"'";
            			    rs = stmt.executeQuery(sql2);
            			    while(rs.next()){
            			    	int_kor_exp = rs.getInt("exp_kor_sum");
            			    	CL[i][1].setText("<html><table width=100><tr><th><font size=4><font color=black>"+int_kor_exp+"</font></th></tr></table></html>");
            			    }
            			}
	            		else {
	            			a= " ";
	            			result_sum2 = int_kor_inc - int_kor_exp;
          	                 CL[i][1].setText("<html><table width=100><tr><th><font size=4><font color=black>"+result_sum2+"</font></th></tr></table></html>");
	            			String sql = "Select exp.currency, inc.income, exp.expense from (select currency, sum(sum) as income from "+username+" where inc_or_exp='income'"
	            					+ "and date like '"+month2+"%"+year2+"' group by currency) as inc "
	            							+ "right join"
	            							+ " (select currency, sum(sum) as expense from "+username+" where inc_or_exp='expense'"
	            					+ "and date like '"+month2+"%"+year2+"' group by currency) as exp on inc.currency=exp.currency "
	            							+ "union"
	            							+ " Select inc.currency, inc.income, exp.expense from (select currency, sum(sum) as income from "+username+" where inc_or_exp='income'"
	            					+ "and date like '"+month2+"%"+year2+"' group by currency) as inc "
	            							+ "left join"
	            							+ " (select currency, sum(sum) as expense from "+username+" where inc_or_exp='expense'"
	            					+ "and date like '"+month2+"%"+year2+"' group by currency) as exp on inc.currency=exp.currency";
            			    rs = stmt.executeQuery(sql);
            			    while (rs.next()){
            			    	cur=rs.getString("currency");
            			    	int_for_inc = rs.getInt("income");
            			    	int_for_exp = rs.getInt("expense");
            			    	result_sum1 = int_for_inc - int_for_exp;
            			    	a = a + result_sum1 + cur + "<br>";
            			    }
            			    CL[i][0].setText("<html><table width=100><tr><th><font size=3><font color=black>"+a+"</font></th></tr></table></html>");           			 
	            			 
	            			 
	            			 result_sum2 = int_kor_inc - int_kor_exp;
           	                 CL[i][1].setText("<html><table width=100><tr><th><font size=4><font color=black>"+result_sum2+"</font></th></tr></table></html>");
	            		}
	            		a=" ";
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
   private void showCal(){
      for(int i=0;i<CAL_HEIGHT;i++){
         for(int j=0;j<CAL_WIDTH;j++){
            String fontColor="black";
            if(j==0) fontColor="red";
            else if(j==6) fontColor="blue";
            
            File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDates[i][j]<10?"0":"")+calDates[i][j]+".txt");
            if(f.exists()){
               dateButs[i][j].setText("<html><b><font color="+fontColor+">"+calDates[i][j]+"</font></b></html>");
            }
            else dateButs[i][j].setText("<html><font color="+fontColor+">"+calDates[i][j]+"</font></html>");

            JLabel todayMark = new JLabel("<html><font color=green>*</html>");
            dateButs[i][j].removeAll();
            if(calMonth == today.get(Calendar.MONTH) &&
                  calYear == today.get(Calendar.YEAR) &&
                  calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)){
               dateButs[i][j].add(todayMark);
               dateButs[i][j].setToolTipText("Today");
            }
            
            if(calDates[i][j] == 0) dateButs[i][j].setVisible(false);
            else dateButs[i][j].setVisible(true);
         }
      }
   }
   private class ListenForCalOpButtons implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         if(e.getSource() == todayBut){
            setToday();
            lForDateButs.actionPerformed(e);
            focusToday();
         }
         else if(e.getSource() == lYearBut) moveMonth(-12);
         else if(e.getSource() == lMonBut) moveMonth(-1);
         else if(e.getSource() == nMonBut) moveMonth(1);
         else if(e.getSource() == nYearBut) moveMonth(12);
         
         curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
         showCal();
      }
   }
   private class listenForDateButs implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         int k=0,l=0;
         for(int i=0 ; i<CAL_HEIGHT ; i++){
            for(int j=0 ; j<CAL_WIDTH ; j++){
               if(e.getSource() == dateButs[i][j]){ 
                  k=i;
                  l=j;
               }
            }
         }
   
         if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; //today버튼을 눌렀을때도 이 actionPerformed함수가 실행되기 때문에 넣은 부분

         cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
         
         String dDayString = new String();
         int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
         if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) 
               && (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
               && (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today"; 
         else if(dDay >=0) dDayString = "D-"+(dDay+1);
         else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);
         
         
         thisyear = calYear;
         thismonth = calMonth;
         thisdate = calDayOfMon;
         
         todayLab2.setText(Integer.toString(thismonth+1)+"/"+calDayOfMon+"/"+calYear);
         DefaultTableModel model = (DefaultTableModel)table1.getModel();
         model.setNumRows(0);
         select();  

        
      }
   }
   public void select() {
       int month2 = thismonth+1;
       try{
    		 Class.forName("com.mysql.jdbc.Driver");
	         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
             stmt = con.createStatement();
             
             rs = stmt.executeQuery("Select date, inc_or_exp, category, details, sum, currency, wonsum from "+username+" where date like '"+month2+"/"+thisdate+"/"+calYear+"'");
             while(rs.next()){
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
       try{
    	   Class.forName("com.mysql.jdbc.Driver");
	       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
           stmt = con.createStatement();
           
           rs = stmt.executeQuery("Select sum(wonsum) as inc_sum from "+username+" where inc_or_exp = 'income' and date like '"+month2+"/"+thisdate+"/"+calYear+"'");
        while(rs.next()){
          incsum = rs.getInt("inc_sum");
          inc.setText("총수입: "+incsum+"원  ");
          }
      } catch(Exception e) {
             System.out.println(e.getMessage());
      }
       
       try{
    	   Class.forName("com.mysql.jdbc.Driver");
	       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
           stmt = con.createStatement();
           rs = stmt.executeQuery("Select sum(wonsum) as exp_sum from "+username+" where inc_or_exp = 'expense' and date like '"+month2+"/"+thisdate+"/"+calYear+"'");
        while(rs.next()){
          expsum = rs.getInt("exp_sum");
          exp.setText("총지출: "+expsum+"원   ");
          }
      } catch(Exception e) {
             System.out.println(e.getMessage());
      }
       int all = incsum-expsum;
       result.setText("총액: "+all+"원" );


           
    }
   public class DateLabelFormatter extends AbstractFormatter {
	    

	    String datePattern = "yyyy-MM-dd";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        return dateFormatter.parseObject(text);
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}

   private class ThreadConrol extends Thread{
      public void run(){
         boolean msgCntFlag = false;
         int num = 0;
         String curStr = new String();
         while(true){
            try{
               today = Calendar.getInstance();
               String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
               String hour;
                     if(today.get(Calendar.HOUR) == 0) hour = "12"; 
                     else if(today.get(Calendar.HOUR) == 12) hour = " 0";
                     else hour = (today.get(Calendar.HOUR)<10?" ":"")+today.get(Calendar.HOUR);
               String min = (today.get(Calendar.MINUTE)<10?"0":"")+today.get(Calendar.MINUTE);
               String sec = (today.get(Calendar.SECOND)<10?"0":"")+today.get(Calendar.SECOND);
               infoClock.setText(amPm+" "+hour+":"+min+":"+sec);

               sleep(1000);
               String infoStr = bottomInfo.getText();
               
               if(infoStr != " " && (msgCntFlag == false || curStr != infoStr)){
                  num = 5;
                  msgCntFlag = true;
                  curStr = infoStr;
               }
               else if(infoStr != " " && msgCntFlag == true){
                  if(num > 0) num--;
                  else{
                     msgCntFlag = false;
                     bottomInfo.setText(" ");
                  }
               }      
            }
            catch(InterruptedException e){
               System.out.println("Thread:Error");
            }
         }
      }
   }
}
