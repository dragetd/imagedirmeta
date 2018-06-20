package net.speciesm.imagedirmeta.model;

/**
 * Enum of image formats handled by this implementation
 *
 * @author Michael Gajda michael.gajda@tu-dortmund.de
 */
public enum HandledFormat {
    GIF, JPEG;

    String format;

    HandledFormat() {
        format = name();
    }
}
