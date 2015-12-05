package ch.meiller.joel.rememberwhat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import ch.meiller.joel.rememberwhat.fragment.Card;
import ch.meiller.joel.rememberwhat.fragment.CardEdit;
import ch.meiller.joel.rememberwhat.model.RememberItemManager;

public class MainActivity extends Activity {

    private static final String DEBUG = "MainActivity";

    private static final int SWIPE_LEFT = 0;
    private static final int SWIPE_RIGHT = 1;

    private GestureDetector mGestureDetector;
    private Card whiteCard, blackCard;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add new remember item
        final Context context = this;

        // Load data
        RememberItemManager.getInstance().setContext(context);

        Log.d("Main", "List: " + RememberItemManager.getInstance().getList().size());

        if (RememberItemManager.getInstance().getList().size() == 0) {

            onFlipEdit(false);

        } else {

            if (savedInstanceState == null) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, getCard())
                        .commit();
            }


        }

        // Edit Button to update the shown RememberItem
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(DEBUG, "Edit Button Clicked");

                onFlipEdit(true);
            }
        });

        // Delete Button to delete the shown RememberItem
        FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(DEBUG, "Delete Button Clicked");

                RememberItemManager.getInstance().deleteItem();

                onSwipeDelete();

            }
        });

        // Add Button to add a new Remember Item
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(DEBUG, "Add Button Clicked");

                onFlipEdit(false);

            }
        });

        // Add Gesture Dector
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);
    }

    public void onFlipCard() {

        Log.d(DEBUG, "Flip Card");

        RememberItemManager.getInstance().switchActiveItem();

        onShowCard();
    }

    public void onFlipEdit(boolean isEditMode) {

        Log.d(DEBUG, "Swipe Edit Card direction " + SWIPE_RIGHT);


        if( isEditMode ){
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)
                    .replace(R.id.container, getCardEdit(isEditMode))
                    .addToBackStack(null)
                    .commit();
        }else{
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_slide_in_right, R.animator.card_slide_out_left)
                    .replace(R.id.container, getCardEdit(isEditMode))
                    .addToBackStack(null)
                    .commit();
        }

        //findViewById(R.id.nav).setAlpha(0.0f);


    }

    public void onFlipSave() {

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)
                .replace(R.id.container, getCard())
                .addToBackStack(null)
                .commit();

        //findViewById(R.id.nav).setAlpha(1.0f);

    }

    public void onShowCard() {

        Card card;
        if (RememberItemManager.getInstance().getActiveItem().getIsWhiteActive()) {
            card = whiteCard;
        } else {
            card = blackCard;
        }

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)
                .replace(R.id.container, card)
                .addToBackStack(null)
                .commit();

        //findViewById(R.id.nav).setAlpha(1.0f);
    }

    public void onSwipe(int direction) {

        Log.d(DEBUG, "Swipe Card direction " + direction);

        switch (direction) {

            case SWIPE_RIGHT:

                if (RememberItemManager.getInstance().next()) {

                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.card_slide_in_left, R.animator.card_slide_out_right)
                            .replace(R.id.container, getCard())
                            .addToBackStack(null)
                            .commit();
                }
                break;

            case SWIPE_LEFT:

                if (RememberItemManager.getInstance().previous()) {

                    getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.card_slide_in_right, R.animator.card_slide_out_left)
                            .replace(R.id.container, getCard())
                            .addToBackStack(null)
                            .commit();
                }
                break;
            default:
                //Do nothing
                break;

        }
    }

    public void onSwipeDelete() {

        Log.d(DEBUG, "Swipe Deleted Card direction " + SWIPE_RIGHT);

        if(RememberItemManager.getInstance().getList().size() > 0) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_slide_in_left, R.animator.card_slide_out_right)
                    .replace(R.id.container, getCard())
                    .addToBackStack(null)
                    .commit();
        }else{
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_slide_in_left, R.animator.card_slide_out_right)
                    .replace(R.id.container, getCardEdit(false))
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Card getCard() {

        Card card;

        whiteCard = new Card();
        whiteCard.setArguments(getCardArgs(true));

        blackCard = new Card();
        blackCard.setArguments(getCardArgs(false));

        if (RememberItemManager.getInstance().getActiveItem().getIsWhiteActive()) {
            card = whiteCard;
        } else {
            card = blackCard;
        }

        return card;
    }

    private Bundle getCardArgs(boolean isWhite) {

        Bundle args = new Bundle();

        args.putBoolean(Card.IS_FRONT, isWhite);
        args.putString(Card.TITLE, RememberItemManager.getInstance().getActiveItem().getTitle());
        if (isWhite) {
            args.putString(Card.TEXT, RememberItemManager.getInstance().getActiveItem().getWhiteText());
        } else {
            args.putString(Card.TEXT, RememberItemManager.getInstance().getActiveItem().getBlackText());
        }

        return args;
    }

    private CardEdit getCardEdit(boolean isEditMode) {

        CardEdit card = new CardEdit();
        Bundle args = new Bundle();

        args.putBoolean(CardEdit.EDIT_MODE, isEditMode);
        if(isEditMode) {
            args.putString(CardEdit.TITLE, RememberItemManager.getInstance().getActiveItem().getTitle());
            args.putString(CardEdit.WHITE_TEXT, RememberItemManager.getInstance().getActiveItem().getWhiteText());
            args.putString(CardEdit.BLACK_TEXT, RememberItemManager.getInstance().getActiveItem().getBlackText());
        }

        card.setArguments(args);

        return card;
    }

    public class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                onSwipe(SWIPE_LEFT);
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                onSwipe(SWIPE_RIGHT);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            onFlipCard();

            return super.onSingleTapConfirmed(e);
        }

    }

}
