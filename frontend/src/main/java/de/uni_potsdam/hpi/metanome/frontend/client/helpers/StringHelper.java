package de.uni_potsdam.hpi.metanome.frontend.client.helpers;

public class StringHelper {

	public static char getFirstCharFromInput(String string){			
		if (string.length() == 1) {
			return string.charAt(0);
		} else if (string.equals("\\n")) {
			return '\n';
		} else if (string.equals("\\t")) {
			return '\t';
		} //else {
			//throw new Exception(); 
//			} TODO throw useful exception
		return 0;
	}
}
