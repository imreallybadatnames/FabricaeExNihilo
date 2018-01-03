package exnihilocreatio.items.tools;


import com.google.common.collect.Sets;
import exnihilocreatio.ExNihiloCreatio;
import exnihilocreatio.registries.manager.ExNihiloRegistryManager;
import exnihilocreatio.util.Data;
import exnihilocreatio.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class HammerBase extends ItemTool implements IHammer, IHasModel {

    final int miningLevel;

    public HammerBase(String name, int maxUses, ToolMaterial material) {
        super(material, Sets.newHashSet(new Block[]{}));
        this.setUnlocalizedName(name);
        this.setRegistryName(name);
        this.setMaxDamage(maxUses);
        this.miningLevel = material.getHarvestLevel();
        this.setCreativeTab(ExNihiloCreatio.tabExNihilo);

        Data.ITEMS.add(this);
    }

    @Override
    public boolean isHammer(ItemStack stack) {
        return true;
    }

    @Override
    public int getMiningLevel(ItemStack stack) {
        return miningLevel;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.isRegistered(state.getBlock()) ? this.efficiency : 1.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        return ExNihiloRegistryManager.HAMMER_REGISTRY.isRegistered(state.getBlock());
    }
}
