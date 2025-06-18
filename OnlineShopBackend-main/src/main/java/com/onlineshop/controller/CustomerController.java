package com.onlineshop.controller;

import com.onlineshop.apifirst.api.UsersApiDelegate;
import com.onlineshop.domain.vo.CustomerRequest;
import com.onlineshop.domain.vo.CustomerResponse;
import com.onlineshop.repository.entities.Customer;
import com.onlineshop.service.customer.CustomerDeleteService;
import com.onlineshop.service.customer.CustomerFindAllService;
import com.onlineshop.service.customer.CustomerQueryService;
import com.onlineshop.service.customer.CustomerUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Clase que implementa la interfaz UsersApiDelegate para manejar las operaciones relacionadas con los usuarios.
@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController implements UsersApiDelegate {

	private final CustomerQueryService customerQueryService;

	private final CustomerDeleteService customerDeleteService;

	private final CustomerUpdateService customerUpdateService;

	private final CustomerFindAllService customerFindAllService;

	private final ConversionService conversionService;

	/**
	 * Obtiene un usuario por su nombre de usuario.
	 * @param username Nombre de usuario del cliente.
	 * @return Respuesta con los detalles del cliente.
	 */
	@Override
	public ResponseEntity<CustomerResponse> getUserByName(String username) {
		log.info("INIT - CustomerController -> getUserByName()");
		Customer customer = customerQueryService.getCustomerByUserName(username);
		CustomerResponse customerResponse = conversionService.convert(customer, CustomerResponse.class);
		log.info("END - CustomerController -> getUserByName()");
		return ResponseEntity.ok(customerResponse);
	}

	/**
	 * Actualiza un usuario.
	 * @param user Detalles del cliente a actualizar.
	 * @return Respuesta con los detalles del cliente actualizado.
	 */
	@Override
	public ResponseEntity<CustomerResponse> updateUser(CustomerRequest user) {
		log.info("INIT - CustomerController -> updateUser()");
		Customer customer = conversionService.convert(user, Customer.class);
		Customer updatedCustomer = customerUpdateService.updateCustomer(customer);
		CustomerResponse customerResponse = conversionService.convert(updatedCustomer, CustomerResponse.class);
		log.info("END - CustomerController -> updateUser()");
		return ResponseEntity.ok(customerResponse);
	}

	/**
	 * Elimina un usuario por su nombre de usuario.
	 * @param username Nombre de usuario del cliente a eliminar.
	 * @return Respuesta vacía indicando que la operación fue exitosa.
	 */
	@Override
	public ResponseEntity<Void> deleteUser(String username) {
		log.info("INIT - CustomerController -> deleteUser()");
		customerDeleteService.deleteCustomerByUsername(username);
		log.info("END - CustomerController -> deleteUser()");
		return ResponseEntity.ok(null);
	}

	/**
	 * Obtiene todos los usuarios.
	 * @return Respuesta con la lista de todos los clientes.
	 */
	@Override
	public ResponseEntity<List<CustomerResponse>> getUsers() {
		log.info("INIT - CustomerController -> getUsers()");
		List<CustomerResponse> customerResponses = customerFindAllService.findAllCustomers()
			.stream()
			.map(customer -> conversionService.convert(customer, CustomerResponse.class))
			.toList();
		log.info("END - CustomerController -> getUsers()");
		return ResponseEntity.ok(customerResponses);
	}

}
