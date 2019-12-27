package aomloxygentitrator;

import java.awt.Color;
import javax.swing.UIManager;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.util.Enumeration;
import java.util.prefs.Preferences;
import javax.swing.JTextArea;
import aomloxygentitrator.ReadSerialPort;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author pedro
 */
public class MainFrame extends javax.swing.JFrame {

    private Preferences prefs;
    private SerialPort port = null;
    private CommPortIdentifier portId = null;
    private boolean filesLoaded = false;
    private String serialPortName = "";
    private String OS = System.getProperty("os.name").toLowerCase();
    private Thread serialPortReader = null;
    ReadSerialPort rsp;
    private File pWD;
    private String dataPath;
    private String cruise;
    int station;
    int cast;
    int niskin;
    double depth;
    double lattitude;
    double longitude;
    int bottle;
    String sampleDate;
    String runDate;
    double thioTemp;
    double volKIO3;
    double NKIO3;
    double stduL;
    double b1;
    double b2;
    double m1;
    double m2;
    double mse1;
    double mse2;
    double blkul;
    double volReg;
    double drawTemp;
    double salinity;
    double EP;
    double SWDensity;
    double thioDensity;
    double M_ThiotL;
    double M_thio20C;
    double botVolume;
    double o2;
    double o2uM;
    double o2umolKg;
    double ulPerst;
    double ulOffset;
    double slope;
    double speed;

   
    int wait;
    
    HashMap<Integer, Double> bottleVolumeMap;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        //main(new String[0]);
        initComponents();

        bottleVolumeMap = new HashMap<>();
        String sp = "/dev/ttyUSB0";
        String spTmp = "";
        prefs = Preferences.userNodeForPackage(getClass());
        if (OS.indexOf("win") >= 0) {
            sp = "COM1";
        }//end if
        serialPortName = prefs.get("serialPortName", sp);

        CommPortIdentifier pid;
        Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        while (portIdentifiers.hasMoreElements()) {
            //Thread.sleep(100);

            pid = (CommPortIdentifier) portIdentifiers.nextElement();
            spTmp = new String(pid.getName());
            if (!spTmp.toLowerCase().contains("lpt")) {
                serialPortComboBox.addItem(spTmp);
            }
            serialPortComboBox.setSelectedItem(serialPortName);

        }// end while        

        //loadFields();

        resetAndSaveButton.setBackground(Color.YELLOW);
        resetAndSaveButton.setOpaque(true);
        resetAndSaveButton.setBorderPainted(false);
        
        fillButton.setBackground(Color.MAGENTA);
        fillButton.setOpaque(true);
        fillButton.setBorderPainted(false);    
        
        blankButton.setBackground(Color.decode("#fefefe"));
        blankButton.setOpaque(true);
        blankButton.setBorderPainted(false); 
        
        sampleButton.setBackground(Color.CYAN);
        sampleButton.setOpaque(true);
        sampleButton.setBorderPainted(false);
        
        saveButton.setBackground(Color.decode("#2c8cdc"));
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);    
        
        uLSetButton.setBackground(Color.MAGENTA);
        uLSetButton.setOpaque(true);
        uLSetButton.setBorderPainted(false);  
        
        slopeSpeedWaitButton.setBackground(Color.MAGENTA);
        slopeSpeedWaitButton.setOpaque(true);
        slopeSpeedWaitButton.setBorderPainted(false);
        
        dateButton.setBackground(Color.MAGENTA);
        dateButton.setOpaque(true);
        dateButton.setBorderPainted(false);        
        
        serialDisconnectButton.setEnabled(false);
        resetAndSaveButton.setEnabled(false);
        fillButton.setEnabled(false);
        blankButton.setEnabled(false);
        sampleButton.setEnabled(false);
        saveButton.setEnabled(false);
        uLSetButton  .setEnabled(false);      
        slopeSpeedWaitButton.setEnabled(false);        
        dateButton.setEnabled(false);       
                
        setVisible(true);

