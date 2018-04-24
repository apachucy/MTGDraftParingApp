package unii.draft.mtg.parings.buisness.algorithm.base;

import unii.draft.mtg.parings.sharedprefrences.IGamePreferences;

public interface IApplicationDraftMemoryState {
    boolean cacheDraft();

    boolean isLoadCachedDraftWasNeeded() throws NullPointerException;

    void clearCache();

    boolean isCacheEmpty();

    IGamePreferences getInstance();
}
