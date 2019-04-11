package exp.rx.fsapi.box;

import exp.rx.fsapi.box.data.BoxFileRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface BoxRetrofitService {

    @Streaming
    @GET("files/{fileId}/content")
    Call<ResponseBody> downloadFile(
            @Header("Authorization") String authHeader,
            @Path("fileId") String fileId
    );

    @Streaming
    @Multipart
    @POST("https://upload.box.com/api/2.0/files/content")
    Call<ResponseBody> uploadFile(
            @Header("Authorization") String authHeader,
            @Part("attributes") BoxFileRequest attributes, @Part MultipartBody.Part fileBody);
}
