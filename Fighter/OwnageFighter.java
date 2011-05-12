import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.AncestorEvent;

import org.rsbot.Application;
import org.rsbot.event.events.ServerMessageEvent;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.event.listeners.ServerMessageListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Magic;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSCharacter;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSItem;

//BANKING MAYBE LOL, high alch.. safe spot?
@ScriptManifest(authors = {"Ownageful"}, name = "Ownageful Fighter", version = 1.4, description = "<html><body style='font-family: Arial; margin: 10px;'><span style='color: #00AA00; font-weight: bold;'>Ownageful Fighter</span>&nbsp;<strong>Version:&nbsp;1.4</strong><br />")
public class OwnagefulFighter extends Script implements PaintListener, ServerMessageListener {

    //Script Variables
    public boolean startScript = false;
    // General Tab
    double mD, pD, tD;
    int failFight = 0;
    int ba1;
    int ba2;
    int pa1;
    int pa2;
    long spa1, spa2, sba1, sba2;
    int bin, bfor;
    long breakAfter;
    long breakFor;
    boolean breakStarted = false;
    boolean tCreated = false;
    boolean antiban;
    boolean hover;
    boolean paint;
    boolean mca;
    boolean altar;
    RSObject altarr;
    int altarID;
    public boolean takingBreaks;
    public RSTile centralTile;
    public int fightDistance;
    boolean tileSet = false;
    boolean distanceSet;
    boolean breaking = false;
    // Food Tab
    boolean isBTP = false;
    boolean isEating;
    boolean foodValid;
    int healAT;
    ArrayList<Integer> foods = new ArrayList<Integer>();
    public int[] foodz;
    public int[] charms = {12158, 12159, 12160, 12161, 12162, 12163, 12164, 12165, 12166, 12167};
    public String foodString;
    // Monster Tab
    public boolean firstMonster;
    public int[] npcMapIndex;
    public RSNPC[] npcMap;
    ArrayList<RSNPC> npcList = new ArrayList<RSNPC>();
    ArrayList<Integer> filteredList = new ArrayList<Integer>();
    public int[] npcz;
    boolean npcAdded;
    boolean npcAddedList2;
    boolean npcKillAdded;
    boolean npcAnimationTaken = false;
    int npcDeathAnimation = 0;
    public int[] ndi = new int[3];
    //Alch Tab
    boolean isAlching;
    boolean alchAdded;
    boolean lootedAlch;
    public int[] alchItemz;
    ArrayList<Integer> alchzList = new ArrayList<Integer>();
    private int initialCoins;
    private int finalCoins;
    //Loot Tab
    boolean lCharms;
    boolean isLooting;
    int lootToAdd;
    String bAction;
    private boolean lootAdded;
    ArrayList<Integer> lootList = new ArrayList<Integer>();
    ArrayList<String> names = new ArrayList<String>();
    public String arrowName;
    public int[] lootz;
    public int[] prices;
    public RSGroundItem customLoot;
    public RSGroundItem boneLoot;
    public RSGroundItem btpLoot;
    public RSGroundItem arrowLoot;
    public RSGroundItem charmLoot;
    private int inititalStackCount;
    private int beforeLoot;
    //Misc Settings Tab
    boolean prayActive = false;
    public boolean bones = false;
    public boolean pots = false;
    public boolean potted = false;
    public boolean spec = false;
    boolean specReady = false;
    public boolean arrowPickup = false;
    public int arrowID;
    public boolean validArrow;
    final int[] potsIDS = {113, 115, 117, 119, 2440, 157, 159, 161, 9739, 9741,
        9743, 9745, 2442, 163, 165, 167, 137, 135, 133, 2432, 2436, 145, 147, 149, 2428, 121, 123, 125, 9739, 9741,
        9743, 9745, 169, 171, 173, 2444};
    final int[] strengthPots = {113, 115, 117, 119, 2440, 157, 159, 161, 9739, 9741,
        9743, 9745};
    final int[] defencePots = {2442, 163, 165, 167, 137, 135, 133, 2432};
    final int[] attackPots = {2436, 145, 147, 149, 2428, 121, 123, 125, 9739, 9741,
        9743, 9745};
    final int[] rangePots = {169, 171, 173, 2444};
    public int[] btpBones = {526, 532, 530, 528, 3183, 2859};
    public int[] allBones = {526, 532, 530, 528, 3183, 2859, 534, 3125, 4834,
        14793, 4812, 3123, 4832, 6729, 536};
    public int[] pPots = {139, 141, 143, 2434};
    public int btp = 8015;
    public int peach = 6883;
    boolean usingQuickPrayer;
    boolean usingPrayerPots;
    //Fighting Settings
    public int randomCamera;
    public int randomInventoryClean;
    ArrayList<Integer> allInventoryClean = new ArrayList<Integer>();
    public int[] inventoryClean;
    private RSNPC npc;
    //Paint Settings
    public int startSTR, startDEF, startHP, startATT, startMAG, startRNG, STRPH, DEFPH, HPPH, ATTPH, RNGPH, MAGPH, STRpr, DEFpr, HPpr, ATTpr, RNGpr, MAGpr, currentSTR, currentDEF, currentHP, currentATT, currentMAG, currentRNG, currentSTRLVL, currentRNGLVL, currentMAGLVL, currentDEFLVL, currentHPLVL, currentATTLVL;
    private long initialStartTime, runTime, seconds, minutes, hours;
    private boolean paintSTR = false;
    private boolean paintATT = false;
    private boolean paintDEF = false;
    private boolean paintHP = false;
    private boolean paintRNG = false;
    private boolean paintMAG = false;
    public int npcKilled = 0;
    public double version = 1.0;
    private int totalLoot;
    private double profitPerSecond;
    private int npcCounter;
    private int interfaceCounter;
    //Other
    public String[] actions = {"Open", "Climb", "Pass", "Through"};
    private Timer tm;
    private Timer tm1;
    private boolean bOpened = false;
    private int mx, cmx;
    private int my, cmy;
    final Filter<RSNPC> filt = new Filter<RSNPC>() {

        public boolean accept(RSNPC npc) {
            if (!npc.isOnScreen()) {
                return false;
            }
            final int npcID = npc.getID();
            for (int x = 0; x < npcz.length; x++) {
                if (npcID == npcz[x] && !npc.isInCombat() && calc.canReach(npc.getLocation(), true)) {
                    return true;
                }
            }
            return false;
        }
    };
    final Filter<RSNPC> filt1 = new Filter<RSNPC>() {

        public boolean accept(RSNPC npc) {
            if (!npc.isOnScreen()) {
                return false;
            }
            final int npcID = npc.getID();
            for (int x = 0; x < npcz.length; x++) {
                if (npcID == npcz[x] && npc.getHPPercent() != 0 && calc.canReach(npc.getLocation(), true)) {
                    return true;
                }
            }
            return false;
        }
    };

    //---------------------Code decleration--------------------------------------
    @Override
    public boolean onStart() {
        startSTR = skills.getCurrentExp(Skills.STRENGTH);
        startDEF = skills.getCurrentExp(Skills.DEFENSE);
        startHP = skills.getCurrentExp(Skills.CONSTITUTION);
        startATT = skills.getCurrentExp(Skills.ATTACK);
        startRNG = skills.getCurrentExp(Skills.RANGE);
        startMAG = skills.getCurrentExp(Skills.MAGIC);


        initialStartTime = System.currentTimeMillis();
        OwnagefulFighterGUI gui = new OwnagefulFighterGUI();
        while (!startScript) {
            this.sleep(10);
        }
        if (game.isLoggedIn()) {
            mouse.setSpeed(random(6, 7));
            return true;
        } else {
            log("You must be logged in to START this script.");
            return false;
        }
    }

    @Override
    public void onFinish() {
        env.takeScreenshot(true);
    }

    public boolean waitToMove(int timeout) {
        long start = System.currentTimeMillis();
        RSPlayer myPlayer = getMyPlayer();
        while (System.currentTimeMillis() - start < timeout) {
            if (myPlayer.isMoving()) {
                return true;
            }
            this.sleep(15);
        }
        return false;
    }

    public int waitForAnim(int timeout) {
        long start = System.currentTimeMillis();
        int anim = -1;

        while (System.currentTimeMillis() - start < timeout) {
            if ((anim = getMyPlayer().getAnimation()) != -1) {
                break;
            }
            this.sleep(15);
        }
        return anim;
    }

    public int loop() {

        if (breaking && !tCreated) {
            tm = new Timer((long) random(sba1, sba2));
            tm.reset();
            tCreated = true;
        } else if (breaking && tCreated && !breakStarted) {
            breakAfter = tm.getRemaining();
            if (breakAfter == 0) {
                breakStarted = true;
            }
        } else if (breaking && tCreated && breakStarted) {
            tm1 = new Timer((long) random(spa1, spa2));
            tm.reset();
            while (tm1.getRemaining() != 0) {
                this.sleep(random(100, 200));
                breakFor = tm1.getRemaining();
            }
            tCreated = false;
            breakStarted = false;
        }

        // Logged in check
        if (!game.isLoggedIn()) {
            return random(200, 700);
        }

        //Set run check.
        if (!isRunning() && walking.getEnergy() > random(20, 75)) {
            walking.setRun(true);
            this.sleep(random(400, 800));
        }
        // Accidental click check
        if (itemSelected() != 0) {
            tiles.doAction(getMyPlayer().getLocation(), "Cancel");
            this.sleep(random(300, 600));
        }

        //Food Eating check.
        if (getMyPlayer().getHPPercent() < (healAT + random(0, 10))) {
            if (!isEating && !isBTP) {
                log("Not using food or BTP, low HP. Stopping script.");
                stopScript();
            }

            if (isEating) {
                if (inventory.containsOneOf(foodz)) {
                    //using custom array method to eat food.
                    while (getMyPlayer().getHPPercent() <= 80 && inventory.containsOneOf(foodz)) {
                        doInventoryItem(foodz, "Eat");
                        if (waitForAnim(1500) != -1) {
                            this.sleep(random(1000, 1200));
                        }
                    }
                } else if (!isBTP) {
                    log("Out of Food, low HP. Stopping script.");
                    stopScript();
                }
            }

            if (isBTP) {
                if (!inventory.containsOneOf(foodz)) {
                    if (inventory.contains(peach)) {
                        while (getMyPlayer().getHPPercent() <= 85 && inventory.contains(peach)) {
                            inventory.getItem(peach).doAction("Eat");
                            if (waitForAnim(1500) != -1) {
                                this.sleep(random(300, 600));
                            }
                        }
                    } else if (inventory.getCount(btpBones) >= 1) {
                        if (inventory.contains(btp)) {
                            inventory.getItem(btp).doAction("Break");
                            this.sleep(random(2000, 2800));
                        } else {
                            log("Out of Bones to Peaches tabs stopping Script!");
                            stopScript();
                        }
                    }
                }
            }
        }

        switch (getState()) {
            case 1:
                RSCharacter npcc = getMyPlayer().getInteracting();
                if (npcc != null) {
                    if (npcc.getHPPercent() == 0) {
                        if (!npcAnimationTaken) {
                            if (ndi[0] == 0) {
                                ndi[0] = npcc.getAnimation();
                            } else if (ndi[1] == 0) {
                                ndi[1] = npcc.getAnimation();
                            } else if (ndi[2] == 0) {
                                ndi[2] = npcc.getAnimation();
                                if (!npcAnimationTaken) {
                                    if (ndi[0] == ndi[1] && ndi[0] == ndi[2]) {
                                        npcDeathAnimation = ndi[0];
                                        if (npcDeathAnimation != -1) {
                                            log("NPC Death animation found: " + npcDeathAnimation);
                                            npcAnimationTaken = true;
                                        } else {
                                            npcAnimationTaken = false;
                                            ndi[0] = 0;
                                            ndi[1] = 0;
                                            ndi[2] = 0;
                                        }
                                    } else {
                                        ndi[0] = 0;
                                        ndi[1] = 0;
                                        ndi[2] = 0;
                                    }
                                }
                            }
                        }

                        if (!npcKillAdded && npcc.getAnimation() == npcDeathAnimation) {
                            npcKilled++;
                            npcKillAdded = true;
                        }

                    }
                }
                fightingAntiban();
                break;
            case 2:
                try {
                    if (customLoot != null) {
                        for (int i = 0; i
                                < names.size(); i++) {
                            if (customLoot.getItem().getID() == lootz[i]) {
                                pickLoot(customLoot, names.get(i));
                            }
                        }
                    } else if (arrowLoot != null) {
                        pickLoot(arrowLoot, arrowName);
                    } else if (charmLoot != null) {
                        pickLoot(charmLoot, "charm");
                    } else if (btpLoot != null) {
                        pickLoot(btpLoot, "Bones");

                    } else if (boneLoot != null) {
                        pickLoot(boneLoot, "Bones");

                    }
                } catch (NullPointerException e) {
                }
                break;
            case 3:
                while (inventory.containsOneOf(allBones)) {
                    doInventoryItem(allBones, "Bury");
                }
                break;
            case 4:
                if (isAlching) {
                    if (inventory.containsOneOf(alchItemz)) {
                        alchItems();
                    }
                }
                attack();
        }
        return 10;
    }

