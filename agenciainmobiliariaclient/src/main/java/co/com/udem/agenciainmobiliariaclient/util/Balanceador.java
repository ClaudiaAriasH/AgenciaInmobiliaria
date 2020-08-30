package co.com.udem.agenciainmobiliariaclient.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

@Component
public class Balanceador {

	private static final Logger logger = LogManager.getLogger(Balanceador.class);
	@Autowired
	private  LoadBalancerClient loadBalancer;

	private Balanceador() {

	}

	public  String urlBalanceador() {
		ServiceInstance serviceInstance = loadBalancer.choose("agenciainmobiliaria");
		logger.info(serviceInstance.getUri());

		return serviceInstance.getUri().toString();
	}
}
