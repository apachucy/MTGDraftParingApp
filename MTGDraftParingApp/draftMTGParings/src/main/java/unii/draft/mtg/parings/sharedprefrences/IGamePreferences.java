package unii.draft.mtg.parings.sharedprefrences;


import unii.draft.mtg.parings.logic.pojo.DraftDataProvider;

public interface IGamePreferences {
    void saveDraftDataProvider(DraftDataProvider draftDataProvider);

    DraftDataProvider getDraftDataProvider();

    void clean();
}
