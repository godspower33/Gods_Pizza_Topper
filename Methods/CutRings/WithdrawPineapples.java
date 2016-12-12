package scripts.tribot.PizzaTopper.Methods.CutRings;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.tribot.PizzaTopper.Node;
import scripts.tribot.PizzaTopper.PizzaMain;
import scripts.tribot.PizzaTopper.Utilites.BankingUtilities;
import scripts.tribot.PizzaTopper.Utilites.Constants;
import scripts.tribot.PizzaTopper.Utilites.Utilities;

public class WithdrawPineapples extends Node {

	@Override
	public boolean isNodeValid() {
		return PizzaMain.rings && Banking.isBankScreenOpen() && !Utilities.hasPineappleStuff();
	}

	@Override
	public void execute() {
		PizzaMain.status = "Withdrawing pineapples";
		if (Banking.isBankScreenOpen()) {
			if (BankingUtilities.isBankItemsLoaded()) {
				if (!BankingUtilities.hasPineappleSuppliesInBank()) {
					General.println("You've run out of pineapples supplies, stopping script");
					PizzaMain.stopScript = true;
				}
			}
			if (Inventory.getCount(Constants.KNIFE) == 0) {
				if (Banking.withdraw(1, Constants.KNIFE)) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Inventory.getCount(Constants.KNIFE) > 0;
						}
					}, 3000);
				}
			}
			if (Banking.withdraw(6, Constants.PINEAPPLE)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return Inventory.getCount(Constants.PINEAPPLE) > 0;
					}
				}, 3000);
			}
		}
		if (Inventory.getCount(Constants.KNIFE) > 0
			&& Inventory.getCount(Constants.PINEAPPLE) > 0) {
			if (Banking.close()) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						General.sleep(250, 400);
						return !Banking.isBankScreenOpen();
					}
				}, 3000);
			}
		}
	}
}
