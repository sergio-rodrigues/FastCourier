package pt.srodrigues.srod.fastcourier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.srodrigues.srod.ClientLocator;
import com.srodrigues.srod.layer.java.mqtt.MQTTInitialContext;
import com.srodrigues.srod.layer.marshalling.json.JSONMarshalling;
import com.srodrigues.test.Constants;
import com.srodrigues.test.srod.CourierTasks;

public class Courier implements CourierTasks {

	private static final long serialVersionUID = 1L;
	
	private final CourierTasks courier ;
	
	// delegate methods from the CourierTasks interface to handler object
	@Override
	public boolean add(final String task) {
		return courier.add(task);
	}

	@Override
	public boolean delete(final String task) {
		return courier.delete(task);
	}

	@Override
	public List<String> get() {
		return courier.get();
	}

	public Courier() throws MalformedURLException, IOException {
		//Initialize framework
		ClientLocator.addContext( new MQTTInitialContext("srod://192.168.1.99", Constants.NAMESPACE, new JSONMarshalling() ) ) ;

		//request service
		this.courier = ClientLocator.getService(Constants.SERVICE, CourierTasks.class) ;
	}
}
