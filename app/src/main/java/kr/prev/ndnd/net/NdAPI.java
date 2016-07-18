package kr.prev.ndnd.net;


import android.util.Log;
import kr.prev.ndnd.data.InitialData;
import kr.prev.ndnd.data.RecordData;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.Callback;

public class NdAPI {
	/**
	 * Standalone-instance
	 */
	private static NdAPI ourInstance = new NdAPI();
	public static NdAPI getInstance() {
		return ourInstance;
	}


	public static final String API_BASE_URL = "http://swm.prev.kr/ndnd/api/";

	private static Retrofit retrofit;


	public interface APIService {
		@GET("load/{token}")
		public Call<InitialData> load(
				@Path("token") String token
		);
	}

	/**
	 * Constructor
	 */
	private NdAPI() {
		retrofit = new Retrofit.Builder()
				.baseUrl(API_BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	/**
	 * Load data
	 * @param accessToken : facebook access token
	 * @param callback : callback function
	 */
	public void load(String accessToken, Callback<InitialData> callback) {
		APIService as = retrofit.create(APIService.class);

		Call< InitialData > call = as.load(accessToken);
		call.enqueue(callback);
	}
}
