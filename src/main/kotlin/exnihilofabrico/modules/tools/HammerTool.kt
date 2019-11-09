package exnihilofabrico.modules.tools

import exnihilofabrico.api.registry.ExNihiloRegistries
import exnihilofabrico.modules.ModTags.HAMMER_TAG
import net.minecraft.block.BlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial

class HammerTool(material: ToolMaterial, settings: Item.Settings):
    ToolItemWithRegistry(material, ExNihiloRegistries.HAMMER, settings) {

    override fun isEffectiveOn(state: BlockState) = ExNihiloRegistries.HAMMER.isRegistered(state.block)

    companion object {
        @JvmStatic
        fun isHammer(stack: ItemStack): Boolean {
            if(stack.item is HammerTool || HAMMER_TAG.contains(stack.item))
                return true
            return false
        }
    }

}