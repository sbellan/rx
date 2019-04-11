package exp.rx.fsapi;

public class FsFileId {
    public String id;
    public FsFolderId folderId;

    public FsFileId(String id, FsFolderId folderId) {
        this.id = id;
        this.folderId = folderId;
    }
}
