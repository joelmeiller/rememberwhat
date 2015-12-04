package ch.meiller.joel.rememberwhat.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.meiller.joel.rememberwhat.MainActivity;
import ch.meiller.joel.rememberwhat.R;
import ch.meiller.joel.rememberwhat.helper.SimpleGestureFilter;

public class Card extends Fragment {

    public static final String DEBUG = "Card";

    public static final String TITLE = "CardTitle";
    public static final String TEXT = "CardText";
    public static final String IS_FRONT = "IsFront";

    private SimpleGestureFilter detector;
    private TextView titleView, textView;

    public Card() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.fragment_card, container, false);

        boolean isFront = getArguments().getBoolean(IS_FRONT);

        //Set title
        titleView = (TextView) layoutView.findViewById(R.id.rememberSwitchTitle);
        titleView.setText(getArguments().getString(TITLE));
        titleView.setTextColor(getResources().getColor(isFront ? R.color.textBlack : R.color.textWhite));

        //Set text
        textView = (TextView) layoutView.findViewById(R.id.rememberSwitchText);
        textView.setText(getArguments().getString(TEXT));
        textView.setTextColor(getResources().getColor(isFront ? R.color.textBlack : R.color.textWhite));

        //Set color
        PercentRelativeLayout bv = (PercentRelativeLayout) layoutView.findViewById(R.id.rememberSwitchCard);

        int rectID = (isFront ? R.drawable.rectangle_white : R.drawable.rectangle_black);
        bv.setBackgroundResource(rectID);
        /*bv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).flipCard();
            }
        });*/


        return layoutView;

    }
}
