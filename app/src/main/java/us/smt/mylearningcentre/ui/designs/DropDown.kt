package us.smt.mylearningcentre.ui.designs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun <T> DropDown(
    modifier: Modifier = Modifier,
    selectedValue: T? = null,
    list: List<T>,
    getText: (item: T) -> String,
    onSelectNewItem: (T) -> Unit,
    addNewItem: () -> Unit
) {
    var isMenuExpended by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val rotate by animateFloatAsState(targetValue = if (isMenuExpended) 180f else 0f, label = "")


    // Manage the dropdown opening/closing and focus handling.
    Box(
        modifier = modifier // Toggle expanded state
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .focusable(interactionSource = interactionSource) // Ensure it's focusable
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable {
                    isMenuExpended = !isMenuExpended
                }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = selectedValue?.let { getText(selectedValue) } ?: "",
                style = TextStyle(
                    color = Color(0xFF98A2B3),
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.W500
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                modifier = Modifier.rotate(rotate),
                contentDescription = null,
                tint = Color(0xFF98A2B3)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        DropdownMenu(
            expanded = isMenuExpended,
            onDismissRequest = { isMenuExpended = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                for (i in 0 until list.size + 1) {
                    DropdownMenuItem(
                        text = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(vertical = 16.dp, horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (i < list.size) {

                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = getText(list[i]),
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            fontWeight = FontWeight.W500
                                        )
                                    )
                                    if (selectedValue == list[i])
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                } else {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = "Add new item",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            fontWeight = FontWeight.W500
                                        )
                                    )
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                }
                            }
                        },
                        onClick = {
                            if (i < list.size) {
                                onSelectNewItem(list[i])
                            } else {
                                addNewItem()
                            }
                            isMenuExpended = false
                        }
                    )
                }
            }
        }
    }

}