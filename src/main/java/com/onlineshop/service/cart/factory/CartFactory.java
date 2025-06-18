package com.onlineshop.service.cart.factory;

import com.onlineshop.domain.vo.CartRequest;
import com.onlineshop.domain.vo.CustomerResponse;
import com.onlineshop.repository.entities.Cart;
import com.onlineshop.repository.entities.CartDetails;
import com.onlineshop.repository.entities.Customer;
import com.onlineshop.repository.entities.Product;
import com.onlineshop.service.auth.AuthenticationService;
import com.onlineshop.service.cart.impl.CartFindQueryServiceImpl;
import com.onlineshop.service.customer.CustomerQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// Factory que crea un carrito con los detalles del cliente y los productos
@Slf4j
@Component
@RequiredArgsConstructor
public class CartFactory {

	private final CartFindQueryServiceImpl cartFindQueryServiceImpl;

	private final CustomerQueryService customerQueryService;

	private final AuthenticationService authenticationService;

	/**
	 * Método que crea un carrito con los detalles del cliente y los productos.
	 * @param cartRequest Objeto que contiene la información del carrito a crear.
	 * @return Cart objeto que representa el carrito creado.
	 */
	public Cart createCartWithCustomerAndCartDetails(CartRequest cartRequest) {
		log.info("INIT - CartFactory -> createCartWithCustomerAndCartDetails()");
		// Obtiene el usuario autenticado y su información de cliente
		CustomerResponse customerResponse = authenticationService.getAuthenticatedUser();
		Customer customer = customerQueryService.getCustomerByUserName(customerResponse.getUsername());

		// Crea un nuevo carrito y establece el cliente y la fecha actual
		Cart cart = new Cart();
		cart.setCustomer(customer);
		cart.setDate(LocalDateTime.now());

		// Verifica si se proporcionaron detalles del carrito
		List<CartDetails> cartDetailsList = cartRequest.getCartDetails().stream().map(details -> {
			CartDetails cartDetails = new CartDetails();
			cartDetails.setCart(cart);
			Product product = cartFindQueryServiceImpl.findProductById(details);
			cartDetails.setProduct(product);
			cartDetails.setQuantity(details.getQuantity());
			cartDetails.setPrice(product.getPrice() * details.getQuantity());
			cartDetails.setDeliveryAddress(cartRequest.getDeliveryAddress());
			return cartDetails;
		}).collect(Collectors.toList());
		cart.setCartDetails(cartDetailsList);
		log.info("END - CartFactory -> createCartWithCustomerAndCartDetails() - The cart was created successfully");
		return cart;
	}

}
