package unii.draft.mtg.parings.buisness.algorithm;

public interface IApplicationDraftMemoryState {
    void cacheDraft();

    boolean isLoadCachedDraftWasNeeded();

    void clearCache();
}
