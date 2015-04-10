package de.metanome.frontend.client.results;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.TextColumn;


/**
 * Implementation of a text column providing tooltips based on object values
 *
 * Created by Alexander Spivak on 27.02.2015.
 */

public abstract class TooltipTextColumn<T> extends TextColumn<T> {

  interface TooltipTemplates extends SafeHtmlTemplates {

    @SafeHtmlTemplates.Template("<div title=\"{0}\">")
    SafeHtml startToolTip(String toolTipText);

    @SafeHtmlTemplates.Template("</div>")
    SafeHtml endToolTip();
  }

  private static final TooltipTemplates TEMPLATES = GWT.create(TooltipTemplates.class);

  protected abstract String getTooltip(T object);

  @Override
  public void render(Cell.Context context, T object, SafeHtmlBuilder sb) {
    // Add a basic tooltip
    sb.append(TEMPLATES.startToolTip(getTooltip(object)));
    super.render(context, object, sb);
    sb.append(TEMPLATES.endToolTip());
  }
}
