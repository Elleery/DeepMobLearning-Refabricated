/*
 * Copyright (C) 2020 Nathan P. Bombana, IterationFunk
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/.
 */

package dev.nathanpb.dml.item

import dev.nathanpb.dml.data.DataModelTier
import dev.nathanpb.dml.data.EntityCategory
import dev.nathanpb.dml.data.dataModel
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class ItemDataModel(val category: EntityCategory? = null) : Item(settings().maxCount(1)) {
    override fun appendTooltip(
        stack: ItemStack?,
        world: World?,
        tooltip: MutableList<Text>?,
        context: TooltipContext?
    ) {
        super.appendTooltip(stack, world, tooltip, context)
        if (world != null && stack != null && tooltip != null) {
            if (category != null) {
                stack.dataModel.let { data ->
                    // todo add that tooltip for creative players
                    // tooltip.add(TranslatableText("tooltip.deepmoblearning.data_model.cheat"))
                    if (!data.tier().isMaxTier()) {
                        tooltip.add(
                            TranslatableText(
                                "tooltip.deepmoblearning.data_model.data_amount",
                                data.dataAmount,
                                data.tier().nextTierOrCurrent().dataAmount - data.dataAmount
                            )
                        )
                    }
                    tooltip.add(
                        TranslatableText(
                            "tooltip.deepmoblearning.data_model.tier",
                            data.tier().text.asFormattedString()
                        )
                    )
                }
            }
        }
    }

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        if (user?.isCreative == true && user.isSneaking && hand != null) {
            val stack = user.getStackInHand(hand)
            if (stack.item is ItemDataModel) {
                val tier = stack.dataModel.tier()
                stack.dataModel.dataAmount = if (tier.isMaxTier()) {
                    DataModelTier.FAULTY.dataAmount
                } else tier.nextTierOrCurrent().dataAmount
            }
        }
        return super.use(world, user, hand)
    }
}
