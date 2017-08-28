package TwitchBot;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Set;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

public class EmoteChart extends ApplicationFrame{

	public EmoteChart(String windowTitle, String chartTitle, HashMap<String, Integer> emotes) {
		super(windowTitle);
		JFreeChart eChart = ChartFactory.createBarChart(chartTitle, "Emote", "Usage", getData(emotes), PlotOrientation.VERTICAL, false, false, false);
		ChartPanel chartPanel = new ChartPanel(eChart);
		chartPanel.setPreferredSize(new Dimension(1200, 500));
		setContentPane(chartPanel);
		super.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public CategoryDataset getData(HashMap<String, Integer> emotes){
		final DefaultCategoryDataset emoteData = new DefaultCategoryDataset();
		Set<String> keys = emotes.keySet();
		String[] keysArray = keys.toArray(new String[keys.size()]);
		for(int i = 0; i < keysArray.length; i++){
			if(emotes.get(keysArray[i])>0){
				emoteData.addValue(emotes.get(keysArray[i]), "Kappa", keysArray[i]);
			}
		}
		return emoteData;
	}
	
}
