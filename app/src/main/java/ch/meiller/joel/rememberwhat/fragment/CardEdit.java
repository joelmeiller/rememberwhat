package ch.meiller.joel.rememberwhat.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ch.meiller.joel.rememberwhat.MainActivity;
import ch.meiller.joel.rememberwhat.R;
import ch.meiller.joel.rememberwhat.model.RememberItem;
import ch.meiller.joel.rememberwhat.model.RememberItemManager;

public class CardEdit extends Fragment {

    public static final String DEBUG = "CardEdit";

    public static final String TITLE = "Title";
    public static final String WHITE_TEXT = "WhiteText";
    public static final String BLACK_TEXT = "BlackText";
    public static final String EDIT_MODE = "Edit";

    private TextView titleView, whiteTextView, blackTextView;
    private String title, whiteText, blackText;
    private boolean isEditMode;

    public CardEdit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.fragment_card_edit, container, false);

        isEditMode = getArguments().getBoolean(EDIT_MODE);

        if(isEditMode) {
            ((EditText) layoutView.findViewById(R.id.titleText)).setText(getArguments().getString(TITLE));
            ((EditText) layoutView.findViewById(R.id.whiteText)).setText(getArguments().getString(WHITE_TEXT));
            ((EditText) layoutView.findViewById(R.id.blackText)).setText(getArguments().getString(BLACK_TEXT));
        }

        FloatingActionButton save = (FloatingActionButton) layoutView.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                title = ((EditText) layoutView.findViewById(R.id.titleText)).getText().toString();
                whiteText = ((EditText) layoutView.findViewById(R.id.whiteText)).getText().toString();
                blackText = ((EditText) layoutView.findViewById(R.id.blackText)).getText().toString();

                RememberItem rememberItem = new RememberItem(title, whiteText, blackText);

                if (isEditMode) {
                    if (!RememberItemManager.getInstance().editItem(rememberItem)) {
                        //TODO Alert that item could not be saved
                    }
                } else {
                    if (!RememberItemManager.getInstance().addItem(rememberItem)) {
                        //TODO Alert that item could not be saved
                    }
                }

                ((MainActivity) getActivity()).onFlipSave();

            }
        });

        FloatingActionButton cancel = (FloatingActionButton) layoutView.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ((MainActivity) getActivity()).onShowCard();
            }
        });
        if( RememberItemManager.getInstance().getList().size() == 0 ) {
            cancel.setVisibility(View.INVISIBLE);
        }

        return layoutView;

    }
}
