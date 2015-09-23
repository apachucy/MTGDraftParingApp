package unii.draft.mtg.parings.algorithm;

public class AlgorithmFactory {

	private static IParingAlgorithm sInstance;
	
	public static IParingAlgorithm getInstance(){
		return sInstance;
	}
	
	public static void configure(IParingAlgorithm paringAlgorithm){
		sInstance = paringAlgorithm;
	}
}
