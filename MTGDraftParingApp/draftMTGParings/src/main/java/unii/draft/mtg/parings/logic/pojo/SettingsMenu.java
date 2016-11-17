package unii.draft.mtg.parings.logic.pojo;


public class SettingsMenu {

    public static final int MENU_DRAFT = 0;
    public static final int MENU_TIME = 1;
    public static final int MENU_OTHER = 2;
    public static final int MENU_HISTORY = 3;
    private int mId;
    private String mTextMenu;
    private int mImageResourceId;


    public SettingsMenu(String description, int resId, int id) {
        mTextMenu = description;
        mImageResourceId = resId;
        mId = id;
    }

    public String getTextMenu() {
        return mTextMenu;
    }

    public void setTextMenu(String mTextMenu) {
        this.mTextMenu = mTextMenu;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public void setImageResourceId(int mImageResourceId) {
        this.mImageResourceId = mImageResourceId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }
}
