package backend.api;

import retrofit2.Call;

import java.util.List;
import java.util.Optional;

// Just a copy-paste template for other services
public interface Dao <T> {

    Call<Optional<T>> get( Integer id);

    Call<List<T>> getAll();

    Call<Integer> save(T t);

    Call<T> update(T t);

    Call<Void> delete(T t);
}
