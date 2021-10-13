package com.belajar.recipes.data

import androidx.annotation.DrawableRes
import com.belajar.recipes.R

data class Recipe(
    val title: String,
    val category: String,
    val cookingTime: String,
    val energy: String,
    val rating: String,
    val description: String,
    val reviews: Reviews,
    val ingredients: List<Ingredient>
)

data class Ingredient(@DrawableRes val image: Int, val title: String, val subtitle: String)
data class Reviews(val photos: String, val comment: String)

val strawberryCake = Recipe(
    title = "Strawberry Cake",
    category = "Desserts",
    cookingTime = "50 min",
    energy = "620 kcal",
    rating = "4.5",
    description = "This dessert is very tasty and not difficult to prepare. Also, you can replace strawberries with any other berry you like.",
    reviews = Reviews("84 Photos", "450 Comments"),
    ingredients = listOf(
        Ingredient(R.drawable.flour, "Flour", "450 g"),
        Ingredient(R.drawable.eggs, "Eggs", "4"),
        Ingredient(R.drawable.juice, "Lemon juice", "150 g"),
        Ingredient(R.drawable.strawberry, "Strawberry", "200 g"),
        Ingredient(R.drawable.suggar, "Sugar", "1 cup"),
        Ingredient(R.drawable.mind, "Mind", "20 g"),
        Ingredient(R.drawable.vanilla, "Vanilla", "1/2 teaspoon"),
    )
)
