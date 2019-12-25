package unii.draft.mtg.parings.view.activities.options;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import unii.draft.mtg.parings.BaseActivity;
import unii.draft.mtg.parings.R;
import unii.draft.mtg.parings.logic.dagger.ActivityComponent;
import unii.draft.mtg.parings.util.config.BundleConst;


public class SaveScoreBoardActivity extends BaseActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @Nullable
    @BindView(R.id.save_score_boardTextInputLayout)
    TextInputLayout mScoreBoardTextInput;

    @OnClick(R.id.save_score_board_acceptButton)
    void onSavedGameNameClicked() {
        String gameName = mScoreBoardTextInput.getEditText().getText().toString();
        if (gameName != null && !gameName.trim().equals("")) {
            Intent intent = new Intent();
            intent.putExtra(BundleConst.BUNDLE_KEY_SAVED_GAME_NAME, gameName);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score_board);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    @Override
    protected void injectDependencies(@NonNull ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void initToolBar() {
        setSupportActionBar(mToolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        mToolBar.setLogoDescription(R.string.app_header_path_configuration);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle(R.string.app_header_path_configuration);
    }

    @Override
    protected void initView() {
        mScoreBoardTextInput.setHint(getString(R.string.activity_save_score_board_info_text));
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
