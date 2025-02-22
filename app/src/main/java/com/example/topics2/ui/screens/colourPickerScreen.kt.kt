package com.example.topics2.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.topics2.ui.components.addTopic.CustomSlider
import com.example.topics2.ui.components.addTopic.chooseColorBasedOnLuminance
import com.example.topics2.ui.components.addTopic.colorToHex
import com.example.topics2.ui.components.addTopic.colorToHsv
import com.example.topics2.ui.components.addTopic.hexToColor
import com.example.topics2.ui.viewmodels.GlobalViewModelHolder
import com.example.topics2.ui.viewmodels.TopicViewModel


@Composable
fun ColourPickerScreen(navController: NavController, viewModel: TopicViewModel = viewModel()) {
    //val noteColour by viewModel.colour.collectAsState()
    val noteColour by viewModel.tempColour.collectAsState()

    //initialColor: Color = MaterialTheme.colorScheme.tertiary
    var initialColor: Color = Color.Cyan
    //val colors = MaterialTheme.colorScheme
    //val hsv = colorToHsv(noteColour)
    val hsv = colorToHsv(noteColour)
    val initialHue = hsv[0] // Hue
    val initialSaturation = hsv[1] // Saturation
    val initialValue = hsv[2] // Value
    val initialAlpha = initialColor.alpha
    // State to track the HSV components
    var hue by remember { mutableStateOf(initialHue) } // Use the preset value if provided
    var saturation by remember { mutableStateOf(initialSaturation) }
    var value by remember { mutableStateOf(initialValue) }
    var alpha by remember { mutableStateOf(initialAlpha) }
    // Calculate the selected color based on HSV
    //var bPastedColour : Boolean = false
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    var tempClip by remember { mutableStateOf("") }
    var bShouldPaste by remember { mutableStateOf(false) }
    val topBarViewModel = GlobalViewModelHolder.getTopBarViewModel()
    val colours = MaterialTheme.colorScheme
    LaunchedEffect(Unit) {
        topBarViewModel.setMenuItems(
            listOf(
            )
        )
    }
    LaunchedEffect(bShouldPaste) {
        if (bShouldPaste) {
            val clip = clipboardManager.primaryClip

            tempClip = clip?.getItemAt(0)?.text?.toString() ?: ""  // Fallback to empty string if clipboard is empty
            var tempHsv= colorToHsv(hexToColor(tempClip))

            hue = tempHsv[0]
            saturation = tempHsv[1]
            value = tempHsv[2]
            alpha = Color.Black.alpha
            bShouldPaste=false
        }

    }

    val newNoteColour = Color.hsv(hue, saturation, value, alpha)
    val vSpacer: Dp = 25.dp // You can change this value as needed
    val vIconSize: Dp = 25.dp // You can change this value as needed


    var bShouldCopy by remember { mutableStateOf(false) }
    LaunchedEffect(bShouldCopy) {
        if (bShouldCopy) {
            val clip = android.content.ClipData.newPlainText("Copied Text", colorToHex(newNoteColour).toString())
            clipboardManager.setPrimaryClip(clip)
            bShouldCopy=false
        }
    }

    // Main UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp)
        //.padding(16.dp)
    ) {

        Row(
            //verticalAlignment = Alignment.CenterVertically,
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton( // Clear button
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Back",
                    tint = colours.onBackground,
                    modifier = Modifier
                        .height(vIconSize)
                )
            }
            Spacer(modifier = Modifier.width(vSpacer))
            // Color preview Box
            Box( // Colour previewer
                modifier = Modifier
                    .semantics { contentDescription= "Recent Colours" }
                    .pointerInput(Unit) { detectTapGestures(
                        onTap = {
                            navController.navigate("navrecentcolours")
                            //Log.d("zzzSelectedColour", "Selected color: ${combinedColors[index]}")
                        },

                        onLongPress = {
                            bShouldPaste =true
                        }
                    ) }
                    .size(100.dp)
                    .clip(CircleShape) // Make the box circular
                    .background(newNoteColour),
                //.align(Alignment.Center)
            ){

                Text(
                    text = "Sample",
                    color = chooseColorBasedOnLuminance(newNoteColour),
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(vSpacer))
            IconButton( // Confirm
                onClick = {
                    viewModel.setColour(newNoteColour)
                    navController.popBackStack()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Confirm",
                    tint = colours.onBackground,
                    modifier = Modifier
                        .height(vIconSize)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Hue: ${hue.toInt()}")
        CustomSlider(// Hue Slider

            value = hue / 360f, // Normalize hue to 0..1
            onValueChange = { hue = it * 360f }, // De-normalize back to 0..360
            valueRange = 0f..1f, // Use normalized range
        )

        Text(text = "Saturation: ${(saturation * 100).toInt()}%")
        CustomSlider(// Saturation Slider

            value = saturation,
            onValueChange = { saturation = it },
            valueRange = 0f..1f,
        )

        Text(text = "Value: ${(value * 100).toInt()}%")
        CustomSlider(// Value Slider

            value = value,
            onValueChange = { value = it },
            valueRange = 0f..1f,
        )

        Text(text = "Alpha: ${(alpha * 100).toInt()}%")
        CustomSlider(// Alpha Slider
            value = alpha,
            onValueChange = { alpha = it },
            valueRange = 0f..1f,
        )

        Text(text = colorToHex(newNoteColour).toString(),
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .pointerInput(Unit) { detectTapGestures(
                    onLongPress = {
                        bShouldCopy =true
                    }

                ) }
        )
    }
}