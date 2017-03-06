package unii.draft.mtg.parings.buisness.algorithm.base;

public interface IApplicationDraftMemoryState {
    void cacheDraft();

    boolean isLoadCachedDraftWasNeeded();

    void clearCache();
}
