package okhttp.demo.com.okhttpdemo.retrofitDemo.services;

import java.util.List;

import okhttp.demo.com.okhttpdemo.retrofitDemo.models.RepositoryModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by uncle_charlie on 21/2/16.
 */
public interface RepoService {
    @GET("/orgs/by-the-way/repos")
    Call<List<RepositoryModel>> getRepositories();
}
