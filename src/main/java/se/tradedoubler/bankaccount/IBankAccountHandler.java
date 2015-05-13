package se.tradedoubler.bankaccount;

import se.tradedoubler.bankaccount.exceptions.BankAccountException;

public interface IBankAccountHandler
{
	/**
	 * Create new account.
	 * 
	 * @param accountName
	 *            Name of the account
	 * 
	 * @return Bank account id for created account
	 * @throws BankAccountException if exception occurs 
	 */
	int createBankAccount(String accountName) throws BankAccountException;

	/**
	 * Withdrawal from account.
	 * 
	 * @param bankAccountId
	 *            Account id
	 * @param amount
	 *            The amount to withdrawal
	 * 
	 * @return Account balance after withdrawal
	 * @throws BankAccountException
	 *             if exception occurs
	 */
	float withdrawal(int bankAccountId, float amount) throws BankAccountException;

	/**
	 * Deposit to account.
	 * 
	 * @param bankAccountId
	 *            Account id
	 * @param amount
	 *            Amount to deposit
	 *            
	 * @return Account balance after deposit
	 * @throws BankAccountException if exception occurs 
	 */
	float deopsit(int bankAccountId, float amount) throws BankAccountException;

	/**
	 * Get balance of an account.
	 * 
	 * @param bankAccountId
	 *            Account id
	 * 
	 * @return Get balance of an account
	 */
	float getBalance(int bankAccountId);

}
