import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.util.Timer;
import org.rsbot.script.util.WindowUtil;
import org.rsbot.script.wrappers.*;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@ScriptManifest(authors = "Ownageful, Aut0r", name = "Green Dragon Killer", version = 4.23, description = "<html><body style='font-family: Arial; margin: 10px;'><span style='color: #00AA00; font-weight: bold;'>Green Dragon Killer</span>&nbsp;<strong>Version:&nbsp;2.43</strong><br />")
public class OwnageGDK extends Script implements MessageListener, PaintListener {

	private boolean startScript;
	boolean useSupers = false;
	boolean playerCombat = false;
	boolean running = true;
	RSComponent i;
	boolean useAnti = false;
	boolean eat = false;
	public int food = 7946;
	int withdraw;
	boolean ntele = false;
	boolean pstr, patt, panti, potted = false;
	int[] drags = {4677, 941, 4678};
	int[] geBoundry = {3182, 3433, 3189, 3446};
	int[] ditchBoundry = {3135, 3515, 3140, 3520};
	int[] wildBoundry = {3134, 3523, 3171, 3566};
	int[] dung1Boundry = {3290, 5472, 3304, 5480};
	int[] dungBoundry = {3284, 5462, 3296, 5471};
	int[] dungANTBoundry = {3298, 5483, 3304, 5487};
	int[] dungPOISONBoundry = {3282, 5473, 3286, 5475};
	int[] dBoundry = {3299, 5442, 3322, 5470};
	int[] nw = {3299, 5456, 3311, 5470};
	int[] sw = {3299, 5443, 3309, 5455};
	int[] ne = {3311, 5452, 3322, 5464};
	int[] se = {3311, 5442, 3322, 5451};
	int[] curePot = {179, 177, 175, 2446, 11435, 11433, 5949, 5947, 5945, 5943, 11475, 11473};
	int curep;
	public RSTile[] toRope = new RSTile[]{new RSTile(3140, 3530), new RSTile(3147, 3538), new RSTile(3155, 3546), new RSTile(3161, 3554), new RSTile(3164, 3562)};
	public RSTile[] toPort = {new RSTile(3293, 5470), new RSTile(3289, 5462)};
	public RSTile[] toGE = {new RSTile(3199, 3429), new RSTile(3188, 3435)};
	public RSTile[] toDitch = {new RSTile(3183, 3448), new RSTile(3171, 3456), new RSTile(3156, 3463), new RSTile(3141, 3466), new RSTile(3134, 3478), new RSTile(3136, 3492), new RSTile(3136, 3506), new RSTile(3138, 3520)};
	public int ditch = 1440;
	public int rope = 28892;
	public RSTile portal = new RSTile(3290, 5463);
	final int[] strengthPots = {2440, 157, 159, 161};
	final int[] attackPots = {2436, 145, 147, 149};
	final int[] antiPots = {2452, 2454, 2456, 2458};
	public int vTabs = 8007;
	RSObject banker;
	private boolean nanti;
	Timer tse = new Timer(0);
	Timer tsw = new Timer(0);
	Timer tne = new Timer(0);
	Timer tnw = new Timer(0);
	public int rTime;
	String status;
	RSNPC[] gd;

	//All Loots
	RSGroundItem allLoot;
	int[] loots = {1249, 536, 1213, 1753, 12158, 12159, 12160, 12163, 18778};
	String[] lootNames = {"Dragon spear", "Dragon bones", "Rune dagger", "Green dragonhide", "Gold charm", "Green charm", "Crimson charm", "Blue charm", "Starved ancient effigy"};

	//Profitable Loots
	int[] profitableLoots = {1249, 536, 1213, 1753};
	String[] profitableLootNames = {"Dragon spear", "Dragon bones", "Rune dagger", "Green dragonhide"};
	int[] prices = new int[4];

	//Priority Looting
	RSGroundItem priorityLoot;
	int[] priorityLoots = {536};
	String[] priorityLootNames = {"Dragon bones"};

	//Drop if picked up.
	public int[] drop = {229, 995, 1355, 1069, 555, 1179};
	private int totalLoot;

	//Classes
	private GDK gui;
	private TimeListener timeListener;
	private ProfitListener profitListener;

	public int startSTR, startDEF, startHP, startATT, startMAG, startRNG;

	public boolean usingFood = true;
	int hp;

	boolean lCharms = false;
	boolean tabTaken;
	int tab;

