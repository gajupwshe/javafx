package e1262;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 *
 * @author Narpat Singh
 */
public class Background_Processes {

    public static volatile boolean pause_insert, run_insert, stop_time, break_time;
    public static Thread insert, timeThread;

    /*
    *Process executor
    *@param python_file_url
     */
    public static void insert_plc_data(String python_file_url, boolean pause, boolean run) {
        System.out.println("Thread started insert : " + python_file_url);
        pause_insert = pause;
        run_insert = run;
        try {
            insert = new Thread(() -> {
                while (run_insert) {
                    try {
                        Thread.sleep(10);
                        if (!pause_insert) {
                            Process child = Runtime.getRuntime().exec(python_file_url);
                            child.waitFor();
//                                BufferedReader reader = new BufferedReader(new InputStreamReader(child.getInputStream()));
//                                String line = "";
//                                String savedOutput = "";
//                                while ((line = reader.readLine()) != null) {
//                                    savedOutput = savedOutput + "/" + line;
//                                }
//                                child.waitFor();
//                                System.out.println(""+savedOutput);
                            if (!run_insert) {
//                                    System.out.println("Thread Stopped : " + python_file_url);
                                break;
                            }
                            if (!run_insert) {
//                                    System.out.println("Thread Stopped : " + python_file_url);
                                break;
                            }
                        } else {
                            if (!run_insert) {
//                                    System.out.println("Thread Stopped : " + python_file_url);
                                break;
                            }
                            switch (Session.get("write_type")) {
                                case "0":
                                    ToolKit.tagWrite(Session.get("tag"), Session.get("write_value"));
                                    Thread.sleep(25);
                                    pause_insert = false;
                                    System.out.println("Written OK 0 ");
                                    break;
                                case "1":
                                    ToolKit.strtagWrite(Session.get("tag"),Session.get("write_value"));
                                    Thread.sleep(25);
                                    pause_insert = false;
                                    System.out.println("Written OK 1 ");
                                    break;
                                case "2":
                                    ToolKit.tagWriteFloat(Session.get("tag"), Session.get("write_value"));
                                    Thread.sleep(25);
                                    pause_insert = false;
                                    System.out.println("Written OK 2 ");
                                    break;
                                default:
                                    System.out.println("WRITE ERROR INVAILID write_type" + Session.get("write_type"));
                                    break;
                            }

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, "INSERT_PLC_THREAD");
            insert.setDaemon(true);
            insert.start();
        } catch (Exception e) {
        }
    }

    public static void date_time(Text txtDate, boolean stop, boolean bre, String threadName) {
        stop_time = stop;
        break_time = bre;
        timeThread = new Thread(() -> {
            while (!stop_time) {
                try {
                    Date dateInstance = new Date();
                    txtDate.setText("Date : " + (dateInstance.getDate() + "/" + (dateInstance.getMonth() + 1) + "/" + (dateInstance.getYear() + 1900)));

                    if (!break_time) {
                    } else {
                        break;
                    }
                    Thread.sleep(900);
                } catch (Exception exc) {
                    System.out.println("Exception in Date_time : " + exc.getMessage());
                }
            }
        }, threadName);
        timeThread.setDaemon(true);
        timeThread.start();

    }

    public static void stop_plc_read() {
        try {
            run_insert = false;
            insert.stop();
        } catch (Exception e) {
            System.out.println("Exception in stoping background process insertPlcThread : " + e.getMessage());
        }
    }

    public static void pause_plc_read() {
        pause_insert = true;
    }

    public static void resume_plc_read() {
        pause_insert = false;
    }

    public static void stop_date_time() {
        stop_time = true;
        break_time = true;
        try {
            timeThread.stop();
        } catch (Exception e) {
        }
    }

    public static void Initialize_Initial_Screen() {
        try {
            System.out.println("python include/insert_initial_init.py localhost" + DatabaseHandler.DB_USER + " " + DatabaseHandler.DB_PASS + " " + DatabaseHandler.DB_NAME + " truncate_Initial_init insert_initial_init_sp");
            Runtime run = Runtime.getRuntime();
            Process child = run.exec("python include/insert_initial_init.py localhost " + DatabaseHandler.DB_USER + " " + DatabaseHandler.DB_PASS + " " + DatabaseHandler.DB_NAME + " truncate_Initial_init insert_initial_init_sp");
            child.waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }
    
    //Write tag mecthod Call for Modbus negative value 
        public static void Write_weighing( ){
        try {
             System.out.println("Thread started insert : " );
             //call tagwrite function for plc 
               ToolKit.tagWrite(Session.get("tag_w"), Session.get("weighing"));
             
    
            }
            catch (Exception e) {
        }
            
        
    }
    

}
