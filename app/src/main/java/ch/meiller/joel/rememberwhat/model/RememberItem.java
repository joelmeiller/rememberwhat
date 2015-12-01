package ch.meiller.joel.rememberwhat.model;

import android.graphics.Color;

/**
 * Created by Joel on 25/11/15.
 */
public class RememberItem {

    private String title;
    private String blackText, whiteText;
    private int blackColor, whiteColor;
    private Boolean isWhiteActive;

    public RememberItem(){
        this("Remember What?","White text...","Black text...");
    }

    public RememberItem(String title, String white, String black){
        this.title = title;
        this.blackText = black;
        this.blackColor = Color.BLACK;
        this.whiteText = white;
        this.whiteColor = Color.WHITE;
        this.isWhiteActive = true;
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

    public void setBlackText(String blackText) {
        this.blackText = blackText;
    }

    public void setWhiteText(String whiteText) {
        this.whiteText = whiteText;
    }

    public Boolean getIsWhiteActive() {
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

}

