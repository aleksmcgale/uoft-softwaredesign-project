package image;

import image.filenamestrategy.FileExtension;
import image.filenamestrategy.FileLocation;
import image.filenamestrategy.FileName;
import image.filenamestrategy.FileUntaggedName;
import overlay.ImageTagOverlay;
import tag.Tag;
import utils.FileManager;
import utils.NameLogger;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An ImageFile. Represents an image file and its associated tags and name history.
 */
public class ImageFile implements Serializable {

    /**
     * The image file that corresponds to this ImageFile object.
     */
    private File imageFile;
    /**
     * The list of all the tags associated with this image File.
     */
    private ArrayList<Tag> tagList = new ArrayList<>();
    /**
     * The list of every name this image file has had.
     */
    private ArrayList<String> nameHistory = new ArrayList<>();
    /**
     * Global logger for tracking all name changes to this ImageFile.
     */
    private NameLogger nameLogger = NameLogger.getInstance();

    private HashMap<String, ImageTagOverlay> imageTagOverlays = new HashMap<>();

    /**
     * Constructs an ImageFile object from a specified file.
     *
     * @param imageFile ImageFile file
     */
    public ImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * Adds a Tag to this ImageFile
     *
     * @param tag Tag to add to this image file.
     */
    void addTag(Tag tag) {
        if (!this.tagList.contains(tag)) {
            this.tagList.add(tag);
            String nameWithoutExtension = new FileName(false).getName(this);
            this.renameImageFile(nameWithoutExtension + " @" + tag.getTagName());
        }
    }

    /**
     * Adds an ImageTagOverlay to this ImageFile for a list of specified tags. Each tag specified will be associated
     * with the ImageTagOverlay.
     *
     * @param newOverlay ImageTagOverlay to associate with this ImageFile and the specified tag names.
     * @param tags       Tag names to be associated with specified ImageTagOverlay.
     */
    void addOverlayForTags(ImageTagOverlay newOverlay, ArrayList<String> tags) {
        for (String tagName : tags) {
            imageTagOverlays.put(tagName, newOverlay);
        }
    }

    /**
     * Removes all ImageTagOverlays from this ImageFile associated with the specified tags.
     *
     * @param tags Tag names associated with ImageTagOverlays to remove from this ImageFile.
     */
    void removeOverlaysForTags(ArrayList<String> tags) {
        for (String tagName : tags) {
            if (imageTagOverlays.containsKey(tagName)) {
                imageTagOverlays.remove(tagName);
            }
        }
    }

    /**
     * Returns a list of ImageTagOverlays that correspond to a list of specified tag names.
     *
     * @param tags The list of tag names to check for.
     * @return A list of ImageTagOverlays that correspond to the list of specified tag names.
     */
    public ArrayList<ImageTagOverlay> getOverlaysForTags(ArrayList<String> tags) {
        ArrayList<ImageTagOverlay> overlays = new ArrayList<>();

        for (String tagName : tags) {
            if (imageTagOverlays.containsKey(tagName)) {
                overlays.add(imageTagOverlays.get(tagName));
            }
        }

        return overlays;
    }

    /**
     * Adds a list of Tags to this ImageFile
     *
     * @param tags List of tags to add to this image file.
     */
    void addTags(ArrayList<Tag> tags) {

        String nameWithoutExtension = new FileName(false).getName(this);
        StringBuilder stringAppendage = new StringBuilder("");

        boolean renameIsNeeded = false;

        for (Tag t : tags) {
            if (!this.tagList.contains(t)) {
                this.tagList.add(t);
                stringAppendage.append(" @");
                stringAppendage.append(t.getTagName());
                renameIsNeeded = true;
            }
        }

        if (renameIsNeeded) {
            this.renameImageFile(nameWithoutExtension + stringAppendage.toString());
        }

    }

    /**
     * Removes specified Tag from this image file.
     *
     * @param tag Tag to remove from this image file.
     */
    public void removeTag(Tag tag) {
        if (this.tagList.contains(tag)) {
            this.tagList.remove(tag);
            renameTagChange();
        }
    }