	final Filter<RSNPC> DRAG_FILTER = new Filter<RSNPC>() {

		public boolean accept(RSNPC npc) {
			if (!npc.isOnScreen()) {
				return false;
			}
			final int npcID = npc.getID();
			for (int x = 0; x < drags.length; x++) {
				if (npcID == drags[x]) {
					return true;
				}
			}
			return false;
		}
	};
	final Filter<RSNPC> thugFilter = new Filter<RSNPC>() {

		public boolean accept(RSNPC npc) {
			return npc.getName().contains("hug");
		}
	};

	@Override
	public boolean onStart() {
		if (game.isLoggedIn()) {
			startSTR = skills.getCurrentExp(Skills.STRENGTH);
			startDEF = skills.getCurrentExp(Skills.DEFENSE);
			startHP = skills.getCurrentExp(Skills.CONSTITUTION);
			startATT = skills.getCurrentExp(Skills.ATTACK);
			startRNG = skills.getCurrentExp(Skills.RANGE);
			startMAG = skills.getCurrentExp(Skills.MAGIC);
			createAndWaitforGUI();
			try {
				String rTimeTemp = WindowUtil.showInputDialog("Approximate Respawn Time (in milliseconds*) \n *1000 milliseconds = 1 second");
				if (rTimeTemp != null) {
					rTime = Integer.parseInt(rTimeTemp);
				} else {
					rTime = 32000;
				}
			} catch (NumberFormatException numberFormatException) {
				rTime = 32000;
			} catch (StringIndexOutOfBoundsException s) {
				rTime = 32000;
			}
			rTime = rTime + 5000;
			while (!startScript) {
				sleep(10);
			}
			log("Loading guide prices for profitable loots.");
			for (int b = 0; b < 4; b++) {
				prices[b] = grandExchange.lookup(profitableLoots[b]).getGuidePrice();
				log("" + profitableLootNames[b] + " [" + profitableLoots[b] + "] guide price is " + prices[b]);
			}
			mouse.setSpeed(random(5, 6));
			timeListener = new TimeListener();
			profitListener = new ProfitListener();
			combat.setAutoRetaliate(true);
			return true;
		} else {
			log("Please log in first.");
			return false;
		}

	}

