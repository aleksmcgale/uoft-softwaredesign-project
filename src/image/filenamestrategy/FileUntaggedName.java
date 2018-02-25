package image.filenamestrategy;

import image.ImageFile;

/**
 * A FileUntaggedName name strategy. Returns the untagged name of the ImageFile file name with or without its file
 * extension.
 */
public class FileUntaggedName implements FileNameStrategy {
    /**
     * Whether or not to return the untagged file name with its file extension.
     */
    private boolean withExtension;

    /**
     * Constructs a new FileUntaggedName name strategy. Specifies whether or not to return the untagged file name of
     * the ImageFile with or without its file extension.
     *
     * @param withExtension Whether or not to return the file name of the ImageFile with its file extension.
     */
    public FileUntaggedName(boolean withExtension) {
        this.withExtension = withExtension;
    }

    /**
     * Returns the untagged name of the ImageFile file name, with or without its file extension.
     *
     * @param imageFile ImageFile for which to get an untagged name.
     * @return The untagged name of the specified ImageFile.
     */
    public String getName(ImageFile imageFile) {

        String[] splitName = new FileName(false).getName(imageFile).split("\\s+");
        String fileExtension = new FileExtension().getName(imageFile);
        StringBuilder untagged = new StringBuilder("");
        //counter to get index of string in list

        int c = 0;
        for (String s : splitName) {
            //todo : starts with vs. contains ??
            if (!(s.contains("@"))) {
                // if it isn't the first string in the name and it isnt a tag, and it has been split, then we
                // need to restore the space to the name
                if (c > 0) {
                    untagged.append(" ");
                }
                untagged.append(s);
            }
            c++;
        }

        if (withExtension) {
            return untagged.toString() + fileExtension;
        } else {
            return untagged.toString();
        }
    }
}
