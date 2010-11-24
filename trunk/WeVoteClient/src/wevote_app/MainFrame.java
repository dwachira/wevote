/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on Oct 4, 2010, 10:34:12 AM
 */
package wevote_app;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.application.Action;

/**
 *
 * @author hollgam
 */
public class MainFrame extends javax.swing.JFrame {

    public static String lastOpenedSessionFileName = "";
    public static String[] topicArrayString;
    ListSelectionModel listSelectionModel; //listModel for topics
    ListSelectionModel listSelectionModelPoll; // listModel for polls
    String[] list1Str = {"Topics"};
    String[] list2Str = {"Polls"};
    public static int currentTopicID = 0;
    public static int currentPollID = -1;
    public Thread sendingPollsThread;
    public Thread deleteMessagesThread;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        disableElementsGUI();

    }

    //overriding for showing exit/confirm dialog
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            int exit = JOptionPane.showConfirmDialog(this, "Are you sure?");
            if (exit == JOptionPane.YES_OPTION) {
                System.exit(0);
            }

        } else {
            // If you do not want listeners processing the WINDOW_CLOSING
            // events, you could this next call in an else block for the:
            //     if (e.getID() ...)
            // statement. That way, only the other types of Window events
            // (iconification, activation, etc.) would be sent out.
            //processWindowEvent(e);
        }
    }

    private void disableElementsGUI() {
        jList1.setEnabled(false);
        jList2.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jMenuItem7.setEnabled(false);
        jMenuItem8.setEnabled(false);
        //jMenuItem9.setEnabled(false);
        jMenuItem10.setEnabled(false);
        jMenuItem11.setEnabled(false);
        jMenuItem3.setEnabled(false);

        //saveSessionItem.setEnabled(false);
    }

    public void setEnabledMenuItem(int item) {
        if (item == 7) {
            jMenuItem7.setEnabled(true);
        }
        if (item == 8) {
            jMenuItem8.setEnabled(true);
        }
        if (item == 9) {
            jMenuItem9.setEnabled(true);
        }
        if (item == 10) {
            jMenuItem10.setEnabled(true);
        }
        if (item == 11) {
            jMenuItem11.setEnabled(true);
        }
    }

    private String[] getPollString(int topicID) {
        String[] pollString = new String[Main.topicArray.get(topicID).getPollArrayList().size()];
        for (int i = 0; i < Main.topicArray.get(topicID).getPollArrayList().size(); i++) {
            pollString[i] = Integer.toString(i + 1) + ". " + Main.topicArray.get(topicID).getPollArrayList().get(i).getQuestion();
        }
        return pollString;
    }

    void setRespondentsRegistered(String string) {
        jLabel7.setText(string);
    }

    void setAnswersReceived(String string) {
        jLabel11.setText(string);
    }

    void setBattery(String string) {
        jLabel5.setText(string + "%");
    }

    void setSignal(String string) {
        jLabel6.setText(string + "%");
    }

    void setMobileNumber(String string) {
        jLabel9.setText(string);
    }

    void setDisabledElement(int id) {
        if (id == 3)
           jButton3.setEnabled(false);
        if (id == 4)
            jButton4.setEnabled(false);
        if (id == 5)
            jButton5.setEnabled(false);
    }

    class SharedListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            boolean isAdjusting = e.getValueIsAdjusting();
            int minIndex = lsm.getMinSelectionIndex();

            currentTopicID = minIndex;
            jButton4.setEnabled(false);
            updateList2(minIndex);
            if (isAdjusting) {
                //if topic was selected
                jButton2.setEnabled(true);
                jButton3.setEnabled(true);
                jButton5.setEnabled(false);
                jMenu3.setEnabled(true);
            }

        }
    }

    class PollListSelectionHandler implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            boolean isAdjusting = e.getValueIsAdjusting();
            int minIndex = lsm.getMinSelectionIndex();

            currentPollID = minIndex; // used for parsing ID after edit button was pressed
            //System.out.println("Topic:" + currentTopicID);
            //System.out.println("Poll:" + currentPollID);

            //setting Current Question and Current Options for output and checks in Main
            Main.currentQuestion = Main.topicArray.get(currentTopicID).getPollArrayList().get(currentPollID).getQuestionString();
            String curOption = Main.currentQuestion.substring(Main.currentQuestion.indexOf(">") + 2, Main.currentQuestion.indexOf(">") + 3);

            if (curOption.equals("A")) {
                Main.currentOptions = new ArrayList<String>();
                if (Main.currentQuestion.indexOf("A.") > 0) {
                    Main.currentOptions.add("A");
                }
                if (Main.currentQuestion.indexOf("B.") > 0) {
                    Main.currentOptions.add("B");
                }
                if (Main.currentQuestion.indexOf("C.") > 0) {
                    Main.currentOptions.add("C");
                }
                if (Main.currentQuestion.indexOf("D.") > 0) {
                    Main.currentOptions.add("D");
                }

            } else if (curOption.equals("1")) {
                Main.currentOptions = new ArrayList<String>();
                if (Main.currentQuestion.indexOf("1.") > 0) {
                    Main.currentOptions.add("1");
                }
                if (Main.currentQuestion.indexOf("2.") > 0) {
                    Main.currentOptions.add("2");
                }
                if (Main.currentQuestion.indexOf("3.") > 0) {
                    Main.currentOptions.add("3");
                }
                if (Main.currentQuestion.indexOf("4.") > 0) {
                    Main.currentOptions.add("4");
                }

            }


            if (!Main.topicArray.get(currentTopicID).getPollArrayList().isEmpty() && isAdjusting) {
                // if there are polls to show, enagle buttons
                jButton4.setEnabled(true);
                jButton5.setEnabled(true);
                jButton3.setEnabled(true);
                //jButton6.setEnabled(true);
            }