	private void createAndWaitforGUI() {
		if (SwingUtilities.isEventDispatchThread()) {
			gui = new GDK();
			gui.setVisible(true);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						gui = new GDK();
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

		for (int i = 0; i < drop.length; i++) {
			if (inventory.contains(drop[i])) {
				inventory.getItem(drop[i]).doAction("Drop");
			}
		}

		camera.setPitch(true);
		if (walking.getEnergy() > random(20, 40)) {
			walking.setRun(true);
		}
		if (ntele) {
			inventory.getItem(vTabs).doAction("Break");
			sleep(random(7000, 8500));
			walking.walkTileMM(new RSTile(3208, 3435));
			sleep(1500, 3000);
			ntele = false;
		}
		if (game.isLoggedIn()) {
			if (getMyPlayer().getHPPercent() < hp && !isInArea(dBoundry) && getMyPlayer().isInCombat()) {
				if (isInArea(dungANTBoundry) || isInArea(dungPOISONBoundry) || isInArea(dung1Boundry) || isInArea(dungBoundry)) {
					sleep(random(100, 200));
					if (getMyPlayer().getHPPercent() <= hp) {
						inventory.getItem(vTabs).doAction("Break");
						sleep(random(7000, 8500));
						walking.walkTileMM(new RSTile(3208, 3435));
						sleep(1500, 3000);
						patt = false;
						pstr = false;
						panti = false;
					}
				} else {
					sleep(random(2000, 3000));
					if (inventory.contains(food)) {
						inventory.getItem(food).doAction("Eat");
						sleep(random(600, 700));
					} else {
						inventory.getItem(vTabs).doClick(true);
						sleep(random(1000, 1000));
					}
					patt = false;
					pstr = false;
					panti = false;
				}
			}
		}

		if (isInArea(geBoundry)) {
			while (combat.isPoisoned()) {
				bank.open();
				sleep(500);
				if (bank.isOpen()) {
					if (bank.getCount(curePot) < 1) {
						bank.close();
						log("poisoned so we logged out!");
						stopScript(true);
					} else {
						for (int i = 0; bank.getCount(curePot[i]) > 0; i++) {
							curep = curePot[i];
						}
						bank.withdraw(curep, 1);
						sleep(200);
						if (inventory.contains(curep)) {
							bank.close();
							sleep(500);
						}
					}
				}
				if (inventory.contains(curep)) {
					inventory.getItem(curep).doClick(true);
					sleep(1000);
				}
			}
			if (!patt || !pstr || !panti) {
				status = "Banking";
				banker = objects.getNearest(782);
				if (banker != null) {
					if (banker.isOnScreen()) {
						if (!bank.isOpen()) {
							banker.doAction("uickly");
							while (!bank.getInterface().isValid()) {
								sleep(random(1500, 2000));
								banker.doAction("uickly");
							}
						} else {
							if (!tabTaken) {
								sleep(random(4000, 5000));
								tab = bank.getCurrentTab();
								log("Current tab is" + tab);

								tabTaken = true;

							} else {
								if (bank.getCurrentTab() != tab) {
									openBankTab(tab);
									sleep(random(2000, 3000));
								}
							}

							if (bank.depositAllExcept(vTabs)) {
								sleep(random(1000, 2000));
								if (eat && bank.isOpen()) {
									while (!inventory.contains(food)) {
										if (bank.getCurrentTab() != tab) {
											openBankTab(tab);
											sleep(random(2000, 3000));
										}
										bank.withdraw(food, withdraw);
										sleep(random(1200, 1400));
									}
								}
								if (useSupers) {
									if (!pstr) {
										bank.withdraw(gap(strengthPots), 1);
										sleep(random(1200, 1400));

									}
									if (!patt) {
										bank.withdraw(gap(attackPots), 1);
										sleep(random(1200, 1400));
									}
								} else {
									patt = true;
									pstr = true;
								}
								if (useAnti) {
									if (!panti) {
										bank.withdraw(gap(antiPots), 1);
										sleep(random(1200, 1400));
									}
								} else {
									panti = true;
								}

								if (bank.close()) {
									sleep(random(600, 700));
									if (useSupers) {
										if (didPot(strengthPots)) {
											sleep(random(800, 1000));
										}
										pstr = true;
										if (didPot(attackPots)) {
											sleep(random(800, 1000));
										}
										patt = true;
									}
									if (useAnti) {
										if (didPot(antiPots)) {
											nanti = false;
											sleep(random(800, 1000));
										}
										panti = true;
									} else {
										nanti = false;
									}
									if (pstr && patt && panti) {
										if (useSupers) {
											if (banker.doAction("uickly")) {
												while (!bank.getInterface().isValid()) {
													sleep(random(1500, 2000));
													banker.doAction("uickly");
												}
											}
											if (bank.isOpen()) {
												if (!inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3]) && useAnti) {
													bank.depositAllExcept(vTabs, food);

													if (bank.getCurrentTab() != tab) {
														if (i != null) {
															mouse.click(i.getComponent(tab).doAction("View"));
														}
														sleep(random(1000, 1800));
													}
													bank.withdraw(gap(antiPots), 1);
													sleep(random(500, 600));
												} else {
													bank.depositAllExcept(vTabs, food, antiPots[0], antiPots[1], antiPots[2], antiPots[3]);
													sleep(random(500, 600));
												}
											}
										}
									}
								}
							}
						}
					} else {
						walking.walkTileMM(new RSTile(3162, 3487));
						sleep(1500, 3000);
					}
				}
			} else {
				if (bank.isOpen()) {
					bank.close();

					sleep(random(500, 1000));
				} else {
					while (!isInArea(ditchBoundry)) {
						walkPath(toDitch);
					}
				}
			}
		} else if (isInArea(ditchBoundry)) {
			if (interfaces.get(382).getComponent(21).isValid()) {
				interfaces.get(382).getComponent(21).doClick();
			}
			status = "Crossing ditch.";
			RSObject dich = objects.getNearest(ditch);
			if (dich != null) {
				if (calc.tileOnScreen(dich.getLocation())) {
					dich.doAction("Cross");
					sleep(random(3000, 3500));
				} else {
					walking.walkTileMM(dich.getLocation());
					sleep(1000, 1200);
				}
			}
		} else if (isInArea(wildBoundry)) {
			try {
				if (getMyPlayer().getInteracting() != null || playerCombat) {
					RSCharacter npc1 = getMyPlayer().getInteracting();
					if (npc1 != null && npc1.getName().contains("Thug")) {
						npc1.doAction("Attack");
						log("Getting attacked by thug.");
						sleep(400, 600);
					} else {
						log("Getting attacked by player!");
						inventory.getItem(vTabs).doClick(true);
						playerCombat = false;
						sleep(200, 400);
					}
				}
				if (interfaces.get(676) != null) {
					if (interfaces.get(676).getComponent(17).containsText("Proceed")) {
						interfaces.get(676).getComponent(17).doClick();
						sleep(random(500, 800));
					}
				}
			} catch (NullPointerException e) {
				// null
			}
			status = "Walking to tunnel.";
			RSObject ropez = objects.getNearest(rope);
			if (ropez != null) {
				if (calc.tileOnScreen(ropez.getLocation())) {
					while (!menu.isOpen()) {
						ropez.doClick(false);
					}
					if (menu.isOpen()) {
						menu.doAction("Enter");
					}
					sleep(1500, 1600);
				} else {
					walkPath(toRope);
					walking.walkTileMM(ropez.getLocation());
				}
			} else {
				walkPath(toRope);
			}
			sleep(750, 1250);
		} else if (isInArea(dung1Boundry)) {
			antiban();
			status = "Walking to portal";
			doPort();
		} else if (isInArea(dungBoundry)) {
			antiban();
			status = "Walking to portal";
			doPort();
		} else if (isInArea(dBoundry)) {
			status = "Fighting drags";
			if (fight() == 101) {
				patt = false;
				pstr = false;
				panti = false;
			}
		} else {
			status = "Walking to G.E";
			walkPath(toGE);
		}
		return 100;
	}

	private boolean isInArea(int[] xy) {
		final int x = getMyPlayer().getLocation().getX();
		final int y = getMyPlayer().getLocation().getY();
		if (x >= xy[0] && x <= xy[2] && y >= xy[1] && y <= xy[3]) {
			return true;
		} else {
			return false;

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

	private boolean npcIsInArea(int[] xy, RSCharacter npc) {
		try {
			final int x = npc.getLocation().getX();
			final int y = npc.getLocation().getY();
			if (x >= xy[0] && x <= xy[2] && y >= xy[1] && y <= xy[3]) {
				return true;
			} else {
				return false;

			}
		} catch (NullPointerException e) {
			return false;
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

	public boolean didPot(int[] pots) {
		if (inventory.containsOneOf(pots)) {
			if (inventory.getItem(pots).doAction("Drink")) {
				return true;
			}
		}
		return false;
	}

	private boolean walkPath(RSTile[] path) {
		if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) <= 1) {
			return walking.walkPathMM(path);
		}
		return false;
	}

	public void onRepaint(Graphics g) {
		if (game.isLoggedIn()) {
			g.setFont(new java.awt.Font("Tahoma", 1, 10));
			g.setColor(new Color(0, 0, 0, 100));
			g.fill3DRect(3, 266, 184, 73, true);
			g.setColor(new Color(227, 100, 45));
			g.drawString("Ownageful GDK", 15, 277);
			g.setColor(Color.white);

			g.drawString("Time running: " + timeListener.getRuntimeString(), 15, 289);
			g.drawString("Status: " + status, 15, 301);
			g.drawString("Total Loot: " + (int) totalLoot, 15, 313);
			g.drawString("Loot Per Hour: " + timeListener.calcPerHour((long) totalLoot), 15, 325);

			g.setColor(new Color(0, 0, 0, 100));
			g.fill3DRect(3, 20, 50, 65, true);
			g.setColor(new Color(227, 100, 45));
			g.drawString("Spawn Times:", 15, 31);
			g.setColor(Color.white);

			g.drawString("Nw: " + ((int) ((tnw.getRemaining()) / 1000)), 15, 43);
			g.drawString("Ne: " + ((int) ((tne.getRemaining()) / 1000)), 15, 55);
			g.drawString("Sw: " + ((int) ((tsw.getRemaining()) / 1000)), 15, 67);
			g.drawString("Se: " + ((int) ((tse.getRemaining()) / 1000)), 15, 79);


			g.setFont(new java.awt.Font("Tahoma", 1, 10));
		}

	}

	@Override
	public void onFinish() {
		// Takes a screen shot when u stop the script.
		running = false;
		profitListener.kill();
		env.saveScreenshot(true);
		super.stopScript();
	}

	private int fight() {
		updateTimers();
		if (nanti) {
			if (inventory.containsOneOf(antiPots[0], antiPots[1], antiPots[2], antiPots[3])) {
				inventory.getItem(antiPots).doAction("Drink");
				sleep(random(1800, 1900));
				nanti = false;
			}
		}
		if (inventory.isFull() && inventory.getCount(food) == 0) {
			try {
				inventory.getItem(vTabs).doAction("Break");
			} catch (NullPointerException e) {
				log("Out of varrock teleport.");
				stopScript(true);
			}
			sleep(random(7000, 8500));
			walking.walkTileMM(new RSTile(3208, 3435));
			while (getMyPlayer().isMoving()) {
				sleep(1000, 2000);
			}
			return 101;
		}
		if (game.isLoggedIn()) {
			if (getMyPlayer().getHPPercent() < hp) {
				if (inventory.contains(food)) {
					inventory.getItem(food).doAction("Eat");
					sleep(random(600, 800));
					return 100;
				} else {
					inventory.getItem(vTabs).doAction("Break");
					sleep(random(7000, 8500));
					walking.walkTileMM(new RSTile(3208, 3435));
					while (getMyPlayer().isMoving()) {
						sleep(1000, 2000);
					}
					return 101;
				}
			}
		}
		priorityLoot = groundItems.getNearest(priorityLoots);
		allLoot = groundItems.getNearest(loots);
		if (priorityLoot != null) {
			if (calc.tileOnScreen(priorityLoot.getLocation())) {
				if (inventory.isFull() && inventory.contains(food)) {
					inventory.getItem(food).doAction("Eat");
					sleep(random(600, 800));
				}
				if (priorityLoot != null) {
					for (int j = 0; j < priorityLoots.length; j++) {
						if (priorityLoot.getItem().getID() == priorityLoots[j]) {
							priorityLoot.doAction("Take " + priorityLootNames[j]);
							while (getMyPlayer().isMoving()) {
								sleep(400, 500);
							}
							sleep(600, 800);
							return 100;
						}
					}
				}
			} else {
				walking.walkTileMM(priorityLoot.getLocation());
				while (getMyPlayer().isMoving()) {
					sleep(200, 400);
				}
				return 100;
			}
		} else if (allLoot != null && priorityLoot == null) {
			if (calc.tileOnScreen(allLoot.getLocation())) {
				if (inventory.isFull() && inventory.contains(food)) {
					inventory.getItem(food).doAction("Eat");
					sleep(random(600, 800));
				}
				if (allLoot != null) {
					for (int j = 0; j < loots.length; j++) {
						if (allLoot.getItem().getID() == loots[j]) {
							allLoot.doAction("Take " + lootNames[j]);
							while (getMyPlayer().isMoving()) {
								sleep(400, 500);
							}
							sleep(600, 800);
							return 100;
						}
					}
				}
			} else {
				walking.walkTileMM(allLoot.getLocation());
				while (getMyPlayer().isMoving()) {
					sleep(200, 400);
				}
				return 100;
			}
		}
		RSCharacter inter = getMyPlayer().getInteracting();
		if (inter != null) {
			if (inter.getName().contains("Green")) {
				if (inter.getAnimation() == 92 || inter.getHPPercent() == 0) {
					if (getArea(inter) == se) {
						tse = new Timer(rTime);
						tse.reset();
					} else if (getArea(inter) == sw) {
						tsw = new Timer(rTime);
						tsw.reset();
					} else if (getArea(inter) == ne) {
						tne = new Timer(rTime);
						tne.reset();
					} else if (getArea(inter) == nw) {
						tnw = new Timer(rTime);
						tnw.reset();
					}
					sleep(random(4200, 4300));
					return 100;
				} else {
					sleep(random(200, 300));
				}
			} else {
				goTArea(getLTA());
				RSNPC drag = npcs.getNearest(drags);
				while (getMyPlayer().isMoving()) {
					if (drag != null) {
						if (drag.isOnScreen()) {
							if (npcIsInArea(getLTA(), drag)) {
								drag.doAction("Attack");
								sleep(random(800, 1200));
							}
						}
					}
				}
				if (drag != null) {
					if (drag.isOnScreen()) {
						if (npcIsInArea(getLTA(), drag)) {
							drag.doAction("Attack");
							sleep(random(800, 1200));
						}
					}
				}

			}
		} else {
			try {
				if (!isInArea(getLTA())) {
					if (getMyPlayer().getInteracting() != null) {
						if (getMyPlayer().getInteracting().getName().contains("Green")) {
							sleep(200, 300);
						} else {
							goTArea(getLTA());
							while (getMyPlayer().isMoving()) {
								RSNPC drag = npcs.getNearest(drags);
								if (drag != null) {
									if (drag.isOnScreen()) {
										if (npcIsInArea(getLTA(), drag)) {
											drag.doAction("Attack");
										}
										sleep(random(800, 1200));
									}
								}
							}
						}
					} else {
						goTArea(getLTA());
						while (getMyPlayer().isMoving()) {
							RSNPC drag = npcs.getNearest(drags);
							if (drag != null) {
								if (drag.isOnScreen()) {
									if (npcIsInArea(getLTA(), drag)) {
										drag.doAction("Attack");
										sleep(random(800, 1200));
									}
								}
							}
						}
					}
				}
			} catch (NullPointerException e) {
				//do nothing
			}
			RSNPC drag = npcs.getNearest(drags);
			if (drag != null) {
				if (getArea(drag) == getArea(getMyPlayer())) {
					drag.doAction("Attack");
					sleep(random(800, 1200));
				}
			}
		}
		return 100;
	}

	private int antiban() {
		int i = random(0, 30);
		int ii = random(0, 25);
		if ((ii == 3) || (ii == 12)) {
			char dir = 37;
			if (random(0, 3) == 2) {
				dir = 39;
			}
			keyboard.pressKey(dir);
			sleep(random(500, 2000));
			keyboard.releaseKey(dir);
			return random(0, 500);
		} else if ((i == 7) || (i == 4)) {
			return random(0, 500);
		} else if ((i == 1) || (i == 8) || (i == 15) || (i == 20)) {
			Thread camera = new Thread() {

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
			Thread mouseThread = new Thread() {

				@Override
				public void run() {
					mouse.move(random(0, 500), random(0, 500));
				}
			};
			if ((i == 7) || (i == 20)) {
				camera.start();
			}
			if (i == 1) {
				mouseThread.start();
			}
			while (camera.isAlive() || mouseThread.isAlive()) {
				sleep(random(100, 300));
				return random(300, 700);
			}
		}
		return random(1000, 1500);

	}

	private void goTArea(int[] a) {
		RSCharacter nc = getMyPlayer().getInteracting();
		if (nc != null) {
			if (!nc.getName().contains("Green")) {
				if (a == ne) {
					if (!isInArea(ne)) {
						walking.walkTileMM(walking.getPath(new RSTile(3316, 5456)).getNext());
					}
				} else if (a == se) {
					if (!isInArea(se)) {
						walking.walkTileMM(walking.getPath(new RSTile(3315, 5449)).getNext());
					}
				} else if (a == sw) {
					if (!isInArea(sw)) {
						walking.walkTileMM(walking.getPath(new RSTile(3305, 5450)).getNext());
					}
				} else if (a == nw) {
					if (!isInArea(nw)) {
						walking.walkTileMM(walking.getPath(new RSTile(3306, 5461)).getNext());
					}
				}
			}
		} else {
			if (a == ne) {
				if (!isInArea(ne)) {
					walking.walkTileMM(walking.getPath(new RSTile(3316, 5456)).getNext());
				}
			} else if (a == se) {
				if (!isInArea(se)) {
					walking.walkTileMM(walking.getPath(new RSTile(3315, 5449)).getNext());
				}
			} else if (a == sw) {
				if (!isInArea(sw)) {
					walking.walkTileMM(walking.getPath(new RSTile(3305, 5450)).getNext());
				}
			} else if (a == nw) {
				if (!isInArea(nw)) {
					walking.walkTileMM(walking.getPath(new RSTile(3306, 5461)).getNext());
				}
			}
		}
	}

	public int[] getLTA() {
		Timer[] timers = {tse, tsw, tne, tnw};
		int n = timers.length;
		Timer t;
		for (int i = 0; i < n; i++) {
			for (int j = 1; j < (n - i); j++) {
				if (timers[j - 1].getRemaining() > timers[j].getRemaining()) {
					t = timers[j - 1];
					timers[j - 1] = timers[j];
					timers[j] = t;
				}
			}
		}
		for (int i = 0; i < timers.length; i++) {
			if (timers[i].getRemaining() != 0) {
				if (timers[i] == tse) {
					return se;
				} else if (timers[i] == tsw) {
					return sw;
				} else if (timers[i] == tne) {
					return ne;
				} else if (timers[i] == tnw) {
					return nw;
				}
			}
		}

		return null;
	}

	public int[] getArea(RSCharacter drag) {
		if (drag != null) {
			if (npcIsInArea(ne, drag)) {
				return ne;
			} else if (npcIsInArea(se, drag)) {
				return se;
			} else if (npcIsInArea(sw, drag)) {
				return sw;
			} else if (npcIsInArea(nw, drag)) {
				return nw;
			}
		}
		return null;
	}

	public RSNPC[] getDA() {
		final ArrayList<RSNPC> RSNPCList = new ArrayList<RSNPC>();

		RSNPC[] monster = npcs.getAll();
		for (int x = 0; x < monster.length; x++) {
			if (monster[x].getName().contains("Green")) {
				RSNPCList.add(monster[x]);
			}
		}
		return RSNPCList.toArray(new RSNPC[RSNPCList.size()]);
	}

	public boolean openTab(int tab) {
		if (!bank.isOpen()) {
			return false;
		} else {
			try {
				return bank.getInterface().getComponent(68 - (tab * 2)).doClick();
			} catch (NullPointerException e) {
				return false;
			}
		}
	}

	private void updateTimers() {
		gd = getDA();
		for (int j = 0; j < gd.length; j++) {
			RSNPC inter = gd[j];
			if (inter != null) {
				if (getArea(inter) == se) {
					if (inter.getAnimation() == 12250 || inter.getHPPercent() == 0) {
						tse = new Timer(rTime);
						tse.reset();
					}
				} else if (getArea(inter) == sw) {
					if (inter.getAnimation() == 12250 || inter.getHPPercent() == 0) {
						tsw = new Timer(rTime);
						tsw.reset();
					}
				} else if (getArea(inter) == ne) {
					if (inter.getAnimation() == 12250 || inter.getHPPercent() == 0) {
						tne = new Timer(rTime);
						tne.reset();
					}
				} else if (getArea(inter) == nw) {
					if (inter.getAnimation() == 12250 || inter.getHPPercent() == 0) {
						tnw = new Timer(rTime);
						tnw.reset();
					}
				}
			}
		}
	}

	private void doPort() {
		RSObject port = objects.getTopAt(portal);
		if (port != null) {
			if (calc.tileOnScreen(port.getLocation()) && calc.distanceTo(port.getLocation()) <= 3) {
				while (getMyPlayer().isMoving()) {
					sleep(1000, 2000);
				}
				port.doAction("Enter");
				while (getMyPlayer().isMoving()) {
					sleep(1000, 2000);
				}
				sleep(random(2500, 3000));
				walking.walkTileMM(new RSTile(3307, 5460));
				while (getMyPlayer().isMoving()) {
					sleep(1000, 2000);
				}
			} else {
				walkPath(toPort);
			}
		} else {
			walkPath(toPort);
			while (getMyPlayer().isMoving()) {
				sleep(1000, 2000);
			}
		}
	}

	public void messageReceived(MessageEvent sme) {
		String str = sme.getMessage().toString();
		if (str.contains("super strength")) {
			pstr = true;
		}
		if (str.contains("super attack")) {
			patt = true;
		}
		if (str.contains("antifire")) {
			panti = true;
		}
		if (str.contains("fiery breath")) {
			nanti = true;
		}
		if (str.contains("unknown portal")) {
			ntele = true;
		}
		if (str.contains("You have been frozen!")) {
			playerCombat = true;
		}
	}

	private class ProfitListener implements Runnable {
		private void sleep(int t) {
			try {
				Thread.sleep(t);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		public ProfitListener() {
			new Thread(this).start();
		}

		public void kill() {
			Thread.currentThread().interrupt();
		}

		public void run() {
			int lastCount[] = {0, 0, 0, 0};
			while (running) {
				sleep(100);
				if (isPaused()) {
					continue;
				}
				for (int x = 0; x < 4; x++) {
					if (lastCount[x] < inventory.getCount(profitableLoots[x]) && lastCount[x] != 0) {
						totalLoot = totalLoot + prices[x];
					}
				}
				for (int x = 0; x < 4; x++) {
					lastCount[x] = inventory.getCount(profitableLoots[x]);
				}
			}
		}
	}

	class TimeListener {
		long startTime = 0;
		long pausedTime = 0;


		public TimeListener() {
			startTime = System.currentTimeMillis();
		}

		public long getMillis() {
			return System.currentTimeMillis() - startTime - pausedTime;
		}

		public long getSeconds() {
			return this.getMillis() / 1000;
		}

		public long getMinutes() {
			return this.getSeconds() / 60;
		}

		public long getHours() {
			return this.getMinutes() / 60;
		}

		public String getRuntimeString() {
			final long HoursRan = this.getHours();
			long MinutesRan = this.getMinutes();
			long SecondsRan = this.getSeconds();
			MinutesRan = MinutesRan % 60;
			SecondsRan = SecondsRan % 60;
			return HoursRan + ":" + MinutesRan + ":" + SecondsRan;
		}

		public long calcPerHour(final long i) {
			return calcPerHour((double) i);
		}

		public long calcPerHour(final double i) {
			final double elapsed_millis = this.getMillis();
			return (long) ((i / elapsed_millis) * 3600000);
		}

	}

	public class GDK extends javax.swing.JFrame {

		public GDK() {
			initComponents();
		}

		private void initComponents() {
			jPanel1 = new javax.swing.JPanel();
			jLabel1 = new javax.swing.JLabel();
			jCheckBox1 = new javax.swing.JCheckBox();
			jCheckBox2 = new javax.swing.JCheckBox();
			jCheckBox3 = new javax.swing.JCheckBox();
			jButton1 = new javax.swing.JButton();
			jCheckBox4 = new javax.swing.JCheckBox();
			jTextField1 = new javax.swing.JTextField();
			jTextField2 = new javax.swing.JTextField();
			jSlider1 = new javax.swing.JSlider();
			jLabel2 = new javax.swing.JLabel();

			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

			jPanel1.setBackground(new java.awt.Color(0, 0, 0));

			jLabel1.setFont(new java.awt.Font("Viner Hand ITC", 0, 36));
			jLabel1.setForeground(new java.awt.Color(204, 0, 0));
			jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			jLabel1.setText("Ownageful GDK");

			jCheckBox1.setBackground(new java.awt.Color(0, 0, 0));
			jCheckBox1.setForeground(new java.awt.Color(255, 0, 0));
			jCheckBox1.setSelected(true);
			jCheckBox1.setText("Eat Food");

			jCheckBox2.setBackground(new java.awt.Color(0, 0, 0));
			jCheckBox2.setForeground(new java.awt.Color(255, 0, 0));
			jCheckBox2.setSelected(true);
			jCheckBox2.setText("Drink Supers");

			jCheckBox3.setBackground(new java.awt.Color(0, 0, 0));
			jCheckBox3.setForeground(new java.awt.Color(255, 0, 0));
			jCheckBox3.setSelected(true);
			jCheckBox3.setText("Drink AntiFire");

			jButton1.setBackground(new java.awt.Color(0, 0, 0));
			jButton1.setText("Start");
			jButton1.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			jCheckBox4.setBackground(new java.awt.Color(0, 0, 0));
			jCheckBox4.setForeground(new java.awt.Color(255, 51, 0));
			jCheckBox4.setSelected(true);
			jCheckBox4.setText("Loot Charms");
			jCheckBox4.setSelected(false);

			jTextField1.setText("Food ID");
			jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {

				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					jTextField1MouseClicked(evt);
				}
			});

			jTextField2.setText("Amount");
			jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {

				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					jTextField2MouseClicked(evt);
				}
			});

			jSlider1.setBackground(new java.awt.Color(0, 0, 0));
			jSlider1.setForeground(new java.awt.Color(204, 0, 0));
			jSlider1.setPaintLabels(true);

			jLabel2.setForeground(new java.awt.Color(204, 0, 0));
			jLabel2.setText("Eat food at (%)");


			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox3).addComponent(jCheckBox1).addComponent(jCheckBox2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(jCheckBox4))).addGap(51, 51, 51))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1))).addGap(27, 27, 27)))));
			jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jCheckBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jCheckBox2).addComponent(jCheckBox4))).addGroup(jPanel1Layout.createSequentialGroup().addGap(82, 82, 82).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jTextField1).addComponent(jTextField2)).addGap(40, 40, 40))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1)).addContainerGap()));

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));

			pack();
		}

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
			try {
				if (jCheckBox1.isSelected()) {
					food = Integer.parseInt(jTextField1.getText());
					withdraw = Integer.parseInt(jTextField2.getText());
					eat = true;
				}
				if (jCheckBox3.isSelected()) {
					useAnti = true;
				}
				if (jCheckBox2.isSelected()) {
					useSupers = true;
				}
				if (jCheckBox4.isSelected()) {
					lCharms = true;
				}
				hp = jSlider1.getValue();
				this.dispose();
				startScript = true;
			} catch (NumberFormatException numberFormatException) {
				log("Invalid food id format.");
			}
		}

		private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {
			jTextField1.setText("");
		}

		private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {
			jTextField2.setText("");
		}

		private javax.swing.JButton jButton1;
		private javax.swing.JCheckBox jCheckBox1;
		private javax.swing.JCheckBox jCheckBox2;
		private javax.swing.JCheckBox jCheckBox3;
		private javax.swing.JCheckBox jCheckBox4;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JSlider jSlider1;
		private javax.swing.JTextField jTextField1;
		private javax.swing.JTextField jTextField2;
	}
}