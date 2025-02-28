package com.mexiti.costogasolina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mexiti.costogasolina.ui.theme.CostoGasolinaTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CostoGasolinaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CostGasLayout("Android")
                }
            }
        }
    }
}

//*Funciones no componibles (funciones cualquiera)
private fun calcularMonto(precio:Double, cantlitros: Double, propina: Double): String{
    val monto = precio * cantlitros + propina
    return NumberFormat.getCurrencyInstance().format(monto)
}

@Composable
fun CostGasLayout(name: String) {
    var preciolitroEntrada by remember { mutableStateOf("") }
    var cantLitrosEntrada by remember { mutableStateOf("") }
    var propinaEntrada by remember { mutableStateOf("") }
    var incluirPropina by remember { mutableStateOf(false) } // Estado del switch

    val precioLitro = preciolitroEntrada.toDoubleOrNull() ?: 0.0
    val cantLitros = cantLitrosEntrada.toDoubleOrNull() ?: 0.0
    val propina = if (incluirPropina) propinaEntrada.toDoubleOrNull() ?: 0.0 else 0.0 // Si el switch está apagado, la propina es 0
    val total = calcularMonto(precioLitro, cantLitros, propina)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFF5d6d7e)),
        verticalArrangement = Arrangement.Center,
        
    ) {
        Text(text = stringResource(R.string.calcular_monto))

        EditNumberField(
            label = R.string.ingresa_gasolina,
            leadingIcon = R.drawable.money_gas,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = preciolitroEntrada,
            onValueChanged = { preciolitroEntrada = it }
        )

        EditNumberField(
            label = R.string.litros,
            leadingIcon = R.drawable.gasolina,
            keyboardsOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = cantLitrosEntrada,
            onValueChanged = { cantLitrosEntrada = it }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "¿Incluir propina?")
            Switch(
                checked = incluirPropina,
                onCheckedChange = { incluirPropina = it }
            )
        }

        if (incluirPropina) {
            EditNumberField(
                label = R.string.propina,
                leadingIcon = R.drawable.propina,
                keyboardsOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                value = propinaEntrada,
                onValueChanged = { propinaEntrada = it }
            )
        }

        // Mostrar total calculado
        Text(text = stringResource(R.string.total, total))
    }
}


@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardsOptions:KeyboardOptions,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
){
    TextField(
        label = { Text(text = stringResource(id = label))},
        value = value,
        singleLine = true,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon) , contentDescription = null) },
        keyboardOptions = keyboardsOptions,
        modifier = modifier.fillMaxWidth(),
        onValueChange = onValueChanged
    )

}

@Preview(showBackground = true)
@Composable
fun CostGasLayoutPreview() {
    CostoGasolinaTheme {
        CostGasLayout("Android")
    }
}