//            if (getPollString(currentTopicID).length == 0 && !isAdjusting) {
//                // if no polls to show, disable edit button
//                jButton4.setEnabled(false);
//            }

        }
    }

    public void refreshGUI() {
        this.repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        fileChooser1 = new javax.swing.JFileChooser();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        saveSessionItem = new javax.swing.JMenuItem();
        saveSessionAsItem = new javax.swing.JMenuItem();
        loadSessionItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        fileChooser.setDialogTitle("Choose a file");
        fileChooser.setFileFilter(new SerFileFilter());
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserHandler(evt);
            }
        });

        fileChooser1.setFileFilter(new TxtFileFilter());

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WeVote client - 1.0");

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.setEnabled(false);
        listSelectionModel = jList1.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        jScrollPane1.setViewportView(jList1);

        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList2.setEnabled(false);
        listSelectionModelPoll = jList2.getSelectionModel();
        listSelectionModelPoll.addListSelectionListener(new PollListSelectionHandler());
        jScrollPane2.setViewportView(jList2);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(MainFrame.class, this);
        jButton1.setAction(actionMap.get("newTopic")); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(119, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(119, 25));
        jButton1.setPreferredSize(new java.awt.Dimension(119, 25));

        jButton2.setAction(actionMap.get("editTopic")); // NOI18N
        jButton2.setMaximumSize(new java.awt.Dimension(119, 23));
        jButton2.setMinimumSize(new java.awt.Dimension(119, 23));
        jButton2.setPreferredSize(new java.awt.Dimension(119, 23));

        jButton3.setAction(actionMap.get("newPoll")); // NOI18N
        jButton3.setMaximumSize(new java.awt.Dimension(119, 23));
        jButton3.setMinimumSize(new java.awt.Dimension(119, 23));
        jButton3.setPreferredSize(new java.awt.Dimension(119, 23));

        jButton4.setAction(actionMap.get("editPoll")); // NOI18N
        jButton4.setMaximumSize(new java.awt.Dimension(111, 23));
        jButton4.setMinimumSize(new java.awt.Dimension(111, 23));
        jButton4.setPreferredSize(new java.awt.Dimension(119, 23));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(13, 13, 13))
        );

        jButton5.setAction(actionMap.get("sendPolls")); // NOI18N

        jButton6.setAction(actionMap.get("stopSendPolls")); // NOI18N

        jScrollPane4.setAutoscrolls(true);

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Lucida Console", 0, 10));
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(1);
        jTextArea1.setToolTipText("Logging window with messages returned by this program."); // NOI18N
        jTextArea1.setBorder(new javax.swing.border.MatteBorder(null));
        jScrollPane4.setViewportView(jTextArea1);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .add(jButton5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 654, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jButton5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton6)
                .addContainerGap(116, Short.MAX_VALUE))
            .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
        );

        jLabel3.setText("Signal:");

        jLabel4.setText("Respondents registered:");

        jLabel2.setText("Battery:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("100%");
        jLabel5.setMaximumSize(new java.awt.Dimension(41, 14));
        jLabel5.setMinimumSize(new java.awt.Dimension(41, 14));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("100%");
        jLabel6.setMaximumSize(new java.awt.Dimension(41, 14));
        jLabel6.setMinimumSize(new java.awt.Dimension(41, 14));
        jLabel6.setPreferredSize(new java.awt.Dimension(41, 14));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel7.setText("0");
        jLabel7.setMaximumSize(new java.awt.Dimension(41, 14));
        jLabel7.setMinimumSize(new java.awt.Dimension(41, 14));
        jLabel7.setPreferredSize(new java.awt.Dimension(41, 14));

        jLabel8.setText("Phone number:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel9.setText("N/A");

        jLabel10.setText("Answers Received:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel11.setText("0");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(28, 28, 28)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 139, Short.MAX_VALUE)
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel9)
                .add(89, 89, 89))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3)
                    .add(jLabel2)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9)
                    .add(jLabel8)
                    .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10)
                    .add(jLabel11))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        jMenu4.setText("New");

        jMenuItem4.setAction(actionMap.get("newTopic")); // NOI18N
        jMenu4.add(jMenuItem4);

        jMenuItem3.setAction(actionMap.get("newPoll")); // NOI18N
        jMenu4.add(jMenuItem3);

        jMenuItem13.setAction(actionMap.get("newMobileNumber")); // NOI18N
        jMenu4.add(jMenuItem13);

        jMenu1.add(jMenu4);
        jMenu1.add(jSeparator4);

        jMenuItem6.setAction(actionMap.get("newSession")); // NOI18N
        jMenu1.add(jMenuItem6);

        saveSessionItem.setAction(actionMap.get("saveSession")); // NOI18N
        jMenu1.add(saveSessionItem);

        saveSessionAsItem.setText("Save Session As");
        saveSessionAsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSessionAsItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveSessionAsItem);

        loadSessionItem.setAction(actionMap.get("loadSession")); // NOI18N
        jMenu1.add(loadSessionItem);
        jMenu1.add(jSeparator1);

        jMenuItem1.setAction(actionMap.get("exitApp")); // NOI18N
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tools");

        jMenuItem10.setAction(actionMap.get("startFetchMobiles")); // NOI18N
        jMenu2.add(jMenuItem10);

        jMenuItem7.setAction(actionMap.get("stopFetchingMobiles")); // NOI18N
        jMenu2.add(jMenuItem7);

        jMenuItem11.setAction(actionMap.get("sendPolls")); // NOI18N
        jMenu2.add(jMenuItem11);

        jMenuItem8.setAction(actionMap.get("stopSendPolls")); // NOI18N
        jMenu2.add(jMenuItem8);

        jMenuItem9.setAction(actionMap.get("deleteAllMessagesInSIM")); // NOI18N
        jMenu2.add(jMenuItem9);

        jMenuItem12.setAction(actionMap.get("clearMobileNumbers")); // NOI18N
        jMenu2.add(jMenuItem12);
        jMenu2.add(jSeparator2);

        jMenuItem5.setText("Properties");
        jMenuItem5.setEnabled(false);
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("About");

        jMenuItem2.setAction(actionMap.get("showAboutFrame")); // NOI18N
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fileChooserHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserHandler
    }//GEN-LAST:event_fileChooserHandler

    private void saveSessionAsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSessionAsItemActionPerformed
        saveSession(evt);
    }//GEN-LAST:event_saveSessionAsItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    @Action
    public void exitApp() {
        int exit = JOptionPane.showConfirmDialog(this, "Are you sure?");
        if (exit == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    @Action
    public void showAboutFrame() {
//        this.setVisible(false);

        AboutFrame aboutFrame = new AboutFrame();
        aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //aboutFrame.setSize(300, 200);
        aboutFrame.setLocationRelativeTo(this);
        aboutFrame.setResizable(false);
        aboutFrame.setVisible(true);
    }

    @Action
    public void sendAT() {
//        AT.sendAT(jTextArea2.getText());
//        String response = AT.getResponse();
//        jTextArea3.setText(response);
    }

    @Action
    public void newPoll() {
//        String question = "question" + Integer.toString(Topic.numberPolls + 1);
//        ArrayList<String> options = new ArrayList<String>();
//        options.add("A");
//        options.add("B");

        NewPoll newPoll = new NewPoll();
        newPoll.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //newPoll.setSize(300, 200);
        newPoll.setLocationRelativeTo(this);
        newPoll.setResizable(false);
        newPoll.setVisible(true);

        //Main.addPoll(WIDTH, pollsToAdd, null);

//        String question = jTextField1.getText();
//        String options = jTextField2.getText();
//        //ArrayList<String> optionsArray = new ArrayList<String>();
//        //optionsArray.add(options);
//        Main.pollsArray.add(new Topic(question, options));

        SessionOperations.sessionChanged = true;
    }

    @Action
    public void saveSession(ActionEvent evt) {
        String fileName = "";

        if (evt.getSource() == saveSessionAsItem) {
            fileChooser.setDialogTitle("Save your session as");

            // Set the current directory to the application's current directory
            try {
                // Create a File object containing the canonical path of the
                // desired file
                File f = new File(new File("session.ser").getCanonicalPath());

                // Set the selected file
                fileChooser.setSelectedFile(f);
            } catch (IOException e) {
            }

            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                // What to do with the file, e.g. display it in a TextArea
                fileName = file.getName();
            } else {
            }
            SessionOperations.saveSession(fileName);
            lastOpenedSessionFileName = fileName;
        } else if (evt.getSource() == saveSessionItem) {

            if (lastOpenedSessionFileName.length() > 0) {
                fileName = lastOpenedSessionFileName;
                SessionOperations.saveSession(fileName);
            } else {
                refreshLog("Cannot save session at this point.");
            }

        }
    }

    @Action
    public void loadSession() {
        fileChooser.setDialogTitle("Load a session");

        // Set the current directory to the application's current directory
        try {
            // Create a File object containing the canonical path of the
            // desired file
            File f = new File(new File("session.ser").getCanonicalPath());

            // Set the selected file
            fileChooser.setSelectedFile(f);
        } catch (IOException e) {
        }

        int returnVal = fileChooser.showOpenDialog(this);
        String fileName = "";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // What to do with the file, e.g. display it in a TextArea
            fileName = file.getName();
        } else {
        }
        SessionOperations.loadSession(fileName);

        updateList1();
        if (Main.topicArray.size() >0)
            updateList2(0);
        clearList2();
        //jList1.setSelectedIndex(0);
        //updateList(jList2, emptyStr);
        lastOpenedSessionFileName = fileName;
        //System.out.println("Loaded OK");
    }

    class SerFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".ser");
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "Session files (*.ser)";
        }
    }

    @Action
    public void newSession() {
        SessionOperations.newSession();
        //System.out.println("MAKING A NEW SESSION");

        updateList(jList1, list1Str);
        updateList(jList2, list2Str);
        jList1.setEnabled(false);
        jList2.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jMenuItem3.setEnabled(false);

    }