    /**
     * Removes a list of specified Tags from this image file.
     *
     * @param tagsToRemove List of Tags to remove from this image file.
     */
    void removeTags(ArrayList<Tag> tagsToRemove) {
        for (Tag tag : tagsToRemove) {
            if (this.tagList.contains(tag)) {
                this.tagList.remove(tag);
            }
        }
        renameTagChange();
    }

    /**
     * Returns all the tags that this image file currently has associated with it.
     *
     * @return A list of tags associated with this image file.
     */
    ArrayList<Tag> getTagList() {
        return this.tagList;
    }

    /**
     * Returns all the names this ImageFile has had.
     *
     * @return A list of names this ImageFile has had.
     */
    ArrayList<String> getNameHistory() {
        return this.nameHistory;
    }

    /**
     * Reverts the name of this ImageFile to an name it has previously had.
     *
     * @param oldName Name to revert to.
     */
    void revertToHistoricalName(String oldName) {
        String currentName = new FileName(false).getName(this);
        if (nameHistory.contains(oldName) && !currentName.equals(oldName)) {
            renameImageFile(oldName);
        }
    }

    /**
     * Renames the File that this image.ImageFile refers to.
     *
     * @param new_name The new name of the image.ImageFile file.
     */
    private void renameImageFile(String new_name) {

        Path source = Paths.get(imageFile.getPath());
        String _ext = new FileExtension().getName(this);

        try {
            String oldName = imageFile.getName();
            nameHistory.add(new FileName(false).getName(this));
            Files.move(source, source.resolveSibling(new_name + _ext));
            imageFile = new File(imageFile.getParent() + "/" + new_name + _ext);
            nameLogger.logNameChange(oldName, imageFile.getName());
        } catch (IOException e) {
            System.out.println("Failed to rename image.ImageFile File");
        }
    }

    /**
     * Gets the File that this image.ImageFile refers to
     *
     * @return The File that this image refers to.
     */
    public File getImageFilePath() {
        return imageFile;
    }

    /**
     * Returns a string representation of this ImageFile
     *
     * @return A string representation of this ImageFile.
     */
    public String toString() {
        return this.imageFile.getName();
    }

    /**
     * Returns the original name of this ImageFile before it was tagged.
     *
     * @return The original name of this ImageFile
     */
    String getOriginalName() {
        // todo : discuss the correctness of this...?
        return new FileUntaggedName(true).getName(this);
    }

    /**
     * Returns true if this ImageFile is equal to an Object.
     *
     * @param other Object with which to check for equality.
     * @return True iff the Object is equal to this ImageFile
     */
    @SuppressWarnings("all")
    public boolean equals(Object other) {
        //todo : SERIOUS!!! examine equality in other cases: if it has same name, but from a different directory?
        //todo : this is a 'bottleneck' function!!
        if (other instanceof ImageFile) {
//          return this.getOriginalName().equals(((ImageFile) other).getOriginalName());
            String thisLocation = new FileLocation().getName(this);
            String otherLocation = new FileLocation().getName((ImageFile) other);

            String thisOriginalName = this.getOriginalName();
            String otherOriginalName = ((ImageFile) other).getOriginalName();
            return thisLocation.equals(otherLocation) && thisOriginalName.equals(otherOriginalName);
        }
        return false;
    }

    /**
     * Renames the ImageFile after a Tag is changed. Is typically called after a Tag is removed.
     */
    private void renameTagChange() {

        String untaggedFileName = new FileUntaggedName(false).getName(this);

        //create a new string to stand for new file name
        StringBuilder newFileName = new StringBuilder("");

        // append the current untagged file name without extensions
        newFileName.append(untaggedFileName);

        // for every existing tag, append tag to new file name
        for (Tag t : tagList) {
            newFileName.append(" @");
            newFileName.append(t.toString());
        }

        // rename the image file
        this.renameImageFile(newFileName.toString());
    }

    /**
     * Moves this ImageFile to a new parent folder.
     *
     * @param fileToMoveTo Folder to move to.
     */
    void moveFileToLocation(File fileToMoveTo) {
        FileManager.moveFile(imageFile, fileToMoveTo.toString() + "/" + imageFile.getName());
        imageFile = new File(fileToMoveTo.toString() + "/" + imageFile.getName());
    }
}
