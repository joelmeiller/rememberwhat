package ch.meiller.joel.rememberwhat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;

import ch.meiller.joel.rememberwhat.fragment.Card;
import ch.meiller.joel.rememberwhat.model.RememberItem;

public class SwitchFragment extends Fragment {

    private static final String DEBUG_TAG = "SwitchFragment";

    private RememberItem rememberItem;
    private Card whiteCard, blackCard;

    public static SwitchFragment init(RememberItem item) {

        return new SwitchFragment();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.rememberItem = ((MainActivity) this.getActivity()).getRememeberItem();

        whiteCard = new Card();
        whiteCard.setArguments( getCardArgs() );

        rememberItem.switchSide();

        blackCard = new Card();
        blackCard.setArguments( getCardArgs() );

        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.switchContainer, whiteCard)
                    .commit();

        }


    }

    private Bundle getCardArgs(){
        Bundle args = new Bundle();
        args.putString(Card.TITLE, rememberItem.getTitle());
        args.putString(Card.TEXT, rememberItem.getText());
        args.putBoolean(Card.IS_FRONT, rememberItem.getIsWhiteActive());

        return args;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layoutView = inflater.inflate(R.layout.fragment_swith, container, false);
        layoutView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                flipCard();

            }
        });


        return layoutView;
    }

    private void flipCard() {
        rememberItem.switchSide();

        Card card = blackCard;

        if (rememberItem.getIsWhiteActive()) {
            card = whiteCard;
        }

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getChildFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.switchContainer, card)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();
    }
}
