package org.minimalj.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.minimalj.application.Configuration;
import org.minimalj.backend.Backend;
import org.minimalj.transaction.Transaction;

public abstract class Authentication implements Serializable {
	private static final long serialVersionUID = 1L;

	private final transient Map<UUID, Subject> subjectByToken = new HashMap<>();

	public static Authentication create() {
		String userFile = Configuration.get("MjUserFile");
		if (userFile != null) {
			return new TextFileAuthentication(userFile);
		}
		
		if (Configuration.available("MjAuthentication")) {
			return Configuration.getClazz("MjAuthentication", Authentication.class);
		}

		return null;
	}
	
	public abstract void login(LoginListener loginListener);
	
	public static interface LoginListener {
		
		public void loginSucceded(Subject subject);
		
		public void loginCancelled();
		
	}
	
	public void logout() {
		Backend.execute(new Transaction<Void>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Void execute() {
				subjectByToken.remove(Subject.getCurrent().getToken());
				return null;
			}
		});
	}
	
	public Subject getUserByToken(Serializable token) {
		return subjectByToken.get(token);
	}

	public Subject createSubject(String name) {
		Subject subject = new Subject();
		subject.setName(name);
		UUID token = UUID.randomUUID();
		subject.setToken(token);
		subjectByToken.put(token, subject);
		return subject;
	}
}
