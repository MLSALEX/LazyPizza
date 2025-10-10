package com.alexmls.lazypizza.catalog.data.seed

data class SeedProduct(
    val id: String,
    val name: String,
    val description: String,
    val priceCents: Int,
    val category: String,
    val imagePath: String,
    val isActive: Boolean = true
)

val seedProducts: List<SeedProduct> = listOf(
    // Pizzas
    SeedProduct("pizza_margherita","Margherita","Tomato sauce, mozzarella, fresh basil, olive oil",899,"pizza","pizza/Margherita.png"),
    SeedProduct("pizza_pepperoni","Pepperoni","Tomato sauce, mozzarella, pepperoni",999,"pizza","pizza/Pepperoni.png"),
    SeedProduct("pizza_hawaiian","Hawaiian","Tomato sauce, mozzarella, ham, pineapple",1049,"pizza","pizza/Hawaiian.png"),
    SeedProduct("pizza_bbq_chicken","BBQ Chicken","BBQ sauce, mozzarella, grilled chicken, onion, corn",1149,"pizza","pizza/BBQ Chicken.png"),
    SeedProduct("pizza_four_cheese","Four Cheese","Mozzarella, gorgonzola, parmesan, ricotta",1199,"pizza","pizza/Four Cheese.png"),
    SeedProduct("pizza_veggie_delight","Veggie Delight","Tomato sauce, mozzarella, mushrooms, olives, bell pepper, onion, corn",979,"pizza","pizza/Veggie Delight.png"),
    SeedProduct("pizza_meat_lovers","Meat Lovers","Tomato sauce, mozzarella, pepperoni, ham, bacon, sausage",1249,"pizza","pizza/Meat Lovers.png"),
    SeedProduct("pizza_spicy_inferno","Spicy Inferno","Tomato sauce, mozzarella, spicy salami, jalapeños, red chili pepper, garlic",1129,"pizza","pizza/Spicy Inferno.png"),
    SeedProduct("pizza_seafood_special","Seafood Special","Tomato sauce, mozzarella, shrimp, mussels, squid, parsley",1399,"pizza","pizza/Seafood Special.png"),
    SeedProduct("pizza_truffle_mushroom","Truffle Mushroom","Cream sauce, mozzarella, mushrooms, truffle oil, parmesan",1299,"pizza","pizza/Truffle Mushroom.png"),

    // Drinks
    SeedProduct("drink_7_up","7-Up","",189,"drink","drink/7-up.png"),
    SeedProduct("drink_apple_juice","Apple Juice","",229,"drink","drink/apple juice.png"),
    SeedProduct("drink_iced_tea","Iced Tea","",219,"drink","drink/iced tea.png"),
    SeedProduct("drink_orange_juice","Orange Juice","",249,"drink","drink/orange juice.png"),
    SeedProduct("drink_pepsi","Pepsi","",199,"drink","drink/pepsi.png"),
    SeedProduct("drink_mineral_water","Mineral Water","",149,"drink","drink/mineral water.png"),

    // Sauces
    SeedProduct("sauce_cheese","Cheese Sauce","",89,"sauce","sauce/Cheese Sauce.png"),
    SeedProduct("sauce_garlic","Garlic Sauce","",59,"sauce","sauce/Garlic Sauce.png"),
    SeedProduct("sauce_spicy_chili","Spicy Chili Sauce","",59,"sauce","sauce/Spicy Chili Sauce.png"),
    SeedProduct("sauce_bbq","BBQ Sauce","",59,"sauce","sauce/BBQ Sauce.png"),

    // Ice cream
    SeedProduct("icecream_vanilla","Vanilla Ice Cream","",249,"icecream","ice cream/vanilla.png"),
    SeedProduct("icecream_chocolate","Chocolate Ice Cream","",249,"icecream","ice cream/chocolate.png"),
    SeedProduct("icecream_strawberry","Strawberry Ice Cream","",249,"icecream","ice cream/strawberry.png"),
    SeedProduct("icecream_cookies","Cookies Ice Cream","",279,"icecream","ice cream/cookies.png"),
    SeedProduct("icecream_pistachio","Pistachio Ice Cream","",299,"icecream","ice cream/pistachio.png"),
    SeedProduct("icecream_mango_sorbet","Mango Sorbet","",269,"icecream","ice cream/mango sorbet.png"),
)