package com.example.ui

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.model.HostStadium
import com.example.model.SeatingSection
import com.example.model.StadiumGalleryDataProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

enum class AROverlayMode(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    SEATING_PLAN("Seating Chart", Icons.Default.AirlineSeatReclineExtra),
    FIELD_MARKINGS("Pitch & Field", Icons.Default.SportsSoccer),
    COMBINED("Full 3D Hologram", Icons.Default.ViewInAr)
}

enum class ARColorTheme(val nameStr: String, val primaryColor: Color, val accentColor: Color) {
    CYBER_EMERALD("Cyber Emerald", Color(0xFF10B981), Color(0xFF34D399)),
    GOLD_VIP("Gold VIP", Color(0xFFF59E0B), Color(0xFFFBBF24)),
    TACTICAL_BLUE("Tactical Blue", Color(0xFF38BDF8), Color(0xFF60A5FA))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StadiumARCameraOverlay(
    stadium: HostStadium,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Permissions state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // AR Configuration state
    var arMode by remember { mutableStateOf(AROverlayMode.SEATING_PLAN) }
    var arTheme by remember { mutableStateOf(ARColorTheme.CYBER_EMERALD) }
    var arOpacity by remember { mutableFloatStateOf(0.75f) }
    var arScale by remember { mutableFloatStateOf(1.0f) }
    var isSimulatedCamera by remember { mutableStateOf(!hasCameraPermission) }

    // Selected section for detailed view
    val galleryData = remember(stadium.id) { StadiumGalleryDataProvider.getGalleryForStadium(stadium.id, stadium.name) }
    var selectedSection by remember { mutableStateOf<SeatingSection?>(galleryData.seatingSections.firstOrNull()) }

    // Compass & HUD Animation states
    var compassHeading by remember { mutableIntStateOf(178) }
    var pitchAngle by remember { mutableFloatStateOf(-12f) }
    var isTargetLocked by remember { mutableStateOf(true) }

    // Snapshot state
    var showSnapshotSuccess by remember { mutableStateOf(false) }
    var snapshotCount by remember { mutableIntStateOf(0) }

    // Simulated compass heading & tilt motion
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            compassHeading = kotlin.random.Random.nextInt(175, 186)
            pitchAngle = -14f + kotlin.random.Random.nextFloat() * 4f
        }
    }

    // Prompt permission if not granted
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // --- 1. CAMERA FEED / SIMULATED FEED ---
        if (hasCameraPermission && !isSimulatedCamera) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                val preview = Preview.Builder().build().also {
                                    it.setSurfaceProvider(surfaceProvider)
                                }
                                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                                cameraProvider.unbindAll()
                                cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                            } catch (e: Exception) {
                                isSimulatedCamera = true
                            }
                        }, ContextCompat.getMainExecutor(ctx))
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // High-resolution camera background simulation with stadium image
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = stadium.image,
                    contentDescription = stadium.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )
            }
        }

        // --- 2. DYNAMIC AR CANVAS OVERLAY ---
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        arScale = (arScale * zoom).coerceIn(0.6f, 2.5f)
                    }
                }
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2f
            val centerY = height / 2f

            val pColor = arTheme.primaryColor.copy(alpha = arOpacity)
            val aColor = arTheme.accentColor.copy(alpha = arOpacity)

            // Draw HUD Target Reticle in Center
            val reticleRadius = 80.dp.toPx() * arScale
            drawCircle(
                color = pColor.copy(alpha = 0.25f),
                radius = reticleRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = aColor,
                radius = 6.dp.toPx(),
                center = Offset(centerX, centerY)
            )

            // Target Crosshairs
            val lineLen = 24.dp.toPx()
            drawLine(
                color = aColor,
                start = Offset(centerX - reticleRadius - lineLen, centerY),
                end = Offset(centerX - reticleRadius + 8.dp.toPx(), centerY),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = aColor,
                start = Offset(centerX + reticleRadius - 8.dp.toPx(), centerY),
                end = Offset(centerX + reticleRadius + lineLen, centerY),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = aColor,
                start = Offset(centerX, centerY - reticleRadius - lineLen),
                end = Offset(centerX, centerY - reticleRadius + 8.dp.toPx()),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = aColor,
                start = Offset(centerX, centerY + reticleRadius - 8.dp.toPx()),
                end = Offset(centerX, centerY + reticleRadius + lineLen),
                strokeWidth = 2.dp.toPx()
            )

            // --- SEATING PLAN OVERLAY ---
            if (arMode == AROverlayMode.SEATING_PLAN || arMode == AROverlayMode.COMBINED) {
                // Outer Stadium Ellipse (Upper Bowl)
                val outerWidth = (width * 0.85f) * arScale
                val outerHeight = (height * 0.32f) * arScale
                val outerRect = Size(outerWidth, outerHeight)
                val outerTopLeft = Offset(centerX - outerWidth / 2f, centerY - outerHeight / 2f + 20.dp.toPx())

                drawOval(
                    color = pColor,
                    topLeft = outerTopLeft,
                    size = outerRect,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Middle Tier Ellipse (Club Level)
                val midWidth = outerWidth * 0.72f
                val midHeight = outerHeight * 0.72f
                val midTopLeft = Offset(centerX - midWidth / 2f, centerY - midHeight / 2f + 20.dp.toPx())

                drawOval(
                    color = aColor.copy(alpha = arOpacity * 0.85f),
                    topLeft = midTopLeft,
                    size = Size(midWidth, midHeight),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Lower Tier Ellipse
                val innerWidth = outerWidth * 0.48f
                val innerHeight = outerHeight * 0.48f
                val innerTopLeft = Offset(centerX - innerWidth / 2f, centerY - innerHeight / 2f + 20.dp.toPx())

                drawOval(
                    color = pColor,
                    topLeft = innerTopLeft,
                    size = Size(innerWidth, innerHeight),
                    style = Stroke(width = 2.dp.toPx())
                )

                // Radial Sector Dividers (Seating Sections)
                val sectors = 12
                for (i in 0 until sectors) {
                    val angleRad = Math.toRadians((i * (360.0 / sectors)).toDouble())
                    val x1 = centerX + (innerWidth / 2f) * cos(angleRad).toFloat()
                    val y1 = (centerY + 20.dp.toPx()) + (innerHeight / 2f) * sin(angleRad).toFloat()
                    val x2 = centerX + (outerWidth / 2f) * cos(angleRad).toFloat()
                    val y2 = (centerY + 20.dp.toPx()) + (outerHeight / 2f) * sin(angleRad).toFloat()

                    drawLine(
                        color = aColor.copy(alpha = 0.5f * arOpacity),
                        start = Offset(x1, y1),
                        end = Offset(x2, y2),
                        strokeWidth = 1.5.dp.toPx()
                    )
                }
            }

            // --- FIELD & PITCH OVERLAY ---
            if (arMode == AROverlayMode.FIELD_MARKINGS || arMode == AROverlayMode.COMBINED) {
                val pitchW = (width * 0.38f) * arScale
                val pitchH = (height * 0.18f) * arScale
                val pitchTopLeft = Offset(centerX - pitchW / 2f, centerY - pitchH / 2f + 20.dp.toPx())

                // Pitch Perimeter
                drawRect(
                    color = Color(0xFF10B981).copy(alpha = arOpacity),
                    topLeft = pitchTopLeft,
                    size = Size(pitchW, pitchH),
                    style = Stroke(width = 2.5.dp.toPx())
                )

                // Center Line
                drawLine(
                    color = Color.White.copy(alpha = arOpacity),
                    start = Offset(centerX, pitchTopLeft.y),
                    end = Offset(centerX, pitchTopLeft.y + pitchH),
                    strokeWidth = 1.5.dp.toPx()
                )

                // Center Circle
                drawCircle(
                    color = Color.White.copy(alpha = arOpacity),
                    radius = 20.dp.toPx() * arScale,
                    center = Offset(centerX, centerY + 20.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // Penalty Boxes
                val boxW = pitchW * 0.22f
                val boxH = pitchH * 0.55f
                val boxY = centerY + 20.dp.toPx() - boxH / 2f

                // Left Box
                drawRect(
                    color = Color.White.copy(alpha = arOpacity),
                    topLeft = Offset(pitchTopLeft.x, boxY),
                    size = Size(boxW, boxH),
                    style = Stroke(width = 1.5.dp.toPx())
                )
                // Right Box
                drawRect(
                    color = Color.White.copy(alpha = arOpacity),
                    topLeft = Offset(pitchTopLeft.x + pitchW - boxW, boxY),
                    size = Size(boxW, boxH),
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }

        // --- 3. TOP NAVIGATION & AR COMPASS HUD ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Close button
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close AR",
                        tint = Color.White
                    )
                }

                // AR Heading & Status Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF0F172A).copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                        .border(1.dp, arTheme.primaryColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(if (isTargetLocked) arTheme.primaryColor else Color.Yellow, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AR TARGET: ${stadium.name.uppercase()}",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🧭 ${compassHeading}° S",
                        color = arTheme.accentColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Camera source toggle
                IconButton(
                    onClick = { isSimulatedCamera = !isSimulatedCamera },
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isSimulatedCamera) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = "Toggle Feed",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AR Mode Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AROverlayMode.entries.forEach { mode ->
                    val isSelected = arMode == mode
                    FilterChip(
                        selected = isSelected,
                        onClick = { arMode = mode },
                        label = {
                            Text(
                                text = mode.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = mode.icon,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = arTheme.primaryColor,
                            selectedLabelColor = Color.White,
                            containerColor = Color.Black.copy(alpha = 0.6f),
                            labelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.White.copy(alpha = 0.3f),
                            selectedBorderColor = arTheme.primaryColor
                        ),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }

        // --- 4. BOTTOM FLOATING INTERACTIVE CONTROLS & SELECTION CARD ---
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF0F172A).copy(alpha = 0.95f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Seating Section Selector Row
            if (arMode == AROverlayMode.SEATING_PLAN || arMode == AROverlayMode.COMBINED) {
                Text(
                    text = "SELECT SEATING ZONE OVERLAY:",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(galleryData.seatingSections) { section ->
                        val isSelected = selectedSection == section
                        Card(
                            onClick = { selectedSection = section },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) arTheme.primaryColor else Color(0xFF1E293B).copy(alpha = 0.8f)
                            ),
                            border = if (isSelected) BorderStroke(1.5.dp, Color.White) else null,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                Text(
                                    text = section.name,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${section.tier} · ${section.viewRating}",
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else arTheme.accentColor,
                                    fontSize = 9.5.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            // AR Controls Card (Opacity & Color Theme)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B).copy(alpha = 0.88f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Opacity & Zoom Sliders
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Opacity,
                                contentDescription = null,
                                tint = arTheme.accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AR Opacity: ${(arOpacity * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Slider(
                            value = arOpacity,
                            onValueChange = { arOpacity = it },
                            valueRange = 0.2f..1.0f,
                            modifier = Modifier
                                .width(140.dp)
                                .height(20.dp),
                            colors = SliderDefaults.colors(
                                thumbColor = arTheme.primaryColor,
                                activeTrackColor = arTheme.primaryColor,
                                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Theme Selector & Photo Capture Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Hologram Theme:", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                            ARColorTheme.entries.forEach { theme ->
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .background(theme.primaryColor, CircleShape)
                                        .border(
                                            width = if (arTheme == theme) 2.dp else 0.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        )
                                        .clickable { arTheme = theme }
                                )
                            }
                        }

                        // Capture AR Photo Button
                        Button(
                            onClick = {
                                snapshotCount++
                                showSnapshotSuccess = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = arTheme.primaryColor
                            ),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Camera,
                                    contentDescription = "Capture Snapshot",
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "CAPTURE AR PHOTO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. SNAPSHOT SUCCESS NOTIFICATION ---
        AnimatedVisibility(
            visible = showSnapshotSuccess,
            enter = fadeIn() + slideInVertically { -it },
            exit = fadeOut() + slideOutVertically { -it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF10B981)
                ),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "AR Seating Photo #$snapshotCount Saved to Gallery!",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    TextButton(
                        onClick = { showSnapshotSuccess = false },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("DISMISS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            LaunchedEffect(snapshotCount) {
                delay(3000)
                showSnapshotSuccess = false
            }
        }
    }
}
