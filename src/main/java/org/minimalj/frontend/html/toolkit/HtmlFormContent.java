package org.minimalj.frontend.html.toolkit;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.FormContent;
import org.minimalj.util.StringUtils;

public class HtmlFormContent implements FormContent, HtmlContent {

	private final StringBuilder builder = new StringBuilder();
	private final int columns;
	private final int columnWidth;
	private final List<List<HtmlComponent>> rows = new ArrayList<>();
	private int col = 0;
	
	public HtmlFormContent(int columns, int columnWidth) {
		this.columns = columns;
		this.columnWidth = columnWidth;
	}

	@Override
	public void add(IComponent field) {
		add(null, field, columns);
	}
	
	@Override
	public void add(String caption, IComponent field, int span) {
		if (field instanceof HtmlComponent) {
			HtmlComponent htmlCaption = (HtmlComponent) field;
			htmlCaption.setCaption(caption);
			if (col == 0) {
				rows.add(new ArrayList<HtmlComponent>());
			}
			rows.get(rows.size()-1).add(htmlCaption);
			col += span;
			if (col >= columns) {
				col = 0;
			}
		} else {
			throw new IllegalArgumentException(getClass().getSimpleName() + " can only accept HtmlCaption not " + field.getClass().getSimpleName());
		}
	}
	
	private void layout() {
		builder.delete(0, builder.length());
		builder.append("<table>\n");
		for (List<HtmlComponent> row : rows) {
			layoutRow(row);
		}
		builder.append("</table>\n");
	}
	
	private void layoutRow(List<HtmlComponent> row) {
		if (row.size() == 1) {
			if (row.get(0) instanceof HtmlLabel) {
				HtmlLabel htmlLabel = (HtmlLabel) row.get(0);
				builder.append("<tr>");
				builder.append("<td>");
				StringUtils.escapeHTML(builder, htmlLabel.text());
				builder.append("</td>");
				builder.append("</tr>");
				return;
			}
			// TODO if instanceof HtmlTitle
		}
		builder.append("<tr>");
		for (HtmlComponent field : row) {
			builder.append("<th>");
			StringUtils.escapeHTML(builder, field.getCaption());
			builder.append("</th>");
		}
		builder.append("</tr>\n<tr>");
		for (HtmlComponent field : row) {
			builder.append("<td>");
			StringUtils.escapeHTML(builder, field.text());
			builder.append("</td>");
		}
		builder.append("</tr>");
	}

	@Override
	public String toHtml() {
		layout();
		return builder.toString();
	}

	@Override
	public void setValidationMessages(IComponent component, List<String> validationMessages) {
		// TODO validation in html Toolkit
	}
	
}
