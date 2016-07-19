package kr.prev.ndnd.net;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import kr.prev.ndnd.data.CommitResult;
import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import kr.prev.ndnd.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

		@POST("data/{id}")
		public Call<CommitResult> saveData(
			@Path("id") int id
		);
	}

	/**
	 * Get base params (incluing access token)
	 * @return Map<String, String> params
	 */
	private static Map<String, String> getBaseParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", SessionManager.getInstance().getAccessToken());

		return params;
	}

	/**
	 * Load initial data (/api/load)
	 * @param callback : callback function
	 */
	public static void loadInitialData(Callback<InitialData> callback) {
		APIService service = retrofit.create(APIService.class);

		Call< InitialData > call = service.load(getBaseParams());
		call.enqueue(callback);
	}
}
