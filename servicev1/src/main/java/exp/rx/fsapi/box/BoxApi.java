package exp.rx.fsapi.box;

import com.google.common.io.ByteStreams;
import exp.rx.fsapi.*;
import exp.rx.fsapi.box.data.BoxFileRequest;
import exp.rx.fsapi.box.data.BoxResource;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class BoxApi implements FsApi {

    private static final Logger logger = LoggerFactory.getLogger(BoxApi.class);

    BoxRetrofitService service;

    public BoxApi() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(s -> logger.info(s));
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.box.com/2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client).build();

        service = retrofit.create(BoxRetrofitService.class);
    }

    @Override
    public void downloadFile(FsConnectionInfo connInfo, FsFileId fileId, OutputStream outputStream,
                             int rangeStart, int rangeEnd) throws IOException {
        Call<ResponseBody> downloadCall = service.downloadFile(getAuthHeader(connInfo), fileId.id);
        Response<ResponseBody> downloadResp = downloadCall.execute();
        if (downloadResp.isSuccessful()) {
            ByteStreams.copy(downloadResp.body().byteStream(), outputStream);
        } else {
            throw new FsApiException("Download failed for " + fileId, downloadResp.code(), downloadResp.errorBody().string(),
                    null);
        }
    }

    @Override
    public FsFileId uploadFile(FsConnectionInfo connInfo, FsFolderId folderId, InputStream is, long size, String name) throws IOException {
        File tmpFile = File.createTempFile("chunk_" + 1, name);
        try {
            try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
                ByteStreams.copy(is, fos);
            }
            MediaType octetMediaType = MediaType.parse("application/octet-stream");
            Call<ResponseBody> uploadCall = service.uploadFile(
                    getAuthHeader(connInfo),
                    new BoxFileRequest(name, new BoxResource(folderId.id)),
                    MultipartBody.Part.createFormData("file", name, RequestBody.create(octetMediaType, tmpFile)));
            Response<ResponseBody> uploadResp = uploadCall.execute();
            if (!uploadResp.isSuccessful()) {
                throw new FsApiException("Upload failed for " + folderId + "/" + name
                        ,uploadResp.code(), uploadResp.errorBody().string(),
                        null);
            }
            return null;
        } finally {
            tmpFile.delete();
        }
    }

    private String getAuthHeader(FsConnectionInfo connInfo) {
        return "Bearer " + connInfo.oAuthToken;
    }
}
