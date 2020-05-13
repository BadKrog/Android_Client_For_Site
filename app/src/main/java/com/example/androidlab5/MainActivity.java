package com.example.androidlab5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.androidlab5.MyInterf.FuncStart;
import com.example.androidlab5.breeds.Breed;
import com.example.androidlab5.breeds.BreedData;
import com.example.androidlab5.likes.ActivityForLikes;
import com.example.androidlab5.likes.DataLike;
import com.example.androidlab5.url.UrlData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Нашли всплывающий список
        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setDropDownVerticalOffset(150);
        // Создаем функцию для второго потока
        FuncStart func = new FuncStart() {
            @Override
            public void start() {
                BreedData repository = BreedData.getInstance();
                List<String> breeds = repository.getBreedNames();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, breeds);
                spinner.setAdapter(arrayAdapter);
            }
        };
        // Запускаем поток
        BreedLoader loader = new BreedLoader(func);
        loader.execute();

        // Подготавливаем остальные элементы для работы
        Button button = findViewById(R.id.btn);
        UrlData.createInstance("");
        DataLike.createInstance();
        final RecyclerView view = findViewById(R.id.recycler_view);
        final RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext());
        final FuncStart notifyData = new FuncStart() {
            @Override
            public void start() {
                adapter.notifyDataSetChanged();
            }
        };
        // Прикрепляем адаптер
        view.setAdapter(adapter);
        // Создаем слушетеля для кнопки "загрузить"
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View e) {
                // Обновляем id породы
                String breedId = "";
                if(spinner.getSelectedItem() != null) {
                    breedId = BreedData.getInstance().getBreedId(spinner.getSelectedItem().toString());
                }
                // Создаем новый URL с необходимой породой и запускаем загрузку
                UrlData urlData = UrlData.createInstance(breedId);
                urlData.setNotify(notifyData);
                urlData.load();
            }
        });
        // Находим кнопку и подключаем слушателя, который перекидывает на лайки
        Button liked = findViewById(R.id.liked);
        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityForLikes.class);
                startActivity(intent);
            }
        });
    }

    public class BreedLoader extends AsyncTask<Void, Void, Void> {
        FuncStart func;
        public BreedLoader(FuncStart func) {
            super();
            this.func = func;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Прикрепляем загруженную инфу
            func.start();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("x-api-key", "b4dc3668-386c-4324-8616-a397748c2b36")
                    .url("https://api.thecatapi.com/v1/breeds")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                List<Breed> breeds = mapper.readValue(body, new TypeReference<List<Breed>>(){});
                BreedData.createInstance(breeds);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        }
}
