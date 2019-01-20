package com.barath.app;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barath.app.model.Order;
import com.barath.app.serivce.OrderService;

@RestController
@RequestMapping( produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrderController {
	
	private final OrderService orderService;
	
	
	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}



	@PostMapping("/place/order")
	@ResponseStatus(code=HttpStatus.ACCEPTED)
	public void placeOrder(@RequestBody Order order) {
		
		this.orderService.publishOrder(order);
	}
	
	@PostMapping("/place/orders")
	@ResponseStatus(code=HttpStatus.ACCEPTED)
	public void placeOrders(@RequestBody List<Order> orders) {
		
		this.orderService.publishOrders(orders);
	}
	


}
