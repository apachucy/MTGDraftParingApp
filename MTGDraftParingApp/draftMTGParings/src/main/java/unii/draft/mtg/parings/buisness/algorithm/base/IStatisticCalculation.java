package unii.draft.mtg.parings.buisness.algorithm.base;

public interface IStatisticCalculation {

	
	
	/**
	 * calculate player match win %
	 */
    void calculatePMW();
	
	/**
	 * calculate oponents match win %
	 */
    void calculateOMW();
	
	/**
	 * calculate player game win %
	 */
    void calculatePGW();
	
	/**
	 * calculate oponents game win %
	 */
    void calculateOGW();
	/**
	 * calculate all statistics
	 */
    void calculateAll();
}
