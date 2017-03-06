package unii.draft.mtg.parings.buisness.algorithm.base;

public interface IStatisticCalculation {

	
	
	/**
	 * calculate player match win %
	 */
	public void calculatePMW();
	
	/**
	 * calculate oponents match win %
	 */
	public void calculateOMW();
	
	/**
	 * calculate player game win %
	 */
	public void calculatePGW();
	
	/**
	 * calculate oponents game win %
	 */
	public void calculateOGW();
	/**
	 * calculate all statistics
	 */
	public void calculateAll();
}
