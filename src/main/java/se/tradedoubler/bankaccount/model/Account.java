package se.tradedoubler.bankaccount.model;

/**
 * A simple POJO class for account information
 */
public class Account
{
	private int accountId;
	private String accountName;

	public int getAccountId()
	{
		return accountId;
	}

	public void setAccountId(int accountId)
	{
		this.accountId = accountId;
	}

	public String getAccountName()
	{
		return accountName;
	}

	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
}
