package com.example.Arena.di

import com.example.Arena.model.gamesmodel.GamesModel
import com.example.Arena.model.gdetailsmodel.GameDetailsModel
import com.example.Arena.model.genresmodel.GenresModel
import com.example.Arena.model.screenshotmodel.ScreenShotModel
import com.example.Arena.model.trailermodel.TrailerModel
import com.example.Arena.utils.Utils
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Games Api interface contains all method required to fetch specific data from Rawg api
 * @author Hassan Dwidar
 */
interface GamesApi {

    /**
     * getGenres method get data from rawg api
     * @return all genres
     */
    @GET("genres?key=${Utils.API_Key}")
    suspend fun getGenres(): GenresModel


    /**
     * getGenres method get data from rawg api
     * @return all genres
     * @param name of genre
     */

    @GET("games?key=${Utils.API_Key}")
    suspend fun getAllGames(@Query("genres") genre: String): GamesModel


    /**
     * getGenres method get games from rawg api based on its genres
     * @return all games
     * @param page is specific page source
     * @param pageSize is size of returned item per page
     * @param genre is name of the specific genre
     *  */


    @GET("games?key=${Utils.API_Key}")
    suspend fun getAllGamesPaging(
            @Query("page") page: Int,
            @Query("page_size") pageSize: Int,
            @Query("genres") genre: String): GamesModel


    /**
     * getGenres method search games from rawg api based on its genres
     *@param searchName name of game that want to be searched
     *  @return all games
     */
    @GET("games?key=${Utils.API_Key}")
    suspend fun searchOutGamesPaging(
            @Query("search") searchName: String,
            @Query("search_exact") exact:Boolean,
            @Query("search_precise") precise:Boolean): GamesModel


    /**
     * getGenres method get game from rawg api
     * @param id is slug of specific game
     * @return specific game
     */
    @GET("games/{id}?key=${Utils.API_Key}")
    suspend fun getSpecificGame(@Path("id") id: String): GameDetailsModel


    /**
     * getGameTrailer method get trailers from rawg api
     * @param id is slug of specific game
     * @return Trailer of target game
     */
    @GET("games/{id}/movies?key=${Utils.API_Key}")
    suspend fun getSpecificGameTrailer(@Path("id") id: String): TrailerModel


    /**
     * getSpecificGameScreenShot method get screenshot from rawg api
     *  @param id is slug of specific game
     * @return screenshot for target game
     */
    @GET("games/{id}/screenshots?key=${Utils.API_Key}")
    suspend fun getSpecificGameScreenshots(@Path("id") id: Int): ScreenShotModel


}
