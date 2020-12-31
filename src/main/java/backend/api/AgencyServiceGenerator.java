package backend.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.time.LocalDate;

public class AgencyServiceGenerator {
    private static final String BASE_URL = "http://localhost:8080/";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter().nullSafe())
            .create();

    private static final Retrofit.Builder builder
            = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(new OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static final Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    private static final class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate ) throws IOException {
            jsonWriter.value(localDate.toString());
        }

        @Override
        public LocalDate read( final JsonReader jsonReader ) throws IOException {
            return LocalDate.parse(jsonReader.nextString());
        }
    }
}
