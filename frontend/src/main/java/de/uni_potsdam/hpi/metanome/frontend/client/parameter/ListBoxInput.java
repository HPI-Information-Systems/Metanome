/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.ListBox;

import java.util.ArrayList;

/**
 * A wrapper for a list box of strings that can contain a remove button. If the remove button is clicked, the list box
 * is removed from the parent widget.
 *
 * @author Tanja
 */
public class ListBoxInput extends InputField {

    protected ListBox listbox;

    /**
     * @param optional If true, a remove button will be rendered, to remove this widget from its parent.
     */
    public ListBoxInput(boolean optional) {
        super(optional);

        this.listbox = new ListBox();
        this.add(this.listbox);
    }

    /**
     * @return the list of string values of its listbox
     */
    public ArrayList<String> getValues() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < this.listbox.getItemCount(); i++)
            list.add(this.listbox.getValue(i));

        return list;
    }

    /**
     * set all values of the list to the list box
     */
    public void setValues(ArrayList<String> list) {
        for (String element : list)
            this.listbox.addItem(element);
    }

    /**
     * @return the selected value of its listbox
     */
    public String getSelectedValue() {
        int selectedIndex = this.listbox.getSelectedIndex();
        return this.listbox.getValue(selectedIndex);
    }

    public void setSelectedValue(String selectedValue) {
        for (int i = 0; i < this.listbox.getItemCount(); i++) {
            if (this.listbox.getValue(i).equals(selectedValue))
                this.listbox.setSelectedIndex(i);
        }
    }
}
