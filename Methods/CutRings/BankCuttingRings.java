package scripts.tribot.PizzaTopper.Methods.CutRings;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;

import scripts.tribot.PizzaTopper.Node;
import scripts.tribot.PizzaTopper.PizzaMain;
import scripts.tribot.PizzaTopper.Utilites.Constants;
import scripts.tribot.PizzaTopper.Utilites.Utilities;

public class BankCuttingRings extends Node {

	@Override
	public boolean isNodeValid() {
		return PizzaMain.rings && !Banking.isBankScreenOpen() && !Utilities.hasPineappleStuff();
	}

	@Override
	public void execute() {
		PizzaMain.status = "opening bank";
		RSObject[] geBank = Objects.findNearest(10, Constants.GE_BANK);
		
		if (geBank.length > 0) {
			if (!Banking.isBankScreenOpen()) {
				if (!geBank[0].isOnScreen()) 
					Camera.turnToTile(geBank[0].getModel());
	
				if (Clicking.click("Bank Grand Exchange booth", geBank[0])) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Banking.isBankScreenOpen();
						}
					}, 6000);
				}
			}
		}
		if (geBank.length < 1) {
			if (!Banking.isBankScreenOpen()) {
				if (Banking.openBank()) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.random(100, 200);
							return Banking.isBankScreenOpen();
						}
					}, 6000);
				}
			}
		}
		if (Banking.isBankScreenOpen()) {
			PizzaMain.status = "depositing";
			Banking.depositAllExcept(Constants.KNIFE);
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					return Inventory.getCount(Constants.KNIFE) > 0;
				}
			}, 4000);
		}
	}
}
