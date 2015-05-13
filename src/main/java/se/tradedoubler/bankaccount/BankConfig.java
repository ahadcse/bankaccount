package se.tradedoubler.bankaccount;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * This class is the configuration class for whole module
 */
public class BankConfig
{
	private static BankConfig instance = new BankConfig();

	/**
	 * Constructor class
	 */
	private BankConfig()
	{
		configureLog();
	}

	/**
	 * Singleton
	 * 
	 * @return Single instance
	 * 
	 */
	public static BankConfig getInstance()
	{
		return instance;
	}

	/**
	 * Configures logging
	 */
	private void configureLog()
	{
		try
		{
			Properties logProperties = new Properties();
			logProperties.load(this.getClass().getResourceAsStream("/log4j.properties"));
			PropertyConfigurator.configure(logProperties);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
