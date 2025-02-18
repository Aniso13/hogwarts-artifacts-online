package com.anis.hogwartsartifactsonline.artifact;

public class ArtifactNotFountException extends RuntimeException {
    public ArtifactNotFountException(String id) {
        super("Could not find artifact with Id " + id);
    }
}
