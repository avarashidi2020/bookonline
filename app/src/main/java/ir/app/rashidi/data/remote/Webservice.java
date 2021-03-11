package ir.app.rashidi.data.remote;

import java.util.List;

import ir.app.rashidi.entity.Book;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("77a0fb50-850d-4dff-8e5d-7ad4ad7f3b97")
    Call<List<Book>> getAllBook();
}
