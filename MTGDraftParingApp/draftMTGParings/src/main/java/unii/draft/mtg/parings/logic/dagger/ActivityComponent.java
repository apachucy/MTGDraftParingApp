package unii.draft.mtg.parings.logic.dagger;

import dagger.Subcomponent;
import unii.draft.mtg.parings.DropPlayerActivity;
import unii.draft.mtg.parings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.MainActivity;
import unii.draft.mtg.parings.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.ParingDashboardActivity;
import unii.draft.mtg.parings.SaveScoreBoardActivity;
import unii.draft.mtg.parings.ScoreBoardActivity;
import unii.draft.mtg.parings.SittingsActivity;
import unii.draft.mtg.parings.view.fragments.DropPlayerFragment;
import unii.draft.mtg.parings.view.fragments.GameMenuFragment;
import unii.draft.mtg.parings.view.fragments.HistoryScoreBoardDetailFragment;
import unii.draft.mtg.parings.view.fragments.HistoryScoreBoardListFragment;
import unii.draft.mtg.parings.view.fragments.SettingsFragment;
import unii.draft.mtg.parings.view.fragments.SittingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.DraftSettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.HistorySettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.OtherSettingsFragment;
import unii.draft.mtg.parings.view.fragments.settings.TimeSettingsFragment;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(SittingsActivity sittingsActivity);

    void inject(ScoreBoardActivity scoreBoardActivity);

    void inject(ParingDashboardActivity paringDashboardActivity);

    void inject(ManualPlayerPairingActivity manualPlayerPairingActivity);

    void inject(HistoryScoreBoardActivity historyScoreBoardActivity);

    void inject(DropPlayerActivity dropPlayerActivity);

    void inject(SaveScoreBoardActivity saveScoreBoardActivity);

    void inject(SettingsFragment settingsFragment);

    void inject(SittingsFragment sittingsFragment);

    void inject(GameMenuFragment gameMenuFragment);

    void inject(DropPlayerFragment dropPlayerFragment);

    void inject(HistoryScoreBoardDetailFragment historyScoreBoardDetailFragment);

    void inject(HistoryScoreBoardListFragment historyScoreBoardListFragment);

    void inject(TimeSettingsFragment timeSettingsFragment);

    void inject(DraftSettingsFragment draftSettingsFragment);

    void inject(OtherSettingsFragment otherSettingsFragment);

    void inject(HistorySettingsFragment historySettingsFragment);
}

