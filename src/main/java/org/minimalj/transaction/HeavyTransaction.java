package org.minimalj.transaction;

import org.minimalj.frontend.page.ProgressListener;

public interface HeavyTransaction<T> extends Transaction<T> {

	public void setProgressListener(ProgressListener listener);
	
	public void cancel();

	public TransactionModality getModality();
	
	public enum TransactionModality {
		/** User may see a progress bar but can work on */
		NONE, 
		/** User may work on in a different window or tab */
		PAGE,
		/** The user starting the transaction is blocked */
		SESSION, 
		/** All users are blocked. Use with caution */
		APPLICATION;
	}
}