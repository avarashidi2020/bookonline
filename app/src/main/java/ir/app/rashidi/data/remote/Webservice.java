package ir.app.rashidi.data.remote;

import java.util.List;

import ir.app.rashidi.entity.Book;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("eb0fcefa-bf55-4b75-a0ed-e835cf73a147")
    Call<List<Book>> getAllBook();
}
