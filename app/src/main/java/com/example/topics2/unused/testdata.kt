package com.example.topics2.unused

fun getTestImagePaths(): List<String> {
    // Example paths (you would replace these with actual file paths)
    return listOf(
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F27.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F22.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F24.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F28.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F23.jpg",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F20.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F26.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F21.png",
        "content://com.android.externalstorage.documents/document/primary%3ADocuments%2FtopicsContent%2F25.png",
    )
}

// Sample Message Function


fun getSampleChatMessage(): String {
    return """
        This is a long sample chat message designed for debugging purposes.
        It has multiple lines of text to test overflow functionality in the chat bubble.
        
        Here's another paragraph to simulate a real-world chat scenario.
        This message should include enough content to exceed the maximum height
        or number of lines allowed in the chat bubble.
        
        Line 1: Testing line wrapping in Jetpack Compose.
        Line 2: Checking if the 'Show More' button is displayed as expected.
        Line 3: Ensuring that the UI updates when expanding or collapsing the chat bubble.
        Line 4: Adding even more lines to push the limits of the chat bubble size.
        
        Paragraph 2: Lorem ipsum dolor sit amet, consectetur adipiscing elit.
        Donec vehicula ullamcorper mi a tempor. Praesent at libero ornare, 
        facilisis sem sed, elementum nunc. Proin tincidunt posuere odio, 
        vitae malesuada nisl bibendum vel.
        
        Extra lines for debugging:
        - Line A
        - Line B
        - Line C
        
        End of the sample message. Happy debugging!
    """.trimIndent()
}

/*
fun generateSampleList(): List<String> {
    return listOf(
        "Apple", "Banana", "Orange", "Grapes", "Pineapple", "Strawberry", "Blueberry", "Blackberry",
        "Mango", "Peach", "Cherry", "Watermelon", "Coconut", "Pear", "Kiwi", "Lemon", "Lime", "Guava",
        "Papaya", "Plum", "Raspberry", "Cranberry", "Fig", "Pomegranate", "Apricot", "Dragonfruit",
        "Lychee", "Passionfruit", "Tangerine", "Starfruit", "Durian", "Avocado", "Jackfruit",
        "Tomato", "Cucumber", "Pumpkin", "Carrot", "Broccoli", "Cauliflower", "Spinach", "Lettuce",
        "Zucchini", "Eggplant", "Potato", "Onion", "Garlic", "Radish", "Beetroot", "Asparagus",
        "Artichoke", "Celery", "Turnip", "Parsnip", "Leek", "Okra", "Chilli", "Peas", "Beans",
        "Corn", "Squash", "Brussels Sprouts", "Kale", "Collard Greens", "Swiss Chard", "Arugula",
        "Cabbage", "Bok Choy", "Endive", "Mustard Greens", "Napa Cabbage", "Rutabaga", "Shallot",
        "Sweet Potato", "Yam", "Watercress", "Chestnut", "Hazelnut", "Walnut", "Pecan", "Cashew",
        "Almond", "Peanut", "Macadamia", "Pistachio", "Sunflower Seeds", "Pumpkin Seeds",
        "Sesame Seeds", "Flaxseeds", "Chia Seeds", "Hemp Seeds", "Quinoa", "Rice", "Barley",
        "Oats", "Wheat", "Cornmeal", "Couscous", "Bulgar", "Buckwheat", "Rye", "Spelt",
        "Amaranth", "Teff", "Millet", "Sorghum", "Polenta", "Tapioca", "Sago", "Arrowroot",
        "Agar Agar", "Gelatin", "Pasta", "Noodles", "Bread", "Bagel", "Croissant", "Donut",
        "Pancake", "Waffle", "Muffin", "Cake", "Cookie", "Brownie", "Pie", "Tart", "Pudding",
        "Custard", "Ice Cream", "Sorbet", "Gelato", "Frozen Yogurt", "Cheesecake", "Eclair",
        "Macaron", "Cupcake", "Candy", "Chocolate", "Caramel", "Fudge", "Truffle", "Nougat",
        "Marshmallow", "Toffee", "Lollipop", "Gum", "Mint", "Cereal", "Granola", "Protein Bar",
        "Energy Bar", "Snack Bar", "Chips", "Popcorn", "Pretzel", "Cracker", "Biscuit", "Cheese",
        "Butter", "Yogurt", "Milk", "Cream", "Eggs", "Ham", "Bacon", "Sausage", "Steak", "Chicken",
        "Turkey", "Duck", "Lamb", "Pork", "Beef", "Goat", "Fish", "Salmon", "Tuna", "Shrimp",
        "Crab", "Lobster", "Oyster", "Mussel", "Clam", "Scallop", "Squid", "Octopus", "Anchovy",
        "Sardine", "Herring", "Cod", "Trout", "Catfish", "Mackerel", "Tilapia", "Halibut",
        "Flounder", "Sea Bass", "Snapper", "Swordfish", "Shark", "Eel", "Caviar", "Roe",
        "Seaweed", "Algae", "Kelp", "Nori", "Wasabi", "Soy Sauce", "Vinegar", "Oil", "Butter",
        "Salt", "Pepper", "Garlic Powder", "Onion Powder", "Chili Powder", "Paprika", "Cinnamon",
        "Nutmeg", "Clove", "Cardamom", "Coriander", "Cumin", "Turmeric", "Ginger", "Mustard",
        "Bay Leaf", "Oregano", "Basil", "Thyme", "Rosemary", "Parsley", "Cilantro", "Dill",
        "Tarragon", "Sage", "Mint", "Vanilla", "Chocolate Syrup", "Strawberry Syrup", "Caramel Sauce",
        "Honey", "Maple Syrup", "Jam", "Jelly", "Marmalade", "Peanut Butter", "Almond Butter",
        "Cashew Butter", "Tahini", "Hummus", "Guacamole", "Salsa", "Relish", "Ketchup", "Mayonnaise",
        "Mustard", "Ranch Dressing", "Caesar Dressing", "Italian Dressing", "Blue Cheese Dressing",
        "Thousand Island Dressing", "Vinaigrette", "BBQ Sauce", "Hot Sauce", "Teriyaki Sauce",
        "Soy Sauce", "Fish Sauce", "Oyster Sauce", "Hoisin Sauce", "Sweet and Sour Sauce",
        "Chili Sauce", "Sriracha", "Tabasco", "Pesto", "Marinara", "Alfredo", "Bolognese",
        "Carbonara", "Arrabbiata", "Putanesca", "Napolitana", "Bechamel", "Veloute", "Espagnole",
        "Hollandaise", "Bearnaise", "Mornay", "Romesco", "Aioli", "Tapenade", "Chimichurri",
        "Gremolata", "Sofrito", "Ragout", "Chowder", "Bisque", "Bouillabaisse", "Gazpacho",
        "Minestrone", "Tomato Soup", "Chicken Noodle Soup", "Beef Stew", "Lentil Soup", "Split Pea Soup",
        "Vegetable Soup", "Miso Soup", "Clam Chowder", "Corn Chowder", "Potato Soup", "Cheddar Soup",
        "Broccoli Soup", "Pumpkin Soup", "Carrot Soup", "Sweet Potato Soup", "Butternut Squash Soup",
        "Zucchini Soup", "Mushroom Soup", "Onion Soup", "French Onion Soup", "Garlic Soup", "Spinach Soup",
        "Asparagus Soup", "Artichoke Soup", "Leek Soup", "Cauliflower Soup", "Cabbage Soup", "Kale Soup"
        // Add more strings if needed to reach 500
    )
}

 */