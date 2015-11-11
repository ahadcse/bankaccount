package se.tradedoubler.bankaccount.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.tradedoubler.bankaccount.model.Account;
import se.tradedoubler.bankaccount.model.Transaction;

/**
 * This class is used as a data storage in memory. It keeps all data in memory (Bullet point 1)
 */
public class DataStore {
    private static DataStore instance = new DataStore();
    private Map<Integer, Account> accounts = new HashMap<>();
    private Map<Integer, List<Transaction>> accountTransactions = new HashMap<>();
    private Map<Integer, Transaction> transactions = new HashMap<>();

    /**
     * Constructor
     */
    private DataStore() {
    }

    /**
     * Single instance of this class will be available throughout the module
     *
     * @return Single instance
     */
    public static DataStore getInstance() {
        return instance;
    }

    /**
     * For test case.
     */
    protected void clear() {
        accounts.clear();
        accountTransactions.clear();
        transactions.clear();
    }

    /**
     * Add account
     *
     * @param account An Account object
     * @return Account Id
     */
    public int addAccount(Account account) {
        // Synchronized so that same account id is not assigned for 2 accounts.
        synchronized (accounts) {
            int accountId = accounts.size() + 1;
            account.setAccountId(accountId);
            accounts.put(account.getAccountId(), account);
            return accountId;
        }
    }

    /**
     * Add transaction
     *
     * @param transaction Transaction
     */
    public void addTransaction(Transaction transaction) {
        int accountId = transaction.getAccount().getAccountId();
        synchronized (transactions) {
            transaction.setTransactionId(transactions.size());
            transactions.put(transaction.getTransactionId(), transaction);
        }
        synchronized (accountTransactions) {
            List<Transaction> accTransactions = accountTransactions.get(accountId);
            if (accTransactions == null) {
                accTransactions = new ArrayList<>();
                accountTransactions.put(accountId, accTransactions);
            }
            accTransactions.add(transaction);
        }
    }

    /**
     * Add support for retrieving account information (Bullet point 2)
     *
     * @param accountId Account Id
     * @return retrieved account.
     */
    public Account getAccount(int accountId) {
        return accounts.get(accountId);
    }

    /**
     * Add support for retrieving account history - 10 latest transactions shall be available (Bullet point 3)
     *
     * @param accountId        Account Id
     * @param transactionCount Number of transactions
     * @return List of Transaction
     */
    public List<Transaction> getLatestTransactions(int accountId, int transactionCount) {
        List<Transaction> accTransactions = accountTransactions.get(accountId);
        if (accTransactions == null || accTransactions.isEmpty()) {
            return Collections.emptyList();
        }
        // Sort by date descending.
        Collections.sort(accTransactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction tx1, Transaction tx2) {
                return (int) (tx2.getDate().getTime() - tx1.getDate().getTime());
            }
        });
        return accTransactions.subList(0, Math.min(accTransactions.size(), transactionCount));
    }

    /**
     * Add support for retrieving accounts, in account balance order, for those accounts with balance exceeding
     * specified balance limit (Bullet point 4).
     *
     * @param balanceLimit Specified balance limit
     * @return List of Accounts that exceeds balance
     */
    public List<Account> getAccountsExceedingBalance(float balanceLimit) {
        List<Account> foundAccounts = new ArrayList<>();
        final Map<Integer, Float> accountBalances = new HashMap<>();
        for (Account account : accounts.values()) {
            float accountBalance = getAccountBalance(account.getAccountId());
            if (accountBalance > balanceLimit) {
                foundAccounts.add(account);
                accountBalances.put(account.getAccountId(), accountBalance); // Keeping the balance to sort later.
            }
        }
        // Sort in balance's descending order.
        Collections.sort(foundAccounts, new Comparator<Account>() {
            @Override
            public int compare(Account account1, Account account2) {
                return (int) (100 * (accountBalances.get(account2.getAccountId()) - accountBalances.get(account1.getAccountId())));
            }
        });
        return foundAccounts;
    }

    /**
     * Negative account balance is allowed (Bullet point 5)
     *
     * @param accountId Account Id
     * @return balance of the account
     */
    public float getAccountBalance(int accountId) {
        List<Transaction> accTransactions = accountTransactions.get(accountId);
        if (accTransactions == null || accTransactions.isEmpty()) {
            return 0.0f;
        }
        float balance = 0.0f;
        for (Transaction accTransaction : accTransactions) {
            if (accTransaction.isDeposite()) {
                balance += accTransaction.getAmount();
            } else if (accTransaction.isWithdraw()) {
                balance -= accTransaction.getAmount();
            }
        }
        return balance;
    }
}
