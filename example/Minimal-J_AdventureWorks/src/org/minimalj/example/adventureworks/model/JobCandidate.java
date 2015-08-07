package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class JobCandidate {
	public static final JobCandidate $ = Keys.of(JobCandidate.class);
	
	public Object id;
	
	@Reference
	public Person person;
	
	@Required @Size(4000) // TODO
	public String resume;
}
