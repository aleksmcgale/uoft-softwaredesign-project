package image.filenamestrategy;

import image.ImageFile;

/**
 * A FileExtension.
 */
public class FileName implements FileNameStrategy {
    /**
     * Whether or not to return the file name with the file extension of the file.
     */
    private boolean withExtension;

    /**
     * Construct a new FileName name strategy. Specifies whether or not to return the file name with its file extension.
     *
     * @param withExtension Whether or not to return the file name with its file extension.
     */
    public FileName(boolean withExtension) {
        this.withExtension = withExtension;
    }

    /**
     * Returns the name of a specified ImageFile.
     *
     * @param imageFile ImageFile for which to get name.
     * @return The file name of the specified ImageFile.
     */
    public String getName(ImageFile imageFile) {
        String fileName = imageFile.getImageFilePath().getName();
        if (withExtension) {
            return fileName;

        } else {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
    }

}
