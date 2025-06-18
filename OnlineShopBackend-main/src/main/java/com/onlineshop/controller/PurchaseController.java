package com.onlineshop.controller;

import com.onlineshop.apifirst.api.PurchaseApiDelegate;
import com.onlineshop.domain.vo.PurchaseRequest;
import com.onlineshop.domain.vo.PurchaseResponse;
import com.onlineshop.repository.entities.Purchase;
import com.onlineshop.service.purchase.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Clase que implementa la interfaz PurchaseApiDelegate para manejar las operaciones relacionadas con las compras.
@Slf4j
@RestController
@RequiredArgsConstructor
public class PurchaseController implements PurchaseApiDelegate {

	private final ConversionService conversionService;

	private final PurchaseFindAllService purchaseFindAllService;

	private final PurchaseQueryService purchaseQueryService;

	private final PurchaseDeleteService purchaseDeleteService;

	private final PurchaseCreateService purchaseCreateService;

	private final PurchaseCancellationService purchaseCancellationService;

	/**
	 * Elimina una compra por su ID.
	 * @param purchaseId ID de la compra a eliminar.
	 * @return Respuesta con un mensaje de confirmación de eliminación.
	 */
	@Override
	public ResponseEntity<String> deletePurchaseById(Long purchaseId) {
		log.info("INIT - PurchaseController -> deletePurchaseById()");
		String message = purchaseDeleteService.deletePurchaseById(purchaseId);
		log.info("END - PurchaseController -> deletePurchaseById()");
		return ResponseEntity.ok(message);
	}

	/**
	 * Obtiene todas las compras.
	 * @return Respuesta con la lista de compras.
	 */
	@Override
	public ResponseEntity<List<PurchaseResponse>> getAllPurchases() {
		log.info("INIT - PurchaseController -> getAllPurchases()");
		List<PurchaseResponse> obtainedPurchases = purchaseFindAllService.findAllPurchases()
			.stream()
			.map(purchase -> conversionService.convert(purchase, PurchaseResponse.class))
			.toList();
		log.info("END - PurchaseController -> getAllPurchases()");
		return ResponseEntity.ok(obtainedPurchases);
	}

	/**
	 * Obtiene una compra por su ID.
	 * @param purchaseId ID de la compra a obtener.
	 * @return Respuesta con los detalles de la compra.
	 */
	@Override
	public ResponseEntity<PurchaseResponse> getPurchaseById(Long purchaseId) {
		log.info("INIT - PurchaseController -> getPurchaseById()");
		Purchase obtainedPurchase = purchaseQueryService.findPurchaseById(purchaseId);
		PurchaseResponse purchaseResponse = conversionService.convert(obtainedPurchase, PurchaseResponse.class);
		log.info("END - PurchaseController -> getPurchaseById()");
		return ResponseEntity.ok(purchaseResponse);
	}

	/**
	 * Crea una nueva compra.
	 * @param purchaseRequest Detalles de la compra a crear.
	 * @return Respuesta con los detalles de la compra creada.
	 */
	@Override
	public ResponseEntity<PurchaseResponse> newPurchase(PurchaseRequest purchaseRequest) {
		log.info("INIT - PurchaseController -> newPurchase()");
		Purchase savedPurchase = purchaseCreateService.makePurchase(purchaseRequest);
		PurchaseResponse purchaseResponse = conversionService.convert(savedPurchase, PurchaseResponse.class);
		log.info("END - PurchaseController -> newPurchase()");
		return ResponseEntity.ok(purchaseResponse);
	}

	/**
	 * Solicita la cancelación de una compra.
	 * @param purchaseId ID de la compra a cancelar.
	 * @return Respuesta con los detalles de la compra cancelada.
	 */
	@Override
	public ResponseEntity<PurchaseResponse> purchaseCancellationRequest(Long purchaseId) {
		log.info("INIT - PurchaseController -> cancellationRequest()");
		PurchaseResponse purchaseResponse = conversionService
			.convert(purchaseCancellationService.cancelPurchaseById(purchaseId), PurchaseResponse.class);
		log.info("END - PurchaseController -> cancellationRequest()");
		return ResponseEntity.ok(purchaseResponse);
	}

	/**
	 * Valida la cancelación de una compra.
	 * @param purchaseId ID de la compra a validar para cancelación.
	 * @return Respuesta con los detalles de la compra validada para cancelación.
	 */
	@Override
	public ResponseEntity<PurchaseResponse> purchaseCancellationValidation(Long purchaseId) {
		log.info("INIT - PurchaseController -> cancellationValidation()");
		PurchaseResponse purchaseResponse = conversionService
			.convert(purchaseCancellationService.cancelPurchaseConfirmationById(purchaseId), PurchaseResponse.class);
		log.info("END - PurchaseController -> cancellationValidation()");
		return ResponseEntity.ok(purchaseResponse);
	}

}
