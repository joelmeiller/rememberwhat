package ch.meiller.joel.rememberwhat.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

/**
 * Created by Joel on 01/12/15.
 *
 * Factory to handle the remember task items of the user including the storage on the mobile phone
 * as file
 * Use this factory to add, remove and update the tasks
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


    /**
     * returns the remember manager instance
     * @return RememberItemManager instance
     */
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

    /**
     * set view context which is required to open the file stream
     * @param context
     * @throws MissingResourceException
     */
    public void setContext(Context context){

        this.context = context;

        if( !loaded ){
            loadList();
        }
    }


    private boolean loadList() {


        if (context == null) throw new MissingResourceException("view context is missing", Context.class.getName(), "android.content.Context");

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

    /**
     * returns list of all active remember tasks
     * @return List<RememberItem>
     */
    public List<RememberItem> getList() {
        return rememberItemList;
    }

    /**
     * sets the active remember item to the next entry in the list. If the end of the list is
     * reached the first item is set to active
     * @return true if the list is not empty, otherwise false.
     */
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
    /**
     * sets the active remember item to the previous entry in the list. If the beginning of the list
     * is reached the last item is set to active
     * @return true if the list is not empty, otherwise false.
     */
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

    /**
     * adds an item to the list. If the item's title is equal to an existing entry the existing
     * entry is overwritten by the new entry
     * @param item
     * @return true if the item could be added to the list, otherwise false
     */
    public boolean addItem(RememberItem item){
        if( item == null ) return false;

        rememberItemList.add(item);
        activeItem = item;

        return writeItems();
    }

    /**
     * updates the active item with the values of the new item.
     * @param item
     * @return true if the active item could be updated, otherwise false
     */
    public boolean editItem(RememberItem item){
        if( item == null ) return false;

        int pos = rememberItemList.indexOf(activeItem);
        rememberItemList.remove(activeItem);
        rememberItemList.add(pos, item);
        activeItem = item;

        return writeItems();
    }

    /**
     * deletes the active item
     * @return true if the item could have been deleted, otherwise false
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

    /**
     * returns the active item
     * @return active remember item
     */
    public RememberItem getActiveItem(){

        if( rememberItemList.size() == 0 ) return null;

        if( activeItem == null ) {
            activeItem = rememberItemList.get(0);
        }

        return activeItem;
    }

    /**
     * switches the side of the active item
     */
    public void switchActiveItem(){

        activeItem.switchSide();

        writeItems();

    }
}
