import exp.rx.fsapi.FsConnectionInfo;
import exp.rx.fsapi.FsFileId;
import exp.rx.fsapi.FsFolderId;
import exp.rx.fsapi.box.BoxApi;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TestV1BoxApi {

    private static final String BOX_TOKEN = "cTqYlHrvzsYrlCWSvHZzQhdcfqG0cYiy";

    @Test
    public File testDownload() throws IOException {
        BoxApi bapi = new BoxApi();
        FsConnectionInfo connInfo = new FsConnectionInfo(BOX_TOKEN);
        FsFileId fileId = new FsFileId("438286756104", null);
        File tempFile = File.createTempFile("test", "dl");
        try (OutputStream os = new FileOutputStream(tempFile)) {
            bapi.downloadFile(connInfo, fileId, os, 0, 0);
        }
        System.out.println(tempFile);
        return tempFile;
    }

    @Test
    public void testUpload() throws IOException {
        File fileToUse = testDownload();
        FsConnectionInfo connInfo = new FsConnectionInfo(BOX_TOKEN);
        FsFolderId folderId = new FsFolderId("72912731724");
        BoxApi bapi = new BoxApi();
        try (InputStream is = new FileInputStream(fileToUse)) {
            bapi.uploadFile(connInfo, folderId, is, fileToUse.length(), fileToUse.getName());
        }

    }
}
