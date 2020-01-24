package az.siftoshka.habitube.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import az.siftoshka.habitube.entities.show.Show;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ShowDAO {
    @Insert Completable addShow(Show show);
    @Transaction @Query("SELECT * FROM shows") Single<List<Show>> getShows();
    @Transaction @Query("SELECT count(*) FROM shows WHERE id = :showId") int getShowById(int showId);
    @Delete Completable deleteShow(Show show);
}
