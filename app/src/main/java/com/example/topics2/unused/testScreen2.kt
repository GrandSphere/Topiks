package com.example.topics2.unused
//import androidx.compose.material.FloatingActionButton

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

// ChatBubble Composable
@Composable
fun ChatBubble(message: String, maxLines: Int = 3) {
    var isExpanded by remember { mutableStateOf(false) }
    var textOverflows by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(8.dp)) {
        Box(
            modifier = Modifier
                .background(color = Color.Cyan, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                //.background(color = Color.Cyan, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = message,
                maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black,
                onTextLayout = { textLayoutResult ->
                    textOverflows = textLayoutResult.hasVisualOverflow
                },
                modifier = Modifier.animateContentSize(),
                fontSize = 16.sp // For better readability
            )


        }

        if (textOverflows || isExpanded) {
            TextButton(onClick = { isExpanded = !isExpanded }) {
                Text(if (isExpanded) "Show Less" else "Show More")
            }
        }

    }

}
// Preview Function
@Composable
fun PreviewChatBubble() {
    ChatBubble(message = getSampleChatMessage())
}






// Resized Component Preview Composable
@Composable
fun PreviewResizedComponents(content: String, maxContentHeight: Dp = 200.dp) {
    var isExpandedState by remember { mutableStateOf(false) }
    var isContentOverflowing by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .background(color = Color.LightGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                .padding(12.dp)
                .fillMaxWidth()
                .heightIn(max = maxContentHeight)
                .animateContentSize()
                //.verticalScroll(rememberScrollState())  // Corrected scrollable modifier
        ) {
            Text(
                text = content,
                maxLines = if (isExpandedState) Int.MAX_VALUE else 5,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Show "Show More" Button if content overflows
        if (isContentOverflowing || isExpandedState) {
            TextButton(onClick = { isExpandedState = !isExpandedState }) {
                Text(if (isExpandedState) "Show Less" else "Show More")
            }
        }
    }
}

// Sample Message Function
fun generateSampleMessage(): String {
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

// Preview Function
@Composable
@Preview
fun PreviewResizedComponentsView() {
    PreviewResizedComponents(content = generateSampleMessage())
}



/*
@Composable
fun OverflowingLayoutExample() {
    var isExpanded by remember { mutableStateOf(false) }
    val maxHeight = if (isExpanded) 1000.dp else 300.dp  // Adjust max height on expansion

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight) // Limit the maximum height of the column
            .verticalScroll(rememberScrollState()) // Make the entire column scrollable
            .padding(16.dp)
    ) {
        // Simulating multiple components inside a column
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                Text("Item $it", modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show "Show More" Button
        Button(onClick = { isExpanded = !isExpanded }) {
            Text(text = if (isExpanded) "Show Less" else "Show More")
        }
    }
}

@Composable
@Preview
fun PreviewOverflowingLayout() {
    OverflowingLayoutExample()
}
 */

@Composable
fun OverflowingLayoutExample() {
    var isExpanded by remember { mutableStateOf(false) }
    val maxHeight = if (isExpanded) 1000.dp else 300.dp  // Adjust max height on expansion

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Column that holds all your components (like boxes, images, etc.)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxHeight) // Limit the maximum height of the column
                //.verticalScroll(rememberScrollState()) // Make the column scrollable
        ) {
            // Simulating multiple components inside a column
            repeat(10) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Text("Item $it", modifier = Modifier.align(Alignment.Center))
                }
            }
        }





        FloatingActionButton(
            onClick = { isExpanded = !isExpanded },
            shape = CircleShape, // Change the shape to rounded corners
            modifier = Modifier
                .align(Alignment.BottomEnd)
                //.align(Alignment.BottomEnd) // Align it to bottom end of the Box
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Add Topic",
                modifier = Modifier.size(24.dp)
                             .graphicsLayer(
            rotationZ = if (isExpanded) -90f else 90f
        )
            )
        }



        // "Show More" / "Show Less" Button at the bottom of the Box
        /*
        Button(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Position it at the bottom center
                .padding(top = 16.dp) // Add some space above the button
        ) {
            Text(text = if (isExpanded) "Show Less" else "Show More")
        }
        */



    }
}

@Composable
@Preview
fun PreviewOverflowingLayout() {
    OverflowingLayoutExample()
}