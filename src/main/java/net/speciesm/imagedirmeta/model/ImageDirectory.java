package net.speciesm.imagedirmeta.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.speciesm.imagedirmeta.model.exception.UnhandledFormatException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a directory containing image files
 *
 * @author Michael Gajda michael.gajda@tu-dortmund.de
 */
@EqualsAndHashCode
@Slf4j(topic = "ImageDirectory")
public class ImageDirectory implements DirectoryEntry {

    // Reference to the path object point at the directory
    @Getter
    private final Path pathReference;

    private final List<ImageDirectory> imageDirs = new ArrayList<>();
    private final List<ImageFile> imageFiles = new ArrayList<>();

    /**
     * ImageDirectory
     * Logs an error if path is not a directory
     *
     * @param path the path object pointing to a directory
     */
    public ImageDirectory(Path path) {
        if (!path.toFile().isDirectory()) {
            throw new IllegalArgumentException();
        }
        pathReference = path;
        recurseSubEntries(pathReference);
    }

    private void recurseSubEntries(Path path) {
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            ds.forEach(this::addEntry);
        } catch (IOException e) {
            log.error("Could not open {} as directory.", path);
        }
    }

    private void addEntry(Path newEntry) {
        log.debug("Processing entry {}", newEntry);
        if (newEntry.toFile().isDirectory()) {
            addDirectory(newEntry);
        } else if (newEntry.toFile().isFile()) {
            addFile(newEntry);
        }
    }

    private void addDirectory(Path dirpath) {
        ImageDirectory newDir = new ImageDirectory(dirpath);
        if (!newDir.getChildEntries().isEmpty()) {
            // if this directory actually contains image data
            imageDirs.add(newDir);
        }
    }

    private void addFile(Path filepath) {
        try {
            imageFiles.add(new ImageFile(filepath));
            log.debug("Registered image {}", filepath);
        } catch (UnhandledFormatException e) {
            log.debug("Ignoring file {}", filepath);
        } catch (IOException e) {
            log.error("Could not open {}", filepath);
        }
    }

    /**
     * Returns a list of all child-entries below this image-directory
     *
     * @return the list of child-entries
     */
    public List<DirectoryEntry> getChildEntries() {
        List<DirectoryEntry> childEntries = new ArrayList<>(imageDirs);
        childEntries.addAll(imageFiles);
        return Collections.unmodifiableList(childEntries);
    }

    private List<ImageFile> getImageFilesRecursively() {
        List<ImageFile> allImages = new ArrayList<>(imageFiles);
        imageDirs.forEach(dir -> allImages.addAll(dir.getImageFilesRecursively()));
        return Collections.unmodifiableList(allImages);
    }

    @Override
    public String getType() {
        return "directory";
    }

    /**
     * Returns the total sum of file-sizes of all images in this directory and all image-subdirectories
     *
     * @return the total size
     */
    @Override
    public long getFileSize() {
        long length = 0;
        for (ImageDirectory imageDir : imageDirs) {
            length += imageDir.getFileSize();
        }
        for (ImageFile imageFile : imageFiles) {
            length += this.getFileSize();
        }
        return length;
    }

    /**
     * Returns the average width of all images found recursively under this directory
     *
     * @return the average width in pixel of all images under this directory. 0 if directory is empty
     */
    @Override
    public int getWidth() {
        return (int) getImageFilesRecursively().stream()
                .mapToInt(ImageFile::getWidth)
                .average()
                .orElse(0);
    }

    /**
     * Returns the average height of all images found recursively under this directory
     *
     * @return the average height in pixel of all images under this directory. 0 if directory is empty
     */
    @Override
    public int getHeight() {
        return (int) getImageFilesRecursively().stream()
                .mapToInt(ImageFile::getHeight)
                .average()
                .orElse(0);
    }

    /**
     * Returns an average compression factor for all images found recursively under this directory
     *
     * @return the average compression factor between 0 and 1
     */
    @Override
    public double getCompressionFactor() {
        return getImageFilesRecursively().stream()
                .mapToDouble(ImageFile::getCompressionFactor)
                .average()
                .orElse(0);
    }

    public String toString() {
        return pathReference.toString();
    }
}
