package aomloxygentitrator;


import java.awt.Color;
import javax.swing.UIManager;
import gnu.io.CommPortIdentifier;
import gnu.io.DriverManager;
import gnu.io.SerialPort;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.prefs.Preferences;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;


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
    private boolean resetAndSave = true;
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
    String lattitude;
    String longitude;
    String bottleFileName;
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
    float lineWidth = (float).1;
    Color transparent;

   
    int wait;

    HashMap<Integer, Double> bottleVolumeMap;


    // raw points x y plot
    XYChart rawPoints = new XYChartBuilder().width(480).height(310).title("Points").xAxisTitle("Titrant (uL)").yAxisTitle("Detector Current (uA)").build();
    JPanel rawPointsChartPanel;
    XYSeries rawPointSeries;

    //linear regression plots
    XYChart lRegression = new XYChartBuilder().width(480).height(310).title("Linear Regression").xAxisTitle("Titrant (uL)").yAxisTitle("Detector Current (uA)").build();
    JPanel lRegressionChartPanel;
    XYSeries lRegressionPointsSeries1;
    XYSeries lRegressionPointsSeries2;

    XYSeries lRegressionLineSeries1;
    XYSeries lRegressionLineSeries2;
    /**
     * 
     * Creates new form MainFrame
     */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        //main(new String[0]);
        initComponents();
