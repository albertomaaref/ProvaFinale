package it.dsgroup.provafinale.utilities;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by utente9.academy on 14/12/2017.
 */

public final class InternalStorage {

    private InternalStorage(){}

    public static void writeObject (Context context, String key, Object object){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(key,Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject (Context context, String key){
        FileInputStream fis = null;
        ObjectInput ois = null;
        Object object = null;
        try {
            fis = context.openFileInput(key);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();


        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;

    }
}
