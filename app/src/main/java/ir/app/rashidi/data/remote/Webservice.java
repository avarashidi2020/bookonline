package ir.app.rashidi.data.remote;

import java.util.List;

import ir.app.rashidi.entity.Book;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("acba91ba-6f57-47ee-a683-59b496f1ea5e")
    Call<List<Book>> getAllBook();
}
