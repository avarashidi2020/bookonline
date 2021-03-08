package ir.app.rashidi.data.remote;

import java.util.List;

import ir.app.rashidi.entity.Book;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Webservice {

    @GET("")
    Call<List<Book>> getAllBook();
}
