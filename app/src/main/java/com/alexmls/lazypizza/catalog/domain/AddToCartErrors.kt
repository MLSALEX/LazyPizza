package com.alexmls.lazypizza.catalog.domain

// Throw when product is not found
class MissingProductException(val productId: String) :
    RuntimeException("Product not found: $productId")

// Throw when some toppings requested by id were not found
class MissingToppingsException(val missingIds: List<String>) :
    RuntimeException("Toppings not found: $missingIds")