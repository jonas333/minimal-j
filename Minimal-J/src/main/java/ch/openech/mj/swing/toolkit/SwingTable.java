package ch.openech.mj.swing.toolkit;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadablePartial;

import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.search.Item;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.util.JodaFormatter;

public class SwingTable extends JScrollPane implements ITable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SwingTable.class.getName());
	
	private final Object[] keys;
	private final List<PropertyInterface> properties;
	private final JTable table;
	private final ItemTableModel tableModel;
	private TableActionListener listener;
	
	public SwingTable(Object[] keys) {
		this.keys = keys;
		this.properties = convert(keys);
		
		tableModel = new ItemTableModel();
		table = new JTable(tableModel);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setFillsViewportHeight(true);
		
//		setDefaultRenderer(BooleanFormat.class, new BooleanTableCellRenderer());
		table.setDefaultRenderer(LocalDate.class, new DateTableCellRenderer());
		table.setDefaultRenderer(LocalTime.class, new DateTableCellRenderer());
		table.setDefaultRenderer(LocalDateTime.class, new DateTableCellRenderer());
		table.setDefaultRenderer(ReadablePartial.class, new DateTableCellRenderer());
		
		table.setAutoCreateRowSorter(true);
		
		setViewportView(table);
		
		bindRowHeightToFont();

		table.addMouseListener(new SwingTableMouseListener());
	}

	private List<PropertyInterface> convert(Object[] keys) {
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
	
	private void bindRowHeightToFont() {
		table.addPropertyChangeListener("UI", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				table.setRowHeight(table.getFont().getSize() * 5 / 3 + 2);
			}
		});
	}

	@Override
	public void setObjects(List<? extends Item> objects) {
		tableModel.setObjects(objects);
	}

	public Item getSelectedObject() {
		if (table.getSelectionModel().getLeadSelectionIndex() >= 0) {
			return tableModel.getObject(table.getSelectionModel().getLeadSelectionIndex());
		} else {
			return null;
		}
	}

	public List<Item> getSelectedObjects() {
		List<Item> selectedObjects = new ArrayList<>(table.getSelectedRowCount());
		for (int row : table.getSelectedRows()) {
			selectedObjects.add(tableModel.getObject(row));
		}
		return selectedObjects;
	}

	@Override
	public void setClickListener(TableActionListener listener) {
		this.listener = listener;
	}

	private class SwingTableMouseListener extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2 && listener != null) {
				try {
					listener.action(getSelectedObject(), getSelectedObjects());
				} catch (Exception x) {
					x.printStackTrace();
				}
			}
		}
	}

	@Override
	public void setInsertListener(final InsertListener listener) {
		if (listener != null) {
			Action action = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					listener.action();
				}
			};
			bindKey(KeyEvent.VK_INSERT, action);
		} else {
			unbindKey(KeyEvent.VK_INSERT);
		}
	}

	@Override
	public void setDeleteListener(final TableActionListener listener) {
		if (listener != null) {
			Action action = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					listener.action(getSelectedObject(), getSelectedObjects());
				}
			};
			bindKey(KeyEvent.VK_DELETE, action);
		} else {
			unbindKey(KeyEvent.VK_DELETE);
		}
	}

	@Override
	public void setFunctionListener(final int function, final TableActionListener listener) {
		if (listener != null) {
			Action action = new AbstractAction() {
				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent e) {
					listener.action(getSelectedObject(), getSelectedObjects());
				}
			};
			bindKey(KeyEvent.VK_F1+function, action);
		} else {
			unbindKey(KeyEvent.VK_F1+function);
		}
	}
	
	private void bindKey(int keyEvent, Action action) {
		getActionMap().put(keyEvent, action);
		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(keyEvent, 0), keyEvent);
	}

	private void unbindKey(int keyEvent) {
		getActionMap().remove(keyEvent);
		getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).remove(KeyStroke.getKeyStroke(keyEvent, 0));
	}
	
	public class ItemTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private List<? extends Item> items = Collections.emptyList();

		public ItemTableModel() {
		}

		public void setObjects(List<? extends Item> items) {
			this.items = items;
			fireTableDataChanged();
		}
		
		public Item getObject(int index) {
			return items.get(index);
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			PropertyInterface property = properties.get(column)
;			return Resources.getObjectFieldName(Resources.getResourceBundle(), property);
		}

		@Override
		public Object getValueAt(int row, int column) {
			Item item = items.get(row);
			return item.getValue(keys[column]);
		}

		@Override
		public int getRowCount() {
			return items.size();
		}

		@Override
		public int getColumnCount() {
			return keys.length;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return properties.get(columnIndex).getFieldClazz();
		}

		public Item getRow(int row) {
			return items.get(row);
		}
	}
	
	private class BooleanTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			if ("1".equals(value)) {
				value = "ja";
			} else {
				value = "nein";
			}
			
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
		}
	}
	
	private class DateTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private final JodaFormatter formatter = new JodaFormatter();
		
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			PropertyInterface property = properties.get(column);
			value = formatter.format(value, property);
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
					row, column);
		}
	}
	

}
