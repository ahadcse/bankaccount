package se.tradedoubler.bankaccount.model;

import java.util.Date;

/**
 * A simple POJO class for storing transaction information
 */
public class Transaction
{
	private int transactionId;
	private Account account;
	private TransactionType type;
	private Date date;
	private double amount;

	public int getTransactionId()
	{
		return transactionId;
	}

	public void setTransactionId(int transactionId)
	{
		this.transactionId = transactionId;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public TransactionType getType()
	{
		return type;
	}

	public void setType(TransactionType type)
	{
		this.type = type;
	}

	public boolean isWithdraw()
	{
		return type == TransactionType.WITHDRAW;
	}

	public boolean isDeposite()
	{
		return type == TransactionType.DEPOSITE;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public double getAmount()
	{
		return amount;
	}

	public void setAmount(double amount)
	{
		this.amount = amount;
	}
}
