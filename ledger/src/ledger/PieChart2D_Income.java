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
 * �⺻ ���� 
 *
 */
public class PieChart2D_Income implements ChartViewer {
   
   public int salary;
   public int allowance;
   public int last;
   public int other;
   /**
    * ������ 
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
      
      // ������ �� ���� 
      dataSet.setValue("Salary", salary); 
      dataSet.setValue("Allowance", allowance); 
      dataSet.setValue("Balance from last month",last); 
      dataSet.setValue("Others", other); 
      
      // �׷��� ����� 
      JFreeChart chart = ChartFactory.createPieChart("Income", // title
                        dataSet, // dataset
                        true, // legend
                        false, // tooltips
                        false); // url 

      
      PiePlot plot = (PiePlot) chart.getPlot();
      plot.setBackgroundPaint(new Color(255,255,255));
      
      
      // �̷������� ��ĥ ����.. ������ ���� ������, �⺻���� ���̺귯���� ������ ��
      plot.setSectionPaint("Salary", new Color(255,167,167)); 
      plot.setSectionPaint("Allowance", new Color(250, 237, 125)); 
      plot.setSectionPaint("Balance from last month", new Color(183, 240, 177));
      plot.setSectionPaint("Others", new Color(110,177,247));
      
      // �̰� �ý����� ������ ������... 
      // ������ ���� ���� 
      //plot.setExplodePercent("Balance from last month", 0.30); 
      
      // �����ֱ� ���� ���·� ���� ��ȯ 
      JPanel panel = new JPanel();
      panel.add(new ChartPanel(chart)); 
      
      return panel; 
   }   
   
   
}