//        resetAndSaveButton.setBackground(java.awt.Color.YELLOW);
//        rawOutputTextArea.append("hello\nhello\nhello");
        //setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        mainJPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        runDateTextField = new javax.swing.JTextField();
        blkulTextField = new javax.swing.JTextField();
        thioTempTextField = new javax.swing.JTextField();
        stdulTextField = new javax.swing.JTextField();
        salinityTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        drawTempTextField = new javax.swing.JTextField();
        volKIO3TextField = new javax.swing.JTextField();
        castSpinner = new javax.swing.JSpinner();
        rawOutputScrollPane = new javax.swing.JScrollPane();
        rawOutputTextArea = new javax.swing.JTextArea();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        niskinSpinner = new javax.swing.JSpinner();
        jLabel16 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        NKIO3TextField = new javax.swing.JTextField();
        longitudeTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        botVolTextField = new javax.swing.JTextField();
        volRegTextField = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        depthTextField = new javax.swing.JTextField();
        stationSpinner = new javax.swing.JSpinner();
        cruiseTextField = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        thioDensityTextField = new javax.swing.JTextField();
        bottleSpinner = new javax.swing.JSpinner();
        M_thio_tl_TextField = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        EPTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        lattitudeTextField = new javax.swing.JTextField();
        M_thio_20C_TextField = new javax.swing.JTextField();
        sampleDateTextField = new javax.swing.JTextField();
        swDensityTextField = new javax.swing.JTextField();
        o2umTextField = new javax.swing.JTextField();
        o2_umolPerKgTextField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        DrawTempBottleVolTextField = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        btlVolFileTextField = new javax.swing.JTextField();
        dataFileTextField = new javax.swing.JTextField();
        openVolumeFileButton = new javax.swing.JButton();
        openDataFileButton = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        b1TextField = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        m1TextField = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        mse1TextField = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        b2TextField = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        m2TextField = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        mse2TextField = new javax.swing.JTextField();
        resetAndSaveButton = new javax.swing.JButton();
        fillButton = new javax.swing.JButton();
        blankButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        sampleButton = new javax.swing.JButton();
        plot1Panel = new javax.swing.JPanel();
        plot2Panel = new javax.swing.JPanel();
        configurationJPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ulPerstTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ulOffsetTextField = new javax.swing.JTextField();
        uLSetButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        speedTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        waitTextField = new javax.swing.JTextField();
        slopeSpeedWaitButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        timeTextField = new javax.swing.JTextField();
        dateButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        slopeTextField = new javax.swing.JTextField();
        dateTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        serialPortComboBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        baudRateComboBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        dataBitsComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        parityComboBox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        stopBitsComboBox = new javax.swing.JComboBox<>();
        flowControlComboBox = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        serialConnectButton = new javax.swing.JButton();
        serialDisconnectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        rawOutputTextAreaConfig = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AOML Oxygen Titratior");
        setSize(new java.awt.Dimension(1366, 768));

        thioTempTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thioTempTextFieldActionPerformed(evt);
            }
        });

        salinityTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salinityTextFieldActionPerformed(evt);
            }
        });
        salinityTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                salinityTextFieldPropertyChange(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel2.setText("Station");

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel20.setText("Niskin");

        jLabel37.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel37.setText("Vol KIO3, ml");

        drawTempTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawTempTextFieldActionPerformed(evt);
            }
        });

        castSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        rawOutputTextArea.setEditable(false);
        rawOutputTextArea.setColumns(20);
        rawOutputTextArea.setRows(5);
        rawOutputTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        rawOutputScrollPane.setViewportView(rawOutputTextArea);

        jLabel31.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel31.setText("Run Date");

        jLabel33.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel33.setText("Thio Temp");

        niskinSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        jLabel16.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel16.setText("Cast");

        jLabel27.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel27.setText("Sample Date");

        longitudeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                longitudeTextFieldActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel1.setText("Cruise");

        jLabel25.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel25.setText("Lattitude");

        botVolTextField.setEditable(false);

        jLabel34.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel34.setText("N KIO3, ml");

        depthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depthTextFieldActionPerformed(evt);
            }
        });

        stationSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        cruiseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cruiseTextFieldActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel29.setText("Bottle #");

        thioDensityTextField.setEditable(false);

        bottleSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        bottleSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bottleSpinnerStateChanged(evt);
            }
        });

        M_thio_tl_TextField.setEditable(false);

        jLabel38.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel38.setText("STD, uL");

        EPTextField.setEditable(false);

        jLabel18.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel18.setText("Depth");

        M_thio_20C_TextField.setEditable(false);

        swDensityTextField.setEditable(false);

        o2umTextField.setEditable(false);

        o2_umolPerKgTextField.setEditable(false);
        o2_umolPerKgTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o2_umolPerKgTextFieldActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel24.setText("Longitude");

        jLabel15.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel15.setText("Blk, uL");

        jLabel17.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel17.setText("Vol Reg");

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel21.setText("Draw Temp");

        jLabel19.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel19.setText("Salinity");

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel22.setText("EP, ul");

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel23.setText("SW Density");

        jLabel26.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel26.setText("Thio Density");

        jLabel32.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel32.setText("M(Thio 20C)");

        jLabel30.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel30.setText("Bot Vol, mL");

        jLabel36.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel36.setText("O2, umol/kg");

        jLabel35.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel35.setText("O2, uM");

        jLabel42.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel42.setText("M(Thio, tL)");

        jLabel48.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel48.setText("Vbot.tS, mL");

        DrawTempBottleVolTextField.setEditable(false);

        jLabel28.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel28.setText("Volume File");

        jLabel39.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel39.setText("Data File");

        btlVolFileTextField.setEditable(false);

        dataFileTextField.setEditable(false);

        openVolumeFileButton.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        openVolumeFileButton.setText("OPEN");
        openVolumeFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openVolumeFileButtonActionPerformed(evt);
            }
        });

        openDataFileButton.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        openDataFileButton.setText("OPEN");
        openDataFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDataFileButtonActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel41.setText("b1");

        b1TextField.setEditable(false);
        b1TextField.setText("                         ");
        b1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1TextFieldActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel43.setText("m1");

        m1TextField.setEditable(false);
        m1TextField.setText("                       ");
        m1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m1TextFieldActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel44.setText("mse1");

        mse1TextField.setEditable(false);
        mse1TextField.setText("                         ");

        jLabel45.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel45.setText("b2");

        b2TextField.setEditable(false);
        b2TextField.setText("                         ");

        jLabel46.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel46.setText("m2");

        m2TextField.setEditable(false);
        m2TextField.setText("                       ");

        jLabel47.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel47.setText("mse2");

        mse2TextField.setEditable(false);
        mse2TextField.setText("                         ");
        mse2TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mse2TextFieldActionPerformed(evt);
            }
        });

        resetAndSaveButton.setBackground(java.awt.Color.yellow);
        resetAndSaveButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        resetAndSaveButton.setForeground(java.awt.Color.black);
        resetAndSaveButton.setText("RESET & SAVE");
        resetAndSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetAndSaveButtonActionPerformed(evt);
            }
        });

        fillButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        fillButton.setForeground(java.awt.Color.black);
        fillButton.setText("FILL");
        fillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillButtonActionPerformed(evt);
            }
        });

        blankButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        blankButton.setForeground(java.awt.Color.black);
        blankButton.setText("BLANK");
        blankButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blankButtonActionPerformed(evt);
            }
        });

        saveButton.setBackground(new java.awt.Color(44, 140, 220));
        saveButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        saveButton.setForeground(java.awt.Color.black);
        saveButton.setText("SAVE");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        sampleButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        sampleButton.setForeground(java.awt.Color.black);
        sampleButton.setText("SAMPLE");
        sampleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 72, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addComponent(jLabel41))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b1TextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b2TextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel46)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(m2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel47))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(m1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel44)
                                .addGap(8, 8, 8)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(mse1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mse2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(sampleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 5, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(resetAndSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fillButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(blankButton))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btlVolFileTextField)
                            .addComponent(dataFileTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openVolumeFileButton)
                            .addComponent(openDataFileButton))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(b1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43)
                            .addComponent(m1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44)
                            .addComponent(mse1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(b2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(m2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(mse2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetAndSaveButton)
                            .addComponent(fillButton)
                            .addComponent(blankButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sampleButton)
                            .addComponent(saveButton))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btlVolFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(openVolumeFileButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(openDataFileButton)
                    .addComponent(dataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cruiseTextField))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(castSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stationSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(niskinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(depthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lattitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(longitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bottleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sampleDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(runDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thioTempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(volKIO3TextField)
                            .addComponent(NKIO3TextField)
                            .addComponent(stdulTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(blkulTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(volRegTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(drawTempTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(salinityTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EPTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(swDensityTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thioDensityTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_tl_TextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_20C_TextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(botVolTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(DrawTempBottleVolTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(o2umTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                            .addComponent(o2_umolPerKgTextField, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rawOutputScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {NKIO3TextField, bottleSpinner, castSpinner, depthTextField, lattitudeTextField, longitudeTextField, niskinSpinner, runDateTextField, sampleDateTextField, stationSpinner, stdulTextField, thioTempTextField, volKIO3TextField});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {DrawTempBottleVolTextField, EPTextField, M_thio_20C_TextField, M_thio_tl_TextField, blkulTextField, botVolTextField, drawTempTextField, o2_umolPerKgTextField, o2umTextField, salinityTextField, swDensityTextField, thioDensityTextField, volRegTextField});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(rawOutputScrollPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(cruiseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(blkulTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel15)
                            .addComponent(stationSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(volRegTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(castSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(drawTempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(niskinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel20)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(salinityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel19))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(depthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel18)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(EPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel22))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lattitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel25)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(swDensityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(longitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel23))
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(thioDensityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bottleSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(sampleDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_tl_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(runDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_20C_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(thioTempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botVolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel37)
                                .addComponent(volKIO3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel48))
                            .addComponent(DrawTempBottleVolTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(NKIO3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(o2umTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(stdulTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel38))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(o2_umolPerKgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel36)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {DrawTempBottleVolTextField, EPTextField, M_thio_20C_TextField, M_thio_tl_TextField, NKIO3TextField, blkulTextField, botVolTextField, bottleSpinner, castSpinner, cruiseTextField, depthTextField, drawTempTextField, lattitudeTextField, longitudeTextField, niskinSpinner, o2_umolPerKgTextField, o2umTextField, runDateTextField, salinityTextField, sampleDateTextField, stationSpinner, stdulTextField, swDensityTextField, thioDensityTextField, thioTempTextField, volKIO3TextField, volRegTextField});

        javax.swing.GroupLayout plot1PanelLayout = new javax.swing.GroupLayout(plot1Panel);
        plot1Panel.setLayout(plot1PanelLayout);
        plot1PanelLayout.setHorizontalGroup(
            plot1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 498, Short.MAX_VALUE)
        );
        plot1PanelLayout.setVerticalGroup(
            plot1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout plot2PanelLayout = new javax.swing.GroupLayout(plot2Panel);
        plot2Panel.setLayout(plot2PanelLayout);
        plot2PanelLayout.setHorizontalGroup(
            plot2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        plot2PanelLayout.setVerticalGroup(
            plot2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(mainJPanel);
        mainJPanel.setLayout(mainJPanelLayout);
        mainJPanelLayout.setHorizontalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(plot1Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(plot2Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainJPanelLayout.setVerticalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainJPanelLayout.createSequentialGroup()
                        .addComponent(plot1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(plot2Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Main", mainJPanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configure Titrator", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel3.setText("uLperst");

        ulPerstTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ulPerstTextFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel4.setText("uLoffset");

        ulOffsetTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ulOffsetTextFieldActionPerformed(evt);
            }
        });

        uLSetButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        uLSetButton.setText("SET");
        uLSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uLSetButtonActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel5.setText("Speed");

        jLabel6.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel6.setText("Wait");

        slopeSpeedWaitButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        slopeSpeedWaitButton.setText("SET");
        slopeSpeedWaitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slopeSpeedWaitButtonActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel7.setText("Time");

        timeTextField.setEditable(false);

        dateButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        dateButton.setText("SET");
        dateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateButtonActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel8.setText("Slope");

        dateTextField.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel9.setText("Date");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(slopeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(ulPerstTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(ulOffsetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(uLSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(speedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(waitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(slopeSpeedWaitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(20, 20, 20))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateTextField, slopeTextField, speedTextField, timeTextField, ulOffsetTextField, ulPerstTextField, waitTextField});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateButton, slopeSpeedWaitButton, uLSetButton});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ulPerstTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(ulOffsetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(uLSetButton)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(speedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(waitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slopeSpeedWaitButton)
                    .addComponent(jLabel8)
                    .addComponent(slopeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateButton)
                        .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dateTextField, slopeTextField, speedTextField, timeTextField, ulOffsetTextField, ulPerstTextField, waitTextField});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configure Serial Port", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel10.setText("Serial Port");

        serialPortComboBox.setToolTipText("");
        serialPortComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                serialPortComboBoxMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serialPortComboBoxMouseClicked(evt);
            }
        });
        serialPortComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialPortComboBoxActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel11.setText("Baud Rate");

        baudRateComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "300", "600", "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200" }));
        baudRateComboBox.setSelectedIndex(5);

        jLabel12.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel12.setText("Data Bits");

        dataBitsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "7", "6", "5" }));
        dataBitsComboBox.setToolTipText("");

        jLabel13.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel13.setText("Parity");

        parityComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "even", "odd" }));
        parityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                parityComboBoxActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel14.setText("Stop Bits");

        stopBitsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));

        flowControlComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "RTS/CTS", "Xon/Xoff" }));

        jLabel40.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel40.setText("Flow Control");

        serialConnectButton.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        serialConnectButton.setText("Connect");
        serialConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialConnectButtonActionPerformed(evt);
            }
        });

        serialDisconnectButton.setFont(new java.awt.Font("Monospaced", 1, 15)); // NOI18N
        serialDisconnectButton.setText("Disconnect");
        serialDisconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialDisconnectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(flowControlComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopBitsComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parityComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataBitsComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(baudRateComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serialPortComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(serialConnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(serialDisconnectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {baudRateComboBox, dataBitsComboBox, flowControlComboBox, parityComboBox, serialPortComboBox, stopBitsComboBox});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(serialPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(baudRateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(dataBitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(parityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(stopBitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(flowControlComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(serialConnectButton)
                    .addComponent(serialDisconnectButton))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {baudRateComboBox, dataBitsComboBox, flowControlComboBox, parityComboBox, serialPortComboBox, stopBitsComboBox});

        rawOutputTextAreaConfig.setEditable(false);
        rawOutputTextAreaConfig.setColumns(20);
        rawOutputTextAreaConfig.setRows(5);
        jScrollPane1.setViewportView(rawOutputTextAreaConfig);

        javax.swing.GroupLayout configurationJPanelLayout = new javax.swing.GroupLayout(configurationJPanel);
        configurationJPanel.setLayout(configurationJPanelLayout);
        configurationJPanelLayout.setHorizontalGroup(
            configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addContainerGap())
        );
        configurationJPanelLayout.setVerticalGroup(
            configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(configurationJPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(193, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Configuration", configurationJPanel);

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1381, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        this.saveFields();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void resetAndSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetAndSaveButtonActionPerformed
        if (port != null) {

            try {
                PrintStream os = null;

                os = new PrintStream(port.getOutputStream());
                os.print("R\r");

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch

            this.saveFields();
        }//end if
    }//GEN-LAST:event_resetAndSaveButtonActionPerformed

    private void longitudeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_longitudeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_longitudeTextFieldActionPerformed

    private void depthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depthTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depthTextFieldActionPerformed

    private void serialConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialConnectButtonActionPerformed

        if (serialPortReader == null) {
            serialConnectButton.setEnabled(false);
            serialDisconnectButton.setEnabled(true);

            resetAndSaveButton.setEnabled(true);
            fillButton.setEnabled(true);
            blankButton.setEnabled(true);
            sampleButton.setEnabled(true);
            saveButton.setEnabled(true);
            uLSetButton.setEnabled(true);
            slopeSpeedWaitButton.setEnabled(true);
            dateButton.setEnabled(true);

            rsp = new ReadSerialPort(this);
            serialPortReader = new Thread(rsp);
            serialPortReader.start();
            loadFields();

        }
    }//GEN-LAST:event_serialConnectButtonActionPerformed

    private void serialDisconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialDisconnectButtonActionPerformed
        if (rsp != null) {
            rsp.stopThread();
            rsp = null;
            serialPortReader = null;
            dateTextField.setText("");
            timeTextField.setText("");
            bottleVolumeMap.clear();
            serialConnectButton.setEnabled(true);
            serialDisconnectButton.setEnabled(false);
            resetAndSaveButton.setEnabled(false);
            fillButton.setEnabled(false);
            blankButton.setEnabled(false);
            sampleButton.setEnabled(false);
            saveButton.setEnabled(false);
            uLSetButton.setEnabled(false);
            slopeSpeedWaitButton.setEnabled(false);
            dateButton.setEnabled(false);
        }//end if
    }//GEN-LAST:event_serialDisconnectButtonActionPerformed

    private void cruiseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseTextFieldActionPerformed

    private void serialPortComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialPortComboBoxActionPerformed

    }//GEN-LAST:event_serialPortComboBoxActionPerformed

    private void serialPortComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serialPortComboBoxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_serialPortComboBoxMouseClicked

    private void serialPortComboBoxMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serialPortComboBoxMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_serialPortComboBoxMousePressed

    private void parityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_parityComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_parityComboBoxActionPerformed

    private void uLSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uLSetButtonActionPerformed

        if (port != null) {

            try {
                PrintStream os = null;
                String s1 = "";
                String s2 = "";
                double num1 = Double.parseDouble(ulPerstTextField.getText());
                double num2 = Double.parseDouble(ulOffsetTextField.getText());

                s1 = String.format("%1$.4f", num1);
                s2 = String.format("%1$.1f", num2);
                os = new PrintStream(port.getOutputStream());
                os.print("P " + s1 + " " + s2);

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch

            saveFields();
        }//end if
    }//GEN-LAST:event_uLSetButtonActionPerformed

    private void slopeSpeedWaitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slopeSpeedWaitButtonActionPerformed
        if (port != null) {

            try {
                PrintStream os = null;
                String s1 = "";
                String s2 = "";
                String s3 = "";
                double num1 = Double.parseDouble(slopeTextField.getText());
                double num2 = Double.parseDouble(speedTextField.getText());
                int num3 = Integer.parseInt(waitTextField.getText());

                s1 = String.format("%1$.1f", num1);
                s2 = String.format("%1$.1f", num2);
                s3 = String.format("%1$04d", num3);

                os = new PrintStream(port.getOutputStream());
                os.print("C " + s1 + " " + s2 + " " + s3);

                if (os != null) {
                    os.flush();
                    os.close();
                }
                saveFields();

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch
        }//end if
    }//GEN-LAST:event_slopeSpeedWaitButtonActionPerformed

    private void dateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateButtonActionPerformed
        if (port != null) {

            try {
                PrintStream os = null;

                os = new PrintStream(port.getOutputStream());
                os.print("T " + dateTextField.getText() + " " + timeTextField.getText());

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch

            saveFields();
        }//end if
    }//GEN-LAST:event_dateButtonActionPerformed

    private void fillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillButtonActionPerformed
        if (port != null) {

            try {
                PrintStream os = null;

                os = new PrintStream(port.getOutputStream());
                os.print("F\r");

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch
        }//end if
    }//GEN-LAST:event_fillButtonActionPerformed

    private void blankButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blankButtonActionPerformed
        if (port != null) {

            try {
                PrintStream os = null;

                os = new PrintStream(port.getOutputStream());
                os.print("B\r");

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch
        }//end if
    }//GEN-LAST:event_blankButtonActionPerformed

    private void sampleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleButtonActionPerformed
        if (port != null) {
            //saveFields();

            try {
                PrintStream os = null;

                os = new PrintStream(port.getOutputStream());
                os.print("S\r");

                if (os != null) {
                    os.flush();
                    os.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }// end catch
        }//end if
    }//GEN-LAST:event_sampleButtonActionPerformed

    private void openDataFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDataFileButtonActionPerformed

        JFileChooser saveFolder = new JFileChooser();
        saveFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = saveFolder.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String dir = saveFolder.getSelectedFile().getAbsolutePath();
            prefs.put("dataPath", dir);
            this.dataFileTextField.setText(dir);

        }//end if
    }//GEN-LAST:event_openDataFileButtonActionPerformed

    private void b1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b1TextFieldActionPerformed

    private void openVolumeFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openVolumeFileButtonActionPerformed


        JFileChooser saveFolder = new JFileChooser();
        int returnVal = saveFolder.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String btlVolFileName = saveFolder.getSelectedFile().getAbsolutePath();
            prefs.put("btlVol", btlVolFileName);
            this.btlVolFileTextField.setText(btlVolFileName);
           
            this.loadBottleFile(btlVolFileName);

        }//end if



        // TODO add your handling code here:
    }//GEN-LAST:event_openVolumeFileButtonActionPerformed

    private void bottleSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bottleSpinnerStateChanged

        bottle = Integer.parseInt(bottleSpinner.getValue().toString());
//        
        if (bottleVolumeMap.containsKey(bottle)) {
            botVolume = bottleVolumeMap.get(bottle);

        } else {
            botVolume = 0;
        }
//        botVolTextField.setText(botVolume + "");
        setBottleVol(botVolume);
        if (filesLoaded) {
//            setDrawTempBottleVol();
//            setO2();
            calculateAndUpdate();
            

        }//end if


    }//GEN-LAST:event_bottleSpinnerStateChanged

    private void salinityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salinityTextFieldActionPerformed
        calculateAndUpdate();

    }//GEN-LAST:event_salinityTextFieldActionPerformed

    private void ulOffsetTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulOffsetTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ulOffsetTextFieldActionPerformed

    private void ulPerstTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulPerstTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ulPerstTextFieldActionPerformed

    private void drawTempTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawTempTextFieldActionPerformed
        calculateAndUpdate();
    }//GEN-LAST:event_drawTempTextFieldActionPerformed

    private void salinityTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_salinityTextFieldPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_salinityTextFieldPropertyChange

    private void m1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m1TextFieldActionPerformed

    private void o2_umolPerKgTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o2_umolPerKgTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_o2_umolPerKgTextFieldActionPerformed

    private void mse2TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mse2TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mse2TextFieldActionPerformed

    private void thioTempTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thioTempTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_thioTempTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    }

    public void run() {
    }//end run

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DrawTempBottleVolTextField;
    private javax.swing.JTextField EPTextField;
    private javax.swing.JTextField M_thio_20C_TextField;
    private javax.swing.JTextField M_thio_tl_TextField;
    private javax.swing.JTextField NKIO3TextField;
    private javax.swing.JTextField b1TextField;
    private javax.swing.JTextField b2TextField;
    private javax.swing.JComboBox<String> baudRateComboBox;
    private javax.swing.JButton blankButton;
    private javax.swing.JTextField blkulTextField;
    private javax.swing.JTextField botVolTextField;
    private javax.swing.JSpinner bottleSpinner;
    private javax.swing.JTextField btlVolFileTextField;
    private javax.swing.JSpinner castSpinner;
    private javax.swing.JPanel configurationJPanel;
    private javax.swing.JTextField cruiseTextField;
    private javax.swing.JComboBox<String> dataBitsComboBox;
    private javax.swing.JTextField dataFileTextField;
    private javax.swing.JButton dateButton;
    private javax.swing.JTextField dateTextField;
    private javax.swing.JTextField depthTextField;
    private javax.swing.JTextField drawTempTextField;
    private javax.swing.JButton fillButton;
    private javax.swing.JComboBox<String> flowControlComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField lattitudeTextField;
    private javax.swing.JTextField longitudeTextField;
    private javax.swing.JTextField m1TextField;
    private javax.swing.JTextField m2TextField;
    private javax.swing.JPanel mainJPanel;
    private javax.swing.JTextField mse1TextField;
    private javax.swing.JTextField mse2TextField;
    private javax.swing.JSpinner niskinSpinner;
    private javax.swing.JTextField o2_umolPerKgTextField;
    private javax.swing.JTextField o2umTextField;
    private javax.swing.JButton openDataFileButton;
    private javax.swing.JButton openVolumeFileButton;
    private javax.swing.JComboBox<String> parityComboBox;
    private javax.swing.JPanel plot1Panel;
    private javax.swing.JPanel plot2Panel;
    private javax.swing.JScrollPane rawOutputScrollPane;
    private javax.swing.JTextArea rawOutputTextArea;
    private javax.swing.JTextArea rawOutputTextAreaConfig;
    private javax.swing.JButton resetAndSaveButton;
    private javax.swing.JTextField runDateTextField;
    private javax.swing.JTextField salinityTextField;
    private javax.swing.JButton sampleButton;
    private javax.swing.JTextField sampleDateTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton serialConnectButton;
    private javax.swing.JButton serialDisconnectButton;
    private javax.swing.JComboBox<String> serialPortComboBox;
    private javax.swing.JButton slopeSpeedWaitButton;
    private javax.swing.JTextField slopeTextField;
    private javax.swing.JTextField speedTextField;
    private javax.swing.JSpinner stationSpinner;
    private javax.swing.JTextField stdulTextField;
    private javax.swing.JComboBox<String> stopBitsComboBox;
    private javax.swing.JTextField swDensityTextField;
    private javax.swing.JTextField thioDensityTextField;
    private javax.swing.JTextField thioTempTextField;
    private javax.swing.JTextField timeTextField;
    private javax.swing.JButton uLSetButton;
    private javax.swing.JTextField ulOffsetTextField;
    private javax.swing.JTextField ulPerstTextField;
    private javax.swing.JTextField volKIO3TextField;
    private javax.swing.JTextField volRegTextField;
    private javax.swing.JTextField waitTextField;
    // End of variables declaration//GEN-END:variables

    public JTextArea getrawOutputTextArea() {

        return this.rawOutputTextArea;

    }//end method

    public JTextArea getrawOutputTextAreaConfig() {

        return this.rawOutputTextAreaConfig;

    }//end method   

    public String getSelectedSerialPort() {

        return (String) this.serialPortComboBox.getSelectedItem();

    }//end method

    public int getPortSpeed() {

        int selectedIndex = baudRateComboBox.getSelectedIndex();
        int selection = -1;

        switch (selectedIndex) {
            case -1:
                selection = 0;
                break;

            case 0:
                selection = 300;
                break;
            case 1:
                selection = 600;
                break;
            case 2:
                selection = 1200;
                break;
            case 3:
                selection = 2400;
                break;
            case 4:
                selection = 4800;
                break;
            case 5:
                selection = 9600;
                break;
            case 6:
                selection = 19200;
                break;
            case 7:
                selection = 38400;
                break;
            case 8:
                selection = 57600;
                break;
            case 9:
                selection = 115200;
                break;
        }// end switch
        return selection;

    }

    public int getStopBits() {

        int selectedIndex = this.stopBitsComboBox.getSelectedIndex();
        int selection = -1;

        switch (selectedIndex) {
            case -1:
                selection = 0;
                break;

            case 0:
                selection = SerialPort.STOPBITS_1;
                break;
            case 1:
                selection = SerialPort.STOPBITS_2;
                break;

        }// end switch
        return selection;

    }

    public int getPortParity() {

        int selectedIndex = this.parityComboBox.getSelectedIndex();
        int selection = -1;

        switch (selectedIndex) {
            case -1:
                selection = 0;
                break;

            case 0:
                selection = SerialPort.PARITY_NONE;
                break;
            case 1:
                selection = SerialPort.PARITY_EVEN;
                break;
            case 2:
                selection = SerialPort.PARITY_ODD;
                break;

        }// end switch
        return selection;

    }

    public int getPortDataBits() {

        int selectedIndex = this.dataBitsComboBox.getSelectedIndex();
        int selection = -1;

        switch (selectedIndex) {
            case -1:
                selection = 0;
                break;

            case 0:
                selection = SerialPort.DATABITS_8;
                break;
            case 1:
                selection = SerialPort.DATABITS_7;
                break;
            case 2:
                selection = SerialPort.DATABITS_6;
                break;
            case 3:
                selection = SerialPort.DATABITS_5;
                break;

        }// end switch
        return selection;

    }

    SerialPort getPort() {

        return port;
    }//end mwthod

    void setPort(SerialPort p) {
        port = p;
    }//end method

    void setDate(String d) {
        dateTextField.setText(d);
    }

    void setTime(String d) {
        timeTextField.setText(d);
    }

    String getCruise() {
        return this.cruiseTextField.getText();
    }//end method

    String getCastNumber() {
        String cs = castSpinner.getValue().toString();
        int x = Integer.parseInt(cs);
        String val = String.format("%1$03d", x);
        return val;
    }

    String getStationNumber() {
        String cs = stationSpinner.getValue().toString();
        int x = Integer.parseInt(cs);
        String val = String.format("%1$03d", x);
        return val;
    }

    String getDataPath() {
        return this.dataFileTextField.getText();
    }//end method

    String getNiskinNumber() {
        String s = niskinSpinner.getValue().toString();
        int x = Integer.parseInt(s);
        String val = String.format("%1$03d", x);
        return val;
    }

    String getBottleNumber() {
        String s = bottleSpinner.getValue().toString();
        int x = Integer.parseInt(s);
        String val = String.format("%1$03d", x);
        return val;
    }

    String getDepth() {
        String s = depthTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }

    String getLattitude() {
        String s = lattitudeTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method

    String getLongitude() {
        String s = longitudeTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method    

    String getSampleDate() {
        String s = sampleDateTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method     

    String getRunDate() {
        String s = runDateTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method   

    double getThioTemp() {
        String s = thioTempTextField.getText();
        double x = Double.parseDouble(s);
        thioTemp = x;
        return thioTemp ;
    }//end method 
    
    

    String getVolKIO3() {
        String s = volKIO3TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method   

    double getNKIO3() {
        String s = NKIO3TextField.getText();
        NKIO3 = Double.parseDouble(s);
        return NKIO3;
    }//end method  

    double getStdul() {
        String s = stdulTextField.getText();
        stduL = Double.parseDouble(s);
        return stduL;
    }//end method    

    double getBlkul() {
        String s = blkulTextField.getText();
        blkul = Double.parseDouble(s);
        return blkul;
    }//end method

    double getVolReg() {
        String s = volRegTextField.getText();
        double volReg = Double.parseDouble(s);
        return volReg;
    }//end method      

    double getDrawTemp() {
        String s = drawTempTextField.getText();
        drawTemp = Double.parseDouble(s);
        return drawTemp;
    }//end method      

    double getSalinty() {
        String s = salinityTextField.getText();
        salinity = Double.parseDouble(s);
        return salinity;
    }//end method    

    double getEPul() {
        String s = EPTextField.getText();
        EP = Double.parseDouble(s);
        return EP;
    }//end method  

    String getSwDensity() {
        String s = swDensityTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method    

    String getThioDensity() {
        String s = thioDensityTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method      

    String getM_thio_tl() {
        String s = M_thio_tl_TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method    

    String getM_thio_20C() {
        String s = M_thio_20C_TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method 

    double getBotVol() {
        String s = botVolTextField.getText();
        botVolume = Double.parseDouble(s);
        return botVolume;
    }//end method       

    String getO2um() {
        String s = o2umTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method  

    String geto2_umolPerKg() {
        String s = o2_umolPerKgTextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method     

    String getB1() {
        String s = b1TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method 

    String getB2() {
        String s = b2TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method  

    String getM1() {
        String s = m1TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method     

    String getM2() {
        String s = m2TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method 

    String getMse1() {
        String s = mse1TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method 

    String getMse2() {
        String s = mse2TextField.getText();
        double x = Double.parseDouble(s);
        return x + "";
    }//end method  
    
    void setEP(double ep) {
        EP = ep;
    }//end method

    void loadBottleFile(String file) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            String bottleNumber="0";
            String bottleVolume="";

            rawOutputTextArea.append("Loading Bottle Volumes\n");
            rawOutputTextAreaConfig.append("Loading Bottle Volumes\n");
            while (line != null) {
                line = br.readLine();

                if (line != null && !line.toLowerCase().contains("flask")) {
                    
                     bottleNumber = line.split("\t")[0];
                     bottleVolume = line.split("\t")[1];
                     
                     bottle=Integer.parseInt(bottleNumber);
                     botVolume=Double.parseDouble(bottleVolume);
                     
                     bottleVolumeMap.put(bottle,botVolume );
                     
                    
                    rawOutputTextArea.append(line + "\n");
                    rawOutputTextAreaConfig.append(line + "\n");
                    
                    
                       
                    
                 
                }
            }

            this.bottleSpinner.setValue(bottle);
            
            br.close();
            //String everything = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end method
    
    void saveFields() {
        prefs.put("cruise", cruiseTextField.getText());
        prefs.putInt("station", Integer.parseInt(stationSpinner.getValue().toString()));
        prefs.putInt("cast", Integer.parseInt(castSpinner.getValue().toString()));
        prefs.putInt("niskin", Integer.parseInt(niskinSpinner.getValue().toString()));
        prefs.putDouble("depth", Double.parseDouble(depthTextField.getText()));
        prefs.putDouble("lattitude", Double.parseDouble(lattitudeTextField.getText()));
        prefs.putDouble("longitude", Double.parseDouble(longitudeTextField.getText()));
        prefs.putInt("bottle", Integer.parseInt(bottleSpinner.getValue().toString()));
        prefs.put("sampleDate", sampleDateTextField.getText());
        prefs.put("runDate", runDateTextField.getText());
        prefs.putDouble("thioTemp", Double.parseDouble(thioTempTextField.getText()));
        prefs.putDouble("volKIO3", Double.parseDouble(volKIO3TextField.getText()));
        prefs.putDouble("NKIO3", Double.parseDouble(NKIO3TextField.getText()));
        prefs.putDouble("stduL", Double.parseDouble(stdulTextField.getText()));
        prefs.putDouble("blkul", Double.parseDouble(blkulTextField.getText()));
        prefs.putDouble("volReg", Double.parseDouble(volRegTextField.getText()));
        prefs.putDouble("drawTemp", Double.parseDouble(drawTempTextField.getText()));
        prefs.putDouble("salinity", Double.parseDouble(salinityTextField.getText()));

        prefs.putDouble("ulPerst", Double.parseDouble(ulPerstTextField.getText()));
        prefs.putDouble("ulOffset", Double.parseDouble(ulOffsetTextField.getText()));
        prefs.putDouble("slope", Double.parseDouble(slopeTextField.getText()));
        prefs.putDouble("speed", Double.parseDouble(speedTextField.getText()));
        prefs.putInt("wait", Integer.parseInt(waitTextField.getText()));
        

    }

    void loadFields() {
        String defaultPWD = System.getProperty("user.home") + File.separatorChar + "data" + File.separatorChar + "oxygen";

        if (OS.indexOf("win") >= 0) {
            defaultPWD = System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "data" + File.separatorChar + "salinometer";
        }//end if

        try {
            pWD = new File(defaultPWD);
            pWD.mkdirs();
        } catch (Exception e) {

            pWD.mkdirs();
            pWD = new File(System.getProperty(defaultPWD));
        }
        
        
        // check if bottle volume file exists
        String tmp = prefs.get("btlVol", "");
        File f = new File(tmp);
        if (f.exists() && !f.isDirectory()) {
            this.btlVolFileTextField.setText(tmp);
            this.loadBottleFile(tmp);
            
        }

        dataPath = prefs.get("dataPath", "");
        if (dataPath.equals("")) {
            prefs.put("dataPath", pWD.getAbsolutePath());
            dataPath = pWD.getAbsolutePath();

        }//end if

        cruise = prefs.get("cruise", "");
        if (cruise.equals("")) {
            cruise = "DEFAULT";
            prefs.put("cruise", cruise);

        }//end if 

        station = prefs.getInt("station", -1);
        if (station == -1) {
            station = 0;
            prefs.putInt("station", station);

        }//end if

        cast = prefs.getInt("cast", -1);
        if (cast == -1) {
            cast = 0;
            prefs.putInt("cast", cast);

        }//end if

        niskin = prefs.getInt("niskin", -1);
        if (niskin == -1) {
            niskin = 1;
            prefs.putInt("niskin", niskin);

        }//end if        

        depth = prefs.getDouble("depth", -1);
        if (depth == -1) {
            depth = 0.0;
            prefs.putDouble("depth", depth);

        }//end if   

        lattitude = prefs.getDouble("lattitude", -1);
        if (lattitude == -1) {
            lattitude = 0.0;
            prefs.putDouble("lattitude", lattitude);

        }//end if 

        longitude = prefs.getDouble("longitude", -1);
        if (longitude == -1) {
            longitude = 0.0;
            prefs.putDouble("longitude", longitude);

        }//end if 

        bottle = prefs.getInt("bottle", -1);
        if (bottle == -1) {
            bottle = 1;
            prefs.putInt("bottle", bottle);

        }//end if  

        sampleDate = prefs.get("sampleDate", "");
        if (sampleDate.equals("")) {
            sampleDate = "01/01/1970";
            prefs.put("sampleDate", sampleDate);

        }//end if 

        runDate = prefs.get("runDate", "");
        if (runDate.equals("")) {
            runDate = "01/01/1970";
            prefs.put("runDate", runDate);

        }//end if   

        thioTemp = prefs.getDouble("thioTemp", -1);
        if (thioTemp == -1) {
            thioTemp = 20.5;
            prefs.putDouble("thioTemp", thioTemp);

        }//end if         

        volKIO3 = prefs.getDouble("volKIO3", -1);
        if (volKIO3 == -1) {
            volKIO3 = 9.971;
            prefs.putDouble("volKIO3", volKIO3);

        }//end if    

        NKIO3 = prefs.getDouble("NKIO3", -1);
        if (NKIO3 == -1) {
            NKIO3 = .01;
            prefs.putDouble("NKIO3", NKIO3);

        }//end if 

        stduL = prefs.getDouble("stduL", -1);
        if (stduL == -1) {
            stduL = 702.0;
            prefs.putDouble("stduL", stduL);

        }//end if

        blkul = prefs.getDouble("blkul", -1);
        if (blkul == -1) {
            blkul = 0.5;
            prefs.putDouble("blkul", blkul);

        }//end if

        volReg = prefs.getDouble("volReg", -1);
        if (volReg == -1) {
            volReg = 2;
            prefs.putDouble("volReg", volReg);

        }//end if

        drawTemp = prefs.getDouble("drawTemp", -1);
        if (drawTemp == -1) {
            drawTemp = 6.5;
            prefs.putDouble("drawTemp", drawTemp);

        }//end if        

        salinity = prefs.getDouble("salinity", -1);
        if (salinity == -1) {
            salinity = 34.91;
            prefs.putDouble("salinity", salinity);

        }//end if

        EP = prefs.getDouble("EP", -1);
        if (EP == -1) {
            EP = 1001.20;
            prefs.putDouble("EP", EP);

        }//end if        
        
        
        ulPerst = prefs.getDouble("ulPerst", -1);
        if (ulPerst == -1) {
            ulPerst = .2514;
            prefs.putDouble("ulPerst", ulPerst);
        }

        ulOffset = prefs.getDouble("ulOffset", -1);
        if (ulOffset == -1) {
            ulOffset = -1.5;
            prefs.putDouble("ulOffset", ulOffset);
        }

        slope = prefs.getDouble("slope", -1);
        if (slope == -1) {
            slope = 4.0;
            prefs.putDouble("slope", slope);
        }

        speed = prefs.getDouble("speed", -1);
        if (speed == -1) {
            speed = .80;
            prefs.putDouble("speed", speed);
        }

        wait = prefs.getInt("wait", -1);
        if (wait == -1) {
            wait = 800;
            prefs.putInt("wait", wait);
        }

        this.cruiseTextField.setText(cruise);
        this.dataFileTextField.setText(dataPath);
        this.stationSpinner.setValue(station);
        this.castSpinner.setValue(cast);
        this.niskinSpinner.setValue(niskin);
        this.depthTextField.setText(depth + "");
        this.lattitudeTextField.setText(lattitude + "");
        this.longitudeTextField.setText(longitude + "");
        //this.bottleSpinner.setValue(bottle);
        this.sampleDateTextField.setText(sampleDate);
        this.runDateTextField.setText(runDate);
        this.thioTempTextField.setText(thioTemp + "");
        this.volKIO3TextField.setText(volKIO3 + "");
        this.NKIO3TextField.setText(NKIO3 + "");
        this.stdulTextField.setText(stduL + "");
        this.blkulTextField.setText(blkul + "");
        this.volRegTextField.setText(volReg + "");
        this.salinityTextField.setText(salinity + "");
        this.EPTextField.setText(EP + "");
        this.drawTempTextField.setText(drawTemp + "");
        this.ulPerstTextField.setText(ulPerst + "");
        this.ulOffsetTextField.setText(ulOffset + "");
        this.slopeTextField.setText(slope + "");
        this.speedTextField.setText(speed + "");
        this.waitTextField.setText(wait + "");
        filesLoaded = true;
        calculateAndUpdate();

    }
   




double getRHO(double S,double T){
double rho=0.0;
rho=999.842594+0.06793952*T-0.00909529*T*T + 
        0.0001001685*T*T*T-0.000001120083*T*T*T*T +
        0.000000006536332*T*T*T*T*T +
        (0.824493-0.0040899*T + 
        0.000076438*T*T-0.00000082467*T*T*T + 
        0.0000000053875*T*T*T*T)*S + 
        (-0.00572466+0.00010227*T-0.0000016546*T*T)*Math.pow(S, 1.5) + 
        0.00048314*S*S ;  
    
    return rho;
}

double getSWDensity(double T, double S){
    
    return round(.001 * getRHO(S,T),4);

}//end method

    void setSWDensity() {
        
        SWDensity = getSWDensity(getDrawTemp(), getSalinty());
        swDensityTextField.setText(String.format("%1$.4f", SWDensity) + "");

    }//end method

    double getVIO3() {

        double vio3;
        volKIO3 = Double.parseDouble(this.volKIO3TextField.getText());
        vio3 = volKIO3 * (1 + .00000975 * (getThioTemp() - 20));
        return round(vio3,3);

    }//end method
    
    double getThioMolarity() {
        double thioMolarity = -1;
        double dV = getStdul() - getBlkul();
        
        if (dV != 0) {
            thioMolarity = (1000 * getVIO3() * getNKIO3() / (dV));
        }//end if

        return round(thioMolarity,5);
    }//end method

    double getDrawTempBottleVol(){
    
        return round(getBotVol() * ( 1 + .00000975 * ( getThioTemp() - 20 ) ),3);
    }//end method
    
    double getO2uM(){
    
        double o2um = -1;
        double dV = getDrawTempBottleVol() - getVolReg();
        if (dV !=0 ){ 
           o2um = ( 250 * ( ( getEPul() - getBlkul() ) *  getThioMolarity() ) - 76 ) / dV;
                }//end if
        return round(o2um,2);
    }//end method
    
    double getO2uMPerKg(){
    
    double swDen = -1;
    double o2uMperKg = -1;
    
    swDen = getSWDensity(getDrawTemp(), getSalinty());
    
    if ( swDen !=0 ){
        o2uMperKg = getO2uM() / swDen;
    }//end if
    return round(o2uMperKg,2);
    }//end method
    
    void setO2(){
        String s1;
        String s2;
        
        
        s1 = String.format("%1$.2f", getO2uM());
        s2 = String.format("%1$.2f", getO2uMPerKg());
        
       o2umTextField.setText(s1);
       o2_umolPerKgTextField.setText(s2);
        
    
    }//end method
    
    void setThioMolaritytL(){
    
        String s1;
        s1 = String.format("%1$.5f", getThioMolarity());
        M_thio_tl_TextField.setText(s1);
    
    }//end method
    
    
    void setBottleVol(double bv){
        String s1;
        s1 = String.format("%1$.3f", bv);
        botVolTextField.setText(s1);
        
    
    }//end method    
    
    void setDrawTempBottleVol(){
        String s1;
        s1 = String.format("%1$.3f", getDrawTempBottleVol());
        DrawTempBottleVolTextField.setText(s1);
        
    
    }//end method
    
    
    
    double round(double num , double place){
        double k = Math.pow(10.0, place );
        int x = (int)Math.round((num * k));
        return ((double)x) / k;
    
    }//end method
    
    
    void calculateAndUpdate() {
        
         setSWDensity();
         setO2();
         setThioMolaritytL();
         setDrawTempBottleVol();
         setDrawTempBottleVol();


    }//end method
}// end class