/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e1262;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author hydro
 */
public class Session {
    private static Preferences session = Preferences.userNodeForPackage(Session.class);

    public static void set(String key, String value) {
        
        session.put(key, value);
        System.out.println("Session set for " + key + " " + value);
    }
    

    public static String get(String key) {
        return session.get(key, null);
    }

    public static void distroy(String key) {
        session.remove(key);
        System.out.println("Session unset = " + key);
    }
    public static void distroyAll() {
        try {
            session.clear();
        } catch (BackingStoreException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
