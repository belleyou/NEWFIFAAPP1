package com.example.model

data class GalleryPhoto(
    val url: String,
    val title: String,
    val caption: String,
    val category: PhotoCategory, // EXTERIOR, INTERIOR, ARCHITECTURE, SEATING
    val architecturalNote: String
)

enum class PhotoCategory(val displayName: String) {
    ALL("All Views"),
    EXTERIOR("Exterior Architecture"),
    INTERIOR("Interior & Pitch"),
    ARCHITECTURE("Structural Features"),
    SEATING("Seating & Bowl")
}

data class SeatingSection(
    val sectionId: String,
    val name: String,
    val tier: String,
    val capacity: String,
    val viewRating: String, // e.g., "5.0 ★ Prime Sideline"
    val colorHex: Long,
    val features: List<String>
)

data class StadiumArchitecturalDetails(
    val stadiumId: String,
    val architect: String,
    val yearOpened: String,
    val roofType: String,
    val facadeMaterial: String,
    val pitchType: String,
    val architecturalStyle: String,
    val seatingSections: List<SeatingSection>,
    val photos: List<GalleryPhoto>
)

object StadiumGalleryDataProvider {

    fun getGalleryForStadium(stadiumId: String, stadiumName: String): StadiumArchitecturalDetails {
        val photos = getPhotosForStadium(stadiumId)
        val sections = getSeatingSectionsForStadium(stadiumId)
        
        return when (stadiumId) {
            "STAD_NEW_YORK" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HOK Sport (Populous), Skanska",
                yearOpened = "2010 (Renovated 2025)",
                roofType = "Open-Air Canopy with LED Louver System",
                facadeMaterial = "Aluminum Louvers & Dynamic Color LED Panels",
                pitchType = "Vacuum-Ventilated Natural Grass",
                architecturalStyle = "Modernist Dynamic Louver Architecture",
                seatingSections = sections,
                photos = photos
            )
            "STAD_MEXICO_CITY" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "Pedro Ramírez Vázquez & Rafael Mijares",
                yearOpened = "1966 (FIFA 2026 Modernization)",
                roofType = "Cantilevered Continuous Ring Canopy",
                facadeMaterial = "Concrete Pillars & Solar Glass Facade",
                pitchType = "Reinforced Hybrid Turf",
                architecturalStyle = "Colossal Aztec Temple Neo-Brutalism",
                seatingSections = sections,
                photos = photos
            )
            "STAD_LOS_ANGELES" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HKS, Inc.",
                yearOpened = "2020",
                roofType = "Translucent ETFE Single-Layer Canopy",
                facadeMaterial = "Perforated Anodized Aluminum Panels",
                pitchType = "Matrix Turf System with Sub-Air Drainage",
                architecturalStyle = "Biomorphic Indoor-Outdoor Aerodynamic Bowl",
                seatingSections = sections,
                photos = photos
            )
            "STAD_ATLANTA" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "tvsdesign, HOK, Goode Van Slyke",
                yearOpened = "2017",
                roofType = "8-Petal Oculi Retractable Roof",
                facadeMaterial = "EFTE Facade & High-Translucent Polymer Wings",
                pitchType = "FieldTurf CORE Hybrid Precision System",
                architecturalStyle = "Kinetic Futuristic Geometry",
                seatingSections = sections,
                photos = photos
            )
            "STAD_DALLAS" -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "HKS, Inc.",
                yearOpened = "2009",
                roofType = "Twin 290-meter Steel Arch Retractable Glass Roof",
                facadeMaterial = "Inclined Canted Glass Wall System",
                pitchType = "Modular FIFA Grade Natural Turf Trays",
                architecturalStyle = "Monumental High-Tech Glass & Steel Arch",
                seatingSections = sections,
                photos = photos
            )
            else -> StadiumArchitecturalDetails(
                stadiumId = stadiumId,
                architect = "Populous & HKS Architecture",
                yearOpened = "2015",
                roofType = "Cantilevered Weather Shield Roof Canopy",
                facadeMaterial = "Perforated Steel & Energy-Smart Glass",
                pitchType = "SISGrass Hybrid Pitch",
                architecturalStyle = "Contemporary World-Class Arena Design",
                seatingSections = sections,
                photos = photos
            )
        }
    }

    private fun getSeatingSectionsForStadium(stadiumId: String): List<SeatingSection> {
        return listOf(
            SeatingSection(
                sectionId = "SEC_VIP",
                name = "VIP Presidential Skybox & Club Row",
                tier = "Exclusive Level 200",
                capacity = "2,400 Seats",
                viewRating = "5.0 ★ Panoramic Midfield",
                colorHex = 0xFFFFD700, // Gold
                features = listOf("Private Lounge Access", "Gourmet Catering", "Luxury Padded Seats", "In-seat Service")
            ),
            SeatingSection(
                sectionId = "SEC_LOWER_MID",
                name = "Lower Bowl Sideline (100 Level)",
                tier = "Lower Tier",
                capacity = "22,500 Seats",
                viewRating = "4.9 ★ Pitch-Side Proximity",
                colorHex = 0xFF10B981, // Emerald
                features = listOf("Player Tunnel Proximity", "Sub-Air Climate Control", "Optimal Tactical View")
            ),
            SeatingSection(
                sectionId = "SEC_GOAL_CURVE",
                name = "Supporters End & Goal Curve",
                tier = "Lower / Middle Tier",
                capacity = "18,000 Seats",
                viewRating = "4.7 ★ Electric Atmosphere",
                colorHex = 0xFF38BDF8, // Cyan
                features = listOf("Atmosphere Safe Standing", "Direct Behind-Goal Penalty View", "Ultra-wide LED View")
            ),
            SeatingSection(
                sectionId = "SEC_CLUB_UPPER",
                name = "Club Mezzanine & Premium Suites",
                tier = "Middle Tier 200/300",
                capacity = "14,200 Seats",
                viewRating = "4.8 ★ Elevated Angle",
                colorHex = 0xFF8B5CF6, // Purple
                features = listOf("Climate Controlled Concours", "Craft Beverage Bars", "Dedicated Fast-Track Gates")
            ),
            SeatingSection(
                sectionId = "SEC_UPPER_BOWL",
                name = "Upper Skyline Deck (400 Level)",
                tier = "Upper Deck",
                capacity = "28,000 Seats",
                viewRating = "4.5 ★ Full Stadium Panorama",
                colorHex = 0xFFF59E0B, // Amber
                features = listOf("Unobstructed Aerial View", "Direct Access to Concessions", "Shaded Roof Canopy")
            ),
            SeatingSection(
                sectionId = "SEC_PRESS_BOX",
                name = "FIFA International Media Press Box",
                tier = "Press Level",
                capacity = "1,200 Workstations",
                viewRating = "5.0 ★ Broadcast View",
                colorHex = 0xFFEC4899, // Pink
                features = listOf("High-Speed Fiber Optics", "Live Tactical Analytics Monitor", "Acoustic Shielding")
            )
        )
    }

    private fun getPhotosForStadium(stadiumId: String): List<GalleryPhoto> {
        return listOf(
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1518063319789-7217e6706b04?auto=format&fit=crop&w=1200&q=80",
                title = "Exterior Nighttime Illumination",
                caption = "The venue exterior glows with dynamic LED louver panels programmed with host country colors.",
                category = PhotoCategory.EXTERIOR,
                architecturalNote = "Features multi-spectrum RGB LED illumination integrated directly into the facade facade system."
            ),
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1522778119026-d647f0596c20?auto=format&fit=crop&w=1200&q=80",
                title = "Panoramic Pitch Bowl & Turf",
                caption = "Panoramic wide-angle view from the center circle towards the massive grandstand and canopy roof.",
                category = PhotoCategory.INTERIOR,
                architecturalNote = "Engineered with 360-degree acoustics to amplify crowd roar down onto the pitch surface."
            ),
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?auto=format&fit=crop&w=1200&q=80",
                title = "Cantilevered Roof Canopy Structure",
                caption = "Precision steel truss canopy structure protecting spectators from weather while optimizing turf sunlight.",
                category = PhotoCategory.ARCHITECTURE,
                architecturalNote = "Aerodynamically tuned to channel breeze through the upper deck while resisting extreme wind loads."
            ),
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1541252260730-0412e8e2108e?auto=format&fit=crop&w=1200&q=80",
                title = "Lower Tier Pitch-Side Perspective",
                caption = "View from the player bench looking out across the pristine hybrid grass pitch toward goal stands.",
                category = PhotoCategory.SEATING,
                architecturalNote = "Seating pitch gradient set at 34 degrees for maximum sightline efficiency without obstruction."
            ),
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1510563800743-aed236490d08?auto=format&fit=crop&w=1200&q=80",
                title = "Monumental Facade Entrance Plaza",
                caption = "Grand pedestrian concourse and monumental structural entry arch accommodating 80,000+ fans.",
                category = PhotoCategory.EXTERIOR,
                architecturalNote = "Designed for seamless fan flow with automated ticketing turnstiles and spacious security zones."
            ),
            GalleryPhoto(
                url = "https://images.unsplash.com/photo-1486286701208-1d58e9338013?auto=format&fit=crop&w=1200&q=80",
                title = "VIP Skybox & Premium Concourse Interior",
                caption = "State-of-the-art hospitality interior featuring glass partitioning and panoramic pitch views.",
                category = PhotoCategory.INTERIOR,
                architecturalNote = "Features double-paned acoustic glass that lets in ambient crowd sound while maintaining climate comfort."
            )
        )
    }
}
