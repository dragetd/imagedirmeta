package net.speciesm.imagedirmeta.model;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.speciesm.imagedirmeta.model.exception.UnhandledFormatException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Represents an image file
 *
 * @author Michael Gajda michael.gajda@tu-dortmund.de
 */
@Value
@Slf4j(topic = "ImageFile")
public class ImageFile implements DirectoryEntry {

    private final Path pathReference;
    private final long fileSize;
    private final HandledFormat imageFormat;
    private final int width;
    private final int height;

    /**
     * ImageFile
     *
     * @param path the path object pointing to an image file
     * @throws IOException              if there was an IO error
     * @throws UnhandledFormatException if the file is not a supported image format
     */
    public ImageFile(Path path) throws IOException, UnhandledFormatException {
        // read image metadata via ImageIO
        try (ImageInputStream inStream = ImageIO.createImageInputStream(path.toFile())) {
            if (inStream == null) {
                // file not an image
                throw new UnhandledFormatException();
            }
            Iterator<ImageReader> readers = ImageIO.getImageReaders(inStream);

            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                reader.setInput(inStream);

                try {
                    imageFormat = HandledFormat.valueOf(reader.getFormatName().toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new UnhandledFormatException();
                }

                try {
                    width = reader.getWidth(0);
                    height = reader.getHeight(0);
                } catch (IllegalStateException | IndexOutOfBoundsException e) {
                    // reader could not read image data - invalid file
                    throw new UnhandledFormatException();
                }
                fileSize = Files.size(path);
            } else {
                throw new UnhandledFormatException();
            }

            pathReference = path;
        }
    }

    @Override
    public String getType() {
        return imageFormat + "-image";
    }

    /**
     * Returns an approximated compression factor by comparing the file-size to the raw image data
     * Might return values larger 1 for animated GIFs.
     * Always returns 1 for an unsupported image format
     *
     * @return the compression factor between 0 and 1
     */
    public double getCompressionFactor() {
        switch (imageFormat) {
            case GIF:
                // FIXME: this approximation only works for images. Animations might cause values > 1
                return (double) fileSize / (width * height);
            case JPEG:
                return (double) fileSize / (width * height * 3);
            default:
                // unhandled compression, return 100% by contract
                log.warn("Could not approximate compression of {}.", pathReference);
                return 1;
        }
    }

    public String toString() {
        return pathReference.toString();
    }
}
