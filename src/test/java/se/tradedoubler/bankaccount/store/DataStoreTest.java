package se.tradedoubler.bankaccount.store;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.tradedoubler.bankaccount.model.Account;
import se.tradedoubler.bankaccount.model.Transaction;
import se.tradedoubler.bankaccount.model.TransactionType;

/**
 * Test class to check the memory data store is working properly (Bullet point 1)
 */
public class DataStoreTest
{
	private DataStore dataStore = DataStore.getInstance();

	@Before
	public void setUp()
	{
		dataStore.clear();
	}

	/**
	 * Test for retrieving account information (Bullet point 2)
	 */
	@Test
	public void testGetAccount()
	{
		int accountId = dataStore.addAccount(getAccount("Account #1"));

		Account retrievedAccount = dataStore.getAccount(accountId);
		Assert.assertEquals("Account #1", retrievedAccount.getAccountName());
	}

	/**
	 * Test to get the the latest transactions. 3 transaction is done in this methods and other 7 are in next method.
	 * log can be checked from banklog.log file. (Bullet point 3 and 8)
	 */
	@Test
	public void testGetLatestTransactions()
	{
		int accountId = dataStore.addAccount(getAccount("Account #1"));
		Account retrievedAccount = dataStore.getAccount(accountId);

		Transaction tx1 = getTransaction(retrievedAccount, 10.00, getDateNDaysAgo(2), TransactionType.DEPOSITE);
		dataStore.addTransaction(tx1);

		Transaction tx2 = getTransaction(retrievedAccount, 20.00, getDateNDaysAgo(1), TransactionType.WITHDRAW);
		dataStore.addTransaction(tx2);

		Transaction tx3 = getTransaction(retrievedAccount, 30.00, getDateNDaysAgo(3), TransactionType.DEPOSITE);
		dataStore.addTransaction(tx3);

		List<Transaction> transactions = dataStore.getLatestTransactions(accountId, 1);
		Assert.assertEquals(1, transactions.size());
		assertEquals(tx2, transactions.get(0));

		transactions = dataStore.getLatestTransactions(accountId, 2);
		Assert.assertEquals(2, transactions.size());
		assertEquals(tx2, transactions.get(0));
		assertEquals(tx1, transactions.get(1));
	}

	/**
	 * Test for finding the accounts that exceeded specified balance limit (Bullet point 4)
	 */
	@Test
	public void testGetAccountsExceedingBalance()
	{
		int accountId1 = dataStore.addAccount(getAccount("Account #1"));
		Account account1 = dataStore.getAccount(accountId1);
		dataStore.addTransaction(getTransaction(account1, 20.00, getDateNDaysAgo(1), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account1, 20.00, getDateNDaysAgo(2), TransactionType.DEPOSITE));

		int accountId2 = dataStore.addAccount(getAccount("Account #2"));
		Account account2 = dataStore.getAccount(accountId2);
		dataStore.addTransaction(getTransaction(account2, 100.00, getDateNDaysAgo(1), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account2, 200.00, getDateNDaysAgo(2), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account2, 299.00, getDateNDaysAgo(2), TransactionType.WITHDRAW));

		int accountId3 = dataStore.addAccount(getAccount("Account #3"));
		Account account3 = dataStore.getAccount(accountId3);
		dataStore.addTransaction(getTransaction(account3, 16.00, getDateNDaysAgo(1), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account3, 20.00, getDateNDaysAgo(2), TransactionType.DEPOSITE));

		List<Account> exceededAccounts = dataStore.getAccountsExceedingBalance(25.00f);
		Assert.assertEquals(2, exceededAccounts.size());
		Assert.assertEquals("Account #1", exceededAccounts.get(0).getAccountName());
		Assert.assertEquals("Account #3", exceededAccounts.get(1).getAccountName());
	}

	/**
	 * Test to check both positive and negative balance allowed or not (Bullet point 5)
	 */
	@Test
	public void testGetBalance()
	{
		int accountId1 = dataStore.addAccount(getAccount("Account #1"));
		Account account1 = dataStore.getAccount(accountId1);
		dataStore.addTransaction(getTransaction(account1, 100.00, getDateNDaysAgo(1), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account1, 200.00, getDateNDaysAgo(2), TransactionType.DEPOSITE));
		dataStore.addTransaction(getTransaction(account1, 299.00, getDateNDaysAgo(2), TransactionType.WITHDRAW));

		Assert.assertEquals(1, dataStore.getAccountBalance(accountId1), 0.01);

		int accountId2 = dataStore.addAccount(getAccount("Account #2"));
		Account account2 = dataStore.getAccount(accountId2);
		dataStore.addTransaction(getTransaction(account2, 100.00, getDateNDaysAgo(2), TransactionType.WITHDRAW));
		dataStore.addTransaction(getTransaction(account2, 200.00, getDateNDaysAgo(2), TransactionType.WITHDRAW));

		Assert.assertEquals(-300, dataStore.getAccountBalance(accountId2), 0.01);
	}

	private Account getAccount(String accountName)
	{
		Account account = new Account();
		account.setAccountName(accountName);
		return account;
	}

	private Transaction getTransaction(Account account, double amount, Date date, TransactionType type)
	{
		Transaction tx = new Transaction();
		tx.setAccount(account);
		tx.setAmount(amount);
		tx.setDate(date);
		tx.setType(type);

		return tx;
	}

	private Date getDateNDaysAgo(int days)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -days);
		return cal.getTime();
	}

	private void assertEquals(Transaction expectedTransaction, Transaction actualTransaction)
	{
		Assert.assertEquals(expectedTransaction.getAccount().getAccountId(), actualTransaction.getAccount().getAccountId());
		Assert.assertEquals(expectedTransaction.getAmount(), actualTransaction.getAmount(), 0.01);
		Assert.assertEquals(expectedTransaction.getDate().getTime(), actualTransaction.getDate().getTime());
		Assert.assertEquals(expectedTransaction.getType(), actualTransaction.getType());
	}
}
