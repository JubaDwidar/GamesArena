package com.example.Arena.di


import android.content.Context
import androidx.room.Room
import com.example.Arena.db.room.Dao
import com.example.Arena.db.room.RemoteKeyDAO
import com.example.Arena.db.room.RoomDb
import com.example.Arena.repository.GameRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Module Class constructs all dependency to directly inject in required class and repository
 */

@Module
@InstallIn(SingletonComponent::class)
class Modules {

    /**
     * getDbInstance construct room database instance
     */
    @Provides
    @Singleton
    fun getDbInstance(@ApplicationContext app: Context) =
            Room.databaseBuilder(app, RoomDb::class.java, "roomDb")
                    .fallbackToDestructiveMigration()
                    .build()

    /**
     * getDbDao construct Dao instance for crud Data
     */

    @Provides
    @Singleton
    fun getDbDao(roomDb: RoomDb): Dao = roomDb.games()


    /**
     * getDbDao construct Dao instance for saving data in order way
     */
    @Provides
    @Singleton
    fun getRemoteKeysDao(roomDb: RoomDb): RemoteKeyDAO = roomDb.gameRemote()

    /**
     * getGenreRepo construct instance of GenreRepository class
     * @param gamesApi is interface that handle api request
     */
    @Provides
    @Singleton
    fun getGenreRepo(gamesApi: GamesApi): GenreRepo = GenreRepo(gamesApi)

    /**
     * getGenreRepo construct instance of GenreRepository class
     * @param gamesApi is interface that handle api request
     * @param roomDb is Room database instance for saving data
     * @param dao is handle dataset
     */

    @ExperimentalCoroutinesApi
    @Provides
    @Singleton
    fun getGameRepo(gamesApi: GamesApi, dao: Dao, roomDb: RoomDb): GameRepo = GameRepo(gamesApi, dao, roomDb)


    /**
     * getRetrofit construct instance of Retrofit library
     */
    @Provides
    @Singleton
    fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * getGameService construct instance Dao to handle remote data
     * @param retrofit is instance of Retrofit library
     */

    @Provides
    @Singleton
    fun getGamesService(retrofit: Retrofit): GamesApi =
            retrofit.create(GamesApi::class.java)

}