package com.example.topics2.unused


import android.R
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/*
fun generateSampleList(): List<String> {
    return listOf(
        "Apple Banana Orange", "Blueberry Strawberry Blackberry", "Beetroot Spinach Carrot",
        "Strawberry Banana Milkshake", "Grapes Pineapple Mango", "Orange Mango Smoothie",
        "Banana Split Dessert", "Apple Pie", "Blackberry Jam", "Carrot Cake", "Beetroot Juice",
        "Strawberry Ice Cream", "Blueberry Pancakes", "Blackberry Tart", "Pineapple Upside Down Cake",
        "Grape Jelly", "Orange Sorbet", "Mango Lassi", "Peach Cobbler", "Cherry Pie",
        "Watermelon Salad", "Coconut Water", "Pear Tart", "Kiwi Smoothie", "Lemonade",
        "Lime Margarita", "Guava Jam", "Papaya Salad", "Plum Sauce", "Raspberry Tart",
        "Cranberry Sauce", "Fig Newtons", "Pomegranate Juice", "Apricot Jam", "Dragonfruit Bowl",
        "Lychee Martini", "Passionfruit Cake", "Tangerine Sorbet", "Starfruit Salad",
        "Durian Pudding", "Avocado Toast", "Jackfruit Chips", "Tomato Soup", "Cucumber Sandwich",
        "Pumpkin Pie", "Carrot Muffin", "Broccoli Salad", "Cauliflower Mash", "Spinach Lasagna",
        "Lettuce Wrap", "Zucchini Bread", "Eggplant Parmesan", "Potato Gratin", "Onion Rings",
        "Garlic Bread", "Radish Pickle", "Beetroot Hummus", "Asparagus Soup", "Artichoke Dip",
        "Celery Sticks", "Turnip Mash", "Parsnip Chips", "Leek Tart", "Okra Stew", "Chilli Sauce",
        "Peas Soup", "Beans Salad", "Cornbread", "Squash Soup", "Brussels Sprouts Casserole",
        "Kale Chips", "Collard Greens Stir Fry", "Swiss Chard Salad", "Arugula Pesto", "Cabbage Slaw"
        // Add more strings as needed to reach 500
    )
}

class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performExactSequentialSearch(query: String) {
        viewModelScope.launch {
            val keywords = query.split(" ").filter { it.isNotBlank() }
            val results = dataList.filter { item ->
                var currentIndex = 0
                keywords.all { keyword ->
                    val index = item.indexOf(keyword, currentIndex)
                    if (index >= currentIndex) {
                        currentIndex = index + keyword.length
                        true
                    } else {
                        false
                    }
                }
            }
            _searchResults.value = results
        }
    }
}

@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.White)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performExactSequentialSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun App() {
    FuzzySearchScreen()
}

 */

/*

 */
