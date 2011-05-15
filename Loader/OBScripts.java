import java.awt.Desktop;
import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import org.rsbot.Configuration;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
@ScriptManifest(authors = {"Ownageful"}, keywords = {"Ownageful"}, name = "Ownagebots Script Loader", version = 1.0, description = "Access to all OwnageBots")
public class OBScripts extends Script {
    Script picked;
    public String user;
    public String line;
    public String pass;
    public OBScriptsGUI gui;
    private String location = Configuration.Paths.getScriptsPrecompiledDirectory() + File.separator;
    private File obprops;
    @Override
    public int loop() {
        return this.picked.loop();
    }
    @Override
    public boolean onStart() {
        createAndWaitforGUI();
        return this.picked.onStart();
    }
    @Override
    public void onFinish() {
        this.picked.onFinish();
    }
    private void createAndWaitforGUI() {
        this.gui = new OBScriptsGUI();
        this.gui.setVisible(true);
        while (this.gui.isVisible()) {
            sleep(10);
        }
    }
    public class OBScriptsGUI extends javax.swing.JFrame {
        String[][] freeScripts;
        String[][] paidScripts;
        public OBScriptsGUI() {
            initComponents();
            this.setLocation(new Point(600, 300));
            obprops = new File(location + "obprops.ini");
            this.readProperies(obprops);
            obprops.delete();
            ArrayList<String> free = setScripts("free");
            ArrayList<String> paid = setScripts("paid");
            this.freeScripts = convertAL(free);
            this.paidScripts = convertAL(paid);
            this.jTable3.setModel(new javax.swing.table.DefaultTableModel(
                    freeScripts,
                    new String[]{
                        "Bot Name"
                    }) {
                Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false, false
                };
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
            jTable4.setModel(new javax.swing.table.DefaultTableModel(
                    paidScripts,
                    new String[]{
                        "Bot Name"
                    }) {
                Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean[]{
                    false, false
                };
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
            OBScripts.this.log("Done init components.");
        }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OwnageBots Scripts");
        setResizable(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Bot Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.setRowSelectionAllowed(false);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable3);
        jTable3.getColumnModel().getColumn(0).setMinWidth(200);
        jTable3.getColumnModel().getColumn(0).setPreferredWidth(200);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Free Scripts                     ", jPanel1);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Bot Name"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.setRowSelectionAllowed(false);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(jTable4);
        jTable4.getColumnModel().getColumn(0).setMinWidth(200);
        jTable4.getColumnModel().getColumn(0).setPreferredWidth(200);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Premium Scripts                        ", jPanel2);

        jLabel1.setText("<html><img src=\"http://ownagebots.com/finalsmall.png\"></html>");

        jButton1.setText("Start Script");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("More Information");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>                        
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (jTabbedPane1.getSelectedIndex() == 0) {
            int freeselected = jTable3.getSelectedRow();
            if (freeselected != -1) {
                String packageSelected = freeScripts[freeselected][2];
                loadPackage(packageSelected);
            }
        } else if (jTabbedPane1.getSelectedIndex() == 1) {
            int paideselected = jTable4.getSelectedRow();
            if (paideselected != -1) {
                String packageSelected = paidScripts[paideselected][2];
                loadPackage(packageSelected);
            }
        } else {
        }
    }                                        
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (jTabbedPane1.getSelectedIndex() == 0) {
            int freeselected = jTable3.getSelectedRow();
            if (freeselected != -1) {
                String packageSelected = freeScripts[freeselected][1];
                try {
                    Desktop.getDesktop().browse(new URI(packageSelected));
                } catch (URISyntaxException uRISyntaxException) {
                } catch (IOException iOException) {
                }
            }
        } else if (jTabbedPane1.getSelectedIndex() == 1) {
            int paideselected = jTable4.getSelectedRow();
            if (paideselected != -1) {
                String packageSelected = paidScripts[paideselected][1];
                try {
                    Desktop.getDesktop().browse(new URI(packageSelected));
                } catch (URISyntaxException uRISyntaxException) {
                } catch (IOException iOException) {
                }
            }
        } else {
        }
    }                                        
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    // End of variables declaration                   
        private void readProperies(File f) {
            try {
                FileReader a = new FileReader(f);
                BufferedReader b = new BufferedReader(a);
                user = b.readLine();
                pass = b.readLine();
                b.close();
                a.close();
            } catch (IOException iOException) {
            }
        }
        private String[][] convertAL(ArrayList<String> free) {
            int theSize = free.size();
            String[][] list = new String[theSize][3];
            for (int i = 0; i < theSize; i++) {
                String work = free.get(i);
                String name = work.substring(0, work.indexOf(":"));
                work = work.substring(work.indexOf(":") + 1);
                String packagename = work.substring(0, work.indexOf(":"));
                work = work.substring(work.indexOf(":") + 1);
                String namelink = work;
                list[i][0] = name;
                list[i][1] = namelink;
                list[i][2] = packagename;
            }
            return list;
        }
        private ArrayList<String> setScripts(String string) {
            try {
                ArrayList<String> list = new ArrayList<String>();
                BufferedReader in = null;
                URL dll = new URL("http://ownagebots.com/loader/3f42522f224t/getPackages.php?user=" + user + "&pass=" + pass + "&type=" + string);
                URLConnection dllConnection = dll.openConnection();
                dllConnection.connect();
                in = new BufferedReader(new InputStreamReader(dllConnection.getInputStream()));
                String queryStatus = in.readLine();
                while (!queryStatus.equals("")) {
                    if (queryStatus.length() == 1) {
                        queryStatus = "";
                    } else {
                        int firstIndex = queryStatus.indexOf(";");//
                        list.add(queryStatus.substring(0, firstIndex));
                        try {
                            queryStatus = queryStatus.substring(firstIndex + 1);
                        } catch (IndexOutOfBoundsException e) {
                            queryStatus = "";
                        }
                    }
                }
                in.close();
                return list;
            } catch (IOException iOException) {
                return null;
            }
        }
        private void loadPackage(String packageSelected) {
            try {
                URL url = new URL("http://ownagebots.com/loader/3f42522f224t/packageRequest.php?user=" + user + "&pass=" + pass + "&request=" + packageSelected);
                URLClassLoader cl = new URLClassLoader(new URL[]{url});
                picked = (Script) cl.loadClass(packageSelected).newInstance();
                delegateTo(picked);
                gui.dispose();
            } catch (Exception e) {
                log.severe("An error occurred, please start the script again .");
            }
        }
    }
}
