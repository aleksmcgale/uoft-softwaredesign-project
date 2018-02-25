package image.filenamestrategy;

import image.ImageFile;

/**
 * FileNameStrategy. Interface to return various string representations or parts of a specified ImageFile.
 */
public interface FileNameStrategy {
    String getName(ImageFile imageFile);
}
