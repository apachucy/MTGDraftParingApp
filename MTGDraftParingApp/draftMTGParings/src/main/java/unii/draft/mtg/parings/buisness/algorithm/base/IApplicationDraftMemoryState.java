package unii.draft.mtg.parings.buisness.algorithm.base;

public interface IApplicationDraftMemoryState {
    boolean cacheDraft();

    boolean isLoadCachedDraftWasNeeded() throws NullPointerException;

    void clearCache();

    boolean isCacheEmpty();
}
