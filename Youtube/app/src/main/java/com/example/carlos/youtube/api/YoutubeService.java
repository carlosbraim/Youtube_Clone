package com.example.carlos.youtube.api;

        import com.example.carlos.youtube.model.Resultado;

        import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Query;

public interface YoutubeService {

    /*

    https://www.googleapis.com/youtube/v3/
    search
    ?part=snippet
    &order=date
    &maxResults=20
    &key=AIzaSyBFo7nAwPBWCwH-p5pRCQlfXojiK7iLIcw
    &channelId=UCVHFbqXqoYvEWM1Ddxl0QDg
    &q=pesquisa+restoda

https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=20&key=AIzaSyBFo7nAwPBWCwH-p5pRCQlfXojiK7iLIcw&channelId=UCVHFbqXqoYvEWM1Ddxl0QDg


     */

    @GET("search")    //Definir o que vai ser pesquisado com relação ao URL_BASE seria a extenção apos a /
    Call<Resultado> recuperarVideos( //retorna o objeto resultado

            @Query("part") String part,
            @Query("order") String order,
            @Query("maxResultspart") String maxResults,
            @Query("key") String key,
            @Query("channelId") String channelId,
            @Query("q") String q
    );

}
