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

import com.google.gwt.user.client.ui.IntegerBox;

public class InputParameterIntegerWidget extends IntegerBox implements InputParameterWidget {
	
	InputParameterInteger inputParameter;

	public InputParameterIntegerWidget(
			InputParameterInteger inputParameter) {
		super();
		this.inputParameter = inputParameter;
	}

	@Override
	public InputParameter getInputParameter() {
		if (this.getValue() != null)
			this.inputParameter.setValue(this.getValue());
		else
			this.inputParameter.setValue(0);
		
		return inputParameter;
	}

}
