package image.filenamestrategy;

import image.ImageFile;

/**
 * A FileExtension. Returns the file location / path of an ImageFile. Implements FileNameStrategy.
 */
public class FileLocation implements FileNameStrategy {

    /**
     * Return the file location/path for a specified ImageFile.
     *
     * @param imageFile ImageFile for which to get file location/path.
     * @return The path of the specified ImageFile.
     */
    @Override
    public String getName(ImageFile imageFile) {
        return imageFile.getImageFilePath().getParent();
    }
}
