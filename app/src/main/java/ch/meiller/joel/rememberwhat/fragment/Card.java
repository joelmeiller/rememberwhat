package ch.meiller.joel.rememberwhat.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ch.meiller.joel.rememberwhat.R;

import static ch.meiller.joel.rememberwhat.R.drawable.rectangle_black;
import static ch.meiller.joel.rememberwhat.R.drawable.rectangle_white;

public class Card extends Fragment {

    public static final String TITLE = "CardTitle";
    public static final String TEXT = "CardText";
    public static final String IS_FRONT = "IsFront";

    public Card() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.fragment_switch_detail, container, false);

        //Set title
        TextView tv = (TextView) layoutView.findViewById(R.id.rememberSwitchTitle);
        tv.setText(getArguments().getString(TITLE));

        //Set text
        tv = (TextView) layoutView.findViewById(R.id.rememberSwitchText);
        tv.setText(getArguments().getString(TEXT));

        //Set color
        Button bv = (Button) layoutView.findViewById(R.id.rememberSwitchButton);

        int rectID = rectangle_white;
        if( !getArguments().getBoolean(IS_FRONT)) rectID = rectangle_black;
        Drawable rect = getResources().getDrawable(rectID);
        bv.setBackground(rect);

        return layoutView;

    }
}
