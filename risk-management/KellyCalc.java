import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KellyCalc {
	
	private double[] real_odds;
	public double[] expected_odds;
	private Map<Integer, Double> gf = new HashMap<Integer, Double>();
	private LinkedHashMap<Integer, Double> reserve_sort_gf;
	public Map<Integer, Double> bet_amount = new HashMap<Integer, Double>();
	
	private Double optimal_pt = 1.0;
	private Double prob_before_tax = (double) 0;
	
	private ArrayList<Double> set2 = new ArrayList<Double>();
	private ArrayList<Double> expected_odds_set = new ArrayList<Double>();
	
	public void calculate_expected(double[] expected_odds, double[] real_odds) {
		for (int idx = 0; idx < expected_odds.length; idx ++) {
			gf.put(idx+1, expected_odds[idx]*real_odds[idx]);
		}
		reserve_sort_gf = gf.entrySet()
					       .stream()             
					       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					       .collect(Collectors.toMap(e -> e.getKey(), 
					                                 e -> e.getValue(), 
					                                 (e1, e2) -> null, // or throw an exception
					                                 () -> new LinkedHashMap<Integer, Double>()));
	}
	
	public void calculate_edge() {
		int horse_no;
		double sum_of_prob;
		for (var entry : reserve_sort_gf.entrySet()) {
			if (entry.getValue() >= optimal_pt) {
				horse_no = entry.getKey()-1;
				set2.add(entry.getValue());
				expected_odds_set.add(expected_odds[horse_no]);
				if (real_odds[horse_no] != 0) {
					prob_before_tax += 1/real_odds[horse_no];
				}
				sum_of_prob = expected_odds_set.stream().mapToDouble(Double::doubleValue).sum();
				optimal_pt = (1-sum_of_prob)/(1-prob_before_tax);
			}
		}
	}
	
	public void calcuate_amount() {
		double tmp_odds, tmp_prob, frac;
		for (int idx = 0;idx < real_odds.length; idx++) {
			tmp_odds = real_odds[idx];
			tmp_prob = expected_odds[idx];
			if (tmp_odds != 0)
				frac = tmp_prob-(optimal_pt/tmp_odds);
			else
				frac = 0;
			if (frac > 0) {
				bet_amount.put(idx+1, frac);
			}
			else
				bet_amount.put(idx+1, 0.0);
		}
		double remain_gf = 1 - bet_amount.values().stream().mapToDouble(Double::doubleValue).sum();
		System.out.println("the g(f) is "+optimal_pt.toString()+" should be equal to "+remain_gf);
	}
	
	public double[] odds_to_prob(double[] oversea_odds) {
		double[] expected_odds_adjusted = new double[oversea_odds.length];
		//double[] tmp_store;
		double sum_of_odds_probs = 0;
		
		for (double i : oversea_odds) {
			if (i != 0) {
				sum_of_odds_probs += 1 / i;
			}
		}
		for (int idx = 0; idx < oversea_odds.length;idx++) {
			if (oversea_odds[idx] != 0 && sum_of_odds_probs != 0) {
				expected_odds_adjusted[idx] = (1/oversea_odds[idx]) / sum_of_odds_probs;
			} else {
				expected_odds_adjusted[idx] = 0;
			}
		}
		return expected_odds_adjusted;
	}
	
	public void run(double[] real_odds, double[] oversea_odds) {
		this.expected_odds = odds_to_prob(oversea_odds);
		this.real_odds = real_odds;
		calculate_expected(expected_odds, real_odds);
		calculate_edge();
		calcuate_amount();
				
	}
	
	public static void main(String[] args){
		KellyCalc kelly = new KellyCalc();
		double[] expected_odds = {16,0,6.5,8.5,2.3,51,2.6};
		double[] real_odds = {28.75,0,9,13.35,1.75,87.85,2.5};
		
		kelly.run(real_odds, expected_odds);
		
		for (var entry : kelly.bet_amount.entrySet()) {
		      System.out.println(entry);
		    }  
		
	}
	
}
