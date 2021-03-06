package org.minimalj.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.transaction.Role;
import org.minimalj.transaction.TransactionAnnotations;

public class Subject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final InheritableThreadLocal<Subject> subject = new InheritableThreadLocal<>();
	private String name;
	private Serializable token;
	
	private final List<String> roles = new ArrayList<>();

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Serializable getToken() {
		return token;
	}
	
	public void setToken(Serializable token) {
		this.token = token;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public boolean hasRole(String... roleNames) {
		for (String roleName : roleNames) {
			if (roles.contains(roleName)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean currentHasRole(String... roleNames) {
		Subject currentSubject = getCurrent();
		return currentSubject != null ? currentSubject.hasRole(roleNames) : false;
	}

	public static void setCurrent(Subject subject) {
		Subject.subject.set(subject);
	}
	
	public static Subject getCurrent() {
		return subject.get();
	}

	public static boolean currentCanAccess(Class<?> clazz) {
		Role role = TransactionAnnotations.getAnnotationOfClassOrPackage(clazz, Role.class);
		if (role == null) {
			return true;
		} else {
			return currentHasRole(role.value());
		}
	}

}