    private int getState() {

        if (getMyPlayer().getInteracting() != null) {
            try {
                if (spec) {
                    if (npc.getHPPercent() > random(20, 40) && settings.getSetting(300) == 1000) {
                        log("Spec ready");
                        specReady = true;
                    }
                    while (specReady && getMyPlayer().getInteracting() != null) {
                        if (game.getCurrentTab() != Game.TAB_ATTACK) {
                            game.openTab(Game.TAB_ATTACK);
                            this.sleep(random(400, 600));
                        }
                        if (settings.getSetting(301) != 1) {
                            interfaces.getComponent(884, 4).doClick(true);
                            this.sleep(random(1000, 2000));
                        }
                    }
                }
            } catch (NullPointerException e) {
                loop();
            } //Fighting
            return 1;
        } else if (needToLoot()) {
            log("Need to loot");
            if (random(0, 60) == 15) {
                setRandomPitch();
            }
            return 2;
        } else if (bones && inventory.isFull() && inventory.containsOneOf(allBones)) {
            // Bury Bones
            return 3;
        } else if (arrowPickup) {
            if (inventory.getCount(arrowID) >= 100 + random(10, 50)) {
                inventory.getItem(arrowID).doAction("Wield");
            } // Attack
            return 4;
        } else {
            if (pots) {
                doPots();
            } // Attack
            if (usingPrayerPots) {
                doPrayerPots();
            }
            if (altar) {
                if (altarr != null) {
                    if (Integer.parseInt(interfaces.get(749).getComponent(4).getText()) < random(5, 10)) {
                        if (calc.tileOnScreen(altarr.getLocation())) {
                            altarr.doAction("Pray");
                            sleepToMove();
                            this.sleep(random(1000, 2000));
                            walking.walkTileMM(centralTile);
                            sleepToMove();
                        } else {
                            walking.walkTileMM(altarr.getLocation());
                            sleepToMove();
                        }
                    }
                }
            }
            if (usingQuickPrayer) {
                if (prayActive) {
                    if (interfaces.get(749).getComponent(1).containsAction("off")) {
                        if (interfaces.getComponent(749, 1).doAction("off")) {
                            prayActive = false;
                        }
                    }
                }
            }
            return 4;
        }
    }

    private void drawMouse(final Graphics g) {
        mx = mouse.getLocation().x;
        my = mouse.getLocation().y;
        g.setColor(Color.black);

        cmx = mx;
        cmy = my;
        g.setColor(Color.black);
        g.drawLine(cmx, cmy, 0, cmy);
        g.drawLine(cmx, cmy, 516, cmy);
        g.drawLine(cmx, cmy, cmx, 0);
        g.drawLine(cmx, cmy, cmx, game.getBaseY());
    }

    private void drawMap(final Graphics g) {
        Point p = calc.tileToMinimap(centralTile);
        g.setColor(new java.awt.Color(34, 139, 34, 85));
        g.fillOval((int) (p.getX() - ((fightDistance + 1) * 6 / 2)), (int) (p.getY()
                - ((fightDistance + 1) * 6 / 2)), (fightDistance * 6 + (fightDistance * 6 / 2)), (fightDistance * 6 + (fightDistance * 6 / 2)));
    }

    public void onRepaint(Graphics g) {
        if (startScript) {
            if (game.isLoggedIn()) {
                if (paint) {
                    g.setFont(new java.awt.Font("Tahoma", 1, 10));
                    runTime = System.currentTimeMillis() - initialStartTime;
                    seconds = runTime / 1000;
                    if (seconds >= 60) {
                        minutes = seconds / 60;
                        seconds -= (minutes * 60);
                    }
                    if (minutes >= 60) {
                        hours = minutes / 60;
                        minutes -= (hours * 60);
                    }
                    double h = (3600 * hours) + (60 * minutes) + seconds;
                    profitPerSecond = totalLoot / h;
                    if (!paintSTR) {
                        currentSTR = skills.getCurrentExp(Skills.STRENGTH) - startSTR;
                        if (currentSTR > 0) {
                            paintSTR = true;
                        }
                    } else {
                        currentSTR = skills.getCurrentExp(Skills.STRENGTH) - startSTR;
                        float STRXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentSTR > 0) {
                            STRXPperSec = ((float) currentSTR) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                        }
                        float STRXPperMin = STRXPperSec * 60;
                        float STRXPperHour = STRXPperMin * 60;
                        currentSTRLVL = skills.getRealLevel(Skills.STRENGTH);
                        STRpr = skills.getPercentToNextLevel(Skills.STRENGTH);
                        g.setColor(new Color(255, 102, 0));
                        g.fill3DRect(187, 290, 329, 13, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentSTRLVL + "   " + " Xp Gained: " + currentSTR + "   " + "Xp/Hour: " + (int) STRXPperHour + "   " + STRpr + "%.", 190, (300));
                    }
                    if (!paintDEF) {
                        currentDEF = skills.getCurrentExp(Skills.DEFENSE) - startDEF;
                        if (currentDEF > 0) {
                            paintDEF = true;
                        }
                    } else {
                        currentDEF = skills.getCurrentExp(Skills.DEFENSE) - startDEF;
                        float DEFXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentDEF > 0) {
                            DEFXPperSec = ((float) currentDEF) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
                        }
                        float DEFXPperMin = DEFXPperSec * 60;
                        float DEFXPperHour = DEFXPperMin * 60;
                        currentDEFLVL = skills.getRealLevel(Skills.DEFENSE);
                        DEFpr = skills.getPercentToNextLevel(Skills.DEFENSE);
                        g.setColor(new Color(102, 102, 255));
                        g.fill3DRect(187, 302, 329, 13, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentDEFLVL + "   " + " Xp Gained: " + currentDEF + "   " + "Xp/Hour: " + (int) DEFXPperHour + "   " + DEFpr + "%.", 190, (312));
                    }
                    if (!paintHP) {
                        currentHP = skills.getCurrentExp(Skills.CONSTITUTION) - startHP;
                        if (currentHP > 0) {
                            paintHP = true;
                        }
                    } else {
                        currentHP = skills.getCurrentExp(Skills.CONSTITUTION) - startHP;
                        float HPXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentHP > 0) {
                            HPXPperSec = ((float) currentHP) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                        }
                        float HPXPperMin = HPXPperSec * 60;
                        float HPXPperHour = HPXPperMin * 60;
                        currentHPLVL = skills.getRealLevel(Skills.CONSTITUTION);
                        HPpr = skills.getPercentToNextLevel(Skills.CONSTITUTION);
                        g.setColor(new Color(255, 0, 102));
                        g.fill3DRect(187, 266, 329, 13, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentHPLVL + "   " + " Xp Gained: " + currentHP + "   " + "Xp/Hour: " + (int) HPXPperHour + "   " + HPpr + "%.", 190, (276));
                    }
                    if (!paintATT) {
                        currentATT = skills.getCurrentExp(Skills.ATTACK) - startATT;
                        if (currentATT > 0) {
                            paintATT = true;
                        }
                    } else {
                        currentATT = skills.getCurrentExp(Skills.ATTACK) - startATT;
                        float ATTXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentATT > 0) {
                            ATTXPperSec = ((float) currentATT) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                        }
                        float ATTXPperMin = ATTXPperSec * 60;
                        float ATTXPperHour = ATTXPperMin * 60;
                        currentATTLVL = skills.getRealLevel(Skills.ATTACK);
                        ATTpr = skills.getPercentToNextLevel(Skills.ATTACK);
                        g.setColor(new Color(255, 51, 0));
                        g.fill3DRect(187, 278, 329, 13, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentATTLVL + "   " + " Xp Gained: " + currentATT + "   " + "Xp/Hour: " + (int) ATTXPperHour + "   " + ATTpr + "%.", 190, (288));

                    }

