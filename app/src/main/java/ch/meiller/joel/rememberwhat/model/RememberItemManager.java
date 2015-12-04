package ch.meiller.joel.rememberwhat.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
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

    static final int BUFFER_SIZE = 4096;
    static final String DEBUG = "RememberItemManager";

    private static RememberItemManager manager;

    private List<RememberItem> rememberItemList;
    private RememberItem activeItem;
    private Context context;
    private boolean loaded;

    private static final String FILENAME = "data/rememberWhat";



    public static RememberItemManager getInstance(){

        if( manager == null ){
            manager = new RememberItemManager();
        }

        return manager;
    }

    private RememberItemManager(){

        rememberItemList = new LinkedList<RememberItem>();

        loaded = false;

    }

    public void setContext(Context context){

        this.context = context;

        if( !loaded ){
            loadList();
        }
    }

    public boolean loadList() {

        try
        {
            Log.d(DEBUG, "Load list ");
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedInputStream inputStream = new BufferedInputStream(fis);

            byte[] inputBuffer= new byte[BUFFER_SIZE];
            String s="";
            int bytesRead;

            while( (bytesRead = inputStream.read(inputBuffer)) != -1){
                s += new String(inputBuffer, 0, bytesRead);
            }

            inputStream.close();



            String[] itemList = s.split("#//");

            Log.d(DEBUG, "Splitted List: " + itemList.length);

            for (String itemData:itemList) {
                RememberItem item = new RememberItem(itemData);
                if( item.isValid ){
                    rememberItemList.add(item);
                }
            }

            Log.d(DEBUG, "Loaded List: " + rememberItemList.size());


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
            Log.d(DEBUG, "Write list: " + rememberItemList.size());
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

    public boolean next() {
        if( rememberItemList.size() > 1 )  {

            int pos = rememberItemList.indexOf(activeItem);

            if( pos < rememberItemList.size() - 1 ){
                pos++;
            }else{
                pos = 0;
            }
            activeItem = rememberItemList.get(pos);
        }
        return rememberItemList.size() > 1;
    }
    public boolean previous() {
        if( rememberItemList.size() > 1 )  {

            int pos = rememberItemList.indexOf(activeItem);

            if( pos > 0 ){
                pos--;
            }else{
                pos = rememberItemList.size() - 1;
            }
            activeItem = rememberItemList.get(pos);
        }
        return rememberItemList.size() > 1;
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
        if( item == null ) return false;

        int pos = rememberItemList.indexOf(activeItem);
        rememberItemList.remove(activeItem);
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
