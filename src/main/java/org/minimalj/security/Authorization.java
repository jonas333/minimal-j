package org.minimalj.security;

import java.util.Collections;
import java.util.List;

import org.minimalj.transaction.Transaction;
import org.minimalj.transaction.TransactionAnnotations;

public class Authorization {

	public static void check(Transaction<?> transaction) {
		if (!isAllowed(transaction)) {
			throw new IllegalStateException(transaction + " forbidden");
		}
	}
	
	public static boolean isAllowed(Transaction<?> transaction) {
		return isAllowed(getCurrentRoles(), transaction);
	}

	public static boolean isAllowed(List<String> currentRoles, Transaction<?> transaction) {
		String[] roles = TransactionAnnotations.getRoles(transaction);
		if (roles != null) {
			for (String allowingRole : roles) {
				if (currentRoles.contains(allowingRole)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public static List<String> getCurrentRoles() {
		Subject subject = Subject.getCurrent();
		return subject != null ? subject.getRoles() : Collections.emptyList();
	}
	
}