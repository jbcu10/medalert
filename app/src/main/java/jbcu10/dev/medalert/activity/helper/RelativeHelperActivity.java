package jbcu10.dev.medalert.activity.helper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.EditRelativeActivity;
import jbcu10.dev.medalert.db.RelativeRepository;
import jbcu10.dev.medalert.model.Relative;

/**
 * Created by dev on 12/10/17.
 */

public class RelativeHelperActivity extends  BaseActivity {
    public RelativeRepository relativeRepository;
    protected Relative relative;
    @BindView(R.id.edit_first_name)
    protected EditText edit_first_name;
    @BindView(R.id.edit_middle_name)
    protected EditText edit_middle_name;
    @BindView(R.id.edit_last_name)
    protected EditText edit_last_name;
    @BindView(R.id.edit_contact_number)
    protected EditText edit_contact_number;
    @BindView(R.id.edit_email)
    protected EditText edit_email;
    @BindView(R.id.edit_relationship)
    protected EditText edit_relationship;
    @BindView(R.id.button_submit)
    protected Button button_submit;
    public void initializedViews() {
        ButterKnife.bind(this);
        relativeRepository = new RelativeRepository(this);
        edit_first_name = findViewById(R.id.edit_first_name);
        edit_middle_name = findViewById(R.id.edit_middle_name);
        edit_last_name = findViewById(R.id.edit_last_name);
        edit_contact_number = findViewById(R.id.edit_contact_number);
        edit_email = findViewById(R.id.edit_email);
        edit_relationship = findViewById(R.id.edit_relationship);
    }

    @OnClick(R.id.edit_relationship)
    public void onClickEditType(View view) {

        new MaterialDialog.Builder(this)
                .title("Select Relationship")
                .items(R.array.relation)
                .itemsCallbackSingleChoice(-1, (dialog, view1, which, text) -> {
                    edit_relationship.setText(text);
                    return true;
                })
                .positiveText("Select")
                .show();

    }

}
