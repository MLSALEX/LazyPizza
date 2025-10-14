package com.alexmls.lazypizza.catalog.data.seed

data class SeedTopping(
    val id: String,
    val name: String,
    val priceCents: Int,
    val imagePath: String,
    val isActive: Boolean = true
)

val seedToppings: List<SeedTopping> = listOf(
    SeedTopping("bacon", "Bacon", 100, "topping/bacon.png"),
    SeedTopping("extra_cheese", "Extra Cheese", 100, "topping/cheese.png"),
    SeedTopping("corn", "Corn", 50, "topping/corn.png"),
    SeedTopping("tomato", "Tomato", 50, "topping/tomato.png"),
    SeedTopping("olives", "Olives", 50, "topping/olive.png"),
    SeedTopping("pepperoni", "Pepperoni", 100, "topping/pepperoni.png"),
    SeedTopping("mushrooms", "Mushrooms", 50, "topping/mashroom.png"),
    SeedTopping("basil", "Basil", 50, "topping/basil.png"),
    SeedTopping("pineapple", "Pineapple", 100, "topping/pineapple.png"),
    SeedTopping("onion", "Onion", 50, "topping/onion.png"),
    SeedTopping("chili_peppers", "Chili Peppers", 50, "topping/chilli.png"),
    SeedTopping("spinach", "Spinach", 50, "topping/spinach.png")
)