package ledger;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

//import ChartViewer;
/**
 * 기본 파이 
 *
 */
public class PieChart2D_Income implements ChartViewer {
   
   public int salary;
   public int allowance;
   public int last;
   public int other;
   /**
    * 생성자 
    */
   public PieChart2D_Income(int salary, int allowance, int last, int other) { 
      this.salary = salary;
      this.allowance = allowance;
      this.last = last;
      this.other = other;
      
      System.out.println(salary);
   }
   /*
    * (non-Javadoc)
    * @see chart.ChartViewer#getChartPanel()
    */
   public JPanel getChartPanel() { 
      DefaultPieDataset dataSet = new DefaultPieDataset(); 
      
      // 보여줄 값 설정 
      dataSet.setValue("Salary", salary); 
      dataSet.setValue("Allowance", allowance); 
      dataSet.setValue("Balance from last month",last); 
      dataSet.setValue("Others", other); 
      
      // 그래프 만들기 
      JFreeChart chart = ChartFactory.createPieChart("Income", // title
                        dataSet, // dataset
                        true, // legend
                        false, // tooltips
                        false); // url 

      
      PiePlot plot = (PiePlot) chart.getPlot();
      plot.setBackgroundPaint(new Color(255,255,255));
      
      
      // 이런식으로 색칠 가능.. 지정해 주지 않으면, 기본으로 라이브러리가 선택한 색
      plot.setSectionPaint("Salary", new Color(255,167,167)); 
      plot.setSectionPaint("Allowance", new Color(250, 237, 125)); 
      plot.setSectionPaint("Balance from last month", new Color(183, 240, 177));
      plot.setSectionPaint("Others", new Color(110,177,247));
      
      // 이건 시스템이 결정한 색으로... 
      // 한조각 떼어 내기 
      //plot.setExplodePercent("Balance from last month", 0.30); 
      
      // 보여주기 위한 형태로 만들어서 반환 
      JPanel panel = new JPanel();
      panel.add(new ChartPanel(chart)); 
      
      return panel; 
   }   
   
   
}
