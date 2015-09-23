package unii.draft.mtg.parings.algorithm;

/**
 * Created by Arkadiusz Pachucy on 2015-07-14.
 */
public interface IAlgorithmConfigure {

    public void setAlgorithm(IParingAlgorithm paringAlgorithm);

    public IParingAlgorithm getInstance();
}
