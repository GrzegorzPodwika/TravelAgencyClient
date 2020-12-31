package backend.api;

import retrofit2.Call;

import java.util.List;
import java.util.Optional;

public interface Dao <T> {

    Call<Optional<T>> get( Integer id);

    Call<List<T>> getAll();

    Call<Integer> save(T t);

    Call<T> update(T t);

    Call<Void> delete(T t);
}
