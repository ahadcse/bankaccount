package se.tradedoubler.bankaccount;


/**
 * This interface is used to provide notification.
 */
public interface IBankAccountEventListener
{
	/**
	 * Notifies when balance is negative
	 * 
	 * @param bankAccountId
	 *            Account id
	 * @param balance
	 *            The current balance of account
	 */
	void onNegativeAccountBalance(int bankAccountId, float balance);
}
