package ch.meiller.joel.rememberwhat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ch.meiller.joel.rememberwhat.fragment.Card;
import ch.meiller.joel.rememberwhat.model.RememberItem;
import ch.meiller.joel.rememberwhat.model.RememberItemManager;

public class MainActivity extends Activity {


    private Card whiteCard, blackCard, card;

    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add new remember item
        final Context context = this;

        // Load data
        RememberItemManager.getInstance().setContext(context);
        RememberItemManager.getInstance().loadList();

        Log.d("Main", "List: " + RememberItemManager.getInstance().getList().size() );

        if( RememberItemManager.getInstance().getList().size() == 0 ) {

            Intent intent = new Intent(context, AddActivity.class);
            startActivity(intent);

        }else {

            whiteCard = new Card();
            whiteCard.setArguments(getCardArgs(true));

            blackCard = new Card();
            blackCard.setArguments(getCardArgs(false));

            Card card = blackCard;

            if (RememberItemManager.getInstance().getActiveItem().getIsWhiteActive()) {
                card = whiteCard;
            }

            if (savedInstanceState == null) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, card)
                        .commit();
            }

            // Edit Button to update the shown RememberItem
            FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle args = new Bundle();
                    args.putString(AddActivity.TITLE, RememberItemManager.getInstance().getActiveItem().getTitle());
                    args.putString(AddActivity.WHITE_TEXT, RememberItemManager.getInstance().getActiveItem().getWhiteText());
                    args.putString(AddActivity.BLACK_TEXT, RememberItemManager.getInstance().getActiveItem().getBlackText());

                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);
                    finish();
                }
            });

            // Delete Button to delete the shown RememberItem
            FloatingActionButton delete = (FloatingActionButton) findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RememberItemManager.getInstance().deleteItem();

                    whiteCard = new Card();
                    whiteCard.setArguments(getCardArgs(true));

                    blackCard = new Card();
                    blackCard.setArguments(getCardArgs(false));

                    flipCard();

                }
            });

            // Add Button to add a new Remember Item
            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, AddActivity.class);
                    startActivity(intent);
                    finish();

                }
            });
        }
    }


    private Bundle getCardArgs(boolean isWhite){
        Bundle args = new Bundle();
        args.putString(Card.TITLE, RememberItemManager.getInstance().getActiveItem().getTitle());
        if( isWhite ){
            args.putString(Card.TEXT, RememberItemManager.getInstance().getActiveItem().getWhiteText());
        }else{
            args.putString(Card.TEXT, RememberItemManager.getInstance().getActiveItem().getBlackText());
        }

        args.putBoolean(Card.IS_FRONT, isWhite);

        return args;
    }

    public void flipCard() {
        RememberItemManager.getInstance().switchActiveItem();

        Card card = blackCard;

        if (RememberItemManager.getInstance().getActiveItem().getIsWhiteActive()) {
            card = whiteCard;
        }

        // Create and commit a new fragment transaction that adds the fragment for the back of
        // the card, uses custom animations, and is part of the fragment manager's back stack.

        getFragmentManager()
                .beginTransaction()

                        // Replace the default fragment animations with animator resources representing
                        // rotations when switching to the back of the card, as well as animator
                        // resources representing rotations when flipping back to the front (e.g. when
                        // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_in, R.animator.card_flip_out)

                        // Replace any fragments currently in the container view with a fragment
                        // representing the next page (indicated by the just-incremented currentPage
                        // variable).
                .replace(R.id.container, card)

                        // Add this transaction to the back stack, allowing users to press Back
                        // to get to the front of the card.
                .addToBackStack(null)

                        // Commit the transaction.
                .commit();
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

}
