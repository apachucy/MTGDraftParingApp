package unii.draft.mtg.parings.util.config;


import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.pojo.SettingsMenu;

public class SettingsMenuItems {

    private List<SettingsMenu> mSettingsMenuList;


    public SettingsMenuItems(@NonNull Context context) {
        mSettingsMenuList = new ArrayList<>();

        //draft
        SettingsMenu settingsMenuDraft = new SettingsMenu(context.getString(R.string.settings_menu_item_draft), R.drawable.ic_draft, SettingsMenu.MENU_DRAFT);
        //time
        SettingsMenu settingsMenuTime = new SettingsMenu(context.getString(R.string.settings_menu_item_time), R.drawable.ic_time, SettingsMenu.MENU_TIME);
        //other
        SettingsMenu settingsMenuOther = new SettingsMenu(context.getString(R.string.settings_menu_item_other), R.drawable.ic_other, SettingsMenu.MENU_OTHER);
        //history
        SettingsMenu settingsMenuHistory = new SettingsMenu(context.getString(R.string.settings_menu_item_history), R.drawable.ic_history, SettingsMenu.MENU_HISTORY);

        mSettingsMenuList.add(settingsMenuDraft);
        mSettingsMenuList.add(settingsMenuTime);
        mSettingsMenuList.add(settingsMenuOther);
        mSettingsMenuList.add(settingsMenuHistory);
    }

    public List<SettingsMenu> getSettingsMenuList() {
        return mSettingsMenuList;
    }

    public void setSettingsMenuList(List<SettingsMenu> mSettingsMenuList) {
        this.mSettingsMenuList = mSettingsMenuList;
    }
}