                    if (!paintRNG) {
                        currentRNG = skills.getCurrentExp(Skills.RANGE) - startRNG;
                        if (currentRNG > 0) {
                            paintRNG = true;
                        }
                    } else {
                        currentRNG = skills.getCurrentExp(Skills.RANGE) - startRNG;
                        float RNGXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentRNG > 0) {
                            RNGXPperSec = ((float) currentRNG) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
                        }
                        float RNGXPperMin = RNGXPperSec * 60;
                        float RNGXPperHour = RNGXPperMin * 60;
                        currentRNGLVL = skills.getRealLevel(Skills.RANGE);
                        RNGpr = skills.getPercentToNextLevel(Skills.RANGE);
                        g.setColor(new Color(51, 153, 0));
                        g.fill3DRect(187, 314, 329, 13, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentRNGLVL + "   " + "Xp Gained: " + currentRNG + "   " + "Xp/Hour: " + (int) RNGXPperHour + "   " + RNGpr + "%.", 190, (324));
                    }

                    if (!paintMAG) {
                        currentMAG = skills.getCurrentExp(Skills.MAGIC) - startMAG;
                        if (currentMAG > 0) {
                            paintMAG = true;
                        }
                    } else {
                        currentMAG = skills.getCurrentExp(Skills.MAGIC) - startMAG;
                        float MAGXPperSec = 0;
                        if ((minutes > 0 || hours > 0 || seconds > 0) && currentMAG > 0) {
                            MAGXPperSec = ((float) currentMAG) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));

                        }
                        float MAGXPperMin = MAGXPperSec * 60;
                        float MAGXPperHour = MAGXPperMin * 60;
                        currentMAGLVL = skills.getRealLevel(Skills.MAGIC);
                        MAGpr = skills.getPercentToNextLevel(Skills.MAGIC);
                        g.setColor(new Color(51, 0, 255));
                        g.fill3DRect(187, 327, 329, 12, true);
                        g.setColor(Color.WHITE);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        g.drawString("  Level: " + currentMAGLVL + "   " + " Xp Gained: " + currentMAG + "   " + "Xp/Hour: " + (int) MAGXPperHour + "   " + MAGpr + "%.", 190, (337));

                    }
                    g.setColor(new Color(0, 0, 0));
                    g.fill3DRect(3, 266, 184, 73, true);
                    if (startScript) {
                        g.setColor(new Color(227, 100, 45));
                        g.drawString("Ownageful Fighter", 15, 277);
                        g.setColor(Color.white);
                        g.drawString("Time running: " + hours + ":" + minutes + ":" + seconds + ".", 15, 289);
                        g.drawString("NPC Killed: " + npcKilled, 15, 301);
                        g.drawString("Total Loot: " + totalLoot, 15, 313);
                        g.drawString("Loot Per Hour: " + (int) (profitPerSecond * 3600), 15, 325);
                        g.drawString("Version: " + version, 15, 337);
                        g.setFont(new java.awt.Font("Tahoma", 1, 10));
                        if (paintHP) {
                            g.setColor(new Color(255, 0, 102));
                            g.drawString("Hp", 164, (276));
                        }
                        if (paintATT) {
                            g.setColor(new Color(255, 51, 0));
                            g.drawString("Att", 164, (288));
                        }
                        if (paintSTR) {
                            g.setColor(new Color(255, 102, 0));
                            g.drawString("Str", 164, (300));
                        }
                        if (paintDEF) {
                            g.setColor(new Color(102, 102, 255));
                            g.drawString("Def", 164, (312));
                        }
                        if (paintRNG) {
                            g.setColor(new Color(51, 153, 0));
                            g.drawString("Rng", 164, (324));
                        }
                        if (paintMAG) {
                            g.setColor(new Color(51, 0, 255));
                            g.drawString("Mgc", 164, (336));
                        }

                        if (breaking) {
                            g.setColor(Color.black);
                            if (!breakStarted) {
                                g.fill3DRect(313, 457, 150, 16, true);
                                g.setColor(Color.white);
                                bin = (int) (breakAfter / 1000 / 60) + 1;
                                g.drawString("Breaking in: " + bin + " minutes.", 325, 471);
                            } else {
                                bfor = (int) (breakFor / 1000 / 60) + 1;
                                g.fill3DRect(313, 457, 150, 16, true);
                                g.setColor(Color.white);
                                g.drawString("Breaking for: " + bfor + " minutes.", 325, 471);
                            }
                        }

                    }
                    drawMouse(g);
                }
            }
        }
    }

    private boolean doInventoryItem(int[] ids, String action) {
        ArrayList<RSComponent> possible = new ArrayList<RSComponent>();
        for (RSComponent com : inventory.getInterface().getComponents()) {
            for (int i : ids) {
                if (i == com.getComponentID()) {
                    possible.add(com);
                }
            }
        }
        if (possible.isEmpty()) {
            return false;
        }
        RSComponent winner = possible.get(random(0,
                possible.size() - 1));
        Rectangle loc = winner.getArea();
        mouse.move(
                (int) loc.getX() + 3, (int) loc.getY() + 3, (int) loc.getWidth() - 3, (int) loc.getHeight() - 3);
        this.sleep(
                random(100, 300));
        String top = menu.getItems()[0].toLowerCase();
        if (top.contains(action.toLowerCase())) {
            mouse.click(true);
            return true;

        } else if (menuContains(action)) {
            return menu.doAction(action);
        }
        return false;
    } //Credits to foul.

    private boolean menuContains(String item) {
        try {
            for (String s : menu.getItems()) {
                if (s.toLowerCase().contains(item.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            return menuContains(item);
        }
        return false;
    }

    //Credits to foul.
    private int itemSelected() {
        for (final RSComponent com : inventory.getInterface().getComponents()) {
            if (com.getBorderThickness() == 2) {
                return com.getComponentID();
            }
        }
        return 0;
    }

    public boolean isInDistance(RSNPC npc) {
        //int mD,pD,tD;
        if (npc == null) {
            return false;
        }
        mD = calc.distanceTo(centralTile);
        pD = calc.distanceTo(npc.getLocation());
        tD = Math.sqrt((Math.pow(pD, 2)) + (Math.pow(mD, 2)));
        if ((int) tD <= fightDistance) {
            return true;
        } else {
            return false;
        }
    }

    private RSNPC getInteractingNPC(final int... ids) {
        for (final RSNPC npc : npcs.getAll()) {
            // skip ID check if npc is null or it's not interacting with the player
            if (npc == null || !getMyPlayer().equals(npc.getInteracting())) {
                continue;
            }
            for (final int id : ids) {
                if (npc.getID() == id) {
                    // NPC is interacting with the player and id matches...
                    return npc;
                }
            }
        }
        // No wanted npcs found...
        return null;
    }

    private boolean needToLoot() {
        arrowLoot = groundItems.getNearest(arrowID);
        if (bones) {
            boneLoot = groundItems.getNearest(allBones);
        }
        if (lCharms) {
            charmLoot = groundItems.getNearest(charms);
        }
        if (isBTP) {
            btpLoot = groundItems.getNearest(btpBones);
        }
        if (isLooting) {
            customLoot = groundItems.getNearest(lootz);
        }
        if (inventory.isFull() && arrowLoot != null && arrowPickup && calc.pointOnScreen(arrowLoot.getModel().getPoint())
                && calc.canReach(arrowLoot.getLocation(), false)) {
            if (isLootInDistance(arrowLoot)) {
                inventoryClean();
                if (inventory.isFull()) {
                    if (inventory.containsOneOf(foodz)) {
                        doInventoryItem(foodz, "Eat");
                        return true;
                    } else if (inventory.containsOneOf(allBones)) {
                        doInventoryItem(allBones, "Drop");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }

        if (inventory.isFull() && charmLoot != null) {
            if (isLootInDistance(charmLoot)) {
                inventoryClean();
                if (charmLoot.getItem().getStackSize() > 1 && inventory.contains(charmLoot.getItem().getID())) {
                    return true;
                } else if (charmLoot.getItem().getStackSize() == 1 && inventory.contains(charmLoot.getItem().getID())) {
                    return true;
                } else if (charmLoot.getItem().getStackSize() > 1 && !inventory.contains(charmLoot.getItem().getID())) {
                    if (!inventory.isFull()) {
                        return true;
                    } else if (inventory.containsOneOf(foodz)) {
                        doInventoryItem(foodz, "Eat");
                        this.sleep(random(500, 700));
                        return true;
                    } else if (inventory.contains(peach)) {
                        inventory.getItem(peach).doAction("Eat");
                        this.sleep(random(500, 700));
                        return true;
                    } else if (inventory.containsOneOf(allBones)) {
                        doInventoryItem(allBones, "Drop");
                        this.sleep(random(500, 700));
                        return true;
                    }
                }
            }
        }

        if (inventory.isFull() && customLoot != null) {
            if (isLootInDistance(customLoot)) {
                inventoryClean();
                if (customLoot.getItem().getStackSize() > 1 && inventory.contains(customLoot.getItem().getID())) {
                    return true;
                } else if (customLoot.getItem().getStackSize() == 1 && inventory.contains(customLoot.getItem().getID())) {
                    for (int i = 0; i < charms.length; i++) {
                        if (customLoot.getItem().getID() == charms[i]) {
                            return true;
                        }
                    }
                    return true;
                } else if (customLoot.getItem().getStackSize() > 1 && !inventory.contains(customLoot.getItem().getID())) {
                    if (!inventory.isFull()) {
                        return true;
                    } else if (inventory.containsOneOf(foodz)) {
                        doInventoryItem(foodz, "Eat");
                        this.sleep(random(500, 700));
                        return true;
                    } else if (inventory.contains(peach)) {
                        inventory.getItem(peach).doAction("Eat");
                        this.sleep(random(500, 700));
                        return true;
                    } else if (inventory.containsOneOf(allBones)) {
                        doInventoryItem(allBones, "Drop");
                        this.sleep(random(500, 700));
                        return true;
                    }
                }
            }
        }

        if (!inventory.isFull()) {
            if (boneLoot != null) {
                if (isLootInDistance(boneLoot)) {
                    return true;
                } else {
                    return false;
                }
            } else if (btpLoot != null) {
                if (isLootInDistance(btpLoot)) {
                    return true;
                } else {
                    return false;
                }
            } else if (customLoot != null) {
                if (isLootInDistance(customLoot)) {
                    return true;
                } else {
                    return false;
                }
            } else if (arrowLoot != null) {
                if (isLootInDistance(arrowLoot)) {
                    return true;
                } else {
                    return false;
                }
            } else if (charmLoot != null) {
                if (isLootInDistance(charmLoot)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void pickLoot(RSGroundItem loot, String action) {
        beforeLoot = inventory.getCount();
        if (loot.getItem().getStackSize() > 1) {
            inititalStackCount = inventory.getCount(loot.getItem().getID());
        }
        lootAdded = false;
        if (!loot.isOnScreen()) {
            walking.walkTileMM(loot.getLocation());
            sleepToMove();
        }
        tiles.doAction(loot.getLocation(), action);
        if (waitToMove(1500)) {
            while (getMyPlayer().isMoving()) {
                this.sleep(random(50, 100));
            }
        }
        if (customLoot != null) {
            for (int i = 0; i
                    < alchItemz.length; i++) {
                if (customLoot.getItem().getID() == alchItemz[i]) {
                    lootedAlch = true;
                } else {
                    lootedAlch = false;
                }
            }
            if (!lootedAlch) {
                for (int i = 0; i
                        < lootz.length; i++) {
                    if (lootz[i] == customLoot.getItem().getID()) {
                        if (!lootAdded) {
                            if (beforeLoot == inventory.getCount()) {
                                if (loot.getItem().getStackSize() > 1) {
                                    totalLoot = totalLoot + ((prices[i]) * (inventory.getCount(loot.getItem().getID()) - inititalStackCount));
                                    lootAdded = true;
                                }
                            } else {
                                if (loot.getItem().getStackSize() > 1) {
                                    totalLoot = totalLoot + ((prices[i]) * (inventory.getCount(loot.getItem().getID()) - inititalStackCount));
                                    lootAdded = true;
                                } else {
                                    totalLoot = totalLoot + (prices[i]);
                                    lootAdded = true;
                                }
                            }
                        } else {
                            lootAdded = true;
                        }
                    }
                }
            }
        }
        boneLoot = null;
        btpLoot = null;
        customLoot = null;
        arrowLoot = null;
    }

    private void attack() {

        //Clean inventory randomly
        if (random(1, 1000) == random(1, 1000)) {
            inventory.dropAllExcept(inventoryClean);
        }

        randomCamera = random(0, 4);
        if (getMyPlayer().getInteracting() == null) {
            RSNPC interacting = getInteractingNPC();
            if (interacting != null) {
                npc = interacting;
            } else {
                if (!mca) {
                    npc = npcs.getNearest(filt);
                } else {
                    npc = npcs.getNearest(filt1);
                }
            }
        }

        if (npc == null) {
            if (calc.distanceTo(centralTile) >= 3) {
                walking.walkTileMM(walking.randomizeTile(centralTile, 2, 2));
                if (waitToMove(1000)) {
                    while (getMyPlayer().isMoving()) {
                        this.sleep(random(50, 100));
                    }
                }
            }
        } else {
            if (calc.pointOnScreen(npc.getScreenLocation())
                    && (getMyPlayer().getInteracting() == null) && calc.canReach(npc.getLocation(), true)) {
                if ((!npc.isInteractingWithLocalPlayer() && !mca && !npc.isInCombat()) || ((mca))) {
                    try {
                        //if (isInDistance(npc)) {
                        if (!needToLoot()) {
                            npc.doAction("Attack " + npc.getName());
                            if (usingQuickPrayer) {
                                if (!prayActive) {
                                    if (Integer.parseInt(interfaces.get(749).getComponent(4).getText()) > random(3, 6)) {
                                        if (interfaces.get(749).getComponent(1).containsAction("on")) {
                                            if (interfaces.getComponent(749, 1).doAction("on")) {
                                                prayActive = true;
                                            }
                                        }
                                    }
                                } else if (Integer.parseInt(interfaces.get(749).getComponent(4).getText()) < random(2, 5)) {
                                    doPrayerPots();
                                }
                            }

                            if (waitToMove(1000)) {
                                if (random(0, 10) == 3) {
                                    setRandomPitch();
                                }
                                while (getMyPlayer().isMoving()) {
                                    this.sleep(random(50, 100));
                                }
                            }
                            npcKillAdded = false;
                        }
//                        } else {
//                            if (distanceTo(centralTile) >= 4) {
//                                walkTileMM(randomizeTile(centralTile, 2, 2));
//                                if (sleepToMove(1000)) {
//                                    while (getMyPlayer().isMoving()) {
//                                        this.sleep(random(50, 100));
//                                    }
//                                }
//                            }
//                        }
                    } catch (IndexOutOfBoundsException e) {
                        loop();
                    } catch (NullPointerException e) {
                        loop();
                    }
                }
            } else {
                if (npc != null) {
                    walking.walkTileMM(npc.getLocation());
                    if (waitToMove(1000)) {
                        while (getMyPlayer().isMoving()) {
                            if (calc.pointOnScreen(npc.getScreenLocation()) && !npc.isInteractingWithLocalPlayer() && !npc.isInCombat()
                                    && calc.canReach(npc.getLocation(), true)) {
                                npc.doAction("Attack " + npc.getName());
                                if (waitToMove(1000)) {
                                    while (getMyPlayer().isMoving()) {
                                        this.sleep(random(50, 100));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void inventoryClean() {
        inventory.dropAllExcept(inventoryClean);
    }

    private void setRandomPitch() {
        if (randomCamera == 0 || randomCamera == 1) {
            keyboard.pressKey((char) 39);
            this.sleep(random(100, 1200));
            keyboard.releaseKey((char) 39);
        } else {
            keyboard.pressKey((char) 37);
            this.sleep(random(100, 1200));
            keyboard.releaseKey((char) 37);
        }
        if (camera.getPitch() != 3072) {
            for (int i = 0; i
                    < 7; i++) {
                camera.setPitch(3072);
            }
        }

    }

    //Credits to Foul.
    private int antiban() {
        int i = random(0, 30);
        int ii = random(0, 25);
        if (i == 2) {
            mouse.move(random(0, Application.getBot(null).getClient().getCanvas().getWidth()), random(0,
                    Application.getBot(null).getClient().getCanvas().getHeight()));
            return random(0, 400);
        } else if ((ii == 3) || (ii == 12)) {
            char dir = 37;
            if (random(0, 3) == 2) {
                dir = 39;
            }
            keyboard.pressKey(dir);
            this.sleep(random(500, 2000));
            keyboard.releaseKey(dir);
            return random(0, 500);
        } else if ((i == 7) || (i == 4)) {
            camera.setPitch(random(35, 150));
            return random(0, 500);
        } else if ((i == 5) || (i == 10) || (i == 11) || (i == 13) || (i == 18)
                || (i == 27)) {
            mouse.moveRandomly(random(-4, 4));
        } else if ((i == 1) || (i == 8) || (i == 15) || (i == 20)) {
            Thread cam = new Thread() {

                @Override
                public void run() {
                    char dir = 37;
                    if (random(0, 3) == 2) {
                        dir = 39;
                    }
                    keyboard.pressKey(dir);
                    try {
                        Thread.sleep(random(500, 2000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    keyboard.releaseKey(dir);
                }
            };
            Thread mousee = new Thread() {

                @Override
                public void run() {
                    try {
                        mouse.move(random(0, Application.getBot(null).getClient().getCanvas().getWidth()), random(
                                0, Application.getBot(null).getClient().getCanvas().getHeight()));
                    } catch (NullPointerException e) {
                    }
                }
            };
            if ((i == 7) || (i == 20)) {
                cam.start();
            }
            if (i == 1) {
                mousee.start();
            }
            while (cam.isAlive() || mousee.isAlive()) {
                this.sleep(random(100, 300));
                return random(300, 700);
            }
        }
        return random(1000, 1500);

    }

    public void serverMessageRecieved(ServerMessageEvent sme) {
        String serverString = sme.getMessage().toString();
        if (serverString.contains("You've just")
                || serverString.contains("Congratulations")) {
            this.sleep(random(1500, 2500));
            if (interfaces.canContinue()) {
                interfaces.clickContinue();
            }
        }
        if (serverString.contains("no ammo") || serverString.contains("last one")) {
            if (!inventory.contains(arrowID)) {
                log("Stopping Script, out of arrows.");
                logOut();
            }
        }

        if (serverString.contains("enough power") || serverString.contains("last one")) {
            if (spec) {
                specReady = false;
            }
        }
    }

    public void logOut() {
        stopScript(false);
    }

    private void fightingAntiban() {
        RSNPC nextNPC = npcs.getNearest(filt);
        if (!needToLoot() || (btpLoot != null && inventory.isFull() && charmLoot == null
                && customLoot == null && arrowLoot == null && boneLoot == null)) {
            if (nextNPC != null && nextNPC.isOnScreen()) {
                try {
                    if (hover) {
                        if (!usingQuickPrayer || (usingQuickPrayer && Integer.parseInt(interfaces.get(749).getComponent(4).getText()) > 5)) {
                            if (npc.getHPPercent() <= 50) {
                                if (npc != null) {
                                    mouse.move(nextNPC.getScreenLocation());
                                    if (npc.getHPPercent() == 0 && npc.getAnimation() == npcDeathAnimation) {
                                        //if (isInDistance(nextNPC)) {
                                        if (!needToLoot() || (btpLoot != null && inventory.isFull() && charmLoot == null
                                                && customLoot == null && arrowLoot == null && boneLoot == null)
                                                && calc.canReach(npc.getLocation(), true)) {
                                            nextNPC.doAction("Attack " + npc.getName());
                                            if (usingQuickPrayer) {
                                                if (!prayActive) {
                                                    if (Integer.parseInt(interfaces.get(749).getComponent(4).getText()) > 0) {
                                                        if (interfaces.get(749).getComponent(1).containsAction("on")) {
                                                            if (interfaces.getComponent(749, 1).doAction("on")) {
                                                                prayActive = true;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            npcKillAdded = false;
                                            npc = nextNPC;
                                            if (waitToMove(1000)) {
                                                while (getMyPlayer().isMoving()) {
                                                    this.sleep(random(40, 100));
                                                }
                                            }
                                        }
//                                        } else {
//                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (antiban) {
                        if (random(0, 30) == 5) {
                            antiban();
                        }
                    }
                } catch (NullPointerException e) {
                    loop();
                }
            }
        }
    }

    private void doPots() {
        if (skills.getCurrentLevel(Skills.ATTACK) == skills.getRealLevel(Skills.ATTACK)) {
            if (inventory.containsOneOf(attackPots)) {
                doInventoryItem(attackPots, "Drink");
                if (waitForAnim(829) != -1) {
                    while (getMyPlayer().getAnimation() != -1) {
                        this.sleep(random(300, 600));
                    }
                }
            }
        }
        if (skills.getCurrentLevel(Skills.STRENGTH) == skills.getRealLevel(Skills.STRENGTH)) {
            if (inventory.containsOneOf(strengthPots)) {
                doInventoryItem(strengthPots, "Drink");
                if (waitForAnim(829) != -1) {
                    while (getMyPlayer().getAnimation() != -1) {
                        this.sleep(random(300, 600));
                    }
                }
            }
        }
        if (skills.getCurrentLevel(Skills.DEFENSE) == skills.getRealLevel(Skills.DEFENSE)) {
            if (inventory.containsOneOf(defencePots)) {
                doInventoryItem(defencePots, "Drink");
                if (waitForAnim(829) != -1) {
                    while (getMyPlayer().getAnimation() != -1) {
                        this.sleep(random(300, 600));
                    }
                }
            }
        }

        if (skills.getCurrentLevel(Skills.RANGE) == skills.getRealLevel(Skills.RANGE)) {
            if (inventory.containsOneOf(rangePots)) {
                doInventoryItem(rangePots, "Drink");
                if (waitForAnim(829) != -1) {
                    while (getMyPlayer().getAnimation() != -1) {
                        this.sleep(random(300, 600));
                    }
                }
            }
        }
    }

    private void alchItems() {
        initialCoins = inventory.getCount(995);
        for (int i = 0; i
                < alchItemz.length; i++) {
            if (inventory.contains(alchItemz[i])) {
                if (inventory.getCount(561) >= 1 && inventory.getCount(561) >= 5) {
                    if (game.getCurrentTab() != Game.TAB_MAGIC) {
                        game.openTab(Game.TAB_MAGIC);
                        this.sleep(random(500, 750));
                    }
                    magic.castSpell(Magic.SPELL_HIGH_LEVEL_ALCHEMY);
                    inventory.getItem(alchItemz[i]).doAction("Cast");
                    if (waitForAnim(1000) != -1) {
                        this.sleep(random(1600, 1800));
                        finalCoins = inventory.getCount(995);
                    }
                }
                totalLoot = totalLoot + (finalCoins - initialCoins);
            }
        }
    }

    public void sleepToMove() {
        if (waitToMove(2000)) {
            while (getMyPlayer().isMoving()) {
                this.sleep(random(100, 200));
            }
        }
    }

    /*public boolean atModel(RSObject obj, String action) {
    if (obj == null) {
    return false;
    }
    if (!(obj.getObject() instanceof RSAnimable)) {
    return atObject(obj, action);
    }
    RSAnimable anim = (RSAnimable) obj.getObject();
    Model model = obj.getModel();
    if (model == null) {
    return false;
    }
    int xoff = model.getXPoints()[random(5, model.getXPoints().length)];
    int yoff = model.getZPoints()[random(5, model.getZPoints().length)];
    int zoff = model.getYPoints()[random(5, model.getYPoints().length)];
    Point p = Calculations.worldToScreen(anim.getX() + xoff, anim.getY() + zoff, -yoff);
    if (p.x == -1) {
    return false;
    }
    moveMouse(p, 3, 3);
    this.sleep(random(400, 600));
    return atMenu(action);
    }*/
    private void doPrayerPots() {
        if (usingQuickPrayer && usingPrayerPots) {
            if (inventory.containsOneOf(pPots)) {
                if (Integer.parseInt(interfaces.get(749).getComponent(4).getText()) < random(5, 10)) {
                    log("Need to drink pray pot.");
                    doInventoryItem(pPots, "Drink");
                    if (waitForAnim(1000) == 829) {
                        while (getMyPlayer().getAnimation() == 829) {
                            this.sleep(random(300, 600));
                        }
                    }
                }
            }
        }
    }

    private boolean isLootInDistance(RSGroundItem tile) {
        if (tile == null) {
            return false;
        }
        if (calc.distanceBetween(centralTile, tile.getLocation()) <= fightDistance) {
            return true;
        } else {
            return false;
        }
    }

    public class OwnagefulFighterGUI extends javax.swing.JFrame {

        private JPanel jPanel10;
        private JCheckBox bankingCheck2;
        private JCheckBox bankingCheck1;
        private JLabel jLabel46;
        private JCheckBox bankingCheck;
        private JLabel jLabel47;

        public OwnagefulFighterGUI() {
            try {
                UIManager.setLookAndFeel(
                        "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                initComponents();
                this.setVisible(true);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(OwnagefulFighter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(OwnagefulFighter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(OwnagefulFighter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(OwnagefulFighter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void initComponents() {

            jPanel1 = new javax.swing.JPanel();
            jLabel1 = new javax.swing.JLabel();
            jSeparator1 = new javax.swing.JSeparator();
            jTabbedPane1 = new javax.swing.JTabbedPane();
            jPanel2 = new javax.swing.JPanel();
            jLabel3 = new javax.swing.JLabel();
            jLabel4 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            jLabel5 = new javax.swing.JLabel();
            jSpinner1 = new javax.swing.JSpinner();
            jLabel6 = new javax.swing.JLabel();
            jSeparator2 = new javax.swing.JSeparator();
            enableBreaksCheck = new javax.swing.JCheckBox();
            jLabel19 = new javax.swing.JLabel();
            jLabel20 = new javax.swing.JLabel();
            jLabel21 = new javax.swing.JLabel();
            breaker1Field = new javax.swing.JTextField();
            period1Field = new javax.swing.JTextField();
            breaker2Field = new javax.swing.JTextField();
            period2Field = new javax.swing.JTextField();
            jLabel22 = new javax.swing.JLabel();
            jLabel23 = new javax.swing.JLabel();
            jLabel24 = new javax.swing.JLabel();
            jLabel25 = new javax.swing.JLabel();
            jSeparator5 = new javax.swing.JSeparator();
            jPanel5 = new javax.swing.JPanel();
            jLabel10 = new javax.swing.JLabel();
            jCheckBox1 = new javax.swing.JCheckBox();
            jLabel11 = new javax.swing.JLabel();
            jTextField2 = new javax.swing.JTextField();
            jLabel12 = new javax.swing.JLabel();
            jSpinner2 = new javax.swing.JSpinner();
            jLabel13 = new javax.swing.JLabel();
            jLabel15 = new javax.swing.JLabel();
            jCheckBox2 = new javax.swing.JCheckBox();
            jPanel4 = new javax.swing.JPanel();
            jLabel9 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jButton2 = new javax.swing.JButton();
            list4 = new java.awt.List();
            jLabel16 = new javax.swing.JLabel();
            jLabel17 = new javax.swing.JLabel();
            jLabel31 = new javax.swing.JLabel();
            jButton5 = new javax.swing.JButton();
            jButton7 = new javax.swing.JButton();
            jPanel8 = new javax.swing.JPanel();
            jLabel26 = new javax.swing.JLabel();
            alchList = new java.awt.List();
            jLabel27 = new javax.swing.JLabel();
            toAlchTable = new java.awt.List();
            jLabel28 = new javax.swing.JLabel();
            jLabel29 = new javax.swing.JLabel();
            jLabel30 = new javax.swing.JLabel();
            jPanel10 = new javax.swing.JPanel();
            jLabel46 = new javax.swing.JLabel();
            jPanel9 = new javax.swing.JPanel();
            jLabel47 = new javax.swing.JLabel();
            jPanel6 = new javax.swing.JPanel();
            jRadioButton1 = new javax.swing.JRadioButton();
            jRadioButton2 = new javax.swing.JRadioButton();
            jRadioButton3 = new javax.swing.JRadioButton();
            jRadioButton4 = new javax.swing.JRadioButton();
            jLabel14 = new javax.swing.JLabel();
            jTextField3 = new javax.swing.JTextField();
            prayer = new javax.swing.JRadioButton();
            prayer1 = new javax.swing.JRadioButton();
            prayer2 = new javax.swing.JRadioButton();
            multiCombatButton = new javax.swing.JRadioButton();
            jLabel41 = new javax.swing.JLabel();
            altarIDField = new javax.swing.JTextField();
            jPanel3 = new javax.swing.JPanel();
            jLabel7 = new javax.swing.JLabel();
            list1 = new java.awt.List();
            jLabel8 = new javax.swing.JLabel();
            list3 = new java.awt.List();
            jButton4 = new javax.swing.JButton();
            jLabel32 = new javax.swing.JLabel();
            jLabel33 = new javax.swing.JLabel();
            jPanel7 = new javax.swing.JPanel();
            jButton3 = new javax.swing.JButton();
            jSeparator4 = new javax.swing.JSeparator();
            enablePaintButton = new javax.swing.JRadioButton();
            hoverMouseButton = new javax.swing.JRadioButton();
            antibanButton = new javax.swing.JRadioButton();
            charmsButton = new javax.swing.JRadioButton();
            jLabel2 = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Ownageful Fighter");
            setBackground(new java.awt.Color(0, 0, 0));
            setForeground(new java.awt.Color(0, 0, 0));
            setResizable(false);

            jPanel1.setBackground(new java.awt.Color(0, 0, 0));

            jLabel1.setFont(new java.awt.Font("Fat", 1, 24));
            jLabel1.setForeground(new java.awt.Color(204, 0, 0));
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Ownageful Fighter");

            jSeparator1.setBackground(new java.awt.Color(204, 0, 0));
            jSeparator1.setForeground(new java.awt.Color(204, 0, 0));

            jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
            jTabbedPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

            jPanel2.setBackground(new java.awt.Color(0, 0, 0));
            jPanel2.setEnabled(false);
            jPanel2.setPreferredSize(new java.awt.Dimension(506, 217));

            jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel3.setForeground(new java.awt.Color(204, 0, 0));
            jLabel3.setText("Centeral Location: ");

            jLabel4.setForeground(new java.awt.Color(204, 0, 0));
            jLabel4.setText("Tile x ,Tile y");

            jButton1.setBackground(new java.awt.Color(0, 0, 0));
            jButton1.setText("Set Current Tile");
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel5.setForeground(new java.awt.Color(204, 0, 0));
            jLabel5.setText("Fight Distance:");

            jSpinner1.addAncestorListener(new javax.swing.event.AncestorListener() {

                public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                }

                public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                    jSpinner1AncestorAdded(evt);
                }

                public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                }

                private void jSpinner1AncestorAdded(AncestorEvent evt) {
                }
            });

            jLabel6.setForeground(new java.awt.Color(204, 0, 0));
            jLabel6.setText("tiles");

            enableBreaksCheck.setBackground(new java.awt.Color(0, 0, 0));
            enableBreaksCheck.setForeground(new java.awt.Color(255, 0, 0));
            enableBreaksCheck.setText("Enable Breaks?");
            enableBreaksCheck.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    enableBreaksCheckActionPerformed(evt);
                }
            });

            jLabel19.setForeground(new java.awt.Color(204, 51, 0));
            jLabel19.setText("Take Breaks after:");

            jLabel20.setForeground(new java.awt.Color(204, 51, 0));
            jLabel20.setText("For a period of:");

            jLabel21.setForeground(new java.awt.Color(204, 51, 0));
            jLabel21.setText("will fight monsters around a radius of:");

            breaker1Field.setEnabled(false);

            period1Field.setEnabled(false);

            breaker2Field.setEnabled(false);

            period2Field.setEnabled(false);

            jLabel22.setForeground(new java.awt.Color(204, 0, 0));
            jLabel22.setText("to");

            jLabel23.setForeground(new java.awt.Color(204, 0, 0));
            jLabel23.setText("to");

            jLabel24.setForeground(new java.awt.Color(204, 0, 0));
            jLabel24.setText("minutes.");

            jLabel25.setForeground(new java.awt.Color(204, 0, 0));
            jLabel25.setText("minutes.");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(enableBreaksCheck).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4))).addGap(38, 38, 38).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel21).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))).addContainerGap()).addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addGap(84, 84, 84).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(50, 50, 50).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(49, 49, 49).addComponent(breaker1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(49, 49, 49).addComponent(period1Field, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(32, 32, 32).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel22).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel23).addGap(32, 32, 32).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(period2Field).addComponent(breaker2Field, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel24)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel25)))))).addComponent(jLabel20).addComponent(jLabel19)).addContainerGap(118, Short.MAX_VALUE)));
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(enableBreaksCheck).addGap(18, 18, 18).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(30, 30, 30).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(breaker1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel20).addComponent(period1Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(breaker2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel24)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(period2Field, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel25)))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel22).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel23))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jLabel21)).addContainerGap()));

            jTabbedPane1.addTab("General    ", jPanel2);

            jPanel5.setBackground(new java.awt.Color(0, 0, 0));
            jPanel5.setPreferredSize(new java.awt.Dimension(506, 235));

            jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel10.setForeground(new java.awt.Color(204, 0, 0));
            jLabel10.setText("Enable Food:");

            jCheckBox1.setBackground(new java.awt.Color(0, 0, 0));
            jCheckBox1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox1ActionPerformed(evt);
                }
            });

            jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel11.setForeground(new java.awt.Color(204, 0, 0));
            jLabel11.setText("Enter Food ID (seperate with commas) :");

            jTextField2.setBackground(new java.awt.Color(204, 0, 0));
            jTextField2.setText(" ");
            jTextField2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField2ActionPerformed(evt);
                }

                private void jTextField2ActionPerformed(ActionEvent evt) {

                }
            });

            jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel12.setForeground(new java.awt.Color(204, 0, 0));
            jLabel12.setText("Eat at:");

            jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel13.setForeground(new java.awt.Color(204, 0, 0));
            jLabel13.setText("%.");

            jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel15.setForeground(new java.awt.Color(204, 0, 0));
            jLabel15.setText("Enable BTP");

            jCheckBox2.setBackground(new java.awt.Color(0, 0, 0));
            jCheckBox2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jCheckBox2ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
            jPanel5.setLayout(jPanel5Layout);
            jPanel5Layout.setHorizontalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jCheckBox2).addComponent(jCheckBox1)).addGap(6, 6, 6).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel15).addComponent(jLabel10))).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(29, 29, 29).addComponent(jLabel12).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel13))).addContainerGap(44, Short.MAX_VALUE)));
            jPanel5Layout.setVerticalGroup(
                    jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addContainerGap().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addComponent(jLabel10).addGap(15, 15, 15).addComponent(jLabel15)).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jCheckBox1).addGap(3, 3, 3).addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGap(33, 33, 33).addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel13).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addGap(221, 221, 221)));

            jTabbedPane1.addTab("Eating ", jPanel5);

            jPanel4.setBackground(new java.awt.Color(0, 0, 0));
            jPanel4.setPreferredSize(new java.awt.Dimension(506, 235));

            jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel9.setForeground(new java.awt.Color(204, 0, 0));
            jLabel9.setText("Enter Loot ID & name");

            jTextField1.setBackground(new java.awt.Color(204, 0, 0));
            jTextField1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField1ActionPerformed(evt);
                }
            });

            jButton2.setBackground(new java.awt.Color(0, 0, 0));
            jButton2.setText("Add Loot");
            jButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            list4.setBackground(new java.awt.Color(204, 0, 0));
            list4.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    list4ActionPerformed(evt);
                }
            });

            jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel16.setForeground(new java.awt.Color(204, 0, 0));
            jLabel16.setText("(ID,name)");

            jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel17.setForeground(new java.awt.Color(204, 0, 0));
            jLabel17.setText("Added Loot (double click to remove)");

            jLabel31.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel31.setForeground(new java.awt.Color(204, 0, 0));
            jLabel31.setText("Ex: (995,Coins)");

            jButton5.setBackground(new java.awt.Color(0, 0, 0));
            jButton5.setText("Save");
            jButton5.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5ActionPerformed(evt);
                }
            });

            jButton7.setBackground(new java.awt.Color(0, 0, 0));
            jButton7.setText("Load");
            jButton7.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton7ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
            jPanel4.setLayout(jPanel4Layout);
            jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel16).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9).addComponent(jLabel31).addComponent(jButton2).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jButton7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton5))).addGap(144, 144, 144).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel17).addGroup(jPanel4Layout.createSequentialGroup().addGap(30, 30, 30).addComponent(list4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
            jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel31).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton2).addGap(40, 40, 40).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton7).addComponent(jButton5))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addGap(16, 16, 16).addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(list4, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE))).addContainerGap()));

            jTabbedPane1.addTab("Looting ", jPanel4);

            jPanel8.setBackground(new java.awt.Color(0, 0, 0));
            jPanel8.setPreferredSize(new java.awt.Dimension(506, 235));

            jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel26.setForeground(new java.awt.Color(204, 0, 0));
            jLabel26.setText("Items currently looting:");

            alchList.setBackground(new java.awt.Color(204, 0, 0));
            alchList.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    alchListActionPerformed(evt);
                }
            });

            jLabel27.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel27.setForeground(new java.awt.Color(204, 0, 0));
            jLabel27.setText("Items to alch, double click to remove.");

            toAlchTable.setBackground(new java.awt.Color(204, 0, 0));
            toAlchTable.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    toAlchTableActionPerformed(evt);
                }
            });

            jLabel28.setForeground(new java.awt.Color(204, 0, 51));
            jLabel28.setText("------->");

            jLabel29.setForeground(new java.awt.Color(204, 0, 51));
            jLabel29.setText("Double click");

            jLabel30.setForeground(new java.awt.Color(204, 0, 51));
            jLabel30.setText("to add.");

            javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
            jPanel8.setLayout(jPanel8Layout);
            jPanel8Layout.setHorizontalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup().addContainerGap(242, Short.MAX_VALUE).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel8Layout.createSequentialGroup().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel29).addGroup(jPanel8Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel30))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup().addComponent(jLabel28).addGap(10, 10, 10))).addGap(46, 46, 46).addComponent(toAlchTable, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel27)).addContainerGap()).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel26).addComponent(alchList, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(348, Short.MAX_VALUE))));
            jPanel8Layout.setVerticalGroup(
                    jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addComponent(jLabel27).addGap(1, 1, 1).addComponent(toAlchTable, 0, 0, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup().addComponent(jLabel28).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel29).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel30).addGap(49, 49, 49)))).addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(jLabel26).addGap(2, 2, 2).addComponent(alchList, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE).addGap(11, 11, 11))));

            jTabbedPane1.addTab("Alching", jPanel8);

            jPanel10.setBackground(new java.awt.Color(0, 0, 0));

            jLabel46.setForeground(new java.awt.Color(255, 0, 0));
            jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel46.setText("To Be Added.");

            javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
            jPanel10.setLayout(jPanel10Layout);
            jPanel10Layout.setHorizontalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE).addContainerGap()));
            jPanel10Layout.setVerticalGroup(
                    jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel10Layout.createSequentialGroup().addContainerGap().addComponent(jLabel46).addContainerGap(251, Short.MAX_VALUE)));

            jTabbedPane1.addTab("Summoning", jPanel10);

            jPanel9.setBackground(new java.awt.Color(0, 0, 0));

            jLabel47.setForeground(new java.awt.Color(255, 0, 0));
            jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel47.setText("To Be Added.");


            javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
            jPanel9.setLayout(jPanel9Layout);
            jPanel9Layout.setHorizontalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 529, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(jLabel47, javax.swing.GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE).addContainerGap()));
            jPanel9Layout.setVerticalGroup(
                    jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 276, Short.MAX_VALUE).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addComponent(jLabel47).addContainerGap(251, Short.MAX_VALUE)));

            jTabbedPane1.addTab("Banking", jPanel9);

            jPanel6.setBackground(new java.awt.Color(0, 0, 0));
            jPanel6.setPreferredSize(new java.awt.Dimension(506, 235));

            jRadioButton1.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton1.setFont(new java.awt.Font("Tahoma", 1, 11));
            jRadioButton1.setForeground(new java.awt.Color(204, 0, 0));
            jRadioButton1.setText("Pick and Bury Bones:");

            jRadioButton2.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton2.setFont(new java.awt.Font("Tahoma", 1, 11));
            jRadioButton2.setForeground(new java.awt.Color(204, 0, 0));
            jRadioButton2.setText("Drink Pots");
            jRadioButton3.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton3.setFont(new java.awt.Font("Tahoma", 1, 11));
            jRadioButton3.setForeground(new java.awt.Color(204, 0, 0));
            jRadioButton3.setText("Special Attack");

            jRadioButton4.setBackground(new java.awt.Color(0, 0, 0));
            jRadioButton4.setFont(new java.awt.Font("Tahoma", 1, 11));
            jRadioButton4.setForeground(new java.awt.Color(204, 0, 0));
            jRadioButton4.setText("Loot Arrows:");

            jLabel14.setFont(new java.awt.Font("Tahoma", 0, 10));
            jLabel14.setForeground(new java.awt.Color(204, 0, 0));
            jLabel14.setText("Enter Arrow ID:");

            jTextField3.setBackground(new java.awt.Color(204, 0, 0));
            jTextField3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTextField3ActionPerformed(evt);
                }

                private void jTextField3ActionPerformed(ActionEvent evt) {

                }
            });

            prayer.setBackground(new java.awt.Color(0, 0, 0));
            prayer.setFont(new java.awt.Font("Tahoma", 1, 11));
            prayer.setForeground(new java.awt.Color(204, 0, 0));
            prayer.setText("Use quick prayers");
            prayer.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    prayerActionPerformed(evt);
                }

                private void prayerActionPerformed(ActionEvent evt) {

                }
            });

            prayer1.setBackground(new java.awt.Color(0, 0, 0));
            prayer1.setFont(new java.awt.Font("Tahoma", 1, 11));
            prayer1.setForeground(new java.awt.Color(204, 0, 0));
            prayer1.setText("Drink Prayer pots");
            prayer1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    prayer1ActionPerformed(evt);
                }

                private void prayer1ActionPerformed(ActionEvent evt) {

                }
            });

            prayer2.setBackground(new java.awt.Color(0, 0, 0));
            prayer2.setFont(new java.awt.Font("Tahoma", 1, 11));
            prayer2.setForeground(new java.awt.Color(204, 0, 0));
            prayer2.setText("Recharge Prayer at nearby Altar");

            multiCombatButton.setBackground(new java.awt.Color(0, 0, 0));
            multiCombatButton.setFont(new java.awt.Font("Tahoma", 1, 11));
            multiCombatButton.setForeground(new java.awt.Color(204, 0, 0));
            multiCombatButton.setText("Multi-Combat Area");
            multiCombatButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    multiCombatButtonActionPerformed(evt);
                }

                private void multiCombatButtonActionPerformed(ActionEvent evt) {

                }
            });

            jLabel41.setFont(new java.awt.Font("Tahoma", 0, 10));
            jLabel41.setForeground(new java.awt.Color(204, 0, 0));
            jLabel41.setText("Enter Altar ID:");

            altarIDField.setBackground(new java.awt.Color(204, 0, 0));
            altarIDField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    altarIDFieldActionPerformed(evt);
                }

                private void altarIDFieldActionPerformed(ActionEvent evt) {

                }
            });

            javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
            jPanel6.setLayout(jPanel6Layout);
            jPanel6Layout.setHorizontalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton1).addComponent(jRadioButton2).addComponent(prayer)).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addGap(99, 99, 99).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jRadioButton4).addGroup(jPanel6Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel14).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(jPanel6Layout.createSequentialGroup().addGap(110, 110, 110).addComponent(jRadioButton3)))).addComponent(prayer1).addComponent(prayer2))).addGroup(jPanel6Layout.createSequentialGroup().addGap(35, 35, 35).addComponent(jLabel41).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(altarIDField, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(multiCombatButton))).addContainerGap(93, Short.MAX_VALUE)));
            jPanel6Layout.setVerticalGroup(
                    jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jRadioButton4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel6Layout.createSequentialGroup().addComponent(jRadioButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jRadioButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(multiCombatButton).addGap(43, 43, 43).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(prayer).addComponent(jRadioButton3)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(prayer1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(prayer2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel41).addComponent(altarIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(55, Short.MAX_VALUE)));

            jTabbedPane1.addTab("Misc Settings   ", jPanel6);

            jPanel3.setBackground(new java.awt.Color(0, 0, 0));
            jPanel3.setPreferredSize(new java.awt.Dimension(506, 235));

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel7.setForeground(new java.awt.Color(204, 0, 0));
            jLabel7.setText("Nearby Monsters:");

            list1.setBackground(new java.awt.Color(204, 0, 0));
            list1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    list1ActionPerformed(evt);
                }
            });

            jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel8.setForeground(new java.awt.Color(204, 0, 0));
            jLabel8.setText("Selected Monsters:");

            list3.setBackground(new java.awt.Color(204, 0, 0));
            list3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    list3ActionPerformed(evt);
                }
            });

            jButton4.setBackground(new java.awt.Color(0, 0, 0));
            jButton4.setText("Refresh");
            jButton4.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton4ActionPerformed(evt);
                }
            });

            jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel32.setForeground(new java.awt.Color(204, 0, 0));
            jLabel32.setText("- double click to add to list");

            jLabel33.setFont(new java.awt.Font("Tahoma", 1, 11));
            jLabel33.setForeground(new java.awt.Color(204, 0, 0));
            jLabel33.setText("- double click to remove from list");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel7).addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(list1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(29, 29, 29).addComponent(jButton4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(18, 18, 18).addComponent(jLabel33)).addGroup(jPanel3Layout.createSequentialGroup().addGap(25, 25, 25).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(list3, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE).addComponent(jLabel8)))).addGap(52, 52, 52)));
            jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jLabel8)).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton4).addComponent(jLabel33)).addComponent(jLabel32)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(list3, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE).addComponent(list1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)).addContainerGap()));

            jTabbedPane1.addTab("Monsters", jPanel3);

            jPanel7.setBackground(new java.awt.Color(0, 0, 0));
            jPanel7.setPreferredSize(new java.awt.Dimension(506, 235));

            jButton3.setText("Start!");
            jButton3.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            enablePaintButton.setBackground(new java.awt.Color(0, 0, 0));
            enablePaintButton.setFont(new java.awt.Font("Tahoma", 1, 11));
            enablePaintButton.setForeground(new java.awt.Color(204, 0, 0));
            enablePaintButton.setText("Enable Paint?");
            enablePaintButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    enablePaintButtonActionPerformed(evt);
                }

                private void enablePaintButtonActionPerformed(ActionEvent evt) {

                }
            });

            hoverMouseButton.setBackground(new java.awt.Color(0, 0, 0));
            hoverMouseButton.setFont(new java.awt.Font("Tahoma", 1, 11));
            hoverMouseButton.setForeground(new java.awt.Color(204, 0, 0));
            hoverMouseButton.setText("Mouse hovering?");
            hoverMouseButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    hoverMouseButtonActionPerformed(evt);
                }

                private void hoverMouseButtonActionPerformed(ActionEvent evt) {

                }
            });

            antibanButton.setBackground(new java.awt.Color(0, 0, 0));
            antibanButton.setFont(new java.awt.Font("Tahoma", 1, 11));
            antibanButton.setForeground(new java.awt.Color(204, 0, 0));
            antibanButton.setText("Antiban?");
            antibanButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    antibanButtonActionPerformed(evt);
                }

                private void antibanButtonActionPerformed(ActionEvent evt) {

                }
            });

            charmsButton.setBackground(new java.awt.Color(0, 0, 0));
            charmsButton.setFont(new java.awt.Font("Tahoma", 1, 11));
            charmsButton.setForeground(new java.awt.Color(204, 0, 0));
            charmsButton.setText("Loot Charms?");
            charmsButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    charmsButtonActionPerformed(evt);
                }

                private void charmsButtonActionPerformed(ActionEvent evt) {

                }
            });

            javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
            jPanel7.setLayout(jPanel7Layout);
            jPanel7Layout.setHorizontalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel7Layout.createSequentialGroup().addGap(219, 219, 219).addComponent(jButton3)).addGroup(jPanel7Layout.createSequentialGroup().addGap(201, 201, 201).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(hoverMouseButton).addComponent(enablePaintButton).addComponent(antibanButton).addComponent(charmsButton)))).addContainerGap(44, Short.MAX_VALUE)));
            jPanel7Layout.setVerticalGroup(
                    jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(enablePaintButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(hoverMouseButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(antibanButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(charmsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3).addGap(101, 101, 101)));

            jTabbedPane1.addTab("Finalize Script  ", jPanel7);

            jLabel2.setBackground(new java.awt.Color(255, 0, 0));
            jLabel2.setFont(new java.awt.Font("Tahoma", 1, 10));
            jLabel2.setForeground(new java.awt.Color(255, 0, 0));
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel2.setText("AIO Fighter                                                                                    Version : 1.00");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE).addContainerGap()).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE).addContainerGap()));
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)));

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

            SpinnerModel sm1 = new SpinnerNumberModel(15, 3, 25, 1);
            SpinnerModel sm2 = new SpinnerNumberModel(50, 20, 99, 1);
            jSpinner1.setModel(sm1);
            jSpinner2.setModel(sm2);
            jSpinner2.setEnabled(true);
            jTextField2.setEnabled(false);
            enablePaintButton.setSelected(true);
            charmsButton.setSelected(true);
            hoverMouseButton.setSelected(true);
            antibanButton.setSelected(true);
            pack();
        }

        private void alchListActionPerformed(java.awt.event.ActionEvent evt) {
            alchAdded = false;
            if (toAlchTable.getItemCount() == 0) {
                alchzList.add(Integer.parseInt(alchList.getItem(alchList.getSelectedIndex()).substring(0, alchList.getItem(alchList.getSelectedIndex()).indexOf(':'))));
                toAlchTable.add(alchList.getItem(alchList.getSelectedIndex()).toString());
                alchAdded = true;
            } else {
                alchAdded = false;
                for (int i = 0; i < toAlchTable.getItemCount(); i++) {
                    if (alchList.getItem(alchList.getSelectedIndex()).toString().equals(toAlchTable.getItem(i).toString())) {
                        alchAdded = true;
                    }
                }
            }

            if (alchAdded == false) {
                alchzList.add(Integer.parseInt(alchList.getItem(alchList.getSelectedIndex()).substring(0, alchList.getItem(alchList.getSelectedIndex()).indexOf(':'))));
                toAlchTable.add(alchList.getItem(alchList.getSelectedIndex()).toString());
            }
        }

        private void toAlchTableActionPerformed(java.awt.event.ActionEvent evt) {
            alchzList.remove(toAlchTable.getSelectedIndex());
            toAlchTable.remove(toAlchTable.getSelectedIndex());
        }

        private void enableBreaksCheckActionPerformed(java.awt.event.ActionEvent evt) {
            if (enableBreaksCheck.isSelected()) {
                takingBreaks = true;
                breaker1Field.setEnabled(true);
                breaker2Field.setEnabled(true);
                period1Field.setEnabled(true);
                period2Field.setEnabled(true);
            } else {
                takingBreaks = false;
                breaker1Field.setEnabled(false);
                breaker2Field.setEnabled(false);
                period1Field.setEnabled(false);
                period2Field.setEnabled(false);
            }
        }

        private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
            ignoreTable.removeAll();
            withdrawTable.removeAll();
        }

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            centralTile = getMyPlayer().getLocation();
            jLabel4.setText("" + centralTile.getX() + "," + "" + centralTile.getY());
        }

        private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
            //Assign Misc Settings Tab variables
            if (jRadioButton1.isSelected()) {
                //Set pickup and bury bones.
                bones = true;
            }
            if (jRadioButton2.isSelected()) {
                //Set pots.
                pots = true;
            }
            if (jRadioButton3.isSelected()) {
                //Set spec.
                spec = true;
            }
            if (jRadioButton4.isSelected()) {
                //Set arrow pickup.
                arrowPickup = true;
                try {
                    arrowID = Integer.parseInt(jTextField3.getText().substring(0, jTextField3.getText().indexOf(',')));
                    arrowName = jTextField3.getText().substring(jTextField3.getText().indexOf(',') + 1);
                    validArrow = true;
                } catch (NumberFormatException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "Please fix arrow pickup ID in misc tab.");
                    foods.clear();
                    validArrow = false;
                } catch (StringIndexOutOfBoundsException e) {
                    foods.clear();
                    validArrow = false;
                    JOptionPane.showMessageDialog(null, "Please use Proper format (arrowID,Name).");
                }
            }

            //Check if looting
            if (lootList.size() == 0) {
                isLooting = false;
            } else {
                isLooting = true;
            }
            //Check for tile set.
            if (centralTile == null) {
                tileSet = false;
                JOptionPane.showMessageDialog(null, "Please select the central Tile from the General Tab.");
                foods.clear();
            } else {
                tileSet = true;
            }
            //Check for food IDs valid?
            if (isEating) {
                //150,120,130
                try {
                    foodString = jTextField2.getText();
                    while (!foodString.equals("")) {
                        if (foodString.contains(",")) {
                            int commaPoint = foodString.indexOf(",");
                            //3
                            foods.add(Integer.parseInt(foodString.substring(0, commaPoint)));
                            foodString = foodString.substring(commaPoint + 1);
                        } else {
                            //250
                            foods.add(Integer.parseInt(foodString));
                            foodString = "";
                        }
                    }
                    foodValid = true;
                } catch (NumberFormatException numberFormatException) {
                    JOptionPane.showMessageDialog(null, "Error with food IDs, fix it.)");
                    foodValid = false;
                    foods.clear();
                }
            } else {
                foodValid = true;
            }

            //Check if alching
            if (toAlchTable.getItemCount() > 0) {
                isAlching = true;
                for (int i = 0; i < alchzList.size(); i++) {
                    log("Alching: " + alchzList.get(i));
                }
            } else {
                isAlching = false;
            }

            //Check if praying.
            if (prayer.isSelected()) {
                usingQuickPrayer = true;
            } else {
                usingQuickPrayer = false;
            }

            //Check if during pray pots.
            if (prayer1.isSelected()) {
                usingPrayerPots = true;
            } else {
                usingPrayerPots = false;
            }

            //Check if praying at altar.
            if (prayer2.isSelected()) {
                altar = true;
                try {
                    altarr = objects.getNearest(Integer.parseInt(altarIDField.getText()));
                } catch (NumberFormatException numberFormatException) {
                    log("Invalid altar ID");
                }
            } else {
                altar = false;
            }

            //Check if breaking.
            if (enableBreaksCheck.isSelected()) {
                breaking = true;
                try {
                    ba1 = Integer.parseInt(breaker1Field.getText());
                    ba2 = Integer.parseInt(breaker2Field.getText());
                    sba1 = ba1 * 60 * 1000;
                    sba2 = ba2 * 60 * 1000;
                    pa1 = Integer.parseInt(period1Field.getText());
                    pa2 = Integer.parseInt(period2Field.getText());
                    spa1 = pa1 * 60 * 1000;
                    spa2 = pa2 * 60 * 1000;
                } catch (NumberFormatException numberFormatException) {
                    log("Invalid break times");
                }
            } else {
                breaking = false;
            }

            // Paint
            if (enablePaintButton.isSelected()) {
                paint = true;
            } else {
                paint = false;
            }

            //Hover
            if (hoverMouseButton.isSelected()) {
                hover = true;
            } else {
                hover = false;
            }

            //Antiban
            if (antibanButton.isSelected()) {
                antiban = true;
            } else {
                antiban = false;
            }

            if (charmsButton.isSelected()) {
                log("Looting charms");
                lCharms = true;
            } else {
                lCharms = false;
            }

            //mcb
            if (multiCombatButton.isSelected()) {
                mca = true;
            } else {
                mca = false;
            }
            //Check for all validations
            if (tileSet && foodValid && ((arrowPickup == true && validArrow == true) || arrowPickup == false)) {
                fightDistance = Integer.parseInt(jSpinner1.getValue().toString());
                log("Setting fight Distance : " + fightDistance);
                healAT = Integer.parseInt(jSpinner2.getValue().toString());
                log("Will heal around : " + healAT + " % hp, if using food.");
                arrayConversion();
                for (int i = 0; i < names.size(); i++) {
                    log(names.get(i));
                }
//
//                if(nSave){
//                     JOptionPane.showMessageDialog(null, "Please give the profile a name.");
//                     JFileChooser fc = new JFileChooser();
//            int retVal;
//            boolean exists;
//            try {
//                File f = new File(new File(".txt").getCanonicalPath());
//                fc.setSelectedFile(f);
//                retVal = fc.showSaveDialog(null);
//                if (retVal == JFileChooser.APPROVE_OPTION) {
//                    //get the currently selected file
//                    File thefile = fc.getSelectedFile();
//                    String nameOfFile = "profile";
//                    nameOfFile = thefile.getPath();
//                    //check if the file exists
//                    File existFile = new File(nameOfFile);
//                    exists = existFile.exists();
//                    if (!exists) {
//                        existFile.createNewFile();
//                    }
//                    BufferedWriter out = new BufferedWriter(new FileWriter(existFile));
//                    out.write();
//                    out.close();
//                }
//            } catch (IOException e) {
//                log("Error loading file");
//            }
//
//                }


                this.dispose();
                startScript = true;
            }
        }

        private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
            npcMap = npcs.getAll();
            npcList.clear();
            filteredList.clear();
            list1.removeAll();
            list3.removeAll();

            for (int i = 0; i < npcMap.length; i++) {
                log(npcMap[i].getName());
                if (npcMap[i] != null) {
                    if (npcList.isEmpty()) {
                        if (npcMap[i].getLevel() != 0) {
                            npcList.add(npcMap[i]);
                        }
                    } else {
                        npcAdded = false;
                        for (int j = 0; j < npcList.size(); j++) {
                            if (npcList.get(j).getID() == npcMap[i].getID()) {
                                npcAdded = true;
                            }
                        }
                        if (npcAdded == false) {
                            if (npcMap[i].getLevel() != 0) {
                                npcList.add(npcMap[i]);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < npcList.size(); i++) {
                list1.add(npcList.get(i).getName() + ": " + npcList.get(i).getID());
            }
        }

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
            lootAdded = false;
            try {
                lootToAdd = Integer.parseInt(jTextField1.getText().substring(0, jTextField1.getText().indexOf(',')));
                if (list4.getItemCount() == 0) {
                    lootList.add(lootToAdd);
                    names.add(jTextField1.getText().substring(jTextField1.getText().indexOf(',') + 1));
                    list4.add(lootList.get(0).toString() + ":" + names.get(0));
                    alchList.add(lootList.get(0).toString() + ":" + names.get(0));
                    lootAdded = true;
                } else {
                    for (int i = 0; i < lootList.size(); i++) {
                        if (lootToAdd == lootList.get(i)) {
                            lootAdded = true;
                        }
                    }
                }

                if (lootAdded == false && lootToAdd != -1) {
                    lootList.add(lootToAdd);
                    names.add(jTextField1.getText().substring(jTextField1.getText().indexOf(',') + 1));
                }
                alchList.removeAll();
                list4.removeAll();
                for (int i = 0; i < lootList.size(); i++) {
                    list4.add(lootList.get(i).toString() + ":" + names.get(i));
                    alchList.add(lootList.get(i).toString() + ":" + names.get(i));
                }
                jTextField1.setText("");
            } catch (NumberFormatException numberFormatException) {
                jTextField1.setText("");
                lootToAdd = -1;
                JOptionPane.showMessageDialog(null, "Please use Proper format (ID,Name).");
            } catch (StringIndexOutOfBoundsException e) {
                jTextField1.setText("");
                lootToAdd = -1;
                JOptionPane.showMessageDialog(null, "Please use Proper format (ID,Name).");
            }
        }

        private void list1ActionPerformed(java.awt.event.ActionEvent evt) {
            npcAddedList2 = false;
            if (list3.getItemCount() == 0) {
                filteredList.add(Integer.parseInt(list1.getItem(list1.getSelectedIndex()).substring(list1.getItem(list1.getSelectedIndex()).indexOf(":") + 2)));
                list3.add(list1.getItem(list1.getSelectedIndex()).toString());
                npcAddedList2 = true;
            } else {
                npcAddedList2 = false;
                for (int i = 0; i < list3.getItemCount(); i++) {
                    if (list1.getItem(list1.getSelectedIndex()).toString().equals(list3.getItem(i).toString())) {
                        npcAddedList2 = true;
                    }
                }
            }

            if (npcAddedList2 == false) {
                filteredList.add(Integer.parseInt(list1.getItem(list1.getSelectedIndex()).substring(list1.getItem(list1.getSelectedIndex()).indexOf(":") + 2)));
                list3.add(list1.getItem(list1.getSelectedIndex()).toString());
            }
        }

        private void list3ActionPerformed(java.awt.event.ActionEvent evt) {
            filteredList.remove(list3.getSelectedIndex());
            list3.remove(list3.getSelectedIndex());
        }

        private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
            jTextField1.setText("");
        }

        private void list4ActionPerformed(java.awt.event.ActionEvent evt) {
            lootList.remove(list4.getSelectedIndex());
            names.remove(list4.getSelectedIndex());
            alchList.remove(list4.getSelectedIndex());
            list4.remove(list4.getSelectedIndex());
        }

        //Eat food checkbox
        private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
            if (jCheckBox1.isSelected()) {
                jSpinner2.setEnabled(true);
                jTextField2.setEnabled(true);
                jTextField2.setText("");
                isEating = true;
            }
            if ((jCheckBox1.isSelected() == false)) {
                jSpinner2.setEnabled(false);
                jTextField2.setEnabled(false);
                isEating = false;
            }
        }

        private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
            JFileChooser fc = new JFileChooser();
            int retVal;
            boolean exists;
            try {
                File f = new File(new File(".txt").getCanonicalPath());
                fc.setSelectedFile(f);
                retVal = fc.showSaveDialog(null);
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    //get the currently selected file
                    File thefile = fc.getSelectedFile();
                    String nameOfFile = "";
                    nameOfFile = thefile.getPath();
                    //check if the file exists
                    File existFile = new File(nameOfFile);
                    exists = existFile.exists();
                    if (!exists) {
                        existFile.createNewFile();
                    }
                    BufferedWriter out = new BufferedWriter(new FileWriter(existFile));
                    for (int i = 0; i < list4.getItemCount(); i++) {
                        log("Loot: " + list4.getItem(i));
                        if (i + 1 < list4.getItemCount()) {
                            out.write(list4.getItem(i));
                            out.newLine();
                        } else {
                            out.write(list4.getItem(i));
                        }
                    }
                    out.close();
                }
            } catch (IOException e) {
                log("Error loading file");
            }
        }

        private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
            log("Opening j file chooser");
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = chooser.showOpenDialog(null);
            log("result: " + result);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = chooser.getSelectedFile().getPath();
                File fc = new File(path);
                log("Reading file: " + fc.getPath());
                if (fc.canRead()) {
                    list4.removeAll();
                    lootList.clear();
                    names.clear();
                    alchList.removeAll();
                    readLootFile(fc);
                } else {
                    JOptionPane.showMessageDialog(null, "Error.");
                }
            }
        }

        //BTP checkbox.
        private void jCheckBox2ActionPerformed(ActionEvent evt) {
            if (jCheckBox2.isSelected()) {
                isBTP = true;
                jRadioButton1.setSelected(false);
                jRadioButton1.setEnabled(false);
            } else if (!jCheckBox2.isSelected()) {
                isBTP = false;
                jRadioButton1.setEnabled(true);
            }
        }
        // Variables declaration - do not modify
        private javax.swing.JButton addIgnoredItem;
        private javax.swing.JButton addWithdrawItem;
        private java.awt.List alchList;
        private javax.swing.JTextField altarIDField;
        private javax.swing.JRadioButton antibanButton;
        private javax.swing.JTextField breaker1Field;
        private javax.swing.JTextField breaker2Field;
        private javax.swing.JCheckBox enableBreaksCheck;
        private javax.swing.JRadioButton enablePaintButton;
        private javax.swing.JRadioButton charmsButton;
        private javax.swing.JRadioButton hoverMouseButton;
        private javax.swing.JTextField ignoreItemsField;
        private java.awt.List ignoreTable;
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton2;
        private javax.swing.JButton jButton3;
        private javax.swing.JButton jButton4;
        private javax.swing.JButton jButton5;
        private javax.swing.JButton jButton6;
        private javax.swing.JButton jButton7;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox2;
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
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel6;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JPanel jPanel9;
        private javax.swing.JRadioButton jRadioButton1;
        private javax.swing.JRadioButton jRadioButton2;
        private javax.swing.JRadioButton jRadioButton3;
        private javax.swing.JRadioButton jRadioButton4;
        private javax.swing.JSeparator jSeparator1;
        private javax.swing.JSeparator jSeparator2;
        private javax.swing.JSeparator jSeparator3;
        private javax.swing.JSeparator jSeparator4;
        private javax.swing.JSeparator jSeparator5;
        private javax.swing.JSpinner jSpinner1;
        private javax.swing.JSpinner jSpinner2;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTextField jTextField3;
        private java.awt.List list1;
        private java.awt.List list3;
        private java.awt.List list4;
        private javax.swing.JRadioButton multiCombatButton;
        private javax.swing.JTextField period1Field;
        private javax.swing.JTextField period2Field;
        private javax.swing.JRadioButton prayer;
        private javax.swing.JRadioButton prayer1;
        private javax.swing.JRadioButton prayer2;
        private java.awt.List toAlchTable;
        private javax.swing.JTextField withdrawItemsField;
        private java.awt.List withdrawTable;
        // End of variables declaration

        private void arrayConversion() {
            foodz = new int[foods.size()];
            for (int i = 0; i < foodz.length; i++) {
                foodz[i] = foods.get(i);
                allInventoryClean.add(foods.get(i));
            }

            lootz = new int[lootList.size()];

            for (int i = 0; i < lootz.length; i++) {
                lootz[i] = lootList.get(i);
                allInventoryClean.add(lootList.get(i));
            }
            npcz = new int[filteredList.size()];
            for (int i = 0; i < npcz.length; i++) {
                npcz[i] = filteredList.get(i);
            }
            alchItemz = new int[alchzList.size()];
            for (int i = 0; i < alchItemz.length; i++) {
                alchItemz[i] = alchzList.get(i);
            }

            int totalSize = lootz.length + foodz.length;
            if (bones) {
                totalSize = totalSize + allBones.length;
                for (int i = 0; i < allBones.length; i++) {
                    allInventoryClean.add(allBones[i]);
                }
            }
            if (isBTP) {
                totalSize = totalSize + btpBones.length;
                totalSize = totalSize + 2;
                for (int i = 0; i < btpBones.length; i++) {
                    allInventoryClean.add(btpBones[i]);
                }
                allInventoryClean.add(btp);
                allInventoryClean.add(peach);
            }

            if (pots) {
                totalSize = totalSize + potsIDS.length;
                for (int i = 0; i < potsIDS.length; i++) {
                    allInventoryClean.add(potsIDS[i]);
                }
            }



            if (usingPrayerPots) {
                totalSize = totalSize + pPots.length;
                for (int i = 0; i < pPots.length; i++) {
                    allInventoryClean.add(pPots[i]);
                }
            }

            if (lCharms) {
                totalSize = totalSize + charms.length;
                log("Adding charms to clean inventory");
                for (int i = 0; i < charms.length; i++) {
                    log("Added: " + charms[i]);
                    allInventoryClean.add(charms[i]);
                }
            }

            if (arrowPickup) {
                totalSize = totalSize + 1;
                allInventoryClean.add(arrowID);
            }

            if (isAlching) {
                totalSize = totalSize + 3;
                allInventoryClean.add(561); //nature rune for alch
                allInventoryClean.add(554); // fire rune for alch
                allInventoryClean.add(995); // coins for alch
            }

            inventoryClean = new int[totalSize];
            for (int i = 0; i < inventoryClean.length; i++) {
                inventoryClean[i] = allInventoryClean.get(i);
            }

            prices = new int[lootz.length];
            JOptionPane.showMessageDialog(null, "The script will now take a while to load Grand Exchange prices");
            for (int i = 0; i < prices.length; i++) {
                prices[i] = grandExchange.lookup(lootz[i]).getGuidePrice();
            }
        }

        private void readLootFile(File fc) {
            try {
                String str;
                BufferedReader in = new BufferedReader(new FileReader(fc));
                while ((str = in.readLine()) != null) {
                    processLoot(str);
                }
                in.close();
            } catch (IOException e) {
            }
        }

        private void processLoot(String str) {
            try {
                int lootID = Integer.parseInt(str.substring(0, str.indexOf(':')));
                String name = str.substring(str.indexOf(':') + 1);
                names.add(name);
                lootList.add(lootID);
                list4.add(str);
                alchList.add(str);
            } catch (NumberFormatException e) {
                log("Invalid file entries");
                list4.removeAll();
                lootList.clear();
                names.clear();
                alchList.removeAll();
            }
        }
        // End of variables declaration
    }
}
