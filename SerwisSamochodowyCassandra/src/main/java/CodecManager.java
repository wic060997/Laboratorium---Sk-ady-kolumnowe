import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;

public class CodecManager{
	CqlSession session;
	public CodecManager(CqlSession session) {
		this.session = session;
	}

	public void registerAddressCodec() {
		CodecRegistry codecRegistry = session.getContext().getCodecRegistry();
		UserDefinedType coordinatesUdt =
		    session
		        .getMetadata()
		        .getKeyspace("serwis")
		        .flatMap(ks -> ks.getUserDefinedType("samochod"))
		        .orElseThrow(IllegalStateException::new);
		TypeCodec<UdtValue> typeCodec = codecRegistry.codecFor(coordinatesUdt);
		CarCodec addressCodec = new CarCodec(typeCodec);
		((MutableCodecRegistry) codecRegistry).register(addressCodec);
	}
	
}
