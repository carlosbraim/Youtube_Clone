package com.example.carlos.youtube.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.carlos.youtube.R;
import com.example.carlos.youtube.adapter.AdapterVideo;
import com.example.carlos.youtube.api.YoutubeService;
import com.example.carlos.youtube.helper.RetrofitConfig;
import com.example.carlos.youtube.helper.YoutubeConfig;
import com.example.carlos.youtube.listener.RecyclerItemClickListener;
import com.example.carlos.youtube.model.Item;
import com.example.carlos.youtube.model.Resultado;
import com.example.carlos.youtube.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    //widget
    private RecyclerView recyclerVideos;
    private MaterialSearchView searchView;

    private List<Item> videos = new ArrayList<>();
    private Resultado resultado; //desta forma é possivel acessar o resultadoo de qualquer parte
    private AdapterVideo adapterVideos;

    //Propjriedades para o retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciar componentes
        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);


        //Configurações iniciais
        retrofit = RetrofitConfig.getRetofit();



        //Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);

        //Recupera videos
        recuperarVideos("");



        //Configurar métodos para SearchView para pesquisar
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//escreve algo e pesquisa apos o enter
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { // pesquisa enquanto digita cada palavra
                return false;
            }
        });


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() { //quando o usuario abrir o search

            }

            @Override
            public void onSearchViewClosed() { //quando o usuario fechar o search
                recuperarVideos("");
            }
        });

    }

    private void recuperarVideos(String pesquisa){

        String q = pesquisa.replaceAll(" ", "+");//trocar um carecter por outro dentro de um String
        YoutubeService youtubeService = retrofit.create(YoutubeService.class );

        youtubeService.recuperarVideos(
                "snippet", "date", "20",
                YoutubeConfig.CHAVE_YOUTUBE_API, YoutubeConfig.CANAL_ID, q
        ).enqueue(new Callback<Resultado>() {
            @Override
            public void onResponse(Call<Resultado> call, Response<Resultado> response) {
                Log.d("resultado", "resultado:"+response);
                if (response.isSuccessful()){
                     resultado = response.body();
                     videos = resultado.items;//recuperando a lista de itens
                     configurarRecyclerView();


                    /*Log.d("resultado", "resultado:"+resultado.regionCode);
                    Log.d("resultado detahado", "resultado detahado:"+resultado.items.get(0).snippet.title);//get pega o primeiro iten da lista
                    Log.d("resultado 1", "resultado 1:"+resultado.items.get(0).id.videoId);*/
                }
            }

            @Override
            public void onFailure(Call<Resultado> call, Throwable t) {

            }
        });
    }

    public void configurarRecyclerView(){

        adapterVideos = new AdapterVideo(videos,this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        recyclerVideos.setAdapter(adapterVideos);

        //configurar o evento de clique
        recyclerVideos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerVideos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Item video = videos.get(position);
                                String idVideo = video.id.videoId;

                                Intent i = new Intent(MainActivity.this, PlayerActivity.class);
                                i.putExtra("idVideo",idVideo);
                                startActivity(i);


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                ));

    }


    //criar o menu de pesquisa do search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);//recuperando o menu
        searchView.setMenuItem(item);

        return true;
    }
}
