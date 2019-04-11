package exp.rx.fsapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface FsApi {

    void downloadFile(FsConnectionInfo connInfo, FsFileId fileId, OutputStream outputStream, int rangeStart, int rangeEnd) throws IOException;

    FsFileId uploadFile(FsConnectionInfo connInfo, FsFolderId folderId, InputStream is, long size, String name) throws IOException;

}
