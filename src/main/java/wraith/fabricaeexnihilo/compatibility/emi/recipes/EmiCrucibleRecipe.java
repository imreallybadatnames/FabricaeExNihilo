package wraith.fabricaeexnihilo.compatibility.emi.recipes;

import dev.emi.emi.api.FabricEmiStack;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiPlugin;
import wraith.fabricaeexnihilo.compatibility.emi.FENEmiTextures;
import wraith.fabricaeexnihilo.recipe.crucible.CrucibleRecipe;

@SuppressWarnings("UnstableApiUsage")
public class EmiCrucibleRecipe extends BasicEmiRecipe {
    private static final int WIDTH = 3 * 18;
    private static final int HEIGHT = 18;
    private final EmiIngredient input;
    private final EmiStack output;

    public EmiCrucibleRecipe(CrucibleRecipe recipe) {
        super(recipe.requiresFireproofCrucible() ? FENEmiPlugin.FIREPROOF_CRUCIBLE : FENEmiPlugin.WOODEN_CRUCIBLE_CATEGORY, recipe.getId(), WIDTH, HEIGHT);
        input = EmiIngredient.of(recipe.getInput());
        inputs.add(input);
        output = FabricEmiStack.of(recipe.getFluid()).setAmount(recipe.getAmount());
        outputs.add(output);
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 0, 0);
        widgets.addTexture(FENEmiTextures.ARROW_RIGHT, 18, 0);
        widgets.addSlot(output, 18 * 2, 0).recipeContext(this);
    }
}
