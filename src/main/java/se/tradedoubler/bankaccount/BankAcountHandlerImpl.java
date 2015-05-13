package se.tradedoubler.bankaccount;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import se.tradedoubler.bankaccount.exceptions.BankAccountException;
import se.tradedoubler.bankaccount.model.Account;
import se.tradedoubler.bankaccount.model.Transaction;
import se.tradedoubler.bankaccount.model.TransactionType;
import se.tradedoubler.bankaccount.store.DataStore;

/**
 * Implements IBankAccountHandler interface
 */
public class BankAcountHandlerImpl implements IBankAccountHandler
{
	private static final Logger logger = Logger.getLogger(BankAcountHandlerImpl.class);

	private DataStore dao = DataStore.getInstance();
	private List<IBankAccountEventListener> accountEventListeners = new ArrayList<>();

	/**
	 * Class constructor
	 */
	public BankAcountHandlerImpl()
	{
		super();
	}

	/**
	 * Support for event listener
	 * 
	 * @param accountEventListener
	 *            IBankAccountEventListener object
	 */
	public void addEventListener(IBankAccountEventListener accountEventListener)
	{
		accountEventListeners.add(accountEventListener);
	}

	public int createBankAccount(String accountName) throws BankAccountException
	{
		if (StringUtils.isEmpty(accountName))
		{
			logger.info("Rejected: Empty account name");
			throw new BankAccountException("Rejected: Empty account name");
		}

		Account account = new Account();
		account.setAccountName(accountName);
		return dao.addAccount(account);
	}

	public float withdrawal(int bankAccountId, float amount) throws BankAccountException
	{
		if (amount < 0)
		{
			logger.info("Rejected: Account " + bankAccountId + ": Illegal amount: " + amount);
			throw new BankAccountException("Rejected: Account " + bankAccountId + ": Illegal amount: " + amount);
		}

		Account account = dao.getAccount(bankAccountId);
		if (account == null)
		{
			logger.warn("Rejected: Account " + bankAccountId + " does not exists");
			throw new BankAccountException("Rejected: Account " + bankAccountId + " does not exists");
		}

		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(amount);
		transaction.setDate(new Date());
		transaction.setType(TransactionType.WITHDRAW);
		dao.addTransaction(transaction);

		float balance = getBalance(account.getAccountId());
		tryNotifyForNegativeBalance(bankAccountId, balance);
		logger.info("Transaction: Account " + amount + ": withdraw: $" + amount + ", balance: $" + balance);
		return balance;
	}

	private void tryNotifyForNegativeBalance(int bankAccountId, float balance)
	{
		if (balance < 0.0f)
		{
			for (IBankAccountEventListener accountEventListener : accountEventListeners)
			{
				accountEventListener.onNegativeAccountBalance(bankAccountId, balance);
			}
		}
	}

	public float deopsit(int bankAccountId, float amount) throws BankAccountException
	{
		if (amount < 0)
		{
			logger.info("Rejected: Account " + bankAccountId + ": Illegal amount: " + amount);
			throw new BankAccountException("Rejected: Account " + bankAccountId + ": Illegal amount: " + amount);
		}

		Account account = dao.getAccount(bankAccountId);
		if (account == null)
		{
			logger.warn("Rejected: Account " + bankAccountId + " does not exists");
			throw new BankAccountException("Rejected: Account " + bankAccountId + " does not exists");
		}

		Transaction transaction = new Transaction();
		transaction.setAccount(account);
		transaction.setAmount(amount);
		transaction.setDate(new Date());
		transaction.setType(TransactionType.DEPOSITE);
		dao.addTransaction(transaction);

		float balance = getBalance(account.getAccountId());
		logger.info("Transaction: Account " + bankAccountId + ": deposit: $" + amount + ", balance: $" + balance);
		return balance;
	}

	@Override
	public float getBalance(int bankAccountId)
	{
		return dao.getAccountBalance(bankAccountId);
	}
}
