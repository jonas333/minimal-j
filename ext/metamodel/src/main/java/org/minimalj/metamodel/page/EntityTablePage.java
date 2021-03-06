package org.minimalj.metamodel.page;

import java.util.List;

import org.minimalj.application.Application;
import org.minimalj.frontend.page.TableDetailPage;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjModel;

public class EntityTablePage extends TableDetailPage<MjEntity> {

	private final List<MjEntity> entities;

	public EntityTablePage() {
		this(new MjModel(Application.getInstance()));
	}
	
	public EntityTablePage(MjModel model) {
		super(new Object[]{MjEntity.$.name, MjEntity.$.type, MjEntity.$.validatable, MjEntity.$.maxInclusive});
		this.entities = model.entities;
	}
	
	public EntityTablePage(List<MjEntity> entities) {
		super(new Object[]{MjEntity.$.name, MjEntity.$.type, MjEntity.$.validatable, MjEntity.$.maxInclusive});
		this.entities = entities;
	}
	
	@Override
	protected TablePage<?> getDetailPage(MjEntity entity) {
		if (entity.isEnumeration()) {
			return new EnumTablePage(entity);
		} else {
			return new PropertyTablePage(entity);
		}
	}

	@Override
	protected List<MjEntity> load() {
		return entities;
	}

}
