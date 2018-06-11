package autodex.com.autodex.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import autodex.com.autodex.model.webresponse.ProfileDetails;


/**
 * Created by yasar on 6/11/17.
 */
@Dao
public interface DaoProfileAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProfileDetails profileDetails);

//    @Query("SELECT * FROM profileDetails WHERE id :userIds")
//    ProfileDetails loadProfileByIds(int userIds);

    @Query("SELECT * FROM profileDetails")
    ProfileDetails loadProfileByIds();

    @Update
    void update(ProfileDetails profileDetails);

    @Delete
    void delete(ProfileDetails profileDetails);

    @Query("DELETE FROM profileDetails")
    void deleteAll();

    @Query("SELECT * FROM profileDetails")
    public ProfileDetails[] loadAllUsers();


}
