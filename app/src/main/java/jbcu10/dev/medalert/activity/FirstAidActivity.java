package jbcu10.dev.medalert.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import jbcu10.dev.medalert.R;
import jbcu10.dev.medalert.activity.helper.BaseActivity;
import jbcu10.dev.medalert.config.AppController;
import jbcu10.dev.medalert.db.FirstAidRepository;
import jbcu10.dev.medalert.model.FirstAid;
import jbcu10.dev.medalert.model.Instructions;

public class FirstAidActivity extends BaseActivity {
    public FirstAidRepository firstAidRepository;
    TextView txt_name, txt_description;
    LinearLayout ll_instruction_handler;
    FirstAid firstAid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);
        initialize();
        firstAidRepository = new FirstAidRepository(FirstAidActivity.this);
        AppController appController = AppController.getInstance();
        firstAid = firstAidRepository.getById(appController.getFirstAidId());
        txt_name.setText(firstAid.getName());
        txt_description.setText(firstAid.getDescription());
        HomeActivity.selectedItem =3;

        for (Instructions instruction : firstAid.getInstructionsList()) {
            TextView txt_instruction = new TextView(this);
            txt_instruction.setText(instruction.getInstruction());
            txt_instruction.setId(instruction.getId());
            txt_instruction.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            ll_instruction_handler.addView(txt_instruction);
        }


    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
    private void initialize() {
        txt_name = findViewById(R.id.txt_name);
        txt_description = findViewById(R.id.txt_description);
        ll_instruction_handler = findViewById(R.id.ll_instruction_handler);

    }
}
