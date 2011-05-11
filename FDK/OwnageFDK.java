import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Equipment;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.util.WindowUtil;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;
@ScriptManifest(authors = {"Ownageful"}, name = "Ownageful's Frost Dragon Killer", version = 1.0, description = "FDK Pro. Settings in GUI")
public class OwnageFDK extends Script implements PaintListener, MessageListener, MouseListener {
    private int[] prayerPotions = {139, 141, 143, 2434};
    private int[] strengthPots = {2440, 157, 159, 161};
    private int[] attackPots = {2436, 145, 147, 149};
    private int[] antiPots = {2452, 2454, 2456, 2458};
    private int[] rangedPots = {2444, 169, 171, 173};
    private int[] summonPots = {12146, 12144, 12142, 12140};
    private int[] bobPouchIDS = {12087, 12007, 12031, 12093};
    private int[] potionAmounts = {0, 0, 0, 0, 0};
    private boolean[] takingPotions = {false, false, false, false, false};
    private int[][] potionArrays = {antiPots, rangedPots, attackPots, strengthPots, prayerPotions};
    private int[] cannonParts = {6, 8, 10, 12};
    private int[] dragons = {11635, 11636, 11634, 11633, 51};
    private int[] gloryCharges = {1712, 1710, 1708, 1706, 1704};
    private int[] lootIDs = {18832, 1249, 1303, 1247, 892, 11286, 985, 987};
    private int[] charms = {12158, 12159, 12160, 12161, 12162, 12163, 12164, 12165, 12166, 12167};
    private int[] prices = new int[8];
    private boolean summonFull = false, usingSummon = false, usingCannon = false, usingFood = false, lootingCharms = false;
    private String[] lootNames = {
        "Frost dragon bones",
        "Dragon spear",
        "Rune longsword",
        "Rune spear",
        "Rune arrow",
        "Draconic visage",
        "Loop half of a key",
        "Tooth half of a key"};
    private int cannonBalls = 2, food = -1, finalPouch = -1, teleportAnim = 9603, foodAmount = -1, eatAt = 40;
    private RSArea draynorBank = new RSArea(new RSTile(3092, 3240), new RSTile(3097, 4539));
    // The north section of the frost area
    private RSArea northFrosts = new RSArea(new RSTile(1293, 4511), new RSTile(1339, 4541));
    // The south section of the frost area
    private RSArea southFrosts = new RSArea(new RSTile(1284, 4482), new RSTile(1339, 4510));
    // The path from draynor bank to the dungeon trapdoor
    private RSTile[] bankToTrap = {
        new RSTile(3085, 3251), new RSTile(3076, 3257),
        new RSTile(3074, 3269), new RSTile(3067, 3273),
        new RSTile(3058, 3264), new RSTile(3053, 3250),
        new RSTile(3041, 3247), new RSTile(3027, 3243),
        new RSTile(3020, 3233), new RSTile(3017, 3219),
        new RSTile(3017, 3205), new RSTile(3022, 3191),
        new RSTile(3020, 3176), new RSTile(3017, 3164),
        new RSTile(3012, 3157), new RSTile(3009, 3150)};
    // The path from trapdoor to frosts dungeon door
    private RSTile[] trapToDungeon = {
        new RSTile(2999, 9549), new RSTile(2994, 9556),
        new RSTile(3004, 9579), new RSTile(3018, 9579),
        new RSTile(3030, 9583), new RSTile(3034, 9591),
        new RSTile(3033, 9591),};
    private RSObject trapdoor, dungeonGate, fullCannon;
    private RSNPC dragon;
    private int trapdoorID = 9472, dungeonGateID = 52859, fullCannonID = 6;
    private final Filter<RSNPC> frostDragonFilter = new Filter<RSNPC>() {
        @Override
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equalsIgnoreCase("Frost dragon")
                        && (!npc.isInCombat() || npc.getInteracting() != getMyPlayer()));
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    FDKGUI gui;
    Timer tm;
    @Override
    public boolean onStart() {
        return (game.isLoggedIn() && createAndWaitForGUI());
    }
    @Override
    public void onFinish() {
        env.takeScreenshot(true);
        super.stopScript();
    }
    @Override
    public int loop() {
        return 100;
    }
    private boolean needToTele() {
        return (((summonFull || !usingSummon) && inventory.isFull() && inventory.getCount(food) == 0)
                || (inventory.getCount(food) == 0 && getMyPlayer().getHPPercent() <= 50));
    }
    private boolean needToSummon() {
        try {
            return (usingSummon && !summoning.isFamiliarSummoned())
                    || (usingSummon && summoning.isFamiliarSummoned() && summoning.getTimeLeft() < 2.00);
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean canSummon() {
        try {
            return inventory.contains(finalPouch) && summoning.getSummoningPoints() > 5;
        } catch (NullPointerException e) {
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void storeSummon() {
        RSItem toStore = inventory.getItem(lootIDs);
        if (toStore != null) {
            if (toStore.doAction("Use")) {
                mouse.click(summoning.getFamiliar().getNPC().getScreenLocation(), false);
                sleep(random(100, 200));
                if (menu.isOpen()) {
                    if (menu.doAction(" -> " + summoning.getFamiliar().getName())) {
                        sleep(random(300, 400));
                    }
                }
            }
        }
    }
    private boolean walkPath(RSTile[] path) {
        if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) <= 1) {
            return walking.walkPathMM(path);
        }
        return false;
    }
    private boolean isInDungeon() {
        return (Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0);
    }
    private void calculatePrices() {
        Thread priceThread = new Thread() {
            @Override
            public void run() {
                log("Loading guide prices for profitable loots.");
                for (int b = 0; b < 4; b++) {
                    prices[b] = grandExchange.lookup(lootIDs[b]).getGuidePrice();
                    log("" + lootNames[b] + " [" + lootIDs[b] + "] guide price is " + prices[b]);
                }
            }
        };
        priceThread.start();
    }
    private void teleport() {
        while (getMyPlayer().getAnimation() != teleportAnim) {
            try {
                game.openTab(5);
                if (interfaces.get(Equipment.INTERFACE_EQUIPMENT).getComponent(Equipment.NECK).doAction("Draynor")) {
                    sleep(random(200, 300));
                }
            } catch (NullPointerException e) {
                break;
            }
        }
        log("Teleported succesfully");
    }
    public int gap(int[] pots) {
        if (bank.isOpen()) {
            for (int i = 0; i < 4; i++) {
                if (bank.getCount(pots[i]) > 0) {
                    return pots[i];
                }
            }
        }
        return 0;
    }
    public void onRepaint(Graphics grphcs) {
    }
    public void messageReceived(MessageEvent me) {
    }
    public void mouseClicked(MouseEvent e) {
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    private boolean createAndWaitForGUI() {
        if (SwingUtilities.isEventDispatchThread()) {
            gui = new FDKGUI();
            gui.setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        gui = new FDKGUI();
                        gui.setVisible(true);
                    }
                });
            } catch (InvocationTargetException ite) {
                return false;
            } catch (InterruptedException ie) {
                return false;
            }
        }
        sleep(100);
        calculatePrices();
        mouse.setSpeed(random(5, 6));
        tm.reset();
        while (gui.isVisible()) {
            sleep(100);
        }
        return true;
    }
    public class FDKGUI extends javax.swing.JFrame {
        public FDKGUI() {
            initComponents();
            this.setLocation(450, 280);
            this.setResizable(false);
            this.setSize(424, 544);
        }
        private void initComponents() {
            jLabel5 = new javax.swing.JLabel();
            jSeparator1 = new javax.swing.JSeparator();
            jLabel1 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jLabel2 = new javax.swing.JLabel();
            jTextField2 = new javax.swing.JTextField();
            jLabel6 = new javax.swing.JLabel();
            jSpinner1 = new javax.swing.JSpinner();
            jLabel7 = new javax.swing.JLabel();
            jSeparator2 = new javax.swing.JSeparator();
            jCheckBox2 = new javax.swing.JCheckBox();
            jCheckBox3 = new javax.swing.JCheckBox();
            jCheckBox4 = new javax.swing.JCheckBox();
            jCheckBox5 = new javax.swing.JCheckBox();
            jCheckBox1 = new javax.swing.JCheckBox();
            jButton1 = new javax.swing.JButton();
            jLabel8 = new javax.swing.JLabel();
            jSeparator3 = new javax.swing.JSeparator();
            jCheckBox7 = new javax.swing.JCheckBox();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel9 = new javax.swing.JLabel();
            jSeparator4 = new javax.swing.JSeparator();
            jLabel10 = new javax.swing.JLabel();
            jSeparator5 = new javax.swing.JSeparator();
            jLabel11 = new javax.swing.JLabel();
            jLabel12 = new javax.swing.JLabel();
            jCheckBox6 = new javax.swing.JCheckBox();
            jSpinner2 = new javax.swing.JSpinner();
            jLabel14 = new javax.swing.JLabel();
            jLabel15 = new javax.swing.JLabel();
            jSpinner3 = new javax.swing.JSpinner();
            jLabel16 = new javax.swing.JLabel();
            jSpinner4 = new javax.swing.JSpinner();
            jLabel17 = new javax.swing.JLabel();
            jSpinner5 = new javax.swing.JSpinner();
            jLabel18 = new javax.swing.JLabel();
            jSpinner6 = new javax.swing.JSpinner();
            jCheckBox8 = new javax.swing.JCheckBox();
            jLabel3 = new javax.swing.JLabel();
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Ownageful's Frost Dragon Killer");
            setAlwaysOnTop(true);
            setBackground(new java.awt.Color(255, 255, 255));
            setFocusable(false);
            jLabel5.setForeground(new java.awt.Color(0, 153, 51));
            jLabel5.setText("Food: (leave blank if not using food)");
            jLabel1.setText("Food ID:");
            jLabel2.setText("Withdraw:");
            jLabel6.setText("Eat around:");
            jSpinner1.setModel(new javax.swing.SpinnerNumberModel(40, 20, 90, 1));
            jLabel7.setForeground(new java.awt.Color(204, 0, 102));
            jLabel7.setText("Pots:");
            jCheckBox2.setSelected(true);
            jCheckBox2.setText("Drink Antifire");
            jCheckBox3.setText("Drink Super Attack");
            jCheckBox4.setText("Drink Range Pots");
            jCheckBox5.setText("Drink Super Strength");
            jCheckBox1.setText("Loot Charms?");
            jButton1.setText("Start");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
            jLabel8.setForeground(new java.awt.Color(0, 51, 255));
            jLabel8.setText("Summoning: (please have summoning potions in bank if using BoB) ");
            jCheckBox7.setText("Beast of Burden:");
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bull Ant", "TerrorBird", "War Tortoise", "Pack Yak"}));
            jLabel9.setForeground(new java.awt.Color(255, 0, 0));
            jLabel9.setText("Looting:");
            jLabel10.setForeground(new java.awt.Color(255, 0, 255));
            jLabel10.setText("Cannon:");
            jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18));
            jLabel11.setForeground(new java.awt.Color(0, 102, 255));
            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Ownageful's Frost Dragon Killer Pro");
            jLabel12.setText("%");
            jCheckBox6.setSelected(true);
            jCheckBox6.setText("Drink Prayer Potions");
            jSpinner2.setModel(new javax.swing.SpinnerNumberModel(2, 1, 4, 1));
            jLabel14.setText("potions per trip");
            jLabel15.setText("potions per trip");
            jSpinner3.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
            jLabel16.setText("potions per trip");
            jSpinner4.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
            jLabel17.setText("potions per trip");
            jSpinner5.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));
            jLabel18.setText("potions per trip");
            jSpinner6.setModel(new javax.swing.SpinnerNumberModel(4, 1, 8, 1));
            jCheckBox8.setText("Use Cannon (if yes, please start with all 4 cannon peices in inventory)");
            jLabel3.setText("Please start the script in draynor bank.");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel5)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel9)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel7)).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 356, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox2).addComponent(jCheckBox4).addComponent(jCheckBox3).addComponent(jCheckBox5).addComponent(jCheckBox6)).addGap(32, 32, 32).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel18)).addGroup(layout.createSequentialGroup().addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel17)).addGroup(layout.createSequentialGroup().addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel16)).addGroup(layout.createSequentialGroup().addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel15)).addGroup(layout.createSequentialGroup().addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel14)))).addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jCheckBox7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox1).addComponent(jLabel10))).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3).addComponent(jCheckBox8).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap()));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addGap(3, 3, 3).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel7).addGap(1, 1, 1).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox2).addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox4).addComponent(jSpinner3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel15)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox3).addComponent(jSpinner4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSpinner5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSpinner6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel18)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE).addComponent(jLabel8).addGap(1, 1, 1).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox7).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel9).addGap(3, 3, 3).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox1).addGap(7, 7, 7).addComponent(jLabel10).addGap(1, 1, 1).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(30, 30, 30)));
            pack();
        }
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            boolean error = false;
            if (jTextField1.getText().isEmpty()) {
                usingFood = false;
            } else {
                try {
                    food = Integer.parseInt(jTextField1.getText());
                    eatAt = (Integer) jSpinner1.getValue();
                    foodAmount = Integer.parseInt(jTextField2.getText());
                } catch (NumberFormatException numberFormatException) {
                    WindowUtil.showDialog("Invalid Food format, please enter proper ID numbers");
                    error = true;
                }
            }

            JCheckBox[] boxes = {jCheckBox2, jCheckBox4, jCheckBox3, jCheckBox5, jCheckBox6};
            JSpinner[] spinners = {jSpinner2, jSpinner3, jSpinner4, jSpinner5, jSpinner6};
            for (int i = 0; i < 5; i++) {
                if (boxes[i].isSelected()) {
                    takingPotions[i] = true;
                    potionAmounts[i] = (Integer) spinners[i].getValue();
                }
            }

            if (jCheckBox7.isSelected()) {
                usingSummon = true;
                finalPouch = bobPouchIDS[jComboBox1.getSelectedIndex()];
            }

            if (jCheckBox1.isSelected()) {
                lootingCharms = true;
            }

            if (jCheckBox8.isSelected()) {
                usingCannon = true;
            }

            if (!error) {
                this.dispose();
            }
        }
        private javax.swing.JButton jButton1;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox2;
        private javax.swing.JCheckBox jCheckBox3;
        private javax.swing.JCheckBox jCheckBox4;
        private javax.swing.JCheckBox jCheckBox5;
        private javax.swing.JCheckBox jCheckBox6;
        private javax.swing.JCheckBox jCheckBox7;
        private javax.swing.JCheckBox jCheckBox8;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel16;
        private javax.swing.JLabel jLabel17;
        private javax.swing.JLabel jLabel18;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JSeparator jSeparator2;
        private javax.swing.JSeparator jSeparator3;
        private javax.swing.JSeparator jSeparator4;
        private javax.swing.JSeparator jSeparator5;
        private javax.swing.JSpinner jSpinner1;
        private javax.swing.JSpinner jSpinner2;
        private javax.swing.JSpinner jSpinner3;
        private javax.swing.JSpinner jSpinner4;
        private javax.swing.JSpinner jSpinner5;
        private javax.swing.JSpinner jSpinner6;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
    }
}
