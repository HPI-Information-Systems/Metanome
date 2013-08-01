package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.lang.reflect.Type;

public class AlgorithmDescriptor {
	private String algorithmName;
	private Type algorithmType;
	
	
	public AlgorithmDescriptor(String name, Type type) {
		this.algorithmName = name;
		this.algorithmType = type;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}
	
	public Type getAlgorithmType() {
		return algorithmType;
	}
}