//    @Action
//    public void importPolls() {
//        fileChooser1.setDialogTitle("Import a file with polls");
//        // Set the current directory to the application's current directory
//        try {
//            // Create a File object containing the canonical path of the
//            // desired file
//            File f = new File(new File("filename.txt").getCanonicalPath());
//
//            // Set the selected file
//            fileChooser1.setSelectedFile(f);
//        } catch (IOException e) {
//        }
//
//        int returnVal = fileChooser1.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser1.getSelectedFile();
//            Main.getPollsFromFile(file);
//              // What to do with the file, e.g. display it in a TextArea
//        } else {
//            //System.out.println("File access cancelled by user.");
//        }
//        refreshGUI();
//        Main.printPolls();
//
//    }
    class TxtFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".txt" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "Text files (*.txt)";
        }
    }
//
//    @Action
//    public void importMobileNumbers() {
//        fileChooser1.setDialogTitle("Import a file with mobile numbers");
//        // Set the current directory to the application's current directory
//        try {
//            // Create a File object containing the canonical path of the
//            // desired file
//            File f = new File(new File("filename.txt").getCanonicalPath());
//
//            // Set the selected file
//            fileChooser1.setSelectedFile(f);
//        } catch (IOException e) {
//        }
//
//        int returnVal = fileChooser1.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser1.getSelectedFile();
//            Main.getMobileNumbersFromFile(file);
//              // What to do with the file, e.g. display it in a TextArea
//        } else {
//            //System.out.println("File access cancelled by user.");
//        }
//        refreshGUI();
//        Main.printMobileNumbers();
//    }

