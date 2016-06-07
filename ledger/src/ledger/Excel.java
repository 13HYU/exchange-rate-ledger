package ledger;

import java.sql.*;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.*;
import org.jdatepicker.impl.*;

import javax.swing.JFormattedTextField.AbstractFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//환율
public class Excel extends JFrame {
	public static String username;
	Connection con;
	Statement stmt;
	ResultSet rs = null;
    JFrame main1Frame;
    String filename;
    String sm;
    String sd;
    String sy;
    String sm2;
    String sd2;
    String sy2;
    JButton search; 
    JLabel range = new JLabel("~");
    
    String inc;
    String exp;
    float Sum;
    String [] strArr;
    

    UtilDateModel model2;
    UtilDateModel model3;
    JDatePanelImpl datePanel;
    JDatePickerImpl datePicker;
    JDatePanelImpl datePanel2;
    JDatePickerImpl datePicker2;

    JFileChooser jfc = new JFileChooser();
    
    
	

    
   public void showMemo(){
       main1Frame.setVisible(false);
        //this.memoCalendar = new MemoCalendar(); // 테스트프레임 오픈
    }      
   

   final String title = "메모 달력 ver 1.0";
   
   public static void main(String[] args){
      SwingUtilities.invokeLater(new Runnable(){
         public void run(){
         }
      });
   }
   
