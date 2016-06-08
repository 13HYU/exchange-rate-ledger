package ledger;

import java.awt.Color;

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
public class PieChart2D_Expense implements ChartViewer {
	int food;
	int transportation;
	int residence;
	int clothing;
	int tele;
	int edu;
	int culture;
	int events;
	int other;
	
	/**
	 * ������ 
	 */
	public PieChart2D_Expense(int food, int transportation, int residence, int clothing, int tele, int edu, int culture, int events, int other){ 
		this.food = food;
		this.transportation = transportation;
		this.residence = residence;
		this.clothing = clothing;
		this.tele = tele;
		this.edu = edu;
		this.culture = culture;
		this.events = events;
		this.other = other;
	}
	/*
	 * (non-Javadoc)
	 * @see chart.ChartViewer#getChartPanel()
	 */
	public JPanel getChartPanel() { 
		DefaultPieDataset dataSet = new DefaultPieDataset(); 
		
		// ������ �� ���� 
		dataSet.setValue("Food",food ); 
		dataSet.setValue("Transportation", transportation); 
		dataSet.setValue("Residence fee", residence); 
		dataSet.setValue("Clothing", clothing);
		dataSet.setValue("Telecommunication", tele); 
		dataSet.setValue("Education", edu); 
		dataSet.setValue("Culture", culture); 
		dataSet.setValue("Family events", events);
		dataSet.setValue("Others", other);
		
		// �׷��� ����� 
		JFreeChart chart = ChartFactory.createPieChart("Expense", // title
								dataSet, // dataset
								true, // legend
								false, // tooltips
								false); // url 

		
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(new Color(255,255,255));
		// �̷������� ��ĥ ����.. ������ ���� ������, �⺻���� ���̺귯���� ������ ��
		
		plot.setSectionPaint("Food", new Color(255,167,167)); 
		plot.setSectionPaint("Transportation", new Color(255,224,140)); 
		plot.setSectionPaint("Residence fee", new Color(183,240,177));
		plot.setSectionPaint("Clothing", new Color(92,209,220));
		plot.setSectionPaint("Telecommunication", new Color(178,204,255));
		plot.setSectionPaint("Education", new Color(255,198,255));
		plot.setSectionPaint("Culture", new Color(225,128,72));
		plot.setSectionPaint("Family events", new Color(111,108,220));
		plot.setSectionPaint("Others", new Color(166,166,166));
		
		
		// �̰� �ý����� ������ ������... 
		// ������ ���� ���� 
		//plot.setExplodePercent("Balance from last month", 0.30); 
		
		// �����ֱ� ���� ���·� ���� ��ȯ 
		JPanel panel = new JPanel(); 
		panel.add(new ChartPanel(chart)); 
		
		
		return panel; 
	}	
	
	
	
}

