package de.uni_potsdam.hpi.metanome.test_helper;

import static org.junit.Assert.fail;

import com.google.gwt.rpc.client.ast.HasValues;
import com.google.gwt.rpc.client.ast.ReturnCommand;
import com.google.gwt.rpc.client.impl.HasValuesCommandSink;
import com.google.gwt.rpc.server.CommandServerSerializationStreamWriter;
import com.google.gwt.rpc.server.HostedModeClientOracle;
import com.google.gwt.user.client.rpc.SerializationException;

public class GwtSerializationTester {
	
	public static void checkGwtSerializability(Object o) {
		HostedModeClientOracle hmco = new HostedModeClientOracle();
	    HasValues command = new ReturnCommand();
	    HasValuesCommandSink hvcs = new HasValuesCommandSink(command);
	    CommandServerSerializationStreamWriter out = new CommandServerSerializationStreamWriter(hmco, hvcs);
	    
	    try {
	    	out.writeObject(o);
	    } catch (SerializationException e) {
	    	fail("Object not serializable: " + o + " Caused by: " + e.getMessage());
	    }
	    
	    try {
			o.getClass().getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			fail("Object not serializable: " + o + " Caused by: " + e.getMessage());
		}
	}
	
}