   public Excel(String u){
	  this.username = u;
      main1Frame = new JFrame(title);
      main1Frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      main1Frame.setSize(600,200);
      main1Frame.setLocationRelativeTo(null);
      main1Frame.setResizable(false);
      
      JPanel allp = new JPanel();
      JLabel range = new JLabel("~");
      model2 = new UtilDateModel();
      //model.setDate(20,04,2014);
      Properties p = new Properties();
      p.put("text.today", "Today");
      p.put("text.month", "Month");
      p.put("text.year", "Year");
      JPanel datep = new JPanel();
      datep.setBorder(BorderFactory.createTitledBorder("저장을 월하는 날짜의 범위를 선택해주세요."));
      datePanel = new JDatePanelImpl(model2, p);
      datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
      
      model3 = new UtilDateModel();
      datePanel2 = new JDatePanelImpl(model3, p);
      datePicker2 = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
      datep.add(datePicker);
      datep.add(range);
      datep.add(datePicker2);
      
      
      /*
      JLabel M = new JLabel("M:");
      JLabel D = new JLabel("D:");
      JLabel Y = new JLabel("Y:");
      JLabel M2 = new JLabel("M:");
      JLabel D2 = new JLabel("D:");
      JLabel Y2 = new JLabel("Y:");
      
      mf = new JTextField(3);
      df = new JTextField(3);
      yf = new JTextField(5);
      mf2 = new JTextField(3);
      df2 = new JTextField(3);
      yf2 = new JTextField(5);

      datep.setLayout(new FlowLayout());
      datep.add(M);
      datep.add(mf);
      datep.add(D);
      datep.add(df);
      datep.add(Y);
      datep.add(yf);
      
      datep.add(M2);
      datep.add(mf2);
      datep.add(D2);
      datep.add(df2);
      datep.add(Y2);
      datep.add(yf2);
      */
      JPanel savep = new JPanel();
      JLabel want = new JLabel("저장 위치: ");
      JLabel jlb = new JLabel();      

      search = new JButton("찾아보기");
      search.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent arg0) {
        	  int userSelection = jfc.showSaveDialog(null);
        	  
        	  if (userSelection == JFileChooser.APPROVE_OPTION) {
        		  if(arg0.getSource() == search){
                           jlb.setText("" + jfc.getSelectedFile().toString() + "." + jfc.getFileFilter().getDescription());
        		  }
                          
                  }
        	  }
          });

      jfc.setFileFilter(new FileNameExtensionFilter("csv", "csv"));
      // 파일 필터
      jfc.setMultiSelectionEnabled(false);//다중 선택 불가
      
      savep.setLayout(new FlowLayout());
      savep.add(want);
      savep.add(jlb);
      savep.add(search);
      
      JPanel bottomp = new JPanel();
      JButton savebut = new JButton("save");
      savebut.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              saveexcel();
          }
      });
      JButton cancelbut = new JButton("cancel");
      cancelbut.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              showMemo();
          }
      });
      bottomp.add(savebut);
      bottomp.add(cancelbut);
      
      allp.setLayout(new BorderLayout());
      allp.add(datep, BorderLayout.NORTH);
      allp.add(savep, BorderLayout.CENTER);
      allp.add(bottomp, BorderLayout.SOUTH);
      
      main1Frame.add(allp);
      main1Frame.setVisible(true);
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

   public void saveexcel(){
		 try{
			 
			 //String csvFileName = "c:/ledger/"+username+"_"+filename+".csv";
			 String csvFileName = ""+jfc.getSelectedFile().toString()+"."+jfc.getFileFilter().getDescription()+"";
			 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFileName)));
			 String header = "date, inc_or_exp, category, details, rate, currency, sum, wonsum \r\n";
			 writer.write(header);
		      //filename = ""+mf.getText()+df.getText()+yf.getText()+"_"+mf2.getText()+df2.getText()+yf2.getText()+"";
			 String getdate = datePicker.getJFormattedTextField().getText();
		     String parts[] = getdate.split("-");
         	 sy = parts[0]; //year
         	 sm = parts[1]; //month
         	 strArr = sm.split("");
         	 if (Integer.parseInt(strArr[0]) == 0){ sm = strArr[1];}
         	 
         	 sd = parts[2]; //day
         	 strArr = sd.split("");
         	 if (Integer.parseInt(strArr[0]) == 0){ sd = strArr[1];}
         	 
         	 String getdate2 = datePicker2.getJFormattedTextField().getText();
		     String parts2[] = getdate2.split("-");
        	 sy2 = parts2[0]; //year
        	 sm2 = parts2[1]; //month
        	 strArr = sm2.split("");
         	 if (Integer.parseInt(strArr[0]) == 0){ sm2 = strArr[1];}
        	 sd2 = parts2[2]; //day
        	 strArr = sd2.split("");
         	 if (Integer.parseInt(strArr[0]) == 0){ sd2 = strArr[1];}
	         Class.forName("com.mysql.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
	            stmt = con.createStatement();
	            
			    String sql = "Select date, inc_or_exp, category, details, rate, currency, sum, wonsum from "+username+" where date between"
			    		+ "'"+sm+"/"+sd+"/"+sy+"' and '"+sm2+"/"+sd2+"/"+sy2+"' order by date ASC";
			    rs = stmt.executeQuery(sql);
			    while(rs.next()){
			    String detail = rs.getString("date")+ "," + rs.getString("inc_or_exp")+ "," + rs.getString("category") + ","+ rs.getString("details") + ","+
			    		 rs.getString("rate")+ "," + rs.getString("currency") + ","+ rs.getString("sum") + ","+  rs.getString("wonsum") +"\r\n";
			   
			    writer.write(detail);
			    }
			    String sql2 = "Select sum(wonsum) as won from "+username+" where inc_or_exp = 'income'"
			    		+ "and date between '"+sm+"/"+sd+"/"+sy+"' and '"+sm2+"/"+sd2+"/"+sy2+"'";
			    rs = stmt.executeQuery(sql2);
			    while(rs.next()){
			    	String Income = "\r\n"+"Income, , , , , , ,-"+ rs.getString("won") + "\r\n"; 
			    	inc = rs.getString("won");
			    	writer.write(Income);
			    }
			    
			    String sql3 = "Select sum(wonsum) as ewon from "+username+" where inc_or_exp = 'expense' "
			    		+ "and date between '"+sm+"/"+sd+"/"+sy+"' and '"+sm2+"/"+sd2+"/"+sy2+"'";
			    rs = stmt.executeQuery(sql3);
			    
			    while(rs.next()){
			    	String Expense = "Expense, , , , , , ,+"+ rs.getString("ewon") + "\r\n"; 
			    	exp = rs.getString("ewon");
			    	writer.write(Expense);
			    }
			    
			    Sum = Float.parseFloat(exp) - Float.parseFloat(inc);
			    writer.write("Sum, , , , , , ,"+Sum);
	            
			    writer.close();
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
	           main1Frame.setVisible(false);
	           }          
		 }
   }
	               
