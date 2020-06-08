import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

@Dao
public interface NaprawaDao {
    @Select
    Naprawa findById(Integer id);

    @Insert
    void save(Naprawa naprawa);

    @Update
    void update(Naprawa naprawa);

    @Delete
    void delete(Naprawa naprawa);
}
