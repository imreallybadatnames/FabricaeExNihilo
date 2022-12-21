package wraith.fabricaeexnihilo.recipe.witchwater;

import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wraith.fabricaeexnihilo.recipe.BaseRecipe;
import wraith.fabricaeexnihilo.recipe.ModRecipes;
import wraith.fabricaeexnihilo.recipe.RecipeContext;
import wraith.fabricaeexnihilo.recipe.util.WeightedList;
import wraith.fabricaeexnihilo.util.CodecUtils;
import wraith.fabricaeexnihilo.util.RegistryEntryLists;

import java.util.Optional;

public class WitchWaterWorldRecipe extends BaseRecipe<WitchWaterWorldRecipe.Context> {
    private final RegistryEntryList<Fluid> target;
    private final WeightedList result;

    public WitchWaterWorldRecipe(Identifier id, RegistryEntryList<Fluid> target, WeightedList result) {
        super(id);
        this.target = target;
        this.result = result;
    }

    public static Optional<WitchWaterWorldRecipe> find(Fluid fluid, @Nullable World world) {
        if (world == null) {
            return Optional.empty();
        }
        return world.getRecipeManager().getFirstMatch(ModRecipes.WITCH_WATER_WORLD, new Context(fluid), world);
    }

    @Override
    public boolean matches(Context context, World world) {
        return target.contains(context.fluid.getRegistryEntry());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.WITCH_WATER_WORLD_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.WITCH_WATER_WORLD;
    }

    @Override
    public ItemStack getDisplayStack() {
        return result.asListOfStacks().get(0);
    }

    public RegistryEntryList<Fluid> getTarget() {
        return target;
    }

    public WeightedList getResult() {
        return result;
    }

    protected record Context(Fluid fluid) implements RecipeContext {
    }

    public static class Serializer implements RecipeSerializer<WitchWaterWorldRecipe> {
        @Override
        public WitchWaterWorldRecipe read(Identifier id, JsonObject json) {
            var target = RegistryEntryLists.fromJson(Registries.FLUID.getKey(), json.get("target"));
            var result = CodecUtils.fromJson(WeightedList.CODEC, json.get("result"));

            return new WitchWaterWorldRecipe(id, target, result);
        }

        @Override
        public WitchWaterWorldRecipe read(Identifier id, PacketByteBuf buf) {
            var target = RegistryEntryLists.fromPacket(Registries.FLUID.getKey(), buf);
            var result = CodecUtils.fromPacket(WeightedList.CODEC, buf);

            return new WitchWaterWorldRecipe(id, target, result);
        }

        @Override
        public void write(PacketByteBuf buf, WitchWaterWorldRecipe recipe) {
            RegistryEntryLists.toPacket(Registries.FLUID.getKey(), recipe.target, buf);
            CodecUtils.toPacket(WeightedList.CODEC, recipe.result, buf);
        }
    }
}
