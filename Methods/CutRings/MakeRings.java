package scripts.tribot.PizzaTopper.Methods.CutRings;

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

import scripts.tribot.PizzaTopper.Node;
import scripts.tribot.PizzaTopper.PizzaMain;
import scripts.tribot.PizzaTopper.Utilites.Constants;
import scripts.tribot.PizzaTopper.Utilites.Utilities;

public class MakeRings extends Node {

    private RSInterfaceChild makeRings = Interfaces.get(Constants.MAKE_RINGS_MASTER, Constants.MAKE_RINGS_CHILD);
	private RSInterfaceChild makeAll = Interfaces.get(Constants.MAKE_ALL_MASTER, Constants.MAKE_ALL_CHILD);
	
	@Override
	public boolean isNodeValid() {
		return PizzaMain.rings && Utilities.hasPineappleStuff() && !Banking.isBankScreenOpen();
	}

	@Override
	public void execute() {
		PizzaMain.status = "Making rings";
		RSItem[] knif = Inventory.find(Constants.KNIFE);
		RSItem[] pine = Inventory.find(Constants.PINEAPPLE);
		if (Clicking.click("Use", knif[0])) {
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					General.random(100, 200);
					return (Game.isUptext("Use"));
				}
			}, General.random(2500, 4500));
		}
		if (Game.isUptext("Use")) {
			if (Clicking.click(pine[0])) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						RSInterface child = Interfaces.get(Constants.MAKE_RINGS_MASTER);
						General.random(100, 200);
						return child != null;
					}
				}, General.random(2500, 4500));
			}
		}

		if (makeRings != null) {
			if (makeRings.click("Continue")) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						RSInterface child = Interfaces.get(Constants.MAKE_ALL_MASTER);
						General.random(300, 600);
						return child != null;
					}
				}, 3000);
			}
			if (makeAll != null) {
				if (makeAll.click("Make All")) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							General.random(600, 900);
							return !Utilities.isCuttingRings();
						}
					}, General.random(8000, 8900));
				}
			}
		}
	}
}
