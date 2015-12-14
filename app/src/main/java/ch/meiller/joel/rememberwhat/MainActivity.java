package ch.meiller.joel.rememberwhat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import ch.meiller.joel.rememberwhat.fragment.Card;
import ch.meiller.joel.rememberwhat.fragment.CardEdit;
import ch.meiller.joel.rememberwhat.model.RememberItemManager;

/**
 * Created by Joel on 04/12/15.
 *
 * UI class to control main activity of the Remember What? app.
 */

public class MainActivity extends Activity {

    private static final String DEBUG = "MainActivity";

    private static final int SWIPE_LEFT = 0;
    private static final int SWIPE_RIGHT = 1;

    private CustomGestureDetector customGestureDetector;
    private GestureDetector mGestureDetector;
    private Card whiteCard, blackCard;
    private ViewGroup navView;

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

        // Add Gesture Dector
        customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);

        // Get navigation view
        navView = (ViewGroup) findViewById(R.id.nav);


        Log.d("Main", "List: " + RememberItemManager.getInstance().getList().size());

        if (RememberItemManager.getInstance().getList().size() == 0) {

            onFlipEdit(false);
            showNavigation(false);

        } else {

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, getCard())
                        .commit();
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


    }

    private void onFlipCard() {

        Log.d(DEBUG, "Flip Card");

        RememberItemManager.getInstance().switchActiveItem();

        onShowCard();
    }

    private void onFlipEdit(boolean isEditMode) {

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

        showNavigation(false);

    }

    private void onFlipSave() {

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)
                .replace(R.id.container, getCard())
                .addToBackStack(null)
                .commit();

        showNavigation(true);

    }

    private void onShowCard() {

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

        showNavigation(true);
    }

    private void onSwipe(int direction) {

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

    private void onSwipeDelete() {

        Log.d(DEBUG, "Swipe Deleted/Cancel Card direction " + SWIPE_RIGHT);

        if(RememberItemManager.getInstance().getList().size() > 0) {
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_slide_in_left, R.animator.card_slide_out_right)
                    .replace(R.id.container, getCard())
                    .addToBackStack(null)
                    .commit();

            showNavigation(true);
        }else{
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.animator.card_slide_in_left, R.animator.card_slide_out_right)
                    .replace(R.id.container, getCardEdit(false))
                    .addToBackStack(null)
                    .commit();

            showNavigation(false);
        }

    }

    private void showNavigation(boolean show){

        if( (navView.getChildAt(0).getVisibility() == View.VISIBLE) != show) {
            TransitionManager.beginDelayedTransition(navView, new Fade());
            for (int i = 0; i < navView.getChildCount(); i++) {

                View child = navView.getChildAt(i);
                child.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        }

        customGestureDetector.setIsEditMode(!show);
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

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private boolean isEditMode = false;


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

            if(isEditMode) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }else{
                onFlipCard();
            }

            return super.onSingleTapConfirmed(e);
        }

        public void setIsEditMode(boolean isEditMode) {
            this.isEditMode = isEditMode;
        }
    }

}
