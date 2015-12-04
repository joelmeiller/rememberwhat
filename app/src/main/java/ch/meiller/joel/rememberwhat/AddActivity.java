package ch.meiller.joel.rememberwhat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.EditText;

import ch.meiller.joel.rememberwhat.model.RememberItem;
import ch.meiller.joel.rememberwhat.model.RememberItemManager;


public class AddActivity extends Activity {

    public static final String TITLE = "Title";
    public static final String WHITE_TEXT = "WhiteText";
    public static final String BLACK_TEXT = "BlackText";

    private String title, whiteText, blackText;
    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final Context context = this;

        Bundle args = getIntent().getExtras();

        //Edit mode
        isEditMode = (args != null );

        if(isEditMode) {
            ((EditText) findViewById(R.id.titleText)).setText(args.getString(TITLE));
            ((EditText) findViewById(R.id.whiteText)).setText(args.getString(WHITE_TEXT));
            ((EditText) findViewById(R.id.blackText)).setText(args.getString(BLACK_TEXT));
        }

        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                title = ((EditText) findViewById(R.id.titleText)).getText().toString();
                whiteText = ((EditText) findViewById(R.id.whiteText)).getText().toString();
                blackText = ((EditText) findViewById(R.id.blackText)).getText().toString();

                RememberItem rememberItem = new RememberItem(title, whiteText, blackText);

                if( isEditMode){
                    if (!RememberItemManager.getInstance().editItem(rememberItem)) {
                        //TODO Alert that item could not be saved
                    }
                }else {

                    if (!RememberItemManager.getInstance().addItem(rememberItem)) {
                        //TODO Alert that item could not be saved
                    }
                }

                //Log.d("Add", rememberItem.getWhiteText() + " " + rememberItem.getBlackText() );

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                //setContentView(R.layout.activity_main);
            }
        });

        FloatingActionButton cancel = (FloatingActionButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                //setContentView(R.layout.activity_main);
            }
        });
        if( RememberItemManager.getInstance().getList().size() == 0 ) {
            cancel.setVisibility(View.INVISIBLE);
        }
    }
}
