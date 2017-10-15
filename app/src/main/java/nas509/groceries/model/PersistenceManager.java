//package nas509.groceries.model;
//
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
//import java.util.List;
//
///**
// * Created by rahul on 10/15/17.
// */
//
//public class PersistenceManager {
//    public final static String DEFAULT_BINARY_FILE_NAME = "data.bin";
//    public final static String DEFAULT_TEXT_FILE_NAME = "data.txt";
//    public final static String DEFAULT_JSON_FILE_NAME = "data.json";
//
//    /**
//     * the facade maintains references to any required model classes
//     */
//    private GroceryItemManager gm;
//
//    /**
//     * Singleton pattern
//     */
//    private static PersistenceManager instance = new PersistenceManager();
//
//    public static PersistenceManager getInstance() {
//        return instance;
//    }
//
//    /**
//     * private constructor for facade pattern
//     */
//    private PersistenceManager() {
//        gm = GroceryItemManager.getInstance();
//    }
//
//    public boolean loadText(File file) {
//        try {
//            //make an input object for reading
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            gm.loadFromText(reader);
//
//        } catch (FileNotFoundException e) {
//            Log.e("ModelSingleton", "Failed to open text file for loading!");
//            return false;
//        }
//
//        return true;
//    }
//
//    public boolean saveText(File file) {
//        System.out.println("Saving as a text file");
//        try {
//            PrintWriter pw = new PrintWriter(file);
//            gm.saveAsText(pw);
//            pw.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.d("UserManagerFacade", "Error opening the text file for save!");
//            return false;
//        }
//
//        return true;
//    }
//}
