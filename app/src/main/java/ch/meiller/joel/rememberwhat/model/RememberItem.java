package ch.meiller.joel.rememberwhat.model;

import android.graphics.Color;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joel on 25/11/15.
 *
 * Contains the data for each remember task
 */
public class RememberItem {




    private String title;
    private String blackText, whiteText;
    private int blackColor, whiteColor;
    private boolean isWhiteActive;

    protected boolean isValid = true;

    public RememberItem(){
        this("Remember What?", "White text...", "Black text...");
    }

    public RememberItem(String title, String white, String black){
        this.title = title;
        this.blackText = black;
        this.blackColor = Color.BLACK;
        this.whiteText = white;
        this.whiteColor = Color.WHITE;
        this.isWhiteActive = true;
    }

    protected RememberItem(String byteString) {

        String[] data = byteString.split("#/");

        if(data.length == 4){
            title = data[0];
            whiteText = data[1];
            blackText = data[2];
            isWhiteActive = (data[3].equals("1"));
        }else{
            isValid = false;
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return isWhiteActive ? whiteText : blackText;
    }

    public int getColor() {
        return isWhiteActive ? whiteColor : blackColor;
    }

    public String getBlackText() {
        return blackText;
    }

    public String getWhiteText() {
        return whiteText;
    }

    public void setBlackText(String blackText) {
        this.blackText = blackText;
    }

    public void setWhiteText(String whiteText) {
        this.whiteText = whiteText;
    }

    public boolean getIsWhiteActive() {
        return isWhiteActive;
    }

    public void setIsWhiteActive(Boolean isWhiteActive) {
        this.isWhiteActive = isWhiteActive;
    }

    public void switchSide() {
        this.isWhiteActive = !this.isWhiteActive;
    }

    public RememberItem clone(){
        return new RememberItem(this.title, this.whiteText, this.blackText);
    }


    public boolean equals(Object obj) {
        if( !(obj instanceof RememberItem) ) return false;

        RememberItem item = (RememberItem) obj;

        return item.getTitle().equals(this.title);
    }

    protected byte[] toOutputString() {

        return (title + "#/" + whiteText + "#/" + blackText + "#/" + (isWhiteActive?"1":"0") + "#//").getBytes();

    }

}