//        URL iconURL = getClass().getResource("/home/pedro/Pictures/o2icon.png");
        ImageIcon icon = new ImageIcon(getClass().getResource("/aomloxygentitrator/resources/o2icon.png"));
        this.setIconImage(icon.getImage());




        //this.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/o2icon.png"));
        
        bottleVolumeMap = new HashMap<>();
        String sp = "/dev/ttyUSB0";
        String spTmp = "";
        prefs = Preferences.userNodeForPackage(getClass());
        if (OS.indexOf("win") >= 0) {
            sp = "COM1";
        }//end if
        serialPortName = prefs.get("serialPortName", sp);

        CommPortIdentifier pid;
        DriverManager.getInstance().loadDrivers();
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
        
        
       
      

        plot1JInternalFrame.setLayout(new GridLayout(0,1,0,0));
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

        setFieldsEnabled(false);

        transparent = new Color(1f, 0f, 0f, 0f);

        //plot1JInternalFrame.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //plot1JInternalFrame.setUI(null);

        //raw points plot
        rawPoints.getStyler().setChartTitleVisible(false);
        rawPoints.getStyler().setXAxisTitleVisible(false);
        rawPoints.getStyler().setMarkerSize(8);
        rawPoints.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
        rawPoints.getStyler().setLegendVisible(false);
        rawPointSeries = rawPoints.addSeries("points", new double[]{0}, new double[]{0});
        rawPointSeries.setMarkerColor(Color.BLUE);
        rawPointSeries.setMarker(SeriesMarkers.SQUARE);
        rawPointsChartPanel = new XChartPanel<XYChart>(rawPoints);
        plot1JInternalFrame.add(rawPointsChartPanel, new GridLayout(0,1,0,0));

        //linear regression plots
        lRegression.getStyler().setChartTitleVisible(false);
        lRegression.getStyler().setMarkerSize(8);
        lRegression.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
        lRegression.getStyler().setLegendVisible(false);
        
        //series 1
        lRegressionPointsSeries1 = lRegression.addSeries("Points 1", new double[]{0}, new double[]{0});
        lRegressionPointsSeries1.setMarkerColor(Color.GREEN);
        lRegressionPointsSeries1.setLineColor(transparent);
        lRegressionPointsSeries1.setLineWidth(lineWidth);
        lRegressionPointsSeries1.setMarker(SeriesMarkers.SQUARE);
        
        //series 2
        lRegressionPointsSeries2 = lRegression.addSeries("Points 2", new double[]{0}, new double[]{0});
        lRegressionPointsSeries2.setMarkerColor(Color.RED);
        lRegressionPointsSeries2.setLineColor(transparent);
        lRegressionPointsSeries2.setLineWidth(lineWidth);
        lRegressionPointsSeries2.setMarker(SeriesMarkers.SQUARE);  
        
        
        
        //series 3
        lRegressionLineSeries1 = lRegression.addSeries("Line 1", new double[]{0}, new double[]{0});
        lRegressionLineSeries1.setMarkerColor(transparent);
        lRegressionLineSeries1.setLineColor(Color.GREEN);
        lRegressionLineSeries1.setMarker(SeriesMarkers.SQUARE);        
        
        //series 4
        lRegressionLineSeries2 = lRegression.addSeries("Line 2", new double[]{0}, new double[]{0});
        lRegressionLineSeries2.setMarkerColor(transparent);
        lRegressionLineSeries2.setLineColor(Color.RED);
        lRegressionLineSeries2.setMarker(SeriesMarkers.SQUARE);  
        
        
        
  
        
        
        
        
        
        lRegressionChartPanel = new XChartPanel<XYChart>(lRegression);
        //lRegressionChartPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        plot1JInternalFrame.add(lRegressionChartPanel, new GridLayout(0,1,0,0));      

        
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
        resetAndSaveButton = new javax.swing.JButton();
        fillButton = new javax.swing.JButton();
        blankButton = new javax.swing.JButton();
        sampleButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
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
        plot1JInternalFrame = new javax.swing.JInternalFrame();
        openDataFileButton = new javax.swing.JButton();
        dataFileTextField = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        btlVolFileTextField = new javax.swing.JTextField();
        openVolumeFileButton = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        configurationJPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ulPerstTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ulOffsetTextField = new javax.swing.JTextField();
        uLSetButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        speedTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        waitTextField = new javax.swing.JTextField();
        slopeSpeedWaitButton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        timeTextField = new javax.swing.JTextField();
        dateButton = new javax.swing.JButton();
        dateTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        slopeTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
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
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AOML Oxygen Titratior");
        setSize(new java.awt.Dimension(1366, 768));
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        runDateTextField.setEditable(false);
        runDateTextField.setText("12/20/19");
        runDateTextField.setToolTipText("The date the sample was processed.");

        blkulTextField.setToolTipText("The reagent blank in micro-liters");
        blkulTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blkulTextFieldActionPerformed(evt);
            }
        });

        thioTempTextField.setToolTipText("The temperature of the ThioSulfate in the lab at time of analysis in C");
        thioTempTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                thioTempTextFieldActionPerformed(evt);
            }
        });

        stdulTextField.setToolTipText("The titrated volume of the standards in micro-liters");
        stdulTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stdulTextFieldActionPerformed(evt);
            }
        });

        salinityTextField.setToolTipText("The salinity of the sample");
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

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel2.setText("Station");

        jLabel20.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel20.setText("Niskin");

        jLabel37.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel37.setText("VKIO3");

        drawTempTextField.setToolTipText("The draw temperature in C");
        drawTempTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawTempTextFieldActionPerformed(evt);
            }
        });

        volKIO3TextField.setToolTipText("The volume of the KIO3 at 20 C in mL");
        volKIO3TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volKIO3TextFieldActionPerformed(evt);
            }
        });

        castSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        castSpinner.setToolTipText("The cast number for the station. A station can have multiple casts.");

        rawOutputTextArea.setEditable(false);
        rawOutputTextArea.setColumns(20);
        rawOutputTextArea.setRows(5);
        rawOutputTextArea.setToolTipText("This text area displays the raw data received from the titrator");
        rawOutputTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        rawOutputScrollPane.setViewportView(rawOutputTextArea);

        jLabel31.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel31.setText("Run D");

        jLabel33.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel33.setText("Thio.tL");

        niskinSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        niskinSpinner.setToolTipText("The niskin bottle the sample was taken from.");

        jLabel16.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel16.setText("Cast");

        jLabel27.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel27.setText("Sample D");

        NKIO3TextField.setToolTipText("The normailty of the KIO3");
        NKIO3TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NKIO3TextFieldActionPerformed(evt);
            }
        });

        longitudeTextField.setToolTipText("The stations longitude");
        longitudeTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                longitudeTextFieldActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel1.setText("Cruise");

        jLabel25.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel25.setText("Lat");

        botVolTextField.setEditable(false);
        botVolTextField.setToolTipText("The volume of oxygen flask at 20C");

        volRegTextField.setToolTipText("The volume of reagents added to the sample in mL");
        volRegTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                volRegTextFieldActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel34.setText("NKIO3");

        depthTextField.setToolTipText("The sample depth. ");
        depthTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                depthTextFieldActionPerformed(evt);
            }
        });

        stationSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        stationSpinner.setToolTipText("The station number where the sample was taken.\nThis value is used in to create the filenames.");

        cruiseTextField.setToolTipText("The Cruise designation. The name in this field will be used to create the filenames.");
        cruiseTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cruiseTextFieldActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel29.setText("Bottle #");

        thioDensityTextField.setEditable(false);
        thioDensityTextField.setToolTipText("The density of the ThioSulfate");

        bottleSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        bottleSpinner.setToolTipText("The  bottle/flask number of the sample.");
        bottleSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bottleSpinnerStateChanged(evt);
            }
        });

        M_thio_tl_TextField.setEditable(false);
        M_thio_tl_TextField.setToolTipText("The molarity of the ThioSulfate at the time of analysis in moles per liter ");

        jLabel38.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel38.setText("STD");

        EPTextField.setEditable(false);
        EPTextField.setToolTipText("The calculated enpoint in micro-liters");

        jLabel18.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel18.setText("Depth");

        lattitudeTextField.setToolTipText("The stations lattitude.");

        M_thio_20C_TextField.setEditable(false);
        M_thio_20C_TextField.setToolTipText("The molarity of the ThioSulfate at 20 C in moles per liter ");

        sampleDateTextField.setToolTipText("The date the sample was taken.");

        swDensityTextField.setEditable(false);
        swDensityTextField.setToolTipText("The density of the sea water");

        o2umTextField.setEditable(false);
        o2umTextField.setToolTipText("The oxygen concentration in volumetric units");

        o2_umolPerKgTextField.setEditable(false);
        o2_umolPerKgTextField.setToolTipText("The oxygen concentration per unit mass of seawater");
        o2_umolPerKgTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o2_umolPerKgTextFieldActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel24.setText("Lon");

        jLabel15.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel15.setText("Blk, uL");

        jLabel17.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel17.setText("VReg");

        jLabel21.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel21.setText("Draw T");

        jLabel19.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel19.setText("Sal");

        jLabel22.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel22.setForeground(java.awt.Color.blue);
        jLabel22.setText("*EP*");

        jLabel23.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel23.setText("SW rho");

        jLabel26.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel26.setText("Thio rho");

        jLabel32.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel32.setText("M.Thio.20C");

        jLabel30.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel30.setText("VBot");

        jLabel36.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel36.setText("O2.uM.kg");

        jLabel35.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel35.setForeground(java.awt.Color.blue);
        jLabel35.setText("*O2.uM.l*");

        jLabel42.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel42.setText("M.Thio.tL");

        jLabel48.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel48.setText("VBot.ts");

        DrawTempBottleVolTextField.setEditable(false);
        DrawTempBottleVolTextField.setToolTipText("The volume of oxygen flask at draw temperature");

        resetAndSaveButton.setBackground(java.awt.Color.yellow);
        resetAndSaveButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        resetAndSaveButton.setText("RESET & SAVE");
        resetAndSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetAndSaveButtonActionPerformed(evt);
            }
        });

        fillButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        fillButton.setText("FILL");
        fillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillButtonActionPerformed(evt);
            }
        });

        blankButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        blankButton.setText("BLANK");
        blankButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blankButtonActionPerformed(evt);
            }
        });

        sampleButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        sampleButton.setText("SAMPLE");
        sampleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sampleButtonActionPerformed(evt);
            }
        });

        saveButton.setBackground(new java.awt.Color(44, 140, 220));
        saveButton.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        saveButton.setText("SAVE");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel41.setForeground(java.awt.Color.green);
        jLabel41.setText("B1");

        b1TextField.setEditable(false);
        b1TextField.setText("                         ");
        b1TextField.setToolTipText("The y intercept for the linear regression where the current is \nless than 5 uA  and U = 1 ");
        b1TextField.setMaximumSize(new java.awt.Dimension(85, 28));
        b1TextField.setMinimumSize(new java.awt.Dimension(85, 28));
        b1TextField.setPreferredSize(new java.awt.Dimension(79, 28));
        b1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1TextFieldActionPerformed(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel43.setForeground(java.awt.Color.green);
        jLabel43.setText("M1");

        m1TextField.setEditable(false);
        m1TextField.setText("                       ");
        m1TextField.setToolTipText("the slope for the linear regression where the current is less than 5 uA \nand U = 1 ");
        m1TextField.setMaximumSize(new java.awt.Dimension(79, 28));
        m1TextField.setMinimumSize(new java.awt.Dimension(79, 28));
        m1TextField.setName(""); // NOI18N
        m1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m1TextFieldActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel44.setForeground(java.awt.Color.green);
        jLabel44.setText("MSE1");

        mse1TextField.setEditable(false);
        mse1TextField.setText("                         ");
        mse1TextField.setToolTipText("The Mean Squared Error for the linear regression where the current is less than 5 uA \nand U = 1 ");
        mse1TextField.setMaximumSize(new java.awt.Dimension(85, 28));
        mse1TextField.setMinimumSize(new java.awt.Dimension(85, 28));
        mse1TextField.setName(""); // NOI18N
        mse1TextField.setPreferredSize(new java.awt.Dimension(79, 28));
        mse1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mse1TextFieldActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel45.setForeground(java.awt.Color.red);
        jLabel45.setText("B2");

        b2TextField.setEditable(false);
        b2TextField.setText("                         ");
        b2TextField.setToolTipText("The y intercept for the linear regression where the current is \nless than 5 uA  and U >= 2");
        b2TextField.setMaximumSize(new java.awt.Dimension(85, 28));
        b2TextField.setMinimumSize(new java.awt.Dimension(85, 28));
        b2TextField.setPreferredSize(new java.awt.Dimension(79, 28));

        jLabel46.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel46.setForeground(java.awt.Color.red);
        jLabel46.setText("M2");

        m2TextField.setEditable(false);
        m2TextField.setText("                       ");
        m2TextField.setToolTipText("the slope for the linear regression where the current is less than 5 uA \nand U >= 2 ");
        m2TextField.setMaximumSize(new java.awt.Dimension(85, 28));
        m2TextField.setMinimumSize(new java.awt.Dimension(85, 28));
        m2TextField.setName(""); // NOI18N

        jLabel47.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        jLabel47.setForeground(java.awt.Color.red);
        jLabel47.setText("MSE2");

        mse2TextField.setEditable(false);
        mse2TextField.setText("                         ");
        mse2TextField.setToolTipText("The Mean Squared Error for the linear regression where the current is less than 5 uA \nand U >=2 ");
        mse2TextField.setMaximumSize(new java.awt.Dimension(85, 28));
        mse2TextField.setMinimumSize(new java.awt.Dimension(85, 28));
        mse2TextField.setPreferredSize(new java.awt.Dimension(79, 28));
        mse2TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mse2TextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel31)
                        .addComponent(jLabel27))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel24)
                        .addComponent(jLabel29)
                        .addComponent(jLabel25)
                        .addComponent(jLabel18))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel33)
                            .addComponent(jLabel34)
                            .addComponent(jLabel38))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel20)
                            .addComponent(jLabel2))
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stationSpinner)
                            .addComponent(castSpinner)
                            .addComponent(niskinSpinner)
                            .addComponent(depthTextField)
                            .addComponent(lattitudeTextField)
                            .addComponent(longitudeTextField)
                            .addComponent(bottleSpinner)
                            .addComponent(sampleDateTextField)
                            .addComponent(runDateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .addComponent(thioTempTextField)
                            .addComponent(volKIO3TextField)
                            .addComponent(NKIO3TextField)
                            .addComponent(stdulTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(blkulTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(volRegTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(drawTempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(salinityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EPTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(swDensityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(thioDensityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_tl_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(M_thio_20C_TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botVolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DrawTempBottleVolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(o2umTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(o2_umolPerKgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cruiseTextField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rawOutputScrollPane)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(resetAndSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fillButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blankButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(sampleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mse1TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b2TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(m2TextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mse2TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)))
                .addGap(28, 28, 28))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {NKIO3TextField, bottleSpinner, castSpinner, depthTextField, lattitudeTextField, longitudeTextField, niskinSpinner, runDateTextField, sampleDateTextField, stationSpinner, stdulTextField, thioTempTextField, volKIO3TextField});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {DrawTempBottleVolTextField, EPTextField, M_thio_20C_TextField, M_thio_tl_TextField, blkulTextField, botVolTextField, drawTempTextField, o2_umolPerKgTextField, o2umTextField, salinityTextField, swDensityTextField, thioDensityTextField, volRegTextField});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(swDensityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(longitudeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
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
                            .addComponent(jLabel32)))
                    .addComponent(rawOutputScrollPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(thioTempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botVolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel37)
                                .addComponent(volKIO3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(DrawTempBottleVolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel48)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(NKIO3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(o2umTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel35)))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(stdulTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel38))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(o2_umolPerKgTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel36))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(b1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43)
                            .addComponent(m1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44)
                            .addComponent(mse1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(b2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(m2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(mse2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetAndSaveButton)
                            .addComponent(fillButton)
                            .addComponent(blankButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sampleButton)
                            .addComponent(saveButton))))
                .addGap(75, 75, 75))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {DrawTempBottleVolTextField, EPTextField, M_thio_20C_TextField, M_thio_tl_TextField, NKIO3TextField, blkulTextField, botVolTextField, bottleSpinner, castSpinner, cruiseTextField, depthTextField, drawTempTextField, lattitudeTextField, longitudeTextField, niskinSpinner, o2_umolPerKgTextField, o2umTextField, runDateTextField, salinityTextField, sampleDateTextField, stationSpinner, stdulTextField, swDensityTextField, thioDensityTextField, thioTempTextField, volKIO3TextField, volRegTextField});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {b1TextField, b2TextField, m1TextField, m2TextField, mse1TextField, mse2TextField});

        plot1JInternalFrame.setResizable(true);
        plot1JInternalFrame.setFocusable(false);
        plot1JInternalFrame.setFrameIcon(null);
        plot1JInternalFrame.setVisible(true);
        plot1JInternalFrame.getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        openDataFileButton.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        openDataFileButton.setText("OPEN");
        openDataFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDataFileButtonActionPerformed(evt);
            }
        });

        dataFileTextField.setEditable(false);
        dataFileTextField.setToolTipText("The directory where the data files will be saved.");

        jLabel39.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel39.setText("Data File");

        btlVolFileTextField.setEditable(false);
        btlVolFileTextField.setToolTipText("The file that holds the measured volumes for each flask.");

        openVolumeFileButton.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        openVolumeFileButton.setText("OPEN");
        openVolumeFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openVolumeFileButtonActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel28.setText("Volume File");

        javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(mainJPanel);
        mainJPanel.setLayout(mainJPanelLayout);
        mainJPanelLayout.setHorizontalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainJPanelLayout.createSequentialGroup()
                .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainJPanelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btlVolFileTextField)
                            .addComponent(dataFileTextField))
                        .addGap(18, 18, 18)
                        .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openVolumeFileButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(openDataFileButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(mainJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(plot1JInternalFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainJPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {openDataFileButton, openVolumeFileButton});

        mainJPanelLayout.setVerticalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainJPanelLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openVolumeFileButton)
                            .addComponent(btlVolFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(openDataFileButton)
                            .addComponent(dataFileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39)))
                    .addComponent(plot1JInternalFrame))
                .addContainerGap())
        );

        mainJPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {openDataFileButton, openVolumeFileButton});

        mainJPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btlVolFileTextField, dataFileTextField});

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

        dateTextField.setEditable(false);

        jLabel9.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel9.setText("Date");

        jLabel8.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel8.setText("Slope");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(slopeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(waitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(slopeSpeedWaitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(speedTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(ulPerstTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ulOffsetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(uLSetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(dateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateButton, slopeSpeedWaitButton, uLSetButton});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateTextField, slopeTextField, speedTextField, timeTextField, ulOffsetTextField, ulPerstTextField, waitTextField});

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
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(slopeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(waitTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(slopeSpeedWaitButton))))
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(speedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(dateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(dateButton))
                .addGap(17, 17, 17))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dateTextField, slopeTextField, speedTextField, timeTextField, ulOffsetTextField, ulPerstTextField, waitTextField});

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Configure Serial Port", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jLabel10.setText("Serial Port");

        serialPortComboBox.setToolTipText("");
        serialPortComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serialPortComboBoxMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                serialPortComboBoxMousePressed(evt);
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
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(flowControlComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(baudRateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(serialPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dataBitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(parityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(stopBitsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(serialDisconnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(serialConnectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {baudRateComboBox, dataBitsComboBox, flowControlComboBox, parityComboBox, serialPortComboBox, stopBitsComboBox});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(flowControlComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serialConnectButton)
                    .addComponent(serialDisconnectButton))
                .addContainerGap())
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
                .addGroup(configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                .addContainerGap())
        );
        configurationJPanelLayout.setVerticalGroup(
            configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(configurationJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(configurationJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(configurationJPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(configurationJPanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(64, 64, 64))))
        );

        jTabbedPane1.addTab("Configuration", configurationJPanel);

        jMenu1.setText("File");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Sample");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator7);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setText("Fill");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator6);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setText("Blank");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator1);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setText("Reset & Save");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);
        jMenu1.add(jSeparator3);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setText("Save");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);
        jMenu1.add(jSeparator5);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem7.setText("Open data files location");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);
        jMenu1.add(jSeparator8);

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setText("Open volume file location");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);
        jMenu1.add(jSeparator9);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("Exit");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jCheckBoxMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("Toggle Hide/Show");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Spinners");

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_CLOSE_BRACKET, 0));
        jMenuItem9.setText("Increment Flask");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_OPEN_BRACKET, 0));
        jMenuItem10.setText("Decrement Flask");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, 0));
        jMenuItem11.setText("Increment Niskin");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, 0));
        jMenuItem12.setText("Decrement Niskin");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        jMenuItem13.setText("Increment Station");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        jMenuItem14.setText("Decrement Station");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void serialConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialConnectButtonActionPerformed

        if (serialPortReader == null) {
            setFieldsEnabled(true);
            serialConnectButton.setEnabled(false);
            serialDisconnectButton.setEnabled(true);
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
            setFieldsEnabled(false);
            serialConnectButton.setEnabled(true);
            serialDisconnectButton.setEnabled(false);

        }//end if
    }//GEN-LAST:event_serialDisconnectButtonActionPerformed

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
                os = new PrintStream(port.getOutputStream());
                os.print("P " + getUlPerStep() + " " + getUlOffset());
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
                os = new PrintStream(port.getOutputStream());
                os.print("C " + getSlope() + " " + getSpeed() + " " + getWait());

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

    private void ulOffsetTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulOffsetTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ulOffsetTextFieldActionPerformed

    private void ulPerstTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ulPerstTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ulPerstTextFieldActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        closeApplication();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        sample();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed

        fill();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        blank();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        resetAndSave();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        save();
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Desktop desktop;
        try {
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                desktop.open(new File(getDataPath()));
            }
        } catch (Exception e) {

        }
   
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Desktop desktop;
        String fName = getBottleFileName();
        File file = new File(fName);
        File parent = new File(file.getParent());
        
        try {
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                desktop.open(parent);
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        bottleSpinner.setValue((int)bottleSpinner.getValue() +1);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        int btl = (int) bottleSpinner.getValue();
        if (btl > 1) {
            bottleSpinner.setValue((int) bottleSpinner.getValue() - 1);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        niskinSpinner.setValue((int)niskinSpinner.getValue() +1);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        int nsk = (int) niskinSpinner.getValue();
        if (nsk > 1) {
            niskinSpinner.setValue((int) niskinSpinner.getValue() - 1);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        stationSpinner.setValue((int)stationSpinner.getValue() +1);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        int sta = (int) stationSpinner.getValue();
        if (sta > 0) {
            stationSpinner.setValue((int) stationSpinner.getValue() - 1);
        }        
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed

        
        showComponents(!rawOutputScrollPane.isVisible());
        
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void mse2TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mse2TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mse2TextFieldActionPerformed

    private void mse1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mse1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mse1TextFieldActionPerformed

    private void m1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_m1TextFieldActionPerformed

    private void b1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b1TextFieldActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        calculateAndUpdate();
        save();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void sampleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sampleButtonActionPerformed

        sample();
    }//GEN-LAST:event_sampleButtonActionPerformed

    private void blankButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blankButtonActionPerformed

        blank();
    }//GEN-LAST:event_blankButtonActionPerformed

    private void fillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillButtonActionPerformed

        fill();
    }//GEN-LAST:event_fillButtonActionPerformed

    private void resetAndSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetAndSaveButtonActionPerformed

        calculateAndUpdate();
        resetAndSave();
    }//GEN-LAST:event_resetAndSaveButtonActionPerformed

    private void o2_umolPerKgTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o2_umolPerKgTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_o2_umolPerKgTextFieldActionPerformed

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

    private void cruiseTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cruiseTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cruiseTextFieldActionPerformed

    private void depthTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_depthTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_depthTextFieldActionPerformed

    private void volRegTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volRegTextFieldActionPerformed
        // TODO add your handling code here:
        calculateAndUpdate();
    }//GEN-LAST:event_volRegTextFieldActionPerformed

    private void longitudeTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_longitudeTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_longitudeTextFieldActionPerformed

    private void NKIO3TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NKIO3TextFieldActionPerformed
        // TODO add your handling code here:
        calculateAndUpdate();
    }//GEN-LAST:event_NKIO3TextFieldActionPerformed

    private void volKIO3TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_volKIO3TextFieldActionPerformed
        // TODO add your handling code here:
        calculateAndUpdate();
    }//GEN-LAST:event_volKIO3TextFieldActionPerformed

    private void drawTempTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawTempTextFieldActionPerformed
        calculateAndUpdate();
    }//GEN-LAST:event_drawTempTextFieldActionPerformed

    private void salinityTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_salinityTextFieldPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_salinityTextFieldPropertyChange

    private void salinityTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salinityTextFieldActionPerformed
        calculateAndUpdate();
    }//GEN-LAST:event_salinityTextFieldActionPerformed

    private void stdulTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stdulTextFieldActionPerformed
        // TODO add your handling code here:
        calculateAndUpdate();
    }//GEN-LAST:event_stdulTextFieldActionPerformed

    private void thioTempTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_thioTempTextFieldActionPerformed
        // TODO add your handling code here:
        calculateAndUpdate();
    }//GEN-LAST:event_thioTempTextFieldActionPerformed

    private void blkulTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blkulTextFieldActionPerformed
        // TODO add your handling code here
        calculateAndUpdate();
    }//GEN-LAST:event_blkulTextFieldActionPerformed

    private void openDataFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDataFileButtonActionPerformed

        File file = null;
        String fName = this.getDataPath();
        if (fName != null) {
            file = new File(fName);
        }

        JFileChooser saveFolder = new JFileChooser(file);
        saveFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saveFolder.setPreferredSize(new Dimension(650, 600));
        int returnVal = saveFolder.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String dir = saveFolder.getSelectedFile().getAbsolutePath();
            prefs.put("dataPath", dir);
            this.dataFileTextField.setText(dir);

        }//end if
    }//GEN-LAST:event_openDataFileButtonActionPerformed

    private void openVolumeFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openVolumeFileButtonActionPerformed

        File parent = null;
        String fName = getBottleFileName();
        if (fName != null) {
            File file = new File(fName);
            parent = new File(file.getParent());
        }

        JFileChooser saveFolder = new JFileChooser(parent);
        saveFolder.setPreferredSize(new Dimension(650, 600));
        int returnVal = saveFolder.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String btlVolFileName = saveFolder.getSelectedFile().getAbsolutePath();
            prefs.put("btlVol", btlVolFileName);
            this.btlVolFileTextField.setText(btlVolFileName);

            this.loadBottleFile(btlVolFileName);

        }//end if

        // TODO add your handling code here:
    }//GEN-LAST:event_openVolumeFileButtonActionPerformed

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
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
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
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
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
    private javax.swing.JInternalFrame plot1JInternalFrame;
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
        runDateTextField.setText(d);
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
        return s;
    }

    String getLattitude() {
        return lattitudeTextField.getText();
    }//end method

    String getLongitude() {
        return longitudeTextField.getText();

    }//end method    

    String getSampleDate() {
        return sampleDateTextField.getText();
    }//end method     

    String getRunDate() {
        return runDateTextField.getText();

    }//end method   

    double getThioTemp() {
        String s = thioTempTextField.getText();
        double x = string2Double(s);
        thioTemp = x;
        return thioTemp ;
    }//end method 
    
    

    String getVolKIO3() {
        String s = volKIO3TextField.getText();
        return s;
    }//end method   

    double getNKIO3() {
        String s = NKIO3TextField.getText();
        NKIO3 = string2Double(s);
        return NKIO3;
    }//end method  

    double getStdul() {
        String s = stdulTextField.getText();
        stduL = string2Double(s);
        return stduL;
    }//end method    

    double getBlkul() {
        String s = blkulTextField.getText();
        blkul = string2Double(s);
        return blkul;
    }//end method

    double getVolReg() {
        String s = volRegTextField.getText();
        double volReg = string2Double(s);
        return volReg;
    }//end method      

    double getDrawTemp() {
        String s = drawTempTextField.getText();
        drawTemp = string2Double(s);
        return drawTemp;
    }//end method      

    double getSalinty() {
        String s = salinityTextField.getText();
        salinity = string2Double(s);
        return salinity;
    }//end method    

    double getEPul() {
        String s = EPTextField.getText();
        EP = string2Double(s);
        return EP;
    }//end method  

    String getSwDensity() {
        String s = swDensityTextField.getText();
        return s;
    }//end method    

    String getThioDensity() {
        String s = thioDensityTextField.getText();
        if (s.isEmpty()) {
            s = "0.0";
        }
        return s;
    }//end method      

    String getM_thio_tl() {
        String s = M_thio_tl_TextField.getText();
        if (s.isEmpty()) {
            s = "0.0";
        }
        return s;
    }//end method    

    String getM_thio_20C() {
        String s = M_thio_20C_TextField.getText();
        if (s.isEmpty()) {
            s = "0.0";
        }
        double x = string2Double(s);
        return x + "";
    }//end method 

    double getBotVol() {
        String s = botVolTextField.getText();
        botVolume = string2Double(s);
        return botVolume;
    }//end method       

    String getO2um() {
        String s = o2umTextField.getText();
        double x = string2Double(s);
        return x + "";
    }//end method  

    String geto2_umolPerKg() {
        String s = o2_umolPerKgTextField.getText();
        double x = string2Double(s);
        return x + "";
    }//end method     

    String getB1() {
        String s = b1TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0";
        }
        double x = string2Double(s);
        return x + "";
    }//end method 

    String getB2() {
        String s = b2TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0";
        }
        double x = string2Double(s);
        return x + "";
    }//end method  

    String getM1() {
        String s = m1TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0";
        }
        double x = string2Double(s);
        return x + "";
    }//end method     

    String getM2() {
        String s = m2TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0";
        }
        double x = string2Double(s);
        return x + "";
    }//end method 

    String getMse1() {
        String s = mse1TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0000000";
        }
        s = String.format("%1$.6f", round(string2Double(s), 6));
        //double x = string2Double(s);
        return s;
    }//end method 

    String getMse2() {
        String s = mse2TextField.getText().trim();
        if (s.isEmpty()) {
            s = "0.0000000";
        }
        s = String.format("%1$.6f", round(string2Double(s), 6));
        //double x = string2Double(s);
        return s;
    }//end method  


    String getUlPerStep() {
        String s1 = "";
        double num1 = string2Double(ulPerstTextField.getText().trim());
        s1 = String.format("%1$.4f", num1);
        return s1;
    }//end method  
    
    
    String getUlOffset() {
        String s2 = "";
        double num2 = string2Double(ulOffsetTextField.getText().trim());
        s2 = String.format("%1$.1f", num2);
        return s2;
    }//end method    
    
    
    String getSlope() {
        String s1 = "";
        double num1 = string2Double(slopeTextField.getText().trim());
        s1 = String.format("%1$.1f", num1);
        return s1;
    }//end method
    
    String getSpeed() {
        String s2 = "";
        double num2 = string2Double(speedTextField.getText().trim());
        s2 = String.format("%1$.1f", num2);
        return s2;
    }//end method
    
    String getWait() {
        String s3 = "";
        int num3 = string2Int(waitTextField.getText().trim().trim());
        s3 = String.format("%1$04d", num3);
        return s3;
    }//end method
    
    void setEP(double ep) {
        EP = ep;
        String s2;
        s2 = String.format("%1$.2f", EP);
        EPTextField.setText(s2);
    }//end method

    void loadBottleFile(String file) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = "";
            String bottleNumber="0";
            String bottleVolume="";
            String[] row;

            rawOutputTextArea.append("Loading Bottle Volumes\n");
            rawOutputTextAreaConfig.append("Loading Bottle Volumes\n");
            while (line != null) {
                line = br.readLine();

                if (line != null && !line.toLowerCase().contains("flask") && !line.toLowerCase().contains("bottle")) {

                    row = line.split("\t");

                    if (row.length == 2 && line.matches("[0-9]+(\\t| )+[\\.0-9]+(.)[0-9]")) {

                        bottleNumber = row[0];
                        bottleVolume = row[1];

                        bottle = Integer.parseInt(bottleNumber);
                        botVolume = string2Double(bottleVolume);

                        bottleVolumeMap.put(bottle, botVolume);

                        rawOutputTextArea.append(line + "\n");
                        rawOutputTextAreaConfig.append(line + "\n");

                    }//end if

                }//end if
            }

            this.bottleSpinner.setValue(bottle);

            setBottleFileName(file);

            br.close();
            //String everything = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end method

    void saveFields() {
        if (port != null) {
            prefs.put("cruise", cruiseTextField.getText());
            prefs.putInt("station", Integer.parseInt(stationSpinner.getValue().toString()));
            prefs.putInt("cast", Integer.parseInt(castSpinner.getValue().toString()));
            prefs.putInt("niskin", Integer.parseInt(niskinSpinner.getValue().toString()));
            prefs.putDouble("depth", string2Double(depthTextField.getText()));
            prefs.put("lattitude", getLattitude());
            prefs.put("longitude", getLongitude());
            prefs.putInt("bottle", Integer.parseInt(bottleSpinner.getValue().toString()));
            prefs.put("sampleDate", getSampleDate());
            prefs.put("runDate", getRunDate());
            prefs.putDouble("thioTemp", string2Double(thioTempTextField.getText()));
            prefs.putDouble("volKIO3", string2Double(volKIO3TextField.getText()));
            prefs.putDouble("NKIO3", string2Double(NKIO3TextField.getText()));
            prefs.putDouble("stduL", string2Double(stdulTextField.getText()));
            prefs.putDouble("blkul", string2Double(blkulTextField.getText()));
            prefs.putDouble("volReg", string2Double(volRegTextField.getText()));
            prefs.putDouble("drawTemp", string2Double(drawTempTextField.getText()));
            prefs.putDouble("salinity", string2Double(salinityTextField.getText()));
            prefs.putDouble("EP", string2Double(EPTextField.getText()));
            prefs.putDouble("ulPerst", string2Double(ulPerstTextField.getText()));
            prefs.putDouble("ulOffset", string2Double(ulOffsetTextField.getText()));
            prefs.putDouble("slope", string2Double(slopeTextField.getText()));
            prefs.putDouble("speed", string2Double(speedTextField.getText()));
            prefs.putInt("wait", string2Int(waitTextField.getText()));
        }//end of

    }

    void loadFields() {
        String defaultPWD = System.getProperty("user.home") + File.separatorChar + "data" + File.separatorChar + "oxygen";

        if (OS.indexOf("win") >= 0) {
            defaultPWD = System.getProperty("user.home") + File.separatorChar + "Documents" + File.separatorChar + "data" + File.separatorChar + "oxygen";
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

        lattitude = prefs.get("lattitude", "lat");
        prefs.put("lattitude", lattitude);

        longitude = prefs.get("longitude", "lon");

        prefs.put("longitude", longitude);

        bottle = prefs.getInt("bottle", -1);
        if (bottle == -1) {
            bottle = 1;
            prefs.putInt("bottle", bottle);

        }//end if  

        sampleDate = prefs.get("sampleDate", "");
        if (sampleDate.equals("")) {
            sampleDate = "01/01/70";
            prefs.put("sampleDate", sampleDate);

        }//end if 

//        runDate = prefs.get("runDate", "");
//        if (runDate.equals("")) {
//            runDate = "01/01/1970";
//            prefs.put("runDate", runDate);
//
//        }//end if   

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
        this.bottleSpinner.setValue(bottle);
        this.sampleDateTextField.setText(sampleDate);
        //this.runDateTextField.setText(runDate);
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

double computeSWDensity(double T, double S){
    
    return round(.001 * getRHO(S,T),4);

}//end method

    double computeThioDensity(double temp) {
        double thioDensity;
        thioDensity = 0.999842594 + 0.00006793952 * temp
                - 0.00000909529 * temp * temp
                + 0.0000001001685 * temp * temp * temp
                - 0.000000001120083 * temp * temp * temp * temp
                + 0.000000000006536332 * temp * temp * temp * temp * temp;
        return thioDensity;
    }//end method



    void setSWDensity() {
        
        SWDensity = computeSWDensity(getDrawTemp(), getSalinty());
        swDensityTextField.setText(String.format("%1$.4f", SWDensity));

    }//end method
    
    void setThioDensity(){
    thioDensity = computeThioDensity(getThioTemp());
    this.thioDensityTextField.setText(String.format("%1$.6f", thioDensity));
    }

    double computeVIO3(double temp) {

        double vio3;
        volKIO3 = string2Double(this.volKIO3TextField.getText());
        vio3 = volKIO3 * ( 1.0 + (.00000975 * ( temp - 20) ) );
        return round(vio3,6);

    }//end method
    
    double computeThioMolarity(double temp) {
        double thioMolarity = -1;
        double dV = getStdul() - getBlkul();
        
        if (dV != 0) {
            thioMolarity =  ( 1000 * computeVIO3(temp) * getNKIO3() ) / (dV) ;
        }//end if

        return round(thioMolarity,6);
    }//end method

    double getDrawTempBottleVol(){
    
        return round(getBotVol() * ( 1 + .00000975 * ( getThioTemp() - 20 ) ),3);
    }//end method
    
    double getO2uM(){
    
        double o2um = -1;
        double dV = getDrawTempBottleVol() - getVolReg();
        if (dV !=0 ){ 
           o2um = ( 250 * ( ( getEPul() - getBlkul() ) *  computeThioMolarity(getThioTemp()) ) - 76 ) / dV;
                }//end if
        return round(o2um,3);
    }//end method
    
    double getO2uMPerKg(){
    
    double swDen = -1;
    double o2uMperKg = -1;
    
    swDen = computeSWDensity(getDrawTemp(), getSalinty());
    
    if ( swDen !=0 ){
        o2uMperKg = getO2uM() / swDen;
    }//end if
    return round(o2uMperKg,3);
    }//end method
    
    void setO2(){
        String s1;
        String s2;
        
        
        s1 = String.format("%1$.3f", getO2uM());
        s2 = String.format("%1$.3f", getO2uMPerKg());
        
       o2umTextField.setText(s1);
       o2_umolPerKgTextField.setText(s2);
        
    
    }//end method
    
    void setThioMolaritytL(){
    
        String s1;
        s1 = String.format("%1$.6f", computeThioMolarity(getThioTemp()));
        M_thio_tl_TextField.setText(s1);
    
    }//end method
    
    
    void setThioMolaritytL20c(){
    
        String s1;
        s1 = String.format("%1$.6f", computeThioMolarity(20));
        M_thio_20C_TextField.setText(s1);
     
    
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

    void setresetAndSave(boolean r) {

        resetAndSave = r;
    }//end method

    void saveDataFiles() {
        String cruise,
                sta,
                cast,
                nsk,
                depth,
                lat,
                lon,
                sampleDate,
                runDate,
                thioDensity,
                flsk,
                drawT,
                sal,
                ep,
                volKIO3,
                nKIO3,
                stduL,
                volReg,
                btlVol,
                swDensity,
                uLpers,
                uLOffset,
                slope,
                speed,
                wait,
                b1,
                m1,
                mse1,
                b2,
                m2,
                mse2,
                o2uM,
                o2_umolpkg,
                thio_m,
                thio_m20c,
                blk,
                thio_t,
                ABRline,
                DATline,
                ABRFileName,
                DATFileName;

        ABRFileName = getCruise() + "_" + getStationNumber() + ".dat_ABR";
        ABRFileName = prefs.get("dataPath", "") + File.separator + ABRFileName;

        File fN = new File(ABRFileName);

        //create header
        if (!fN.exists()) {
            logText(" Sta\tCast\tNsk\tFlsk#\tDraw_T\tSal\tEp\t02_uM\t02_umol/kg\tThio_M\tBlank\tThio_T\n", ABRFileName);
        }//end if  

        DATFileName = getCruise() + "_" + getStationNumber() + ".dat";
        DATFileName = prefs.get("dataPath", "") + File.separator + DATFileName;

        File datfN = new File(DATFileName);

        //create header
        if (!datfN.exists()) {
            //logText(" Sta\tCast\tNsk\tFlsk#\tDraw_T\tSal\tEp\t02_uM\t02_umol/kg\tThio_M\tBlank\tThio_T\n", DATFileName);
        }//end if      

        //ABR file
        sta = getStationNumber();
        cast = getCastNumber();
        nsk = getNiskinNumber();
        flsk = getBottleNumber();
        drawT = String.format("%06.3f", getDrawTemp());
        sal = String.format("%06.3f", getSalinty());
        ep = String.format("%09.3f", getEPul());
        o2uM = String.format("%07.3f", getO2uM());
        o2_umolpkg = String.format("%07.3f", getO2uMPerKg());
        thio_m = this.getM_thio_tl();
        blk = String.format("%06.3f", getBlkul());
        thio_t = String.format("%05.2f", getThioTemp());

        //DAT file
        cruise = getCruise();
        depth = getDepth();
        lat = getLattitude();
        lon = getLongitude();
        sampleDate = getSampleDate();
        runDate = getRunDate();
        thioDensity = getThioDensity();
        thio_m20c = getM_thio_20C();
        volKIO3 = getVolKIO3();
        nKIO3 = getNKIO3() + "";
        stduL = getStdul() + "";
        volReg = getVolReg() + "";
        swDensity = getSwDensity();
        uLpers = getUlPerStep();
        uLOffset = getUlOffset();
        slope = getSlope();
        speed = getSpeed();
        wait = getWait();
        b1 = getB1();
        m1 = getM1();
        mse1 = getMse1();
        b2 = getB2();
        m2 = getM2();
        mse2 = getMse2();
        btlVol = botVolTextField.getText();
        

        ABRline = " " + sta + "\t"
                + cast + " \t"
                + nsk + "\t"
                + flsk + "\t"
                + drawT + "\t"
                + sal + "\t"
                + ep + "\t"
                + o2uM + "\t"
                + o2_umolpkg + "\t"
                + thio_m + "\t"
                + blk + "\t"
                + thio_t + " \t";

        logText(ABRline + "\n", ABRFileName);

        DATline = cruise + "\t"
                + sta + "\t"
                + cast + "\t"
                + nsk + "\t"
                + depth + "\t"
                + lat + " \t"
                + lon + " \t"
                + flsk + "\t"
                + sampleDate + "\t"
                + runDate + "\t"
                + thio_t + "\t"
                + thioDensity + "\t"
                + thio_m + "\t"
                + thio_m20c + "\t"
                + ep + "\t"
                + volKIO3 + "\t"
                + nKIO3 + "\t"
                + stduL + "\t"
                + blk + "\t"
                + volReg + "\t"
                + btlVol + "\t"
                + o2uM + "\t"
                + drawT + "\t"
                + sal + "\t"
                + swDensity + "\t"
                + o2_umolpkg + "\t"
                + uLpers + "\t"
                + uLOffset + "\t"
                + slope + "\t"
                + speed + "\t"
                + wait + "\t"
                + b1 + "\t"
                + m1 + "\t"
                + mse1 + "\t"
                + b2 + "\t"
                + m2 + "\t"
                + mse2 + "\t";

         logText(DATline + "\n", DATFileName);
    }//end method

    
    /**
     * When called strings passed to it are appended to a file
     *
     * @param line
     * @param logFileName
     */
    public void logText(String line, String logFileName) {
        
        try {

            FileWriter logFile = new FileWriter(logFileName, true);
            logFile.append(line);
            logFile.close();
        } catch (Exception e) {

        }// end catch
    }// end logText    
    
    double round(double num , double place){
        double k = Math.pow(10.0, place );
        int x = (int)Math.round((num * k));
        return ((double)x) / k;
    
    }//end method
    
    
    void calculateAndUpdate() {
        
         setSWDensity();
         setO2();
         setThioMolaritytL();
         setThioMolaritytL20c();
         setDrawTempBottleVol();
         setThioDensity();


    }//end method
    
    
    void setPoints(HashMap<Integer,Points> points){

    

        //caclulate b2 m2 and mse2
        double[] x = new double[points.size()];
        double[] y = new double[points.size()];

        ArrayList<Double> x2 = new ArrayList<>();
        ArrayList<Double> y2 = new ArrayList<>();
        ArrayList<Double> x1 = new ArrayList<>();
        ArrayList<Double> y1 = new ArrayList<>();

        int U;

        Iterator pointsIterator = points.entrySet().iterator();

        int i = 0;

        while (pointsIterator.hasNext()) {
            Map.Entry mapping = (Map.Entry) pointsIterator.next();
            x[i] = ((Points) mapping.getValue()).getTitrant();
            y[i] = ((Points) mapping.getValue()).getCurrent();
            U = ((Points) mapping.getValue()).getU();

            //detect the first batch of points
            if (U == 1 && y[i] < 5.00) {
                x1.add(x[i]);
                y1.add(y[i]);

            }//end if

            //detect last points where U=2
            if (U >= 2) {
                x2.add(x[i]);
                y2.add(y[i]);
            }//end if

            i++;
        }//end while
        
        
        
        
        double[] titrant1 = new double[x1.size()];
        double[] current1 = new double[y1.size()];
        
        for (int j = 0; j < x1.size(); j++) {
            titrant1[j] = x1.get(j);
            current1[j] = y1.get(j);
        
        }//end for        
        

        double[] titrant2 = new double[x2.size()];
        double[] current2 = new double[y2.size()];
        
        for (int j = 0; j < x2.size(); j++) {
            titrant2[j] = x2.get(j);
            current2[j] = y2.get(j);
        
        }//end for
        
     
        LinearRegression l1 = new LinearRegression(titrant1, current1);
        b1TextField.setText(String.format("%1$.3f", round(l1.intercept(), 3)));
        m1TextField.setText(String.format("%1$.6f", round(l1.slope(), 6)));
        mse1TextField.setText(String.format("%1$.6f", round(l1.getMSE(), 6)));

        LinearRegression l2 = new LinearRegression(titrant2, current2);
        b2TextField.setText(String.format("%1$.3f", round(l2.intercept(), 3)));
        m2TextField.setText(String.format("%1$.6f", round(l2.slope(), 6)));
        mse2TextField.setText(String.format("%1$.6f", round(l2.getMSE(), 6)));
        //this.rawOutputTextArea.setText(l.toString()+"\n");
        
        updateLinearRegressionPlot(titrant1,current1,titrant2,current2);

    }//end method

    void updateRawDataPoints(HashMap<Integer, Points> points) {
        Iterator pointsIterator = points.entrySet().iterator();
        int pointsSize = points.size();
        // only plot the latest 10
        if (pointsSize > 10) {

            for (int i = 0; i < pointsSize - 10; i++) {
                if (pointsIterator.hasNext()) {
                    pointsIterator.next();
                }//end if

            }//end for            
            pointsSize = 10;
        }

        double[] titrant = new double[pointsSize];
        double[] current = new double[pointsSize];

        int i = 0;

        while (pointsIterator.hasNext()) {
            Map.Entry mapping = (Map.Entry) pointsIterator.next();
            titrant[i] = ((Points) mapping.getValue()).getTitrant();
            current[i] = ((Points) mapping.getValue()).getCurrent();

            i++;
        }//end while        

        if (pointsSize > 0) {
            rawPointsChartPanel.setVisible(false);
            rawPoints.removeSeries("points");
            rawPointSeries = rawPoints.addSeries("points", titrant, current);
            rawPointSeries.setMarkerColor(Color.BLUE);
            rawPointSeries.setMarker(SeriesMarkers.SQUARE);
            rawPointsChartPanel.setVisible(true);
        }
    }//end method

    void updateLinearRegressionPlot(double[] titrant1, double[] current1, double[] titrant2, double[] current2) {

        //plot2JInternalFrame.setVisible(false);
        plot1JInternalFrame.setVisible(false);
        //series 1
        lRegression.removeSeries("Points 1");
        lRegressionPointsSeries1 = lRegression.addSeries("Points 1", titrant1, current1);
        lRegressionPointsSeries1.setMarkerColor(Color.GREEN);
        lRegressionPointsSeries1.setLineColor(transparent);
        lRegressionPointsSeries1.setLineWidth(lineWidth);
        lRegressionPointsSeries1.setMarker(SeriesMarkers.SQUARE);

        //series 2
        lRegression.removeSeries("Points 2");
        lRegressionPointsSeries2 = lRegression.addSeries("Points 2", titrant2, current2);
        lRegressionPointsSeries2.setMarkerColor(Color.RED);
        lRegressionPointsSeries2.setLineColor(transparent);
        lRegressionPointsSeries2.setLineWidth(lineWidth);
        lRegressionPointsSeries2.setMarker(SeriesMarkers.SQUARE);

        double[] x1 = new double[2];
        double[] y1 = new double[2];
        double[] x2 = new double[2];
        double[] y2 = new double[2];
        double[] m = new double[2];
        double[] b = new double[2];

        m[0] = string2Double(getM1());
        b[0] = string2Double(getB1());

        m[1] = string2Double(getM2());
        b[1] = string2Double(getB2());

        //line 1
        x1[0] = titrant1[0];
        x1[1] = titrant2[titrant2.length - 1];

        y1[0] = m[0] * x1[0] + b[0];
        y1[1] = m[0] * x1[1] + b[0];

        //line 2
        x2[0] = titrant1[0];
        x2[1] = titrant2[titrant2.length - 1];

        y2[0] = m[1] * x2[0] + b[1];
        y2[1] = m[1] * x2[1] + b[1];

        //series 3
        lRegression.removeSeries("Line 1");
        lRegressionLineSeries1 = lRegression.addSeries("Line 1", x1, y1);
        lRegressionLineSeries1.setMarkerColor(transparent);
        lRegressionLineSeries1.setLineColor(Color.GREEN);
        lRegressionLineSeries1.setMarker(SeriesMarkers.SQUARE);

        //series 4
        lRegression.removeSeries("Line 2");
        lRegressionLineSeries2 = lRegression.addSeries("Line 2", x2, y2);
        lRegressionLineSeries2.setMarkerColor(transparent);
        lRegressionLineSeries2.setLineColor(Color.RED);
        lRegressionLineSeries2.setMarker(SeriesMarkers.SQUARE);

        //plot2JInternalFrame.setVisible(true);
        plot1JInternalFrame.setVisible(true);

    }//end method  

    void resetAndSave() {
        rawOutputTextArea.setText("");
        rawOutputTextAreaConfig.setText("");

        rawPointsChartPanel.setVisible(false);
        //plot2JInternalFrame.setVisible(false);
        plot1JInternalFrame.setVisible(false);
        rawPoints.removeSeries("points");
        rawPointSeries = rawPoints.addSeries("points", new double[]{0}, new double[]{0});
        rawPointSeries.setMarkerColor(Color.BLUE);
        rawPointSeries.setMarker(SeriesMarkers.SQUARE);

        //series 1
        lRegression.removeSeries("Points 1");
        lRegressionPointsSeries1 = lRegression.addSeries("Points 1", new double[]{0}, new double[]{0});
        lRegressionPointsSeries1.setMarkerColor(Color.GREEN);
        lRegressionPointsSeries1.setLineColor(transparent);
        lRegressionPointsSeries1.setLineWidth(lineWidth);
        lRegressionPointsSeries1.setMarker(SeriesMarkers.SQUARE);

        //series 2
        lRegression.removeSeries("Points 2");
        lRegressionPointsSeries2 = lRegression.addSeries("Points 2", new double[]{0}, new double[]{0});
        lRegressionPointsSeries2.setMarkerColor(Color.RED);
        lRegressionPointsSeries2.setLineColor(transparent);
        lRegressionPointsSeries2.setLineWidth(lineWidth);
        lRegressionPointsSeries2.setMarker(SeriesMarkers.SQUARE);

        //series 3
        lRegression.removeSeries("Line 1");
        lRegressionLineSeries1 = lRegression.addSeries("Line 1", new double[]{0}, new double[]{0});
        lRegressionLineSeries1.setMarkerColor(transparent);
        lRegressionLineSeries1.setLineColor(Color.GREEN);
        lRegressionLineSeries1.setMarker(SeriesMarkers.SQUARE);

        //series 4
        lRegression.removeSeries("Line 2");
        lRegressionLineSeries2 = lRegression.addSeries("Line 2", new double[]{0}, new double[]{0});
        lRegressionLineSeries2.setMarkerColor(transparent);
        lRegressionLineSeries2.setLineColor(Color.RED);
        lRegressionLineSeries2.setMarker(SeriesMarkers.SQUARE);

        rawPointsChartPanel.setVisible(true);
        //plot2JInternalFrame.setVisible(true);        
        plot1JInternalFrame.setVisible(true);

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
            saveDataFiles();
        }//end if

    }//end method

    void fill() {
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

    }//end method

    void blank() {
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
    }//end method

    void sample() {
        if (port != null && resetAndSave) {
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
    }//end method

    void save() {
        
        saveFields();
        saveDataFiles();
    }//endmethod
    
    double string2Double(String s) {
        double num = -999.999;
        if (s.matches("^[-+]?[0-9]*\\.?[0-9]+$")) {
            
            num = Double.parseDouble(s);
        }
        
        return num;
    }//end method

    int string2Int(String s) {
        int num = -999;
        if (s.matches("^[-+]?[0-9]*\\.?[0-9]+$")) {
            
            num = Integer.parseInt(s);
        }
        
        return num;
    }//end method

void setBottleFileName(String s){

    bottleFileName = s;
}//end method  

String getBottleFileName(){
return bottleFileName;
}//end method

    int closeApplication() {
        save();
        System.exit(0);
        return 0;
    }//end method

    
    void setFieldsEnabled(boolean set) {

        //set fields
        this.cruiseTextField.setEnabled(set);
        this.stationSpinner.setEnabled(set);
        this.castSpinner.setEnabled(set);
        this.niskinSpinner.setEnabled(set);
        this.depthTextField.setEnabled(set);
        this.lattitudeTextField.setEnabled(set);
        this.longitudeTextField.setEnabled(set);
        this.bottleSpinner.setEnabled(set);
        this.sampleDateTextField.setEnabled(set);
        this.runDateTextField.setEnabled(set);
        this.thioTempTextField.setEnabled(set);
        this.volKIO3TextField.setEnabled(set);
        this.NKIO3TextField.setEnabled(set);
        this.stdulTextField.setEnabled(set);
        this.blkulTextField.setEnabled(set);
        this.volRegTextField.setEnabled(set);
        this.drawTempTextField.setEnabled(set);
        this.salinityTextField.setEnabled(set);
        this.EPTextField.setEnabled(set);
        this.swDensityTextField.setEnabled(set);
        this.thioDensityTextField.setEnabled(set);
        this.M_thio_20C_TextField.setEnabled(set);
        this.M_thio_tl_TextField.setEnabled(set);
        this.botVolTextField.setEnabled(set);
        this.DrawTempBottleVolTextField.setEnabled(set);
        this.o2umTextField.setEnabled(set);
        this.o2_umolPerKgTextField.setEnabled(set);
        this.b1TextField.setEnabled(set);
        this.b2TextField.setEnabled(set);
        this.m1TextField.setEnabled(set);
        this.m2TextField.setEnabled(set);
        this.mse2TextField.setEnabled(set);
        this.mse1TextField.setEnabled(set);
        this.ulPerstTextField.setEnabled(set);
        this.ulOffsetTextField.setEnabled(set);
        this.slopeTextField.setEnabled(set);
        this.speedTextField.setEnabled(set);
        this.waitTextField.setEnabled(set);
        this.dateTextField.setEnabled(set);
        this.timeTextField.setEnabled(set);
        this.dataFileTextField.setEnabled(set);
        this.rawOutputTextArea.setEnabled(set);
        this.rawOutputTextAreaConfig.setEnabled(set);
        
        

        //set buttons
        resetAndSaveButton.setEnabled(set);
        fillButton.setEnabled(set);
        blankButton.setEnabled(set);
        sampleButton.setEnabled(set);
        saveButton.setEnabled(set);
        uLSetButton.setEnabled(set);
        slopeSpeedWaitButton.setEnabled(set);
        dateButton.setEnabled(set);
        openDataFileButton.setEnabled(set);

        serialPortComboBox.setEnabled(!set);
        baudRateComboBox.setEnabled(!set);
        dataBitsComboBox.setEnabled(!set);
        parityComboBox.setEnabled(!set);
        stopBitsComboBox.setEnabled(!set);
        flowControlComboBox.setEnabled(!set);

    }
    
    void showComponents(boolean show)
    {
    
        this.saveButton.setVisible(show);
        this.resetAndSaveButton.setVisible(show);
        this.blankButton.setVisible(show);
        this.fillButton.setVisible(show);
        this.sampleButton.setVisible(show);
        this.b1TextField.setVisible(show);
        this.b2TextField.setVisible(show);
        this.m1TextField.setVisible(show);
        this.m2TextField.setVisible(show);
        this.mse1TextField.setVisible(show);
        this.mse2TextField.setVisible(show);
        this.rawOutputScrollPane.setVisible(show);
        this.rawOutputTextArea.setVisible(show);
        this.btlVolFileTextField.setVisible(show);
        this.dataFileTextField.setVisible(show);
        this.openDataFileButton.setVisible(show);
        this.openVolumeFileButton.setVisible(show);
        jLabel41.setVisible(show);
        jLabel45.setVisible(show);
        jLabel43.setVisible(show);
        jLabel46.setVisible(show);
        jLabel44.setVisible(show);
        jLabel47.setVisible(show);
        jLabel28.setVisible(show);
        jLabel39.setVisible(show);
        
        
        Dimension d = rawOutputScrollPane.getSize();
        Dimension x = this.getSize();
        int h = d.height;
        int w = d.width;
        
        int h1 = x.height;
        int w1 = x.width;
         int H =0;
         int W = 0;
        
        if (show){
        
            W = w + w1;
            
        }//end if
        else
        {
            W = w1 - w;

        }

        Dimension d1 = new Dimension(W, h1);
        this.setSize(d1);

        if (show) {
            this.pack();
        }

        
        
        
        
    }//end method
}// end class
