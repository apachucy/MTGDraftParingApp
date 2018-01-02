package unii.draft.mtg.parings.logic.dagger;

import dagger.Subcomponent;
import unii.draft.mtg.parings.RoundActivity;
import unii.draft.mtg.parings.view.activities.options.AddPlayerActivity;
import unii.draft.mtg.parings.view.activities.options.DropPlayerActivity;
import unii.draft.mtg.parings.view.activities.settings.HistoryPlayerAchievementsActivity;
import unii.draft.mtg.parings.view.activities.settings.HistoryScoreBoardActivity;
import unii.draft.mtg.parings.MainActivity;
import unii.draft.mtg.parings.view.activities.options.ManualPlayerPairingActivity;
import unii.draft.mtg.parings.view.activities.options.SaveScoreBoardActivity;
import unii.draft.mtg.parings.ScoreBoardActivity;
import unii.draft.mtg.parings.view.activities.settings.SettingsMenuActivity;
import unii.draft.mtg.parings.view.activities.options.SittingsActivity;
import unii.draft.mtg.parings.view.fragments.DropPlayerFragment;
import unii.draft.mtg.parings.view.fragments.GameMenuFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerAchievementsFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryPlayerListFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryScoreBoardDetailFragment;
import unii.draft.mtg.parings.view.fragments.history.HistoryScoreBoardListFragment;
import unii.draft.mtg.parings.view.fragments.settings.AddPlayerFragment;
import unii.draft.mtg.parings.view.fragments.settings.SettingsMenuFragment;
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

    void inject(AddPlayerActivity addPlayerActivity);

    void inject(HistoryPlayerAchievementsActivity historyPlayerAchievementsActivity);

    void inject(ScoreBoardActivity scoreBoardActivity);

    void inject(RoundActivity roundActivity);

    void inject(ManualPlayerPairingActivity manualPlayerPairingActivity);

    void inject(HistoryScoreBoardActivity historyScoreBoardActivity);

    void inject(DropPlayerActivity dropPlayerActivity);

    void inject(SaveScoreBoardActivity saveScoreBoardActivity);

    void inject(SettingsMenuActivity settingsMenuActivity);

    void inject(SettingsMenuFragment settingsMenuFragment);

    void inject(SittingsFragment sittingsFragment);

    void inject(GameMenuFragment gameMenuFragment);

    void inject(DropPlayerFragment dropPlayerFragment);

    void inject(HistoryScoreBoardDetailFragment historyScoreBoardDetailFragment);

    void inject(HistoryScoreBoardListFragment historyScoreBoardListFragment);

    void inject(TimeSettingsFragment timeSettingsFragment);

    void inject(DraftSettingsFragment draftSettingsFragment);

    void inject(OtherSettingsFragment otherSettingsFragment);

    void inject(HistorySettingsFragment historySettingsFragment);

    void inject(HistoryPlayerListFragment historyPlayerListFragment);

    void inject(HistoryPlayerAchievementsFragment historyPlayerAchievementsFragment);

    void inject(AddPlayerFragment addPlayerFragment);
}

