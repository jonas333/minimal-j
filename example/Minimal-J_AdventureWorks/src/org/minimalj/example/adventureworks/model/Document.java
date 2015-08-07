package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Document {
	public static final Document $ = Keys.of(Document.class);
	
	public Object id;
	
	@Reference
	public Document parent;
	
	@Size(50) @Required
	public String title;
	
	@Reference @Required
	public Person owner;
	
	public Boolean folder;
	
	@Size(400) @Required
	public String fileName;
	@Size(3) @Required
	public String fileExtension;
	
	@Size(5) @Required
	public String revision;
	
	@Size(5) @Required
	public Integer changeNumber;
	
	@Size(4000) @Required
	public Integer documentSummary;
	
	@Size(4000) @Required // TODO binary data
	public Integer document;

	public static enum DocumentStatus implements Code {
		Don_t(1),
		know_the(2),
		encoding(3);
		
		public final Integer id;
		
		private DocumentStatus(Integer id) {
			this.id = id;
		}
	}

}
