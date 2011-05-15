import java.awt.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import org.rsbot.Configuration;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
@ScriptManifest(authors = {"Ownageful"}, keywords = {"Ownageful"}, name = "Ownagebots Script Loader", version = 1.0, description = "Access to all OwnageBots")
public class OBLogin extends Script {
    private boolean doneLogin = false;
    private Script scripts;
    private String user;
    private String pass;
    OBLoginGUI loginGui;
    private String location1 = Configuration.Paths.getScriptsPrecompiledDirectory() + File.separator;
    private File obprops;
    private enum STATUS {
        S_VERIFIED, S_DECLINED, S_ERROR, S_INPUT_ERROR, S_IPCONFLICT;
    }
    @Override
    public boolean onStart() {
        if (loginGui == null) {
            loginGui = new OBLoginGUI();
            loginGui.setVisible(true);
        } else {
        }
        while (!doneLogin) {
            createAndWaitforGUI();
        }
        switch (getStatus()) {
            case S_VERIFIED:
                obprops = new File(location1 + "obprops.ini");
                try {
                    if (obprops.exists()) {
                        obprops.delete();
                        obprops.createNewFile();
                        writeProperies(obprops);
                        loadOBLoader();
                    } else {
                        obprops.createNewFile();
                        writeProperies(obprops);
                        loadOBLoader();
                    }
                } catch (IOException iOException) {
                    loginGui.jLabel4.setForeground(Color.red);
                    loginGui.jLabel4.setText("An Error Occurred, Try again.");
                    doneLogin = false;
                }
                break;
            case S_DECLINED:
                loginGui.jLabel4.setForeground(Color.red);
                loginGui.jLabel4.setText("Invalid Username or Password");
                doneLogin = false;
                break;
            case S_ERROR:
                loginGui.jLabel4.setForeground(Color.red);
                loginGui.jLabel4.setText("Error connecting to server.");
                doneLogin = false;
                break;
            case S_INPUT_ERROR:
                loginGui.jLabel4.setForeground(Color.red);
                loginGui.jLabel4.setText("Please fill in all fields");
                doneLogin = false;
                break;
            case S_IPCONFLICT:
                loginGui.jLabel4.setForeground(Color.red);
                loginGui.jLabel4.setText("IP Conflict. Please change it from your CPanel");
                doneLogin = false;
                break;
        }
        if (doneLogin) {
            return this.scripts.onStart();
        } else {
            return this.onStart();
        }
    }
    @Override
    public int loop() {
        return scripts.loop();
    }
    private STATUS getStatus() {
        if (user.isEmpty() || pass.isEmpty()) {
            return STATUS.S_INPUT_ERROR;
        } else {
            try {
                BufferedReader in = null;
                URL dll = new URL("http://ownagebots.com/loader/verify.php?user=" + user + "&pass=" + pass);
                URLConnection dllConnection = dll.openConnection();
                dllConnection.connect();
                in = new BufferedReader(new InputStreamReader(dllConnection.getInputStream()));
                String queryStatus = in.readLine();
                if (queryStatus.equals("Declined")) {
                    return STATUS.S_DECLINED;
                } else if (queryStatus.equals("IPerror")) {
                    return STATUS.S_IPCONFLICT;
                } else if (queryStatus.equals("ServerError")) {
                    return STATUS.S_ERROR;
                } else if (queryStatus.equals("Verified")) {
                    return STATUS.S_VERIFIED;
                } else {
                    return STATUS.S_ERROR;
                }
            } catch (IOException iOException) {
                return STATUS.S_ERROR;
            } catch (NumberFormatException numberFormatException) {
                return STATUS.S_ERROR;
            }
        }
    }
    private void createAndWaitforGUI() {
        sleep(100);
    }
    public void writeProperies(File f) {
        try {
            FileWriter a = new FileWriter(f);
            BufferedWriter b = new BufferedWriter(a);
            b.write(user);
            b.newLine();
            b.write(pass);
            b.close();
            a.close();
        } catch (IOException iOException) {
        }
    }
    @Override
    public void onFinish() {
        this.scripts.onFinish();
    }
    private void loadOBLoader() {
        try {
            URL url = new URL("http://ownagebots.com/loader/index.php?request=obscripts");
            URLClassLoader cl = new URLClassLoader(new URL[]{url});
            this.scripts = (Script) cl.loadClass("OBScripts").newInstance();
            delegateTo(this.scripts);
            this.loginGui.dispose();
            this.doneLogin = true;
        } catch (Exception e) {
            this.log.severe("An error occurred, please start the script again .");
        }
    }
    public class OBLoginGUI extends javax.swing.JFrame {
        public OBLoginGUI() {
            initComponents();
            this.setLocation(new Point(600, 300));
        }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OwnageBots Login Panel");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Username:");

        jLabel3.setText("Password:");

        jLabel1.setText("<html><img src=\"http://ownagebots.com/finalsmall.png\"></html>");

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Please Enter Credentials");
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        user = jTextField1.getText();
        pass = jPasswordField1.getText();
        doneLogin = true;
    }                                        
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
    }
}
