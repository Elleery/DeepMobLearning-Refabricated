/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This file is part of Deep Mob Learning: Refabricated.
 *
 * Deep Mob Learning: Refabricated is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deep Mob Learning: Refabricated is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Deep Mob Learning: Refabricated.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.nathanpb.dml.config

import dev.nathanpb.dml.MOD_ID
import dev.nathanpb.dml.config
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer
import kotlin.math.max
import kotlin.math.min


fun registerConfigs() {
    AutoConfig.register(ModConfig::class.java, ::GsonConfigSerializer)
    config = AutoConfig.getConfigHolder(ModConfig::class.java).config
}

@Config(name = MOD_ID)
class ModConfig : ConfigData {

    @ConfigEntry.Category("trial")
    @ConfigEntry.Gui.TransitiveObject
    var trial = Trial()

    @ConfigEntry.Category("loot_fabricator")
    @ConfigEntry.Gui.TransitiveObject
    var lootFabricator = LootFabricator()

    @ConfigEntry.Category("data_model")
    @ConfigEntry.Gui.TransitiveObject
    var dataModel = DataModel()

    @ConfigEntry.Category("system_glitch")
    @ConfigEntry.Gui.TransitiveObject
    var systemGlitch = SystemGlitch()

    @ConfigEntry.Category("data_collection")
    @ConfigEntry.Gui.TransitiveObject
    var dataCollection = DataCollection()


    @ConfigEntry.Category("affix")
    @ConfigEntry.Gui.TransitiveObject
    var affix = TrialAffix()

    override fun validatePostLoad() {
        trial.validatePostLoad()
        lootFabricator.validatePostLoad()
        dataModel.validatePostLoad()
        affix.validatePostLoad()
    }

}

@Config(name = "trial")
class Trial : ConfigData {
    var maxMobsInArena = 8
    var postEndTimeout = 60
    var arenaRadius = 12
    var maxTime = 24000

    var allowStartInWrongTerrain = false
    var allowPlayersLeavingArena = false
    var allowMobsLeavingArena = false
    var buildGriefPrevention = true
    var interactGriefPrevention = true
    var explosionGriefPrevention = true

    var trialKeyConsume = true
    var trialKeyReturnIfSucceed = true

    override fun validatePostLoad() {
        maxTime = max(0, maxTime)
        if (maxMobsInArena < 0) {
            maxMobsInArena = 0
        }

        if (postEndTimeout < 0) {
            postEndTimeout = 0
        }

        if (arenaRadius < 1) {
            arenaRadius = 1
        }
    }
}

@Config(name = "affix")
class TrialAffix : ConfigData {
    var maxAffixesInKey = 3
    var enableMobStrength = true
    var enableMobSpeed = true
    var enableMobResistance = true
    var enableThunderstorm = true
    var enablePartyPoison = true

    var thunderstormBoltChance = .05F
    var partyPoisonChance = .005F

    override fun validatePostLoad() {
        maxAffixesInKey = max(0, maxAffixesInKey)
    }
}

@Config(name = "loot_fabricator")
class LootFabricator : ConfigData {
    var pristineExchangeRate = 16
    var processTime = 200

    override fun validatePostLoad() {
        if (pristineExchangeRate < 0) {
            pristineExchangeRate = 0
        }

        if (processTime < 0) {
            processTime = 0
        }
    }
}

@Config(name = "data_model")
class DataModel : ConfigData {

    var basicDataRequired = 8
    var advancedDataRequired = 16
    var superiorDataRequired = 32
    var selfAwareDataRequired = 64

    override fun validatePostLoad() {
        if (basicDataRequired <= 0) {
            basicDataRequired = 1
        }
        if (advancedDataRequired < basicDataRequired) {
            advancedDataRequired = basicDataRequired
        }
        if (superiorDataRequired < advancedDataRequired) {
            superiorDataRequired = basicDataRequired
        }
        if (selfAwareDataRequired < advancedDataRequired) {
            selfAwareDataRequired = advancedDataRequired
        }
    }
}

@Config(name = "system_glitch")
class SystemGlitch : ConfigData {

    var teleportChance = 0.05F
    var teleportMinDistance = 5
    var teleportDelay = 100
    var teleportAroundPlayerRadius = 2

    override fun validatePostLoad() {
        teleportChance = max(0F, min(1F, teleportChance))
        teleportMinDistance = max(0, teleportMinDistance)
        teleportDelay = max(0, teleportDelay)
        teleportAroundPlayerRadius = max(1, teleportAroundPlayerRadius)
    }
}

@Config(name = "data_collection")
class DataCollection : ConfigData {
    var baseDataGainPerKill = 1
}
