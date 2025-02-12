package wraith.fabricaeexnihilo.recipe.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public sealed abstract class BlockIngredient implements Predicate<BlockState> {
    protected final Map<String, String> properties;

    protected BlockIngredient(Map<String, String> properties) {
        this.properties = Map.copyOf(properties);
    }

    public static BlockIngredient fromJson(JsonElement json) {
        var states = new HashMap<String, String>();
        String id;
        if (json instanceof JsonObject object) {
            id = JsonHelper.getString(object, "id");
            for (var entry : JsonHelper.getObject(object, "states", new JsonObject()).entrySet()) {
                states.put(entry.getKey(), entry.getValue().getAsString());
            }
        } else {
            id = JsonHelper.asString(json, "block ingredient");
        }

        if (id.startsWith("#"))
            return new Tag(TagKey.of(RegistryKeys.BLOCK, new Identifier(id.substring(1))), states);
        else
            return new Single(Registries.BLOCK.get(new Identifier(id)), states);
    }

    public static BlockIngredient fromPacket(PacketByteBuf buf) {
        var states = buf.readMap(PacketByteBuf::readString, PacketByteBuf::readString);
        var id = buf.readByte();
        return switch (id) {
            case 0 -> new Single(buf.readRegistryValue(Registries.BLOCK), states);
            case 1 -> new Tag(TagKey.of(RegistryKeys.BLOCK, buf.readIdentifier()), states);
            default -> throw new IllegalStateException("Unexpected block ingredient type: " + id);
        };
    }

    protected boolean stateMatches(BlockState state) {
        for (var entry : properties.entrySet()) {
            var found = state.getProperties()
                    .stream()
                    .filter(property -> property.getName().equals(entry.getKey()))
                    .findFirst();
            if (found.isEmpty()) return false;
            var current = state.get(found.get());
            var parsed = found.get().parse(entry.getValue());
            // Parsing fails are silently ignored since multiple properties can have the same name
            if (parsed.isPresent() && !current.equals(parsed.get())) return false;
        }

        return true;
    }

    public static BlockIngredient single(Block block) {
        return new Single(block, Map.of());
    }

    public static BlockIngredient tag(TagKey<Block> tag) {
        return new Tag(tag, Map.of());
    }

    public abstract void toPacket(PacketByteBuf buf);

    public abstract JsonElement toJson();

    public abstract Either<Block, TagKey<Block>> getValue();

    private static final class Single extends BlockIngredient {
        private final Block block;

        private Single(Block block, Map<String, String> properties) {
            super(properties);
            this.block = block;
        }

        @Override
        public boolean test(BlockState state) {
            return state.isOf(block) && stateMatches(state);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeMap(properties, PacketByteBuf::writeString, PacketByteBuf::writeString);
            buf.writeByte(0);
            buf.writeRegistryValue(Registries.BLOCK, block);
        }

        @Override
        public JsonElement toJson() {
            if (properties.isEmpty()) return new JsonPrimitive(Registries.BLOCK.getId(block).toString());
            var json = new JsonObject();
            json.addProperty("id", Registries.BLOCK.getId(block).toString());
            var states = new JsonObject();
            properties.forEach(states::addProperty);
            json.add("states", states);
            return json;
        }

        @Override
        public Either<Block, TagKey<Block>> getValue() {
            return Either.left(block);
        }
    }

    private static final class Tag extends BlockIngredient {
        private final TagKey<Block> tag;

        private Tag(TagKey<Block> tag, Map<String, String> properties) {
            super(properties);
            this.tag = tag;
        }

        @Override
        public boolean test(BlockState state) {
            return state.getRegistryEntry().isIn(tag) && stateMatches(state);
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeMap(properties, PacketByteBuf::writeString, PacketByteBuf::writeString);
            buf.writeByte(1);
            buf.writeIdentifier(tag.id());
        }

        @Override
        public JsonElement toJson() {
            if (properties.isEmpty()) return new JsonPrimitive("#" + tag.id().toString());
            var json = new JsonObject();
            json.addProperty("id", "#" + tag.id().toString());
            var states = new JsonObject();
            properties.forEach(states::addProperty);
            json.add("states", states);
            return json;
        }

        @Override
        public Either<Block, TagKey<Block>> getValue() {
            return Either.right(tag);
        }
    }
}
