package se.tradedoubler.bankaccount;

import org.apache.log4j.Logger;

/**
 * Implements IBankAccountEventListener interface
 */
public class BankAccountEventListenerImpl implements IBankAccountEventListener
{
	private static final Logger logger = Logger.getLogger(BankAccountEventListenerImpl.class);

	public void onNegativeAccountBalance(int bankAccountId, float balance)
	{
		logger.info("Transaction: Account " + bankAccountId + "is at negative balance" + ", balance: $" + balance);
	}
}
