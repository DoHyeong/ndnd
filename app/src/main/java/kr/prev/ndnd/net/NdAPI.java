package kr.prev.ndnd.net;


import android.util.Log;

import com.facebook.AccessToken;

import java.util.HashMap;
import java.util.Map;

import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.Callback;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class NdAPI {

	/**
	 * API BASE URL
	 */
	public static final String API_BASE_URL = "http://swm.prev.kr/ndnd/api/";


	/**
	 * Retrofit instance
	 */
	private static Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(API_BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();


	/**
	 * Interface of including Restful API URI
	 */
	public interface APIService {
		@GET("load")
		public Call<InitialData> load(
				@QueryMap Map<String, String> queries
		);

		@POST("data")
		@FormUrlEncoded
		public Call<CommitResult> insertRecordData(
				@Field("type") int type,
				@Field("target_user_name") String targetUserName,
				@Field("amount") int amount,
				@Field("note") String note,
				@Field("date") String date,
				@Field("location") String location,
                @QueryMap Map<String, String> queries
		);

		@PUT("data/{dataId}")
		public Call<CommitResult> updateData(
				@Path("dataId") int dataId,
				@QueryMap Map<String, String> queries
		);
	}

	/**
	 * Get base params (incluing access token)
	 * @return Map<String, String> params
	 */
	public static Map<String, String> getBaseParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", AccessToken.getCurrentAccessToken().getToken());

		return params;
	}


	public static APIService createService() {
		APIService service = retrofit.create(APIService.class);
		return service;
	}
}
