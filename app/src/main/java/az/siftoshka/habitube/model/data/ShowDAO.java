package az.siftoshka.habitube.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import az.siftoshka.habitube.entities.show.Show;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ShowDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) Completable addShow(Show show);
    @Update Completable updateShow(Show show);
    @Transaction @Query("SELECT * FROM shows") Single<List<Show>> getShows();
    @Transaction @Query("SELECT count(*) FROM shows WHERE id = :showId") int getShowCount(int showId);
    @Transaction @Query("SELECT * FROM shows WHERE id = :showId") Maybe<Show> getShowById(int showId);
    @Delete Completable deleteShow(Show show);
    @Transaction @Query("DELETE FROM shows") Completable deleteAllShows();
}
