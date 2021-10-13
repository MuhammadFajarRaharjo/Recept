package com.belajar.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.belajar.recipes.data.Ingredient
import com.belajar.recipes.data.Recipe
import com.belajar.recipes.data.strawberryCake
import com.belajar.recipes.ui.AppBarCollapsedHeight
import com.belajar.recipes.ui.AppBarExpendedHeight
import com.belajar.recipes.ui.theme.*
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import kotlin.math.max
import kotlin.math.min

@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProvideWindowInsets {
                RecipesTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = Color.White) {
                        MainFragment(strawberryCake)
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MainFragment(recipe: Recipe) {
    val scrollState = rememberLazyListState()
    Box {
        Content(recipe, scrollState)
        ParallaxToolbar(recipe, scrollState)
    }
}

@Composable
fun ParallaxToolbar(recipe: Recipe, scrollState: LazyListState) {
    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOffset =
        with(LocalDensity.current) { imageHeight.roundToPx() } - LocalWindowInsets.current.statusBars.layoutInsets.top
    val offset = min(scrollState.firstVisibleItemScrollOffset, maxOffset)
    val offsetProgress = max(0f, offset * 3f - 2f * maxOffset) / maxOffset

    val state = remember { mutableStateOf(false) }
    val icon = if (state.value) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_favorite
    val color = if (state.value) Pink else Gray

    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = Color.White,
        modifier = Modifier
            .height(AppBarExpendedHeight)
            .offset { IntOffset(0, -offset) },
        elevation = if (offset == maxOffset) 4.dp else 0.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(imageHeight)
                    .graphicsLayer { alpha = 1f - offsetProgress }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.strawberry_pie_1),
                    contentDescription = "Strawberry pie 1",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.4f, Color.Transparent),
                                    Pair(1f, Color.White)
                                )
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = recipe.category,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(LightGray)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = (16 + 20 * offsetProgress).dp).scale(1f - 0.25f * offsetProgress)
                )
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp)
    ) {
        RoundedButton(R.drawable.ic_arrow_back)
        RoundedButton(
            iconResource = icon,
            color = color
        ) { state.value = !state.value }
    }
}

@Composable
fun RoundedButton(
    @DrawableRes iconResource: Int,
    color: Color = Gray,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(38.dp)
            .background(color = Color.White, shape = RoundedCornerShape(13.dp)),
    ) {
        Icon(painter = painterResource(id = iconResource), contentDescription = null, tint = color)
    }
}

@ExperimentalFoundationApi
@Composable
fun Content(recipe: Recipe, scrollState: LazyListState) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = AppBarExpendedHeight
        ),
        state = scrollState
    ) {
        item {
            BaseInformation(recipe)
            Text(
                text = recipe.description,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            ServingCalculator()
            IngredientsHeader()
            IngredientsList(recipe)
            AddShoppingListButton()
            Reviews(recipe)
        }
    }
}

@Composable
fun Reviews(recipe: Recipe) {
    Column(modifier = Modifier.padding(top = 10.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        ) {
            Text(text = "Reviews", fontWeight = FontWeight.Bold)
            TextButton(onClick = { /*TODO*/ }, contentPadding = PaddingValues(0.dp)) {
                Text(text = "See all", color = Pink)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Icon See all"
                )
            }
        }
        Row {
            Text(text = recipe.reviews.photos, color = DarkGray)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = recipe.reviews.comment, color = DarkGray)
        }
    }
}

@Composable
fun AddShoppingListButton() {
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = LightGray,
            contentColor = Color.Black,
        ),
        contentPadding = PaddingValues(15.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Add to shopping list")
    }
}

@ExperimentalFoundationApi
@Composable
fun IngredientsList(recipe: Recipe) {
    // menentukan berapa jumlah item yang akan di looping per row
    val numberOfItemsByRow = LocalConfiguration.current.screenWidthDp / 100
    Column {
        recipe.ingredients.chunked(numberOfItemsByRow).forEach { ingredient ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ingredient.forEach {
                    Column {
                        CardIngredient(ingredient = it)
                    }
                }
            }
        }
    }
    // LazyVerticalGrid(cells = GridCells.Adaptive(100.dp)) {
    //     items(recipe.ingredients.size) { index ->
    //         val ingredient = recipe.ingredients[index]
    //         Column(
    //             horizontalAlignment = Alignment.CenterHorizontally,
    //             modifier = Modifier.padding(bottom = 16.dp)
    //         ) {
    //             CardIngredient(ingredient)
    //         }
    //     }
    // }
}

@Composable
fun CardIngredient(ingredient: Ingredient) {
    Card(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = LightGray,
        modifier = Modifier
            .size(100.dp)
            .padding(bottom = 8.dp)
    ) {
        Image(
            painter = painterResource(id = ingredient.image),
            contentDescription = ingredient.title,
            modifier = Modifier.padding(18.dp)
        )
    }
    Text(
        text = ingredient.title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
    Text(
        text = ingredient.subtitle,
        color = DarkGray,
        fontSize = 14.sp
    )
}

@Composable
fun IngredientsHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .background(color = LightGray, shape = RoundedCornerShape(18.dp))
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("Ingredients", true, Modifier.weight(1f))
        TabButton("Tools", false, Modifier.weight(1f))
        TabButton("Steps", false, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) {
    Button(
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(18.dp),
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (active) Pink else LightGray,
            contentColor = if (active) Color.White else DarkGray
        ),
        elevation = null,
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun ServingCalculator() {
    val quantity = remember { mutableStateOf(1) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = LightGray, shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp)

    ) {
        Text(text = "Servings", modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        RoundedButton(iconResource = R.drawable.ic_minus, Pink) { quantity.value-- }
        Text(
            text = quantity.value.toString(),
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Medium
        )
        RoundedButton(iconResource = R.drawable.ic_plus, Pink) { quantity.value++ }
    }
}

@Composable
fun BaseInformation(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconInfo(
            iconResource = R.drawable.ic_clock,
            contentDescription = "Cooking time",
            text = recipe.cookingTime
        )
        IconInfo(
            iconResource = R.drawable.ic_flame,
            contentDescription = "Energy",
            text = recipe.energy
        )
        IconInfo(
            iconResource = R.drawable.ic_star,
            contentDescription = "Rating",
            text = recipe.rating
        )
    }
}

@Composable
fun IconInfo(@DrawableRes iconResource: Int, contentDescription: String, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = contentDescription,
            tint = Pink,
            modifier = Modifier.height(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true, widthDp = 380, heightDp = 1400)
@Composable
fun DefaultPreview() {
    RecipesTheme {
        MainFragment(strawberryCake)
    }
}