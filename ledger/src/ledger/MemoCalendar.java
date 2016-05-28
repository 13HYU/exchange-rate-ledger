package ledger;
import java.sql.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;


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
      mainFrame.setSize(1200,700);
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
            RatedateP.setLayout(new FlowLayout());
            Dimension rateSize = RatedateP.getPreferredSize();
            rateSize.width = 300;
    
            RatedateP.setPreferredSize(rateSize);
            RatedateP.add(M);
            RatedateP.add(RatedateM);
            RatedateP.add(D);
            RatedateP.add(RatedateD);
            RatedateP.add(Y);
            RatedateP.add(RatedateY);
            
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
            
            Resultrate = new JLabel("                               ");
            
            rateSub2Panel.setLayout(new BorderLayout());
            rateSub2Panel.add(RatedateP, BorderLayout.WEST);
            rateSub2Panel.add(Curr, BorderLayout.CENTER);
            rateSub2Panel.add(Resultrate, BorderLayout.EAST);
            rateSubPanel=new JPanel();
            testBut = new JButton("환율 보기");
            testBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                	String selectedcurr = Curr.getSelectedItem();
                    UrlDownload urldownload = new UrlDownload(Integer.parseInt(RatedateY.getText()), Integer.parseInt(RatedateM.getText()), Integer.parseInt(RatedateD.getText()));
              
                    ExchangeRate exchangerate = new ExchangeRate(selectedcurr ,Integer.parseInt(RatedateY.getText()), Integer.parseInt(RatedateM.getText()), Integer.parseInt(RatedateD.getText()));
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
            
      JPanel frameSubPanelWest = new JPanel();
      Dimension calOpPanelSize = calOpPanel.getPreferredSize();
      calOpPanelSize.height = 90;
      calOpPanel.setPreferredSize(calOpPanelSize);
      frameSubPanelWest.setLayout(new BorderLayout());
      frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
      frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

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
         Input input = new Input(username, thisyear, thismonth, thisdate);

        
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