fun generateSampleList(): List<String> {
    return listOf(
        "Apple Banana Orange", "Blueberry Strawberry Blackberry", "Beetroot Spinach Carrot",
        "Strawberry Grapes", "Grapes Pineapple Mango", "Orange Mango Smoothie",
        "Banana Split Dessert", "Apple Pie", "Blackberry Jam", "Carrot Cake", "Beetroot Juice",
        "Strawberry Ice Cream", "Blueberry Pancakes", "Blackberry Tart", "Pineapple Upside Down Cake",
        "Grape Jelly", "Orange Sorbet", "Mango Lassi", "Peach Cobbler", "Cherry Pie",
        "Watermelon Salad", "Coconut Water", "Pear Tart", "Kiwi Smoothie", "Lemonade",
        "Lime Margarita", "Guava Jam", "Papaya Salad", "Plum Sauce", "Raspberry Tart",
        "Cranberry Sauce", "Fig Newtons", "Pomegranate Juice", "Apricot Jam", "Dragonfruit Bowl",
        "Lychee Martini", "Passionfruit Cake", "Tangerine Sorbet", "Starfruit Salad",
        "Durian Pudding", "Avocado Toast", "Jackfruit Chips", "Tomato Soup", "Cucumber Sandwich",
        "Pumpkin Pie", "Carrot Muffin", "Broccoli Salad", "Cauliflower Mash", "Spinach Lasagna",
        "Lettuce Wrap", "Zucchini Bread", "Eggplant Parmesan", "Potato Gratin", "Onion Rings",
        "Garlic Bread", "Radish Pickle", "Beetroot Hummus", "Asparagus Soup", "Artichoke Dip",
        "Celery Sticks", "Turnip Mash", "Parsnip Chips", "Leek Tart", "Okra Stew", "Chilli Sauce",
        "Peas Soup", "Beans Salad", "Cornbread", "Squash Soup", "Brussels Sprouts Casserole",
        "Kale Chips", "Collard Greens Stir Fry", "Swiss Chard Salad", "Arugula Pesto", "Cabbage Slaw",
        "chonen die ansehen anrufen anderen den weibern. Pa ku an bist da beim wohl scho luke. Bist das lust sto kaum zeit bis buch. Nachmittag sonderling es erkundigte grasgarten regungslos ja. Gerbersteg auskleiden knabenhaft he aneinander zu am angenommen ubelnehmen. Leicht diesen herauf heimat musset bei wei. Ansehen schoner he so instand am.",
        "Gelernte gespielt sa er frohlich zu sprechen. Zu te dabei faden ja notig. Es em dazwischen vorsichtig unsicherer se. Gang ja im etwa duse he ob. Da ja en plotzlich duftenden unterwegs es schnellen. So mageren es ja zuhorte gerufen sondern nachdem spuckte. Bat grundlich fur ausdenken vom schwachen ausblasen kam. Endigend menschen ja madchens zu da. En da verlogen brannten gegangen gerberei in talseite gemessen. Ers leer floh auf habt wohl.",
        "Feierabend messingnen grasgarten zu la. Ture sehr mann hort ich mut dem. Du laut bist es eben hier trug. Grundstuck ab zu he lattenzaun dazwischen schuchtern so. Vor hab heimweh gerbers und samstag. Madchens gemessen in blaulich so hindurch liebsten. Hob mehr see man laut hand seid dort ehe moge.",
        "Vor bedeckten eintreten mir senkrecht. Wach kein was wort dem gewi tur man uber heut. Das litze wurde sahen neuen hat. Bin nicht auf horen wesen begru. Bi haus da buch vorn teil habs du er. Knabenhaft mi kuchenture werkstatte je grasgarten vorsichtig. Gab geheiratet feierabend ins nun hereintrat von ordentlich achthausen. Sofort karten es stunde jedoch je bi so stehen hangen. En im kopf stie hals warm. Konntet im melodie heiland es en raschen leuchte konnten.",
        "Weibern gebeugt steigst spruche gewesen auf dus gro. Geholfen sorgfalt getrennt im zu. Kiste was ubers guten ihnen dabei blies gru. Unruhig langsam gemacht nur nur oha schritt. Tod gehabt halfte spahte den schatz. Hof sie arme geht moge kinn kurz. Aufmerksam sog gesprachig verstehsts kartoffeln unsicherer ton. Wo halb es um er uben nein.",
        "Verlohnt he zwischen ab in einander pa. Bodenlosen he zu vorpfeifen dachkammer. Schwachem mannsbild ri schreibet um. Eia dessen lustig weg sachen neckte suchte. Wendete zuhorer dritten ein nachdem antwort gesicht tur den ton. Leuchtete neugierig dahinging vergnugen man ehe. Kam nachtessen was vertreiben brotkugeln see.",
        "Pa zu getafel ei seufzer da gewogen. Auf kollegen eigentum schmalen gegangen als. Tat langweilig ordentlich das was wohnzimmer. Ja befehlen so weiblein liebsten lachelte so te. Diesen zuruck hut ige kaffee bin kurios. Nettigkeit sa handarbeit ei du aneinander geschlafen getunchten leuchtturm. Sie erhaltenen jahreszeit zaunpfahle verrichtet knabenhaft des. Gab flo dem merken hinauf zarten kissen. Heiland gerbers im anblick bildnis zu. Wahrend flecken gewogen der braunen scheint was zweimal und.",
        "Da schweren mi an gewartet hinabsah brauchen da gegenden. Ruhig warum meine ruhen dafur tun zog vor moget. Kennt ihnen damit am leuen sagte wette es. Ja hauswesen geheimnis so wichszeug fu er. Gebe je fein habe mich du magd kerl wo so. Ordentlich nachmittag grasgarten das der. Spater welche dunner zu an. Fur gefunden ich schuppen bezahlen. Betrubtes mi im verlassen du schonsten furchtete ei. Nest sah satz sie kinn mirs grob see magd.",
        "Unruhig bruchig brummte du zu er wachter. Gerbers spuckte ri glatten fischen ku gefreut. Zerfasert sie nachsicht bedeckten blo und unterwegs grundlich. Jedoch seines regnen mir nur jemand haften gib das. Es ja geruckt familie nachtun es zwiebel dachern. Wandern um je schlich schwach. Bliebe konnen wie sie sieben hinter flo kammer daumen. Alt verschwand vorpfeifen ein marktplatz geheiratet weg befangenen bescheiden. Kammer wandte bandes jedoch schale zu du. Ja vorpfeifen nachmittag kuchenture sa grundstuck vielleicht wo um aufzulosen.",
        "Grasgarten bilderbuch se bi ob dienstmagd so hereintrat. Es ja bessern obenhin ri braunen er bereits schonen. Gegenteil ab am ausgeruht ja mu bedeckten. Voll kann er erde es seit. Stockwerke nachtessen he ja knabenhaft pa abendsuppe lattenzaun. Alt mag gebeugt gedeckt kleines unruhig. Lag die kennen lassen drehte hin.",
        // Add more strings as needed to reach 500
    )
}
/*
class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performMixedSearch(query: String) {
        viewModelScope.launch {
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Check for ordered matches (substrings without spaces must appear in order)
                val orderedKeywords = keywords.filter { !it.contains(" ") }
                var currentIndex = 0
                val orderedMatch = orderedKeywords.all { keyword ->
                    val index = lowercasedItem.indexOf(keyword, currentIndex)
                    if (index >= currentIndex) {
                        currentIndex = index + keyword.length
                        true
                    } else {
                        false
                    }
                }

                // Check for unordered matches (substrings with spaces can appear in any order)
                val unorderedKeywords = keywords.filter { it.contains(" ") }
                val unorderedMatch = unorderedKeywords.all { keyword ->
                    lowercasedItem.contains(keyword)
                }

                // Combine results
                orderedMatch && unorderedMatch
            }

            _searchResults.value = results
        }
    }
}

@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.Red)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun App() {
    FuzzySearchScreen()
}*/


/* // working ONE
@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.Red)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier.padding(8.dp))
            }
        }
    }
}

class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    private val dataList = generateSampleList()

    fun performMixedSearch(query: String) {
        viewModelScope.launch {
            // Split the query into substrings (case-insensitive)
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Ensure all substrings (keywords) are found in the item in any order
                keywords.all { keyword ->
                    lowercasedItem.contains(keyword)
                }
            }

            _searchResults.value = results
        }
    }
}

 */


//fun generateSampleList(): List<String> {
//    return listOf(
//        "Apple Banana Orange", "Blueberry Strawberry Blackberry", "Beetroot Spinach Carrot",
//        "Strawberry Grapes", "Grapes Pineapple Mango", "Orange Mango Smoothie",
//        "Banana Split Dessert", "Apple Pie", "Blackberry Jam", "Carrot Cake", "Beetroot Juice",
//        "Strawberry Ice Cream", "Blueberry Pancakes", "Blackberry Tart", "Pineapple Upside Down Cake",
//        "Grape Jelly", "Orange Sorbet", "Mango Lassi", "Peach Cobbler", "Cherry Pie",
//        "Watermelon Salad", "Coconut Water", "Pear Tart", "Kiwi Smoothie", "Lemonade",
//        "Lime Margarita", "Guava Jam", "Papaya Salad", "Plum Sauce", "Raspberry Tart",
//        "Cranberry Sauce", "Fig Newtons", "Pomegranate Juice", "Apricot Jam", "Dragonfruit Bowl",
//        "Lychee Martini", "Passionfruit Cake", "Tangerine Sorbet", "Starfruit Salad",
//        "Durian Pudding", "Avocado Toast", "Jackfruit Chips", "Tomato Soup", "Cucumber Sandwich",
//        "Pumpkin Pie", "Carrot Muffin", "Broccoli Salad", "Cauliflower Mash", "Spinach Lasagna",
//        "Lettuce Wrap", "Zucchini Bread", "Eggplant Parmesan", "Potato Gratin", "Onion Rings",
//        "Garlic Bread", "Radish Pickle", "Beetroot Hummus", "Asparagus Soup", "Artichoke Dip",
//        "Celery Sticks", "Turnip Mash", "Parsnip Chips", "Leek Tart", "Okra Stew", "Chilli Sauce",
//        "Peas Soup", "Beans Salad", "Cornbread", "Squash Soup", "Brussels Sprouts Casserole",
//        "Kale Chips", "Collard Greens Stir Fry", "Swiss Chard Salad", "Arugula Pesto", "Cabbage Slaw"
//        // Add more strings as needed to reach 500
//    )
//}


/*
@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.padding(16.dp).background(Color.Red)) {
        BasicTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults.size) { index ->
                Text(text = searchResults[index], modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = {Log.d("aaccdd",""})

                )

            }
        }
    }
}
*/
@Composable
fun App() {
    FuzzySearchScreen()
}


@Composable
fun FuzzySearchScreen(viewModel: FuzzySearchViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    val searchResults by viewModel.searchResults.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.DarkGray)
            .clip(RoundedCornerShape(4.dp))
    ) {
        BasicTextField(
            textStyle = TextStyle(fontSize = 20.sp) ,
            value = query,
            onValueChange = {
                query = it
                viewModel.performMixedSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResults) { item ->
                Text(
                    text = item,
                    fontSize = 16.sp,
                    color=Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(onClick = {
                            // Log the clicked text value
                            Log.d("aaccdd", "Clicked on: $item")
                        })
                )
            }
        }
    }
}

class FuzzySearchViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<String>>(emptyList())
    val searchResults: StateFlow<List<String>> = _searchResults

    //private val dataList = generateSampleList()
    private val dataList = generateMyList(50000)

    fun performMixedSearch(query: String) {
        viewModelScope.launch {
            // Split the query into substrings and handle exclusion with "!"
            val keywords = query.lowercase().split(" ").filter { it.isNotBlank() }

            val results = dataList.filter { item ->
                val lowercasedItem = item.lowercase()

                // Process each keyword to check for exclusion
                keywords.all { keyword ->
                    if (keyword.startsWith("!")) {
                        // If keyword starts with "!", exclude results containing this substring
                        !lowercasedItem.contains(keyword.substring(1)) // Remove the "!"
                    } else {
                        // Otherwise, include results that contain the substring
                        lowercasedItem.contains(keyword)
                    }
                }
            }

            _searchResults.value = results
        }
    }
}