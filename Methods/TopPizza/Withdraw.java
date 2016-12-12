package scripts.tribot.PizzaTopper.Methods.TopPizza;

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
import scripts.tribot.PizzaTopper.Utilites.Variables;

public class Withdraw extends Node {

	@Override
	public boolean isNodeValid() {
		return Banking.isBankScreenOpen() && !Utilities.hasStuff() && !PizzaMain.rings;
	}

	@Override
	public void execute() {
		PizzaMain.status = "Withdrawing";
		if (Banking.isBankScreenOpen()) {
			if (BankingUtilities.isBankItemsLoaded()) {
				if (!BankingUtilities.hasSuppliesInBank()) {
					General.println("You've run out of supplies, stopping script");
					PizzaMain.stopScript = true;
				}
			}
			if (Banking.withdraw(14, Constants.PLAIN_PIZZA)) {
		      	Timing.waitCondition(new Condition() {
	    			@Override
	    			public boolean active() {
	    				General.sleep(250, 400);
	    				return Inventory.getCount(Constants.PLAIN_PIZZA) > 0;
	    			}
	    		}, 3000);
		      	if (Banking.withdraw(14, Variables.topping)) {
			      	Timing.waitCondition(new Condition() {
		    			@Override
		    			public boolean active() {
		    				General.sleep(250, 400);
		    				return Inventory.getCount(Variables.topping) > 0;
		    			}
		    		}, 3000);
		      	}
		   }
		} 
		if (Inventory.getCount(Constants.PLAIN_PIZZA) > 0
			&& Inventory.getCount(Variables.topping) > 0) {
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
