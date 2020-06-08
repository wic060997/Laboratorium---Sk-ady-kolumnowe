import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import edu.umd.cs.findbugs.annotations.NonNull;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import javax.annotation.Nullable;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;

public class CarCodec extends MappingCodec<UdtValue,Samochod> {
    public CarCodec(@NonNull TypeCodec<UdtValue> innerCodec) {
        super(innerCodec, GenericType.of(Samochod.class));
    }

    @NonNull
    @Override
    public UserDefinedType getCqlType(){
        return (UserDefinedType) super.getCqlType();
    }

    @Nullable
    @Override
    protected Samochod innerToOuter(@Nullable UdtValue udtValue) {
        return udtValue == null ? null: new Samochod(udtValue.getString("marka"),udtValue.getString("model"),udtValue.getInt("rok"),udtValue.getInt("moc"),udtValue.getString("rej"));
    }

    @Nullable
    @Override
    protected UdtValue outerToInner(@Nullable Samochod car) {
        return car == null ? null: getCqlType().newValue().setString("marka",car.getMarka()).setString("model",car.getModel()).setInt("rok",car.getRokP()).setInt("moc",car.getkM()).setString("rej",car.getRej());
    }
}
