package scripts.tribot.PizzaTopper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Login.STATE;
import org.tribot.api2007.util.ThreadSettings;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;
import org.tribot.script.interfaces.Starting;

import scripts.AntiBan;
import scripts.tribot.PizzaTopper.Methods.CutRings.*;
import scripts.tribot.PizzaTopper.Methods.TopPizza.*;
import scripts.tribot.PizzaTopper.Utilites.Variables;
import scripts.tribot.lib.game.observers.FCInventoryListener;
import scripts.tribot.lib.game.observers.FCInventoryObserver;

@ScriptManifest(
		authors = { "Godspower33" }, 
		category = "Cooking", name = "Gods AIO Pizza Topper 2",
		description = "Makes pizzas or cuts pineapple rings")
public class PizzaMain extends Script implements Painting, MessageListening07, Starting, FCInventoryListener {

	private final FCInventoryObserver INV_OBSERVER = new FCInventoryObserver();
	public ArrayList<Node> nodes = new ArrayList<Node>();
	
    public static String status; // status
    public static boolean stopScript = false;
     
    // --- stuff needed for script ---- \\
    private int pizzasMade, ringsCut;
	public static boolean rings; // make rings?
	boolean makingRings;
    
	@Override
	public void run() {
		Mouse.setSpeed(General.random(88, 108));
	    General.useAntiBanCompliance(true);
		INV_OBSERVER.addListener(this);
		ThreadSettings.get().setClickingAPIUseDynamic(true);
		initialize();
		while (!stopScript) {
			if (Login.getLoginState() != STATE.LOGINSCREEN) {
				for (final Node n : nodes) {
					if (n.isNodeValid()) {
						n.execute();
					} else {
						status = "AntiBan";
			            AntiBan.timedActions();
					}
					General.sleep(20, 35);
				}
			}
			General.sleep(75, 105);
		}
	}

	public void initialize() {
		status = "Waiting for user to enter settings";
        rings = Boolean.parseBoolean(JOptionPane.showInputDialog("Make pineapple rings? Enter true / false"));
        if (!rings) {
        	Variables.topping = Integer.parseInt(JOptionPane.showInputDialog("Enter Topping ID(2118 for pineapple, 319 for anchovies, 2142 for meat)"));
        }
		if (rings) {
			Collections.addAll(nodes, new BankCuttingRings(), new MakeRings(),
					new WithdrawPineapples());
		}
		if (!rings) {
			Collections.addAll(nodes, new Bank(), new MakePizza(),
					new Withdraw());
		}
	}
	
	//Setting start time and font
    private static final long startTime = System.currentTimeMillis();
	Font font = new Font("Verdana", Font.BOLD, 12);

	// Method for adding a graphic to paint
	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	private final Image IMG = getImage(""); // http://i.imgur.com/TUlYgDr.png
	
	@Override
	public void onPaint(Graphics r) {
		Graphics2D gg = (Graphics2D)r;
		gg.drawImage(IMG, 0, 340, null);
		
		long timeRan = System.currentTimeMillis() - startTime;
		int pizzaPerHour = (int) (pizzasMade * 3600000d / timeRan);
		int ringsPerHour = (int) (ringsCut *3600000d / timeRan);
		
		   r.setFont(font);
	       r.setColor(Color.RED);
	       r.drawString("Pizza Maker V1.0", 220, 360);
	       r.drawString("Running for: " + Timing.msToString(timeRan), 220, 375);
	       r.drawString("Status: " + status, 220, 390);
	    if (!rings) {
           r.drawString("Pizzas: " + pizzasMade, 220, 405);
	       r.drawString("Pizza/hr: " + pizzaPerHour, 220, 420);
	    }
		if (rings) {
			r.drawString("Rings cut: " + ringsCut, 220, 405);
			r.drawString("Rings/hr: " + ringsPerHour, 220, 420);
		}
	}
	
	@Override
	public void clanMessageReceived(String arg0, String arg1) {	}

	@Override
	public void duelRequestReceived(String arg0, String arg1) {	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) { }

	@Override
	public void serverMessageReceived(String arg0) {
		if (arg0.contains("You add the")) {
			pizzasMade++;
		}
		if (arg0.contains("You cut the")) {
			makingRings = true;
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {	}

	@Override
	public void onStart() {
        AntiBan.setPrintDebug(true);	
	}

	@Override
	public void inventoryAdded(String i, int count) {
		if (i.contains("Pineapple ring") && count == 4) {
			ringsCut += 4;
		}
	}

	@Override
	public void inventoryRemoved(String id, int count) {	}
	
}