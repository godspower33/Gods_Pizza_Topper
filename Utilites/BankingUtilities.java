package scripts.tribot.PizzaTopper.Utilites;

import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSInterface;

public class BankingUtilities {

	// Checks if bank items are loaded or not
    public static boolean isBankItemsLoaded() {
        return getCurrentBankSpace() == Banking.getAll().length;
    }

    // Gets current bank space
    public static int getCurrentBankSpace() {
           RSInterface amount = Interfaces.get(Constants.BANK_MASTER,Constants.BANK_AMOUNT_CHILD);
           if(amount != null) {
               String txt = amount.getText();
               if(txt != null) {
                   try {
                       int toInt = Integer.parseInt(txt);
                       if(toInt > 0)
                           return toInt;
                   } catch(NumberFormatException e) {
                       return -1;
                   }
               }
           }
           return -1;
   }
    
	// Check if has supplies in bank for pizza making
	public static boolean hasSuppliesInBank() {
		return Banking.find(Variables.topping).length > 0 && Banking.find(Constants.PLAIN_PIZZA).length > 0;
	}
	
	// Check if has supplies in bank for pineapple ring making
	public static boolean hasPineappleSuppliesInBank() {
		return Banking.find(Constants.PINEAPPLE).length > 0 && 
				(Inventory.find(Constants.KNIFE).length > 0 || Banking.find(Constants.KNIFE).length > 0);
	}
	
}
