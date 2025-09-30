package com.lance5057.extradelight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.lance5057.extradelight.ExtraDelightItems;
import com.lance5057.extradelight.ExtraDelightNeoForgeClientEvents;
import com.lance5057.extradelight.data.advancement.EDAdvancementGenerator;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class EDItemGenerator {
	public static List<Drink> drinks = new ArrayList<Drink>();

	public static List<RegistryObject<Item>> hotFood = new ArrayList<RegistryObject<Item>>();
	public static List<RegistryObject<Item>> coldFood = new ArrayList<RegistryObject<Item>>();
	RegistryObject<Item> item;

	private EDItemGenerator(RegistryObject<Item> item) {
		this.item = item;
	}

	public static <I extends Item> EDItemGenerator register(String registrationName, Supplier<? extends I> item) {
		return new EDItemGenerator(ExtraDelightItems.ITEMS.register(registrationName, item));
	}

	public EDItemGenerator advancementSnack() {
		EDAdvancementGenerator.SNACKS.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementFeast() {
		EDAdvancementGenerator.FEASTS.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementMeal() {
		EDAdvancementGenerator.MEALS.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementDessert() {
		EDAdvancementGenerator.DESSERTS.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementCandy() {
		EDAdvancementGenerator.CANDY.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementCookie() {
		EDAdvancementGenerator.COOKIES.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementButchercraft() {
		EDAdvancementGenerator.BUTCHERCRAFT.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator advancementIngredients() {
		EDAdvancementGenerator.INGREDIENTS.put(item.getId().toLanguageKey(), item);
		return this;
	}

	public EDItemGenerator butchercraftToolTip() {
		ExtraDelightNeoForgeClientEvents.butchercraft.add(item);
		return this;
	}

	public EDItemGenerator feastToolTip() {
		ExtraDelightNeoForgeClientEvents.feasts.add(item);
		return this;
	}

	public EDItemGenerator servingToolTip() {
		ExtraDelightNeoForgeClientEvents.servings.add(item);
		return this;
	}

	public EDItemGenerator isHotFood() {
		EDItemGenerator.hotFood.add(item);
		return this;
	}

	public EDItemGenerator isColdFood() {
		EDItemGenerator.coldFood.add(item);
		return this;
	}

	public RegistryObject<Item> finish() {
		return item;
	}

	public Drink drink() {
		EDAdvancementGenerator.DRINKS.put(item.getId().toLanguageKey(), item);
		return new Drink(item);
	}

	public class Drink {
		public RegistryObject<Item> item;
		public int thirst;
		public int hydration;
		public int poison;
		public boolean isHot;

		public Drink(RegistryObject<Item> item2) {
			this.item = item2;
		}

		public Drink setThirst(int t) {
			thirst = t;
			return this;
		}

		public Drink setHydration(int h) {
			hydration = h;
			return this;
		}

		public Drink setPoison(int p) {
			poison = p;
			return this;
		}

		public Drink isHot(boolean hot) {
			isHot = hot;
			return this;
		}

		public RegistryObject<Item> finish() {
			EDItemGenerator.drinks.add(this);
			return item;
		}
	}

}
