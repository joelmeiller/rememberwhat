package ch.meiller.joel.rememberwhat.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.meiller.joel.rememberwhat.MainActivity;
import ch.meiller.joel.rememberwhat.R;

/**
 * Created by Joel on 04/12/15.
 *
 * UI class to create card fragments
 */
public class Card extends Fragment {

    public static final String DEBUG = "Card";

    public static final String TITLE = "CardTitle";
    public static final String TEXT = "CardText";
    public static final String IS_FRONT = "IsFront";

    private TextView titleView, textView;

    public Card() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layoutView = inflater.inflate(R.layout.fragment_card, container, false);

        boolean isFront = getArguments().getBoolean(IS_FRONT);

        //Set title
        titleView = (TextView) layoutView.findViewById(R.id.cardTitle);
        titleView.setText(getArguments().getString(TITLE));
        titleView.setTextColor(getResources().getColor(isFront ? R.color.textBlack : R.color.textWhite));


        //Set text
        textView = (TextView) layoutView.findViewById(R.id.cardText);
        textView.setText(getArguments().getString(TEXT));
        textView.setTextColor(getResources().getColor(isFront ? R.color.textBlack : R.color.textWhite));

        //Set color
        PercentRelativeLayout bv = (PercentRelativeLayout) layoutView.findViewById(R.id.card);

        int rectID = (isFront ? R.drawable.rectangle_white : R.drawable.rectangle_black);
        bv.setBackgroundResource(rectID);

        ImageView img = (ImageView) layoutView.findViewById(R.id.watermark);
        img.setImageResource(isFront ? R.drawable.watermark_large_white : R.drawable.watermark_large_black);


        return layoutView;

    }
}
