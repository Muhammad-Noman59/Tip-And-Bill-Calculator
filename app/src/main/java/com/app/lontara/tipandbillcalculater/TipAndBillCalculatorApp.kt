package com.app.lontara.tipandbillcalculater

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.lontara.tipandbillcalculater.ui.theme.CeruleanBlue
import com.app.lontara.tipandbillcalculater.ui.theme.PeacockBlue


@Composable
fun Calculation() {

    val amount = remember { mutableStateOf("") }
    val personCounter = remember { mutableIntStateOf(1) }
    val tipPercentage = remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        TotalHeader(
            amount = formatTowDecimalPoints(
                getTotalHeaderAmount(
                    amount = amount.value,
                    personCounter = personCounter.value,
                    tipPercentage = tipPercentage.value
                )
            )
        )

        UserInputArea(amount = amount.value, amountChange = {
            amount.value = it
        }, personCounter = personCounter.value, onAddOrReducePerson = {
            if (it < 0) {
                if (personCounter.value != 1) {
                    personCounter.value--
                }
            } else {
                personCounter.value++
            }
        }, tipPercentage = tipPercentage.value, tipPercentageChange = {
            tipPercentage.value = it
        })
    }

}


@Composable
fun TotalHeader(amount: String) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        color = CeruleanBlue,
        shape = RoundedCornerShape(8)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "Total Per Person", style = TextStyle(
                    color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$ $amount", style = TextStyle(
                    color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold
                )
            )

        }

    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserInputArea(
    amount: String,
    amountChange: (String) -> Unit,
    personCounter: Int,
    onAddOrReducePerson: (Int) -> Unit,
    tipPercentage: Float,
    tipPercentageChange: (Float) -> Unit
) {

    val keyBordController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 12.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = amount,
                onValueChange = { amountChange.invoke(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter your amount")
                },

                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = CeruleanBlue,
                    unfocusedBorderColor = Color.Black,
                    focusedBorderColor = CeruleanBlue,
                    selectionColors = TextSelectionColors(
                        handleColor = CeruleanBlue,
                        backgroundColor = PeacockBlue
                    )

                ),

                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),

                keyboardActions = KeyboardActions(onDone = {

                    keyBordController?.hide()
                })

            )

            if (amount.isNotEmpty()) {

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Split", style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.fillMaxWidth(.50f))

                    CustomButton(imageVector = Icons.Default.KeyboardArrowDown) {
                        onAddOrReducePerson.invoke(-1)
                    }

                    Text(
                        text = "${personCounter}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    CustomButton(imageVector = Icons.Default.KeyboardArrowUp) {
                        onAddOrReducePerson.invoke(1)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Tip", style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.fillMaxWidth(.70f))

                    Text(
                        text = "$ ${formatTowDecimalPoints(getTipAmount(amount, tipPercentage))}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${formatTowDecimalPoints(tipPercentage.toString())} %",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = tipPercentage,
                    onValueChange = {
                        tipPercentageChange.invoke(it)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = CeruleanBlue,
                        activeTickColor = CeruleanBlue,
                        inactiveTickColor = CeruleanBlue,
                        activeTrackColor = PeacockBlue

                    ),
                    valueRange = 0f..100f,
                    steps = 5,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth()
                )

            }
        }

    }

}


@Composable
fun CustomButton(imageVector: ImageVector, onClick: () -> Unit) {

    Card(modifier = Modifier
        .wrapContentSize()
        .clickable {
            onClick.invoke()
        }
        .padding(12.dp)
        .shadow(
            elevation = 3.dp, spotColor = Color.Black, shape = CircleShape
        ), colors = CardDefaults.cardColors(
        containerColor = Color.White
    ), shape = CircleShape) {

        Image(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .padding(4.dp)
        )
    }
}