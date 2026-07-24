package com.example.util

import com.example.R

object PlayerPhotoHelper {
    /**
     * Returns an upper half-body photo model (Drawable resource ID or String URL)
     * for a given player name or team.
     */
    fun getPlayerPhoto(playerName: String, teamAbbr: String = ""): Any {
        val nameLower = playerName.lowercase().trim()
        val teamLower = teamAbbr.lowercase().trim()

        return when {
            nameLower.contains("messi") -> R.drawable.img_messi_portrait_1784922201792
            nameLower.contains("mbapp") || nameLower.contains("mbappe") -> R.drawable.img_mbappe_portrait_1784922214386
            nameLower.contains("pulisic") -> R.drawable.img_pulisic_portrait_1784922228137
            teamLower == "arg" -> R.drawable.img_messi_portrait_1784922201792
            teamLower == "fra" -> R.drawable.img_mbappe_portrait_1784922214386
            teamLower == "usa" -> R.drawable.img_pulisic_portrait_1784922228137
            else -> R.drawable.img_default_player_portrait_1784922241270
        }
    }
}
