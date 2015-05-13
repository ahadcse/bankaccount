package se.tradedoubler.bankaccount.exceptions;

import org.apache.log4j.Logger;

/**
 * This class is for exception handling related to this module
 */
final public class BankAccountException extends Exception
{
	private static final long serialVersionUID = -314439670131687936L;
	private static final Logger logger = Logger.getLogger(BankAccountException.class);

	/**
	 * Constructor class
	 * 
	 * @param reason
	 *            Reason of the exception
	 */
	public BankAccountException(String reason)
	{
		super(reason);
		logger.info("Error: " + reason);
	}
}
