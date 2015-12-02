package ch.meiller.joel.rememberwhat.model;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Joel on 01/12/15.
 */
public class RememberItemManager {

    static final int BUFFER_SIZE = 2048;

    private static RememberItemManager manager;

    private List<RememberItem> rememberItemList;
    private RememberItem activeItem;
    private Context context;

    private static final String FILENAME = "rememberWhat.txt";
    private static final int READ_BLOCK_SIZE = 100;



    public static RememberItemManager getInstance(){

        if( manager == null ){
            manager = new RememberItemManager();
        }

        return manager;
    }

    private RememberItemManager(){

        rememberItemList = new LinkedList<RememberItem>();

    }

    public void setContext(Context context){
        this.context = context;
    }

    public boolean loadList() {

        try
        {
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader InputRead= new InputStreamReader(fis);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                s +=String.copyValueOf(inputBuffer,0,charRead);
            }
            InputRead.close();

            String[] itemList = s.split("#//");

            for (String itemData:itemList) {
                RememberItem item = new RememberItem(itemData);
                if( item.isValid ){
                    rememberItemList.add(item);
                }
            }


        }
        catch (Exception ex)
        {
            return false;
        }


        return rememberItemList.size() > 0;
    }
    private boolean writeItems() {

        if( context == null ) throw new ExceptionInInitializerError("Missing Context");

        try
        {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            BufferedOutputStream outputWriter=new BufferedOutputStream(fos, BUFFER_SIZE);
            for (RememberItem item:rememberItemList) {
                outputWriter.write(item.toOutputString());
            }
            outputWriter.flush();
            outputWriter.close();
        }
        catch (Exception ex)
        {
            return false;
        }
        return true;
    }

    public List<RememberItem> getList() {
        return rememberItemList;
    }
    /*
        adds the remember item to the list

     */
    public boolean addItem(RememberItem item){
        if( item == null ) return false;

        rememberItemList.add(item);
        activeItem = item;

        return writeItems();
    }

    /*
        update the remember item in the list
    */
    public boolean editItem(RememberItem item){
        if( item == null || !rememberItemList.contains(item) ) return false;

        int pos = rememberItemList.indexOf(item);
        rememberItemList.remove(pos);
        rememberItemList.add(pos, item);
        activeItem = item;

        return writeItems();
    }

    /*
        deletes the active remember item from the list

     */
    public boolean deleteItem(){
        if( activeItem == null || !rememberItemList.contains(activeItem) ) return false;

        int pos = rememberItemList.indexOf(activeItem);
        rememberItemList.remove(activeItem);
        if( rememberItemList.size() > 0 ){
            if( pos > 0 ) pos--;
            activeItem = rememberItemList.get(pos);
        }
        return writeItems();
    }

    public RememberItem getActiveItem(){

        if( rememberItemList.size() == 0 ) return null;

        if( activeItem == null ) {
            activeItem = rememberItemList.get(0);
        }

        return activeItem;
    }

    public void switchActiveItem(){

        activeItem.switchSide();

        writeItems();

    }
}