//    @Action
//    public void exportPolls() {
//        fileChooser1.setDialogTitle("Export polls into a file");
//        // Set the current directory to the application's current directory
//        try {
//            // Create a File object containing the canonical path of the
//            // desired file
//            File f = new File(new File("filename.txt").getCanonicalPath());
//
//            // Set the selected file
//            fileChooser1.setSelectedFile(f);
//        } catch (IOException e) {
//        }
//
//        int returnVal = fileChooser1.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser1.getSelectedFile();
//            Main.writePollsToFile(file);
//              // What to do with the file, e.g. display it in a TextArea
//        } else {
//            //System.out.println("File access cancelled by user.");
//        }
//    }
//    @Action
//    public void exportMobileNumbers() {
//        fileChooser1.setDialogTitle("Export mobile numbers into a file");
//        // Set the current directory to the application's current directory
//        try {
//            // Create a File object containing the canonical path of the
//            // desired file
//            File f = new File(new File("filename.txt").getCanonicalPath());
//
//            // Set the selected file
//            fileChooser1.setSelectedFile(f);
//        } catch (IOException e) {
//        }
//
//        int returnVal = fileChooser1.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser1.getSelectedFile();
//            Main.writeMobileNumbersToFile(file);
//              // What to do with the file, e.g. display it in a TextArea
//        } else {
//            //System.out.println("File access cancelled by user.");
//        }
//    }
    @Action
    public void newTopic() {
        NewTopic newTopic = new NewTopic();
        newTopic.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //newPoll.setSize(300, 200);
        newTopic.setLocationRelativeTo(this);
        //newTopic.setResizable(false);
        newTopic.setVisible(true);

        SessionOperations.sessionChanged = true;
    }

    public void updateList(JList list, String[] objs) {
        DefaultListModel listModel = new DefaultListModel();
        listModel.clear();
        for (int i = 0; i < objs.length; i++) {
            listModel.add(i, objs[i]);
        }
        list.setModel(listModel);


        //issue about showing Polls after creating a new Topic in new Session
        try {
            if (Main.topicArray.size() == 1 && Main.topicArray.get(0).getPollArrayList().isEmpty()) {
                jList2.setEnabled(false);
                //System.out.println("DISABLED");
            } else {
                jList2.setEnabled(true);
                //System.out.println("ENABLED");
            }

            if (Main.topicArray.get(currentTopicID).getPollArrayList().isEmpty()) {
                // if no polls to show, disable edit button
                jButton4.setEnabled(false);
                jButton5.setEnabled(false);
                jButton6.setEnabled(false);
            }
        } catch (Exception e) {
        }


    }

    public void updateList1() {
        topicArrayString = new String[Main.topicArray.size()];

        for (int i = 0; i < Main.topicArray.size(); i++) {
            topicArrayString[i] = Integer.toString(Main.topicArray.get(i).getTopicID()) + ". " + Main.topicArray.get(i).getTitle();
        }

//        for (String str : topicArrayString) {
//            //System.out.println(str);
//        }


        updateList(jList1, topicArrayString);
        jList1.setSelectedIndex(Main.topicArray.size() - 1);

        if (topicArrayString.length != 0) {
            jList1.setEnabled(true);
            jList2.setEnabled(true);
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton6.setEnabled(false);
            jMenuItem3.setEnabled(true);

        } else {
            jList1.setEnabled(false);
            jList2.setEnabled(false);
            jButton2.setEnabled(false);
            jButton3.setEnabled(false);
            jButton4.setEnabled(false);
            jMenuItem3.setEnabled(false);

        }

    }

    public void updateList2(int topicID) {
        try {
            ////System.out.println("topicID" + topicID);
            //topicID++;
            //currentTopicID = topicID;
            String[] pollString = getPollString(topicID);

            updateList(jList2, pollString);



        } catch (Exception e) {
        }
    }

    public void clearList2() {
        try {
            String[] pollString = {}; //making an empty array of String to add to the list to make it empty
            if (Main.topicArray.size() < 1)
                updateList(jList2, pollString);
            if (Main.topicArray.isEmpty()) {
                jButton2.setEnabled(false);
            }
            jButton3.setEnabled(true);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
            jButton6.setEnabled(false);

        } catch (Exception e) {
        }

    }

    @Action
    public void editTopic() {
        NewTopic.editState = true;
        NewTopic newTopic = new NewTopic();
        newTopic.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //newPoll.setSize(300, 200);
        newTopic.setLocationRelativeTo(this);
        //newTopic.setResizable(false);
        newTopic.setVisible(true);
    }

    @Action
    public void editPoll() {
        NewPoll.editStatePoll = true;
        NewPoll newPoll = new NewPoll();
        newPoll.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //newPoll.setSize(300, 200);
        newPoll.setLocationRelativeTo(this);
        newPoll.setResizable(false);
        newPoll.setVisible(true);
    }

    public void refreshLog(String str) {
        str = jTextArea1.getText() + "\n " + str;
        jTextArea1.setText(str);
        ////System.out.println("String has been added to log: " + str);
    }

    @Action
    public void sendPolls() {
        FetchMobiles.fetchMobileVar = false; //stop getting new numbers
        if (Main.respondentsRegistered > 0) {
            Main.answerArray = new ArrayList<Answer>(); // erase all answers received previously

            jButton6.setEnabled(true); // set Shutdown button to TRUE
            jButton5.setEnabled(false); // set Send button to FALSE
            jMenuItem11.setEnabled(false);
            jMenuItem8.setEnabled(true);
            jMenuItem10.setEnabled(false);
            jMenuItem7.setEnabled(false);

            refreshLog("Topic:                " + Main.topicArray.get(currentTopicID).getTitle());
            refreshLog("Question of the poll: " + Main.topicArray.get(currentTopicID).getPollArrayList().get(currentPollID).getQuestionString());

            //Sending current poll to mobile numbers from the array from Main
            //System.out.println("BEFORE SENDING");

            Main.answersRegistered  = 0;

            setAnswersReceived("0");
            

            //Main.answerArray = new ArrayList<Answer>();

            // current Topic and Poll. these are global vars that are unique for current poll
            Main.topicSent = Main.topicArray.get(currentTopicID);
            Main.pollSent = Main.topicArray.get(currentTopicID).getPollArrayList().get(currentPollID);
            Main.topicSent.uploadPoll(); // Upload Question, pool and answers to database

            Runnable threadJob = new SendPolls();
            sendingPollsThread = new Thread(threadJob);
            sendingPollsThread.setName("Sending polls");

            Main.modemListenerThread.stop(); // dangerous operation but it increases performance in many times

            sendingPollsThread.start();
            refreshLog("Sending polls has started.");

            //System.out.println("AFTER SENDING");
        } else {
            refreshLog("NO RESPONDENTS HAVE REGISTERED.");
        }
    }

    @Action
    public void stopSendPolls() {
//        for (int i = 0; i < Main.answerArray.size(); i++) {
//            Main.answerArray.get(i).uploadAnswer();
//        }

        //stopiing sending Polls
        jButton5.setEnabled(true); // set SHutdown button to TRUE
        jButton6.setEnabled(false); // set SHutdown button to TRUE
        jMenuItem11.setEnabled(true);
        jMenuItem8.setEnabled(false);

        FetchAnswers.fetchAnswersVar = false;
        sendingPollsThread.stop();
        refreshLog("Fetching answers has been stopped.");
        for (Answer ans : Main.answerArray) {
            ans.checkRegistration();
        }
        Main.showSummary();

        //make current topic and poll to be null for sending of new poll
        Main.topicSent = new Topic();
        Main.pollSent = new Poll();
    }

    @Action
    public void stopFetchingMobiles() {
        FetchMobiles.fetchMobileVar = false;
        Main.modemListenerThread.stop(); // dangerous operation but it increases performance in many times
        jMenuItem7.setEnabled(false);
    }

    @Action
    public void startFetchMobiles() {

        //thread for collecting mobile numbers
        FetchMobiles.fetchMobileVar = true;
        Runnable threadJob = new FetchMobiles();
        Main.modemListenerThread = new Thread(threadJob);
        Main.modemListenerThread.setName("Fetching numbers");
        Main.modemListenerThread.start();
        refreshLog("Fetching mobile numbers has been started.");

        jMenuItem10.setEnabled(false);
        jMenuItem7.setEnabled(true);
    }

    @Action
    public void deleteAllMessagesInSIM() {

        jMenuItem9.setEnabled(false);

        Runnable threadJobDelete = new DeleteMessagesThread();
        deleteMessagesThread = new Thread(threadJobDelete);
        deleteMessagesThread.setName("Deleting messages");
        deleteMessagesThread.start();
        refreshLog("Now deleting all messages from the SIM card.");

    }

    @Action
    public void clearMobileNumbers() {
        Main.mobileNumberArray = new ArrayList<MobileNumber>();
    }

    @Action
    public void newMobileNumber() {
        NewMobileNumber newMobNum = new NewMobileNumber();
        newMobNum.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //newPoll.setSize(300, 200);
        newMobNum.setLocationRelativeTo(this);
        newMobNum.setResizable(false);
        newMobNum.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JFileChooser fileChooser1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JMenuItem loadSessionItem;
    private javax.swing.JMenuItem saveSessionAsItem;
    private javax.swing.JMenuItem saveSessionItem;
    // End of variables declaration//GEN-END:variables
}
