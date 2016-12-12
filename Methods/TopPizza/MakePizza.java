package scripts.tribot.PizzaTopper.Methods.TopPizza;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;

import scripts.AntiBan;
import scripts.tribot.PizzaTopper.Node;
import scripts.tribot.PizzaTopper.PizzaMain;
import scripts.tribot.PizzaTopper.Utilites.Constants;
import scripts.tribot.PizzaTopper.Utilites.Utilities;
import scripts.tribot.PizzaTopper.Utilites.Variables;

public class MakePizza extends Node {

	private RSInterfaceChild makeAll = Interfaces.get(Constants.MAKE_ALL_MASTER, Constants.MAKE_ALL_CHILD);
	
	@Override
	public boolean isNodeValid() {
		return !PizzaMain.rings && Utilities.hasStuff() && !Banking.isBankScreenOpen();
	}

	@Override
	public void execute() {
		PizzaMain.status = "Making pizza";
		RSItem[] selectedTopping = Inventory.find(Variables.topping);
		RSItem[] pizza = Inventory.find(Constants.PLAIN_PIZZA);
		
		if (Clicking.click("Use", pizza[0])) {
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.random(100, 200);
					return Game.isUptext("Use");
				}
			}, 3000);
			
			if (Game.isUptext("Use")) {
				if (Clicking.click(selectedTopping[0])) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							RSInterface makeAll = Interfaces.get(Constants.MAKE_ALL_MASTER);
							General.random(100, 200);
							return makeAll != null;
						}
					}, 3000);
				}
			}
			
			if (makeAll != null) {
				if (makeAll.click("Make All")) {
					if (Inventory.getCount(Constants.PLAIN_PIZZA) >= General.random(4, 8)) {
						AntiBan.leaveGame();
					}
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.random(100, 200);
							return !Utilities.isMakingPizza();
						}
					}, General.random(21000, 22500));
				}
			}
		}
	}
}
