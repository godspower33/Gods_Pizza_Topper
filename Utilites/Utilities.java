package scripts.tribot.PizzaTopper.Utilites;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Skills;

import scripts.AntiBan;

public class Utilities {

	// Method to determine if making pizzas
	public static boolean isMakingPizza() {
		boolean a = true;
		long lastPizza = Skills.SKILLS.COOKING.getXP();
		if ((Inventory.getCount(Variables.topping) == 0) && (Inventory.getCount(Constants.PLAIN_PIZZA) == 0)
			|| NPCChat.getMessage() != null) {
				return a == false;
			} else {
			if (Inventory.getCount(Variables.topping) >= 1
				&& Inventory.getCount(Constants.PLAIN_PIZZA) >= 1
				&& lastPizza < Skills.SKILLS.COOKING.getXP()) {
				if (Inventory.getCount(Constants.PLAIN_PIZZA) >= General
						.random(4, 8)) {
					AntiBan.leaveGame();
				}
				AntiBan.timedActions();
				return a == true;
			}
		}
		return a;
	}
	
	// Checks if has stuff for pizza making in inventory.
	public static boolean hasStuff() { 
		return Inventory.find(Variables.topping).length > 0 && Inventory.find(Constants.PLAIN_PIZZA).length > 0;
	}
	
	public static boolean isCuttingRings() {
        return Inventory.getCount(Constants.PINEAPPLE) > 0;
	}
	
	//Checks if it has stuff for pineapple making
	public static boolean hasPineappleStuff() {
		return Inventory.find(Constants.PINEAPPLE).length > 0 && Inventory.find(Constants.KNIFE).length > 0;
	}
}
