package mezz.jei.plugins.vanilla.ingredients;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.List;

import com.google.common.base.Preconditions;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.util.ErrorUtil;
import mezz.jei.util.StackHelper;
import mezz.jei.util.color.ColorGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemStackHelper implements IIngredientHelper<ItemStack> {
	private final StackHelper stackHelper;

	public ItemStackHelper(StackHelper stackHelper) {
		this.stackHelper = stackHelper;
	}

	@Override
	public List<ItemStack> expandSubtypes(List<ItemStack> contained) {
		return stackHelper.getAllSubtypes(contained);
	}

	@Override
	@Nullable
	public ItemStack getMatch(Iterable<ItemStack> ingredients, ItemStack toMatch) {
		return stackHelper.containsStack(ingredients, toMatch);
	}

	@Override
	public String getDisplayName(ItemStack ingredient) {
		return Preconditions.checkNotNull(ingredient.getDisplayName(), "No display name for ItemStack.");
	}

	@Override
	public String getUniqueId(ItemStack ingredient) {
		return stackHelper.getUniqueIdentifierForStack(ingredient);
	}

	@Override
	public String getWildcardId(ItemStack ingredient) {
		return stackHelper.getUniqueIdentifierForStack(ingredient, StackHelper.UidMode.WILDCARD);
	}

	@Override
	public String getModId(ItemStack ingredient) {
		Preconditions.checkArgument(!ingredient.isEmpty(), "itemStack ingredient cannot be empty");

		Item item = ingredient.getItem();
		ResourceLocation itemName = item.getRegistryName();
		if (itemName == null) {
			String stackInfo = getErrorInfo(ingredient);
			throw new IllegalStateException("item.getRegistryName() returned null for: " + stackInfo);
		}

		return itemName.getResourceDomain();
	}

	@Override
	public Iterable<Color> getColors(ItemStack ingredient) {
		return ColorGetter.getColors(ingredient, 2);
	}

	@Override
	public String getErrorInfo(ItemStack ingredient) {
		return ErrorUtil.getItemStackInfo(ingredient);
	}
}
