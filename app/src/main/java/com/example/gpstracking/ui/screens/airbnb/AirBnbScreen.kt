package com.example.gpstracking.ui.screens.airbnb

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class PropertyCategory(
    val name: String,
    val icon: ImageVector,
    val isSelected: Boolean = false
)

data class Property(
    val id: String,
    val location: String,
    val distance: String,
    val dates: String,
    val pricePerNight: String,
    val rating: Float,
    val reviewCount: Int,
    val isFavorite: Boolean = false
)

@Composable
fun AirBnbScreen() {
    var selectedCategory by remember { mutableStateOf("OMG!") }
    var selectedBottomTab by remember { mutableStateOf("Explore") }

    val categories = listOf(
        PropertyCategory("OMG!", Icons.Filled.Home, selectedCategory == "OMG!"),
        PropertyCategory("Beach", Icons.Filled.WaterDrop, selectedCategory == "Beach"),
        PropertyCategory("Pools", Icons.Filled.Water, selectedCategory == "Pools"),
        PropertyCategory("Islands", Icons.Filled.Terrain, selectedCategory == "Islands"),
        PropertyCategory("Arctic", Icons.Filled.AcUnit, selectedCategory == "Arctic")
    )

    val properties = listOf(
        Property("1", "Abiansemal, Indonesia", "1,669 km away", "Jul 2–7", "\$360 / night", 4.87f, 71),
        Property("2", "Seminyak, Indonesia", "1,665 km away", "Jul 10–15", "\$280 / night", 4.92f, 45),
        Property("3", "Canggu, Indonesia", "1,672 km away", "Jul 20–25", "\$420 / night", 4.95f, 128)
    )

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Search bar
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Where to?", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("Anywhere · Any week · Add guests", fontSize = 14.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier.size(32.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Category chips
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(categories) { category ->
                CategoryChip(category = category, onClick = { selectedCategory = category.name })
            }
        }

        Spacer(Modifier.height(16.dp))

        // Property list
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(properties, key = { it.id }) { property ->
                PropertyCard(property = property)
            }
        }

        AirBnbBottomBar(selectedTab = selectedBottomTab, onTabSelected = { selectedBottomTab = it })
    }
}

@Composable
private fun CategoryChip(category: PropertyCategory, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }.padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = category.icon,
            contentDescription = category.name,
            modifier = Modifier.size(24.dp),
            tint = if (category.isSelected) Color.Black else Color.Gray
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = category.name,
            fontSize = 12.sp,
            color = if (category.isSelected) Color.Black else Color.Gray,
            fontWeight = if (category.isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
        if (category.isSelected) {
            Spacer(Modifier.height(4.dp))
            Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color.Black))
        }
    }
}

@Composable
private fun PropertyCard(property: Property) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(280.dp)
                .clip(RoundedCornerShape(12.dp)).background(Color.LightGray)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
            }
            Box(
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
                    .size(32.dp).background(Color.White.copy(alpha = 0.8f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(18.dp),
                    tint = if (property.isFavorite) Color.Red else Color.Black
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(property.location, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(property.distance, fontSize = 14.sp, color = Color.Gray)
                Text(property.dates, fontSize = 14.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Text(property.pricePerNight, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("${property.rating} (${property.reviewCount})", fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun AirBnbBottomBar(selectedTab: String, onTabSelected: (String) -> Unit) {
    val tabs = listOf(
        Triple("Explore", Icons.Outlined.Search, Icons.Filled.Search),
        Triple("Wishlist", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite),
        Triple("Trips", Icons.Outlined.Home, Icons.Filled.Home),
        Triple("Inbox", Icons.Outlined.Message, Icons.Filled.Message),
        Triple("Profile", Icons.Outlined.Person, Icons.Filled.Person)
    )

    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp, color = Color.White) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEach { (name, outlined, filled) ->
                val isSelected = selectedTab == name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onTabSelected(name) }.padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isSelected) filled else outlined,
                        contentDescription = name,
                        modifier = Modifier.size(24.dp),
                        tint = if (isSelected) Color(0xFFFF5A5F) else Color.Gray
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = name,
                        fontSize = 10.sp,
                        color = if (isSelected) Color(0xFFFF5A5F) else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AirBnbScreenPreview() {
    MaterialTheme { AirBnbScreen() }
}
