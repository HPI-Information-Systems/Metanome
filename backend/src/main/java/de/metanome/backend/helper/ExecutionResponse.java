package de.metanome.backend.helper;


import java.io.Serializable;

public class ExecutionResponse implements Serializable {

    protected String identifier;
    protected String algorithm;
    protected long started;

    public ExecutionResponse setIdentifier(String id) {
        this.identifier = id;

        return this;
    }

    public ExecutionResponse setAlgorithm(String algorithm) {
        this.algorithm = algorithm;

        return this;
    }

    public ExecutionResponse setStarted(long started) {
        this.started = started;

        return this;
    }






}
