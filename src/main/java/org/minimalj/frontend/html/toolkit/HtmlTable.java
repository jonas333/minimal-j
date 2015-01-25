package org.minimalj.frontend.html.toolkit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.minimalj.frontend.toolkit.ITable;
import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.DateUtils;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

public class HtmlTable<T> implements ITable<T>, HtmlContent {
	private static final Logger logger = Logger.getLogger(HtmlTable.class.getName());

	private final StringBuilder s = new StringBuilder(10*1000);
	private final List<PropertyInterface> properties;
	private List<T> objects = Collections.emptyList();
	private final String[] columnTitleArray;
	private InsertListener insertListener;
	private TableActionListener<T> clickListener, deleteListener;
	
	public HtmlTable(Object[] keys) {
		this.properties = convert(keys);
		
		columnTitleArray = new String[keys.length];
		for (int i = 0; i<properties.size(); i++) {
			PropertyInterface property = properties.get(i);
			columnTitleArray[i] = Resources.getObjectFieldName(Resources.getResourceBundle(), property);

		}
	}
	
	private static List<PropertyInterface> convert(Object[] keys) {
		List<PropertyInterface> properties = new ArrayList<PropertyInterface>(keys.length);
		for (Object key : keys) {
			PropertyInterface property = Keys.getProperty(key);
			if (property != null) {
				properties.add(property);
			} else {
				logger.log(Level.WARNING, "Key not a property: " + key);
			}
		}
		if (properties.size() == 0) {
			logger.log(Level.SEVERE, "PropertyTable without valid keys");
		}
		return properties;
	}
	
	public void update() {
		s.delete(0, s.length());
		s.append("<table>\n");
		s.append("<tr>");
		for (int i = 0; i<properties.size(); i++) {
			s.append("<th>");
			String value = columnTitleArray[i];
			StringUtils.escapeHTML(s, value);
			s.append("</th>");
		}
		s.append("</tr>\n");

		for (int line = 0; line<objects.size(); line++) {
			s.append("<tr>");
			
			for (int i = 0; i<properties.size(); i++) {
				s.append("<td>");
				String value = getValue(line, i);
				StringUtils.escapeHTML(s, value);
				s.append("</td>");
			}
			s.append("</tr>");
			line++;
		}
		s.append("</table>\n");
	}

	private T getObject(int index) {
		return objects.get(index);
	}
	
	protected String getValue(int row, int column) {
		Object value = properties.get(column).getValue(getObject(row));
		if (value instanceof LocalTime) {
			PropertyInterface property = properties.get(column);
			return DateUtils.getTimeFormatter(property).format((LocalTime) value); 
		} else if (value instanceof LocalDate) {
			return DateUtils.DATE_FORMATTER.format((LocalDate) value); 
		}
		return "" + value;
	}
	
	//
	
	@Override
	public void setObjects(List<T> objects) {
		if (objects != null) {
			this.objects = objects;
		} else {
			this.objects = Collections.emptyList();
		}
		update();
	}

	public List<T> getSelectedObjects() {
		return Collections.emptyList();
	}

	public T getSelectedObject() {
		return null;
	}

	@Override
	public void setClickListener(TableActionListener<T> listener) {
		this.clickListener = listener;
	}

	@Override
	public void setDeleteListener(TableActionListener<T> listener) {
		this.deleteListener = listener;
	}

	@Override
	public void setInsertListener(InsertListener listener) {
		this.insertListener = listener;
	}

	@Override
	public void setFunctionListener(int function, TableActionListener<T> listener) {
		// TODO Function Action in Lanterna Table
	}

	@Override
	public String toHtml() {
		return s.toString();
	}
}
