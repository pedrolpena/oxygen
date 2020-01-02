/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aomloxygentitrator;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.prefs.Preferences;

/**
 *
 * @author pedro
 */
public class ReadSerialPort implements Runnable {

    MainFrame mainFrame;
    private boolean stopped = false;
    private SerialPort port = null;
    private BufferedReader is = null;
    private InputStreamReader ist = null;
    private CommPortIdentifier portId = null;
    private Preferences prefs;

    public ReadSerialPort(MainFrame mF) {

        mainFrame = mF;
        prefs = Preferences.userNodeForPackage(getClass());
    }//end constructor

    @Override
    public void run() {

        portId = getPort(mainFrame.getSelectedSerialPort());
        String someChar = "";
        String someLine = "";
        String temp;
        String tmp = "";
        boolean isSampling = false;
        Calendar date;
        HashMap<Integer, Points> points;
        points = new HashMap<>();

        try {

            port = (SerialPort) portId.open("OxygenTitator", 10000);
            port.setSerialPortParams(
                    mainFrame.getPortSpeed(),
                    mainFrame.getPortDataBits(),
                    mainFrame.getStopBits(),
                    mainFrame.getPortParity());
            mainFrame.setPort(port);

            ist = new InputStreamReader(port.getInputStream());

        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch

        while (!stopped && port != null) {
            Date time = new Date();
            date = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            date.setTime(time);

            mainFrame.setDate(String.format("%1$tm" + "/" + "%1$td" + "/" + "%1$ty", date, date, date));
            mainFrame.setTime(String.format("%1$tH" + ":" + "%1$tM" + ":" + "%1$tS", date, date, date));

            try {

                Thread.sleep(25L);
                if (ist != null && ist.ready()) {

                    someChar = (char) ist.read() + "";
                    someLine += someChar;
                    someLine = someLine.replaceAll("\r", "\n").replaceAll("\n\n", "\n");

                    if (someLine.endsWith("\n") /*&& someLine.length() > 1*/) // detect end of line
                    {

                        someLine = someLine.replaceAll("\n", "");
                        mainFrame.getrawOutputTextArea().append(someLine + "\n");
                        mainFrame.getrawOutputTextArea().setCaretPosition(mainFrame.getrawOutputTextArea().getDocument().getLength());
                        mainFrame.getrawOutputTextAreaConfig().append(someLine + "\n");
                        mainFrame.getrawOutputTextAreaConfig().setCaretPosition(mainFrame.getrawOutputTextAreaConfig().getDocument().getLength());

                        //log raw data
                        logText(someLine + "\n", mainFrame.getCruise() + "_" + mainFrame.getStationNumber() + ".dat_ABR_TRT");

                        // detect if the titrator is sampling
                        if (someLine.equals("SampleType 0")) {
                            isSampling = true;
                            points.clear();
                            mainFrame.setresetAndSave(false);

                        }//end if

                        // detect when the titrator is done sampling
                        if (someLine.contains("set clock in titrator") || someLine.contains("Limit of data points exceeded")) {
                            isSampling = false;

                        }//end if 

                        // detect reset
                        if (someLine.equals("Reseting") && !isSampling) {

                            mainFrame.setresetAndSave(true);

                        }//end if                    

                        if (isSampling) {

                            //collect points
                            String line[] = someLine.split(",");

                            if (line.length == 5) {
                               

                                int sampleNumber = Integer.parseInt(line[3].trim());
                                double titrant = string2Double(line[1].trim());
                                double current = string2Double(line[2].trim());
                                int U = Integer.parseInt(line[4].trim());
                                points.put(sampleNumber, new Points(titrant,current,U));
                                mainFrame.updateRawDataPoints(points);
                            }//end if
                           

                            
                            
                            
                            //detect endpoint and set it
                            if (someLine.contains("Endpoint is")) {
                                double x;
                                String[] a = someLine.split(" ");
                                String num = a[a.length - 1];
                                x = string2Double(num);
                                mainFrame.setEP(x);
                                mainFrame.setPoints(points);
                                mainFrame.calculateAndUpdate();
                                
                            }//end if
                            
                            
                            
                            

                        }

                        temp = someLine;
                        someChar = "";
                        someLine = "";

                        //rawDataDisplay.setForeground(Color.black);                      
                    }//end if

                }//end if                mainFrame.getrawOutputTextArea().append("hello\n");

            }//end try
            catch (Exception e) {
                isSampling = false;
                e.printStackTrace();
            }//end catch
            tmp = (new Date()).getTime() + "";
        }//end while
    }//end run

    public void stopThread() {
        try {
            stopped = true;
            if (ist != null) {
                ist.close();
                ist = null;
            }//end f   
            if (is != null) {
                is.close();
                is = null;

            }//end f

            if (port != null) {
                port.close();
                port = null;

            }//end if        

            if (portId != null) {
                portId = null;

            }//end if

        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch
    }

    /**
     * Searches for available ports
     *
     * @param port String the name of the port that you want to return
     * @return CommPortIdentifier
     */
    private CommPortIdentifier getPort(String port) {
        // Javacomm fields
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier pid = null;
        while (portIdentifiers.hasMoreElements()) {
            pid = (CommPortIdentifier) portIdentifiers.nextElement();
            // this.portComboBox.addItem(pid.getName());
            if (pid.getName().equals(port)) {
                break;
            }
        }

        return pid;
    }// end getPort

    /**
     * When called strings passed to it are appended to a file
     *
     * @param line
     * @param logFileName
     */
    public void logText(String line, String logFileName) {

        logFileName = prefs.get("dataPath", "") + File.separator + logFileName;

        try {
            //line = line.replaceAll("\n", "");
            FileWriter logFile = new FileWriter(logFileName, true);
            logFile.append(line);
            logFile.close();
        } catch (Exception e) {

        }// end catch
    }// end logText   

 
    double string2Double(String s) {
        double num = -999.999;
        if (s.matches("^[-+]?[0-9]*\\.?[0-9]+$")) {
            
            num = Double.parseDouble(s);
        }
        
        return num;
    }//end method    
    
}//end class
