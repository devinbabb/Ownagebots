
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSCharacter;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = {"Ownageful"}, name = "Ownageful's Blue Dragon Killer Pro", version = 1.0, description = "BDK Pro")
public class AutoBDKPro extends Script implements PaintListener,
        MessageListener, MouseListener {

    public long startTime = System.currentTimeMillis();
    public int agility1 = 11844;
    public int fallytab = 8009, storedHides = 0, storedBones = 0;
    private int[] loots = {536, 1751, 985, 1213, 1163, 18778};
    String[] names = {"Dragon bones", "Blue drag", "Tooth", "Rune da", "Rune ful", "Ancient"};
    public int[] charms = {12158, 12159, 12160, 12161, 12162, 12163, 12164, 12165, 12166, 12167};
    public int[] prices = new int[6];
    private boolean shortcut = false, summonFull = false;
    final int[] strengthPots = {2440, 157, 159, 161};
    final int[] attackPots = {2436, 145, 147, 149};
    final int[] antiPots = {2452, 2454, 2456, 2458};
    private int[] rangedPots = {2444, 169, 171, 173};
    private int[] combatPots = {9739, 9741, 9743, 9745};
    private int[] bobPouchIDS = {12087, 12007, 12031, 12093};
    private int[] summonPots = {12146, 12144, 12142, 12140};
    private int finalPouch = -1;
    public int startBone, startHide, bankBone, bankHide, totalBone, totalHide, ivenBone, ivenHide;
    private boolean lootHide, lootBones;
    private int eatAt = -1;
    private boolean nBank = false;
    private int tab, ptab = 1;
    public int[] drop = {229, 995, 1355, 1069, 555, 1179};
    private int dusty = 1590;
    private boolean tabTaken = false;
    private int[] bankBoundry = {2943, 3368, 2949, 3373};
    private boolean dungeon = false;
    private boolean dPots = false, rPots = false, cPots = false, dAnti = false, lCharms = false, useSummon = false;
    private int food, withdraw;
    private RSTile center = new RSTile(2902, 9803);
    private RSGroundItem charmz;
    private RSGroundItem lootzd;
    private Timer tm = new Timer(0);
    private RSGroundItem lootz;
    RSTile[] toBank = {new RSTile(2952, 3379), new RSTile(2946, 3368)};
    private RSNPC drag;
    private int[] startExp = new int[Skill.values().length];
    private BDKGUI gui;
    private boolean nanti;
    private String st;
    private int totalLoot;
    private int lph;
    private int bankRun = 0;
    String stat;
    private RSTile[] centerDung = {new RSTile(1011, 4520), new RSTile(1002, 4509), new RSTile(987, 4502)};
    private RSTile[] agilitysctotravdung = {new RSTile(2922, 3364),
        new RSTile(2912, 3373), new RSTile(2898, 3377),
        new RSTile(2890, 3391), new RSTile(2884, 3395)};
    private RSTile[] walktodungeon = {new RSTile(2903, 9804), new RSTile(2911, 9808)};
    //credits to Warlock for this path.
    private final RSTile[] LADDERS_TO_GATE_ENTRANCE = {new RSTile(2884, 9800),
        new RSTile(2884, 9802), new RSTile(2884, 9804),
        new RSTile(2884, 9806), new RSTile(2884, 9808),
        new RSTile(2884, 9810), new RSTile(2884, 9812),
        new RSTile(2884, 9814), new RSTile(2884, 9817),
        new RSTile(2884, 9820), new RSTile(2884, 9824),
        new RSTile(2884, 9826), new RSTile(2885, 9829),
        new RSTile(2885, 9832), new RSTile(2885, 9836),
        new RSTile(2885, 9839), new RSTile(2883, 9842),
        new RSTile(2885, 9844), new RSTile(2888, 9845),
        new RSTile(2890, 9847), new RSTile(2893, 9849),
        new RSTile(2897, 9849), new RSTile(2901, 9849),
        new RSTile(2905, 9849), new RSTile(2909, 9849),
        new RSTile(2913, 9849), new RSTile(2917, 9849),
        new RSTile(2921, 9848), new RSTile(2924, 9846),
        new RSTile(2926, 9843), new RSTile(2929, 9840),
        new RSTile(2933, 9837), new RSTile(2935, 9833),
        new RSTile(2937, 9830), new RSTile(2937, 9827),
        new RSTile(2938, 9823), new RSTile(2938, 9820),
        new RSTile(2938, 9816), new RSTile(2938, 9812),
        new RSTile(2940, 9809), new RSTile(2941, 9806),
        new RSTile(2942, 9802), new RSTile(2944, 9799),
        new RSTile(2945, 9796), new RSTile(2949, 9795),
        new RSTile(2951, 9791), new RSTile(2951, 9788),
        new RSTile(2951, 9785), new RSTile(2951, 9782),
        new RSTile(2951, 9779), new RSTile(2951, 9776),
        new RSTile(2949, 9774), new RSTile(2946, 9774),
        new RSTile(2943, 9776), new RSTile(2940, 9778),
        new RSTile(2937, 9778), new RSTile(2935, 9775),
        new RSTile(2935, 9772), new RSTile(2934, 9768),
        new RSTile(2934, 9765), new RSTile(2934, 9762),
        new RSTile(2935, 9759), new RSTile(2934, 9757),
        new RSTile(2931, 9756), new RSTile(2928, 9756),
        new RSTile(2925, 9756), new RSTile(2923, 9759),
        new RSTile(2924, 9762), new RSTile(2924, 9765),
        new RSTile(2925, 9768), new RSTile(2927, 9771),
        new RSTile(2930, 9774), new RSTile(2930, 9777),
        new RSTile(2931, 9781), new RSTile(2933, 9784),
        new RSTile(2936, 9786), new RSTile(2935, 9789),
        new RSTile(2934, 9792), new RSTile(2932, 9796),
        new RSTile(2930, 9799), new RSTile(2928, 9802),
        new RSTile(2924, 9803)};
    private transient final Filter<RSNPC> filt1 = new Filter<RSNPC>() {
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equalsIgnoreCase("Blue dragon")
                        && !npc.isInCombat()
                        && npc.getInteracting() != getMyPlayer()
                        && npc.getAnimation() == -1 && !npc.isInteractingWithLocalPlayer());
            } catch (NullPointerException e) {
                return false;
            }
        }
    };
    private transient final Filter<RSNPC> filt2 = new Filter<RSNPC>() {
        public boolean accept(RSNPC npc) {
            try {
                return (npc.getName().equalsIgnoreCase("Blue dragon")
                        && !npc.isInCombat());
            } catch (NullPointerException e) {
                return false;
            }
        }
    };

    @Override
    public boolean onStart() {
        if (game.isLoggedIn()) {

            if (skills.getRealLevel(Skills.AGILITY) >= 70) {
                shortcut = true;
            } else {
                shortcut = false;
                if (!inventory.contains(dusty)) {
                    log("Dusty Key Not Found! Stopping Script, please start with Dusty Key!");
                    return false;
                }
            }

            if (skills.getRealLevel(Skills.DUNGEONEERING) >= 60) {
                dungeon = true;
            } else {
                dungeon = false;
            }

            createAndWaitforGUI();

            combat.setAutoRetaliate(true);

            log("Loading prices");
            for (int i = 0; i < 2; i++) {
                prices[i] = grandExchange.lookup(loots[i]).getGuidePrice();
            }
            mouse.setSpeed(random(5, 6));
            try {
                ivenBone = inventory.getCount(536);
                ivenHide = inventory.getCount(1753);
            } catch (Exception e) {
                ivenBone = 0;
                ivenHide = 0;
            }
            tm.reset();
            return true;
        }
        log("Not logged in.");
        return false;
    }

    private void createAndWaitforGUI() {
        if (SwingUtilities.isEventDispatchThread()) {
            gui = new BDKGUI();
            gui.setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        gui = new BDKGUI();
                        gui.setVisible(true);
                    }
                });
            } catch (InvocationTargetException ite) {
            } catch (InterruptedException ie) {
            }
        }
        sleep(100);
        while (gui.isVisible()) {
            sleep(100);
        }
    }

    @Override
    public int loop() {
        if (random(1000, 5000) == random(1000, 5000)) {
            performHumanAction();
        }

        try {
            for (int i = 0; i < drop.length; i++) {
                if (inventory.contains(drop[i])) {
                    inventory.getItem(drop[i]).doAction("Drop");
                }
            }

            if (!lCharms) {
                if (inventory.containsOneOf(charms)) {
                    inventory.getItem(charms).doAction("Drop");
                }
            }

            manageSummoning();
        } catch (NullPointerException e) {
        }


        camera.setPitch(true);

        if (walking.getEnergy() > random(20, 40)) {
            walking.setRun(true);
        }


        if (nBank) {
            stat = "Walking to Bank";
            walking.walkPathMM(toBank);
            if (isInArea(bankBoundry)) {
                bankRun++;
                nBank = false;
            }
        } else if (isInArea(bankBoundry)) {
            stat = "Banking";
            if (inventory.contains(fallytab) && inventory.contains(food)
                    && !inventory.contains(1751) && !inventory.contains(536) && (!useSummon || (useSummon && inventory.contains(finalPouch)))) {
                stat = "Walking to wall";
                walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
            } else {
                RSObject b = objects.getNearest(11758);
                if (b != null) {
                    camera.turnToObject(b);
                    if (!b.isOnScreen()) {
                        walking.walkTileMM(b.getLocation());
                    } else {
                        bank.open();
                        sleep(random(2000, 2200));
                        if (bank.isOpen()) {
                            if (!tabTaken) {
                                sleep(random(3000, 3500));
                                tab = bank.getCurrentTab();
                                tabTaken = true;
                                startBone = bank.getCount(loots[0]);
                                startHide = bank.getCount(loots[1]);
                            } else {
                                if (bank.getCurrentTab() != tab) {
                                    openBankTab(tab);
                                    sleep(random(2000, 3000));
                                }
                            }
                            if (bank.depositAllExcept(fallytab, finalPouch)) {
                                bank.depositAllFamiliar();
                                storedBones = 0;
                                storedHides = 0;
                                summonFull = false;
                                sleep(random(300, 500));
                                totalBone = bank.getCount(loots[0]) - startBone - ivenBone;
                                totalHide = bank.getCount(loots[1]) - startHide - ivenHide;
                                ivenBone = 0;
                                ivenHide = 0;
                                sleep(random(1000, 2000));
                                if ((food != 0) && bank.isOpen()) {
                                    while (!inventory.contains(food)) {
                                        if (bank.getCurrentTab() != tab) {
                                            openBankTab(tab);
                                            sleep(random(2000, 3000));
                                        }

                                        bank.withdraw(food, withdraw);
                                        sleep(random(800, 1000));
                                    }
                                }
                                if (dPots) {
                                    bank.withdraw(gap(strengthPots), 1);
                                    sleep(random(800, 1000));
                                }
                                if (rPots) {
                                    bank.withdraw(gap(rangedPots), 1);
                                    sleep(random(800, 1000));
                                }
                                if (cPots) {
                                    bank.withdraw(gap(combatPots), 1);
                                    sleep(random(800, 1000));
                                }
                                if (dAnti) {
                                    bank.withdraw(gap(antiPots), 1);
                                    sleep(random(800, 1000));
                                }
                                if (useSummon) {
                                    if (!inventory.contains(finalPouch)) {
                                        bank.withdraw(finalPouch, 1);
                                        sleep(random(800, 1000));
                                    }
                                    bank.withdraw(gap(summonPots), 1);
                                    sleep(random(800, 1000));
                                }
                                if (bank.close()) {
                                    sleep(random(600, 700));
                                    if (dPots) {
                                        if (didPot(strengthPots)) {
                                            sleep(random(800, 1000));
                                        }
                                        if (didPot(attackPots)) {
                                            sleep(random(800, 1000));
                                        }
                                    }
                                    if (rPots) {
                                        if (didPot(rangedPots)) {
                                            sleep(random(800, 1000));
                                        }
                                    }
                                    if (cPots) {
                                        if (didPot(combatPots)) {
                                            sleep(random(800, 1000));
                                        }
                                    }
                                    if (dPots) {
                                        if (bank.open()) {
                                            while (!bank.getInterface().isValid()) {
                                                sleep(random(1500, 2000));
                                                bank.open();
                                            }
                                        }
                                        if (bank.isOpen()) {
                                            if (!inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3]) && dAnti) {
                                                bank.depositAllExcept(fallytab, food, finalPouch, summonPots[0], summonPots[1], summonPots[2], summonPots[3]);

                                                if (bank.getCurrentTab() != tab) {
                                                    openBankTab(tab);
                                                    sleep(random(1000, 1800));
                                                }
                                                bank.withdraw(gap(antiPots), 1);
                                                sleep(random(500, 600));
                                            } else {
                                                bank.depositAllExcept(fallytab, finalPouch, food,
                                                        antiPots[0], antiPots[1], antiPots[2], antiPots[3],
                                                        summonPots[0], summonPots[1], summonPots[2], summonPots[3]);
                                                sleep(random(500, 600));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else if (isInArea(new int[]{2937, 3352, 2942, 3376})) {
            stat = "Walking to wall";
            RSObject a = objects.getNearest(agility1);
            if (a != null) {
                if (calc.distanceTo(a) <= 4) {
                    camera.turnToObject(a);
                    a.doAction("Climb");
                    waitToMove();
                } else {
                    walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
                }
            } else {
                walking.walkTo(walking.getPath(new RSTile(2939, 3356)).getNext());
            }
        } else if (isInArea(new int[]{2878, 3350, 2943, 3422})) {
            stat = "Walking to dungeon";
            RSObject l = objects.getNearest(55404);
            if (l != null) {
                if (l.isOnScreen()) {
                    l.doAction("Climb");
                    sleep(random(800, 1200));
                } else {
                    walking.walkPathMM(agilitysctotravdung);
                    while (getMyPlayer().isMoving() && calc.distanceTo(walking.getDestination()) >= 4) {
                        sleep(random(100, 200));
                    }
                }
            } else {
                walking.walkPathMM(agilitysctotravdung);
            }
        } else if (isInArea(new int[]{2881, 9794, 2887, 9801}) && shortcut) {
            RSObject h = objects.getNearest(9293);
            if (h != null) {
                if (h.isOnScreen()) {
                    h.doAction("Squeeze");
                    sleep(random(3000, 4000));
                    waitToMove();
                } else {
                    camera.turnToObject(h);
                }
            }
        } else if (isInArea(new int[]{2889, 9793, 2924, 9813})) {
            stat = "Fighting drags";
            if (dungeon) {
                RSObject d = objects.getNearest(52852);
                if (d != null) {
                    if (d.isOnScreen()) {
                        d.doAction("Enter");
                        sleep(random(1500, 2200));
                    } else {
                        walking.walkPathMM(walktodungeon);
                        sleep(random(600, 700));
                    }
                } else {
                    walking.walkPathMM(walktodungeon);
                    sleep(random(600, 700));
                }
            } else {
                fight();
            }
        } else if ((Math.floor(getMyPlayer().getLocation().getY() / 1000) == 4.0)) {
            fight();
        } else if (!shortcut && !isInArea(new int[]{2889, 9793, 2923, 9813})) {
            log("We are where we wanna be");
            RSObject gate = objects.getNearest(2623);
            if (gate == null) {
                walking.walkPathMM(LADDERS_TO_GATE_ENTRANCE);
                sleep(random(1200, 1300));
            } else if (!gate.isOnScreen()) {
                walking.walkPathMM(LADDERS_TO_GATE_ENTRANCE);
                sleep(random(1200, 1300));
            } else {
                camera.turnToObject(gate);
                RSItem key = inventory.getItem(dusty);
                if (key.doAction("Use")) {
                    sleep(500, 1500);
                    if (gate.doAction("Use Dusty key -> Gate")) {
                        sleep(1500, 2500);
                    }
                }
            }
        }
        return 1;

    }

    public void waitToMove() {
        sleep(random(1500, 2000));
        while (getMyPlayer().isMoving()) {
            sleep(random(100, 200));
        }
    }

    public int gap(int[] pots) {
        if (bank.isOpen()) {
            if (bank.getCount(pots[0]) > 0) {
                return pots[0];
            } else if (bank.getCount(pots[1]) > 0) {
                return pots[1];
            } else if (bank.getCount(pots[2]) > 0) {
                return pots[2];
            } else if (bank.getCount(pots[3]) > 0) {
                return pots[3];
            }
        } else {
            return 0;
        }
        return 0;
    }

    private boolean isInArea(int[] xy) {//Creds Ownageful
        final int x = getMyPlayer().getLocation().getX();
        final int y = getMyPlayer().getLocation().getY();
        if (x >= xy[0] && x <= xy[2] && y >= xy[1] && y <= xy[3]) {
            return true;
        } else {
            return false;

        }
    }

    //Paint
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    private void manageSummoning() {
        if (useSummon) {
            if (!summoning.isFamiliarSummoned()) {
                if (summoning.getSummoningPoints() == 0) {
                    if (didPot(summonPots)) {
                        sleep(random(400, 600));
                        inventory.getItem(finalPouch).doAction("Summon");
                    }
                } else {
                    inventory.getItem(finalPouch).doAction("Summon");
                }

            } else if (summoning.getTimeLeft() <= 2) {
                if (summoning.getSummoningPoints() == 0) {
                    if (didPot(summonPots)) {
                        sleep(random(400, 600));
                        summoning.doRenewFamiliar();
                    }
                } else {
                    summoning.doRenewFamiliar();
                }
            }
        }
    }

    private void storeSummon(boolean foodQuery) {
        if (summoning.isFamiliarSummoned()) {
            if (summoning.getFamiliar().canStore()) {
                RSItem toStore = inventory.getItem(loots[0], loots[1]);
                if (toStore != null) {
                    if (toStore.doAction("Use")) {
                        mouse.click(summoning.getFamiliar().getNPC().getScreenLocation(), false);
                        if (menu.isOpen()) {
                            if (menu.doAction("Use")) {
                                if (toStore.getID() == loots[0]) {
                                    storedBones += 1;
                                } else if (toStore.getID() == loots[1]) {
                                    storedHides += 1;
                                }
                                sleep(random(300, 400));
                            }
                        }
                    }
                }
            } else if (foodQuery == false) {
                try {
                    inventory.getItem(fallytab).doAction("Break");
                    nBank = true;
                } catch (NullPointerException e) {
                    log("Out of fally teleport.");
                    stopScript();
                }
                sleep(random(7000, 8500));
            }
        }
    }

    private enum Skill {

        ATTACK(Skills.ATTACK, "Attack", 0),
        STRENGTH(Skills.STRENGTH, "Strength", 1),
        DEFENCE(Skills.DEFENSE, "Defence", 2),
        CONSTITUTION(Skills.CONSTITUTION, "Constitution", 3),
        RANGE(Skills.RANGE, "Range", 4);
        int skillID;
        String skillName;
        int index;

        private Skill(int skillID, String skillName, int index) {
            this.skillID = skillID;
            this.skillName = skillName;
            this.index = index;
        }
    }
    private final Image closed = getImage("http://img685.imageshack.us/img685/6185/bluedragclosed.png");
    private final Image tabOne = getImage("http://img218.imageshack.us/img218/4589/bluedraggen.png");
    private final Rectangle hideRect = new Rectangle(477, 336, 34, 37);
    private final Rectangle tabOneRect = new Rectangle(327, 336, 148, 37);

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        st = tm.toElapsedString();
        totalLoot = ((totalBone + storedBones + inventory.getCount(loots[0]) - ivenBone) * prices[0])
                + ((totalHide + storedHides + inventory.getCount(loots[1]) - ivenHide) * prices[1]);
        lph = (int) (totalLoot / ((Double.parseDouble(st.substring(0, 2))) + (Double.parseDouble(st.substring(3, 5)) / 60)
                + (Double.parseDouble(st.substring(6, 8)) / 3600)));

        if (ptab == 1) {
            g.drawImage(tabOne, 0, 288, null);
            drawSkillBars(g);
            drawString(g, "Time Running: " + st, 290, 400);
            drawString(g, "Status: " + stat, 290, 415);
            drawString(g, "Total Loot: " + totalLoot, 290, 430);
            drawString(g, "Loot Per Hour: " + lph, 290, 445);
            drawString(g, "Bank Runs: " + bankRun, 290, 460);

        } else {
            g.drawImage(closed, 0, 288, null);
        }
        drawMouse(g);
    }

    private void drawString(Graphics g, String s, int x, int y) {
        g.setColor(new Color(90, 15, 15));
        g.setFont(new Font("Serif", 0, 12));
        g.drawString(s, x, y);
        g.setColor(new Color(255, 255, 255, 90));
        g.drawString(s, x + 1, y + 1);
    }

    private void drawSkillBars(Graphics g) {
        for (Skill s : Skill.values()) {
            int x1 = s.index <= 4 ? 20 : 180;
            int y1 = s.index <= 4 ? 360 + (s.index * 20) : 360 + ((s.index - 4) * 20);
            g.setColor(new Color(90, 15, 15, 100));
            g.drawRect(x1, y1, 250, 15);
            g.setColor(new Color(0, 0, 0, 50));
            g.fillRect(x1, y1, (int) (skills.getPercentToNextLevel(s.skillID) * 2.5), 15);
            g.setColor(new Color(90, 15, 15));
            g.setFont(new Font("Serif", 0, 12));
            g.drawString(s.skillName + ": " + skills.getPercentToNextLevel(s.skillID)
                    + "% to level " + (skills.getRealLevel(s.skillID) + 1)
                    + " (Gained: " + (skills.getCurrentExp(s.skillID) - startExp[s.index]) + ")", x1 + 6, y1 + 13);
            g.setColor(new Color(255, 255, 255, 90));
            g.drawString(s.skillName + ": " + skills.getPercentToNextLevel(s.skillID)
                    + "% to level " + (skills.getRealLevel(s.skillID) + 1)
                    + " (Gained: " + (skills.getCurrentExp(s.skillID) - startExp[s.index]) + ")", x1 + 7, y1 + 14);
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (hideRect.contains(e.getPoint())) {
            ptab = 3;
        } else if (tabOneRect.contains(e.getPoint())) {
            ptab = 1;
        }
    }

    public boolean openBankTab(int tab) {
        if (!bank.isOpen() || (63 - (tab * 2) < 47)) {
            return false;
        }
        try {
            return bank.getInterface().getComponent(63 - (tab * 2)).doClick();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean didPot(int[] pots) {
        if (inventory.containsOneOf(pots)) {
            if (inventory.getItem(pots).doAction("Drink")) {
                return true;
            }
        }
        return false;
    }

    private void performHumanAction() {//RawRs antiban
        int randomNum = random(1, 30);
        int r = random(1, 35);
        if (randomNum == 6) {
            if (r == 1) {
                if (game.getCurrentTab() != game.TAB_STATS) {
                    game.openTab(game.TAB_STATS);
                    mouse.move(random(680, 730), random(355, 370));
                    sleep(random(1000, 1500));
                }
            }
            if (r == 2) {
                game.openTab(random(1, 14));
            }
            if (r == 3) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 4) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 5) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 6) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 7) {
                Point mPos = mouse.getLocation();
                mouse.move(mPos.x + random(-90, 90), mPos.y + random(-90, 90));
            }
            if (r == 8) {
                camera.setAngle(random(100, 360));
            }
            if (r == 9) {
                camera.setAngle(random(100, 360));
            }
            if (r == 10) {
                camera.setAngle(random(100, 360));
            }
        }
    }

    private void drawMouse(final Graphics g) {
        final Point loc = mouse.getLocation();
        if (System.currentTimeMillis()
                - mouse.getPressTime() < 500) {
            g.setColor(Color.red);
            g.fillOval(loc.x - 5, loc.y - 5, 10, 10);
        } else {
            g.setColor(Color.black);
        }
        g.drawLine(0, loc.y, game.getWidth(), loc.y);
        g.drawLine(loc.x, 0, loc.x, game.getHeight());
    }

    private int fight() {
        if (nanti) {
            if (inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3])) {
                if (inventory.getItem(antiPots).doAction("Drink")) {
                    sleep(random(3900, 3950));
                    nanti = false;
                    return 20;
                }
            }
        }

        if (inventory.isFull() && inventory.getCount(food) == 0) {
            if (useSummon && !summonFull) {
                storeSummon(false);
            } else {
                try {
                    inventory.getItem(fallytab).doAction("Break");
                    nBank = true;
                    return 100;
                } catch (NullPointerException e) {
                    log("Out of fally teleport.");
                    stopScript();
                }
                sleep(random(7000, 8500));
            }
        }
        if (game.isLoggedIn()) {
            if (getMyPlayer().getHPPercent() < (random(eatAt - 5, eatAt + 5))) {
                if (inventory.contains(food)) {
                    inventory.getItem(food).doAction("Eat");
                    sleep(random(3900, 3950));
                    return 100;
                } else {
                    inventory.getItem(fallytab).doAction("Break");
                    sleep(random(7000, 8500));
                    nBank = true;
                    return 100;
                }
            }
        }
        if (lCharms) {
            charmz = groundItems.getNearest(charms);
            if (charmz != null) {
                if (calc.tileOnScreen(charmz.getLocation())) {
                    if (inventory.isFull() && inventory.contains(food) && !inventory.contains(charmz.getItem().getID())) {
                        if (useSummon && !summonFull) {
                            storeSummon(true);
                        }
                        sleep(random(800, 900));
                        if (inventory.isFull()) {
                            inventory.getItem(food).doAction("Eat");
                            sleep(random(100, 300));
                        }
                    }
                    charmz.doAction("charm");
                    sleep(600, 850);
                    while (getMyPlayer().isMoving()) {
                        sleep(1200, 1400);
                    }
                    return 100;
                } else {
                    if (calc.tileOnMap(charmz.getLocation())) {
                        walking.walkTileMM(charmz.getLocation());
                        while (getMyPlayer().isMoving()) {
                            sleep(200, 400);
                        }
                        return 100;
                    }
                }
            }
        }
        lootzd = groundItems.getNearest(loots[0]);
        if (lootzd != null) {
            if (calc.tileOnScreen(lootzd.getLocation())) {
                if (inventory.isFull() && inventory.contains(food)) {
                    if (useSummon && !summonFull) {
                        storeSummon(true);
                    }
                    sleep(random(800, 900));
                    if (inventory.isFull()) {
                        inventory.getItem(food).doAction("Eat");
                        sleep(random(1200, 1300));
                    }
                }
                lootzd.doAction("Take Drag");

                sleep(600, 850);
                while (getMyPlayer().isMoving()) {
                    sleep(200, 400);
                }
                return 100;
            } else {
                if (calc.tileOnMap(lootzd.getLocation())) {
                    walking.walkTileMM(lootzd.getLocation());
                    while (getMyPlayer().isMoving()) {
                        sleep(200, 400);
                    }
                    return 100;
                }
            }
        }

        lootz = groundItems.getNearest(loots);
        if (lootz != null) {
            for (int i = 0; i < loots.length; i++) {
                if (lootz.getItem().getID() == loots[i]) {
                    if (calc.tileOnScreen(lootz.getLocation())) {
                        if (inventory.isFull() && inventory.contains(food)) {
                            if (useSummon && !summonFull) {
                                storeSummon(true);
                            }
                            sleep(random(800, 900));
                            if (inventory.isFull()) {
                                inventory.getItem(food).doAction("Eat");
                                sleep(random(1200, 1300));
                            }
                        }
                        lootz.doAction("Take " + names[i]);

                        sleep(600, 850);
                        return 100;
                    } else {
                        if (calc.tileOnMap(lootz.getLocation())) {
                            walking.walkTileMM(lootz.getLocation());
                            while (getMyPlayer().isMoving()) {
                                sleep(200, 400);
                            }
                            return 100;
                        }
                    }
                }
            }
        }

        if (dungeon) {
            RSCharacter inter = getMyPlayer().getInteracting();
            if (inter != null) {
                if (inter.getName().equals("Blue dragon")) {
                    if (inter.getAnimation() == 12250) {
                        sleep(random(3900, 4500));
                        return 100;
                    } else {
                        sleep(random(200, 300));
                    }
                }
            } else {
                drag = npcs.getNearest(filt2);
                if (drag != null) {
                    if (drag.isOnScreen()) {
                        drag.doAction("Attack");
                        sleep(random(1500, 2000));
                        while (getMyPlayer().isMoving()) {
                            sleep(200, 400);
                        }
                    } else {
                        walking.walkTileMM(drag.getLocation());
                        sleep(random(600, 700));
                        while (getMyPlayer().isMoving()) {
                            sleep(200, 400);
                        }
                    }
                } else {
                    if (dungeon) {
                        walking.walkPathMM(centerDung);
                        sleep(random(600, 700));
                    }
                }
                return 10;
            }
            return 10;
        } else {
            RSNPC blueDragon = npcs.getNearest(filt1);
            if (!getMyPlayer().isInCombat() && getMyPlayer().isIdle()
                    && getMyPlayer().getAnimation() == -1
                    && getMyPlayer().getInteracting() == null) {
                if (blueDragon != null) {
                    if (blueDragon.isOnScreen()) {
                        blueDragon.doAction("Attack");
                        sleep();
                    } else {
                        camera.turnToTile(blueDragon.getLocation());
                    }
                } else {
                    camera.moveRandomly(30);
                    if (calc.distanceTo(center) > 6) {
                        walking.walkTileMM(center);
                        sleep();
                    }
                    sleep();
                }
            }
            return 10;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void sleep() {
        sleep(500, 1000);
    }

    public void messageReceived(MessageEvent me) {
        String str = me.getMessage().toString();
        if (str.contains("fiery breath") && dAnti) {
            nanti = true;
        } else if (str.contains("Your familiar cannot") && useSummon) {
            summonFull = true;
        }
    }

    class BDKGUI extends javax.swing.JFrame {

        public BDKGUI() {
            initComponents();
            this.setResizable(false);
            this.setLocation(520, 250);
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
            jLabel4 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            jLabel8 = new javax.swing.JLabel();
            jSeparator3 = new javax.swing.JSeparator();
            jCheckBox7 = new javax.swing.JCheckBox();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel9 = new javax.swing.JLabel();
            jSeparator4 = new javax.swing.JSeparator();
            jCheckBox8 = new javax.swing.JCheckBox();
            jLabel10 = new javax.swing.JLabel();
            jSeparator5 = new javax.swing.JSeparator();
            jLabel11 = new javax.swing.JLabel();
            jCheckBox9 = new javax.swing.JCheckBox();
            jLabel12 = new javax.swing.JLabel();
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("General's Auto BDK  Pro.");
            setAlwaysOnTop(true);
            setBackground(new java.awt.Color(255, 255, 255));
            setFocusable(false);
            jLabel5.setForeground(new java.awt.Color(0, 153, 51));
            jLabel5.setText("Food:");
            jLabel1.setText("Food ID:");
            jLabel2.setText("Withdraw:");
            jLabel6.setText("Eat At:");
            jSpinner1.setModel(new javax.swing.SpinnerNumberModel(40, 20, 90, 1));
            jLabel7.setForeground(new java.awt.Color(204, 0, 102));
            jLabel7.setText("Pots:");
            jCheckBox2.setText("Drink Antifire");
            jCheckBox3.setText("Drink Supers");
            jCheckBox4.setText("Drink Range Pots");
            jCheckBox5.setText("Drink Combat Pots");
            jCheckBox1.setText("Loot Charms?");
            jLabel4.setText("Fally Teleport Tabs, and Dusty Key (if not 70 agility), food in first slot (if Using)");
            jLabel3.setText("Start script in fally bank with:");
            jButton1.setText("Start");
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
            jLabel8.setForeground(new java.awt.Color(0, 51, 255));
            jLabel8.setText("Summoning:");
            jCheckBox7.setText("Beast of Burden:");
            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bull Ant", "TerrorBird", "War Tortoise", "Pack Yak"}));
            jLabel9.setForeground(new java.awt.Color(255, 0, 0));
            jLabel9.setText("Looting:");
            jCheckBox8.setText("Loot Hides");
            jLabel10.setForeground(new java.awt.Color(255, 0, 255));
            jLabel10.setText("Finalize:");
            jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
            jLabel11.setForeground(new java.awt.Color(0, 102, 255));
            jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel11.setText("Ownageful's Blue Dragon Killer Pro");
            jCheckBox9.setText("Loot Dragon Bones");
            jLabel12.setText("%");
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel12)).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jCheckBox1).addGap(18, 18, 18).addComponent(jCheckBox8).addGap(10, 10, 10).addComponent(jCheckBox9)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addGroup(layout.createSequentialGroup().addComponent(jCheckBox2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox5))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel9))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel7)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel5)).addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jLabel10)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jCheckBox7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addGap(3, 3, 3).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addGap(26, 26, 26).addComponent(jLabel7).addGap(1, 1, 1).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox2).addComponent(jCheckBox4).addComponent(jCheckBox3).addComponent(jCheckBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(jLabel8).addGap(1, 1, 1).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox7).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE).addComponent(jLabel9).addGap(1, 1, 1).addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox1).addComponent(jCheckBox8).addComponent(jCheckBox9)).addGap(15, 15, 15).addComponent(jLabel10).addGap(1, 1, 1).addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel3).addGap(1, 1, 1).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(7, 7, 7)));

            pack();
        }

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            if (jCheckBox1.isSelected()) {
                lCharms = true;
            }
            if (jCheckBox2.isSelected()) {
                dAnti = true;
            }
            if (jCheckBox3.isSelected()) {
                dPots = true;
            }

            if (jCheckBox4.isSelected()) {
                rPots = true;
            }

            if (jCheckBox5.isSelected()) {
                cPots = true;
            }

            eatAt = (Integer) jSpinner1.getValue();


            if (jCheckBox7.isSelected()) {
                useSummon = true;
                int a = jComboBox1.getSelectedIndex();
                finalPouch = bobPouchIDS[a];
            }

            try {
                food = Integer.parseInt(jTextField1.getText());
                withdraw = Integer.parseInt(jTextField2.getText());
            } catch (NumberFormatException numberFormatException) {
            }
            this.dispose();
        }
        // Variables declaration - do not modify
        private javax.swing.JButton jButton1;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox2;
        private javax.swing.JCheckBox jCheckBox3;
        private javax.swing.JCheckBox jCheckBox4;
        private javax.swing.JCheckBox jCheckBox5;
        private javax.swing.JCheckBox jCheckBox7;
        private javax.swing.JCheckBox jCheckBox8;
        private javax.swing.JCheckBox jCheckBox9;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
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
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
    }
}
