package org.minimalj.example.petclinic.frontend;

import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.example.petclinic.model.Vet;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.transaction.criteria.Criteria;

public class VetTablePage extends TablePage<Vet> {

	private static final Object[] keys = {Vet.$.person.getName(), Vet.$.specialties};
	
	public VetTablePage() {
		super(keys);
	}

	@Override
	protected List<Vet> load() {
		return Backend.persistence().read(Vet.class, Criteria.all(), 100);
	}

}