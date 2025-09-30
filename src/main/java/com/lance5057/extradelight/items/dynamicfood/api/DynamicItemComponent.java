package com.lance5057.extradelight.items.dynamicfood.api;

import com.google.common.collect.Iterables;
import com.lance5057.extradelight.ExtraDelight;
import com.lance5057.extradelight.ExtraDelightComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
//import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraft.world.item.component.TooltipProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class DynamicItemComponent implements INBTSerializable<CompoundTag> {
	public static final DynamicItemComponent EMPTY = new DynamicItemComponent(NonNullList.create());
//	public static final Codec<DynamicItemComponent> CODEC = Slot.CODEC.sizeLimitedListOf(256)
//			.xmap(DynamicItemComponent::fromSlots, DynamicItemComponent::asSlots);
//	public static final StreamCodec<RegistryFriendlyByteBuf, DynamicItemComponent> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC
//			.apply(ByteBufCodecs.list(256)).map(DynamicItemComponent::new, p_331691_ -> p_331691_.items);
	private NonNullList<ItemStack> items;

	public NonNullList<ItemStack> getItems() {
		return items;
	}
	
	public void setItems(NonNullList<ItemStack> items) {
		this.items = items;
	}

	public void addItem(ItemStack stack) {
		this.items.add(stack);
	}

	private int hashCode;

	public DynamicItemComponent() {
		this(NonNullList.create());
	}

	public DynamicItemComponent(NonNullList<ItemStack> items) {
		if (items.size() > 256) {
			throw new IllegalArgumentException("Got " + items.size() + " items, but maximum is 256");
		} else {
			this.items = items;
			this.hashCode = hashStackList(items);
		}
	}



	private DynamicItemComponent(int size) {
		this(NonNullList.withSize(size, ItemStack.EMPTY));
	}

	private DynamicItemComponent(List<ItemStack> items) {
		this(items.size());

		for (int i = 0; i < items.size(); i++) {
			this.items.set(i, items.get(i));
		}
	}






	// 替代 ItemStack.hashStackList 方法
	private static int hashStackList(NonNullList<ItemStack> stacks) {
		int i = 1;

		for (ItemStack itemstack : stacks) {
			i = 31 * i + (itemstack.isEmpty() ? 0 : hashItemStack(itemstack));
		}

		return i;
	}

	// 计算单个 ItemStack 的哈希值
	private static int hashItemStack(ItemStack stack) {
		int result = stack.getItem().hashCode(); // 物品类型本身的哈希码
		result = 31 * result + stack.getCount(); // 物品数量

		// 考虑物品的损坏值（耐久度）
		if (stack.isDamageableItem()) {
			result = 31 * result + stack.getDamageValue();
		}

		// 如果物品有 NBT 标签，也纳入哈希计算
		// 注意：在 1.20.1，物品的附加数据主要通过 getTag() 获取其 NBT
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			// 这里使用 NBT 的 toString() 并计算其哈希，是一种简单但可能低效的方式。
			// 对于性能敏感处，你可能需要更精细地处理 NBT 哈希。
			result = 31 * result + (tag == null ? 0 : tag.toString().hashCode());
		}

		// 你可以根据需要添加其他影响物品“唯一性”的因素
		// 例如：stack.getRarity(), 附魔等...

		return result;
	}

	private static boolean listMatches(NonNullList<ItemStack> first, NonNullList<ItemStack> second) {
		if (first.size() != second.size()) {
			return false;
		}

		for (int i = 0; i < first.size(); i++) {
			ItemStack stack1 = first.get(i);
			ItemStack stack2 = second.get(i);

			if (stack1.isEmpty() != stack2.isEmpty()) {
				return false;
			}

			if (!stack1.isEmpty() && !ItemStack.matches(stack1, stack2)) {
				return false;
			}
		}

		return true;
	}


	private static DynamicItemComponent fromSlots(List<Slot> slots) {
		OptionalInt optionalint = slots.stream().mapToInt(Slot::index).max();
		if (optionalint.isEmpty()) {
			return EMPTY;
		} else {
			DynamicItemComponent DynamicItemComponent = new DynamicItemComponent(optionalint.getAsInt() + 1);

			for (Slot DynamicItemComponent$slot : slots) {
				DynamicItemComponent.items.set(DynamicItemComponent$slot.index(), DynamicItemComponent$slot.item());
			}

			return DynamicItemComponent;
		}
	}

	public static DynamicItemComponent fromItems(List<ItemStack> items) {
		int i = findLastNonEmptySlot(items);
		if (i == -1) {
			return EMPTY;
		} else {
			DynamicItemComponent DynamicItemComponent = new DynamicItemComponent(i + 1);

			for (int j = 0; j <= i; j++) {
				DynamicItemComponent.items.set(j, items.get(j).copy());
			}

			return DynamicItemComponent;
		}
	}

	private static int findLastNonEmptySlot(List<ItemStack> items) {
		for (int i = items.size() - 1; i >= 0; i--) {
			if (!items.get(i).isEmpty()) {
				return i;
			}
		}

		return -1;
	}



	private List<Slot> asSlots() {
		List<Slot> list = new ArrayList<>();

		for (int i = 0; i < this.items.size(); i++) {
			ItemStack itemstack = this.items.get(i);
			if (!itemstack.isEmpty()) {
				list.add(new Slot(i, itemstack));
			}
		}

		return list;
	}

	public void copyInto(NonNullList<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
			list.set(i, itemstack.copy());
		}
	}

	public ItemStack copyOne() {
		return this.items.isEmpty() ? ItemStack.EMPTY : this.items.get(0).copy();
	}

	public Stream<ItemStack> stream() {
		return this.items.stream().map(ItemStack::copy);
	}

	public Stream<ItemStack> nonEmptyStream() {
		return this.items.stream().filter(p_331322_ -> !p_331322_.isEmpty()).map(ItemStack::copy);
	}

	public Iterable<ItemStack> nonEmptyItems() {
		return Iterables.filter(this.items, p_331420_ -> !p_331420_.isEmpty());
	}

	public Iterable<ItemStack> nonEmptyItemsCopy() {
		return Iterables.transform(this.nonEmptyItems(), ItemStack::copy);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			if (other instanceof DynamicItemComponent dynamicItemComponent &&
					listMatches(this.items, dynamicItemComponent.items)) {
				return true;
			}

			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.hashCode;
	}

	/**
	 * Neo: {@return the number of slots in this container}
	 */
	public int getSlots() {
		return this.items.size();
	}

	/**
	 * Neo: Gets a copy of the stack at a particular slot.
	 *
	 * @param slot The slot to check. Must be within [0, {@link #getSlots()}]
	 * @return A copy of the stack in that slot
	 * @throws UnsupportedOperationException if the provided slot index is
	 *                                       out-of-bounds.
	 */
	public ItemStack getStackInSlot(int slot) {
		validateSlotIndex(slot);
		return this.items.get(slot).copy();
	}

	/**
	 * Neo: Throws {@link UnsupportedOperationException} if the provided slot index
	 * is invalid.
	 */
	private void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots()) {
			throw new UnsupportedOperationException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		ListTag list = new ListTag();

		for (int i = 0; i < items.size(); i++) {
			CompoundTag itemTag = new CompoundTag();
			itemTag.putInt("Slot", i);
			items.get(i).save(itemTag);
			list.add(itemTag);
		}

		tag.put("Items", list);
		tag.putInt("Size", items.size());

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ListTag list = nbt.getList("Items", Tag.TAG_COMPOUND);
		int size = nbt.getInt("Size");

		items = NonNullList.withSize(size, ItemStack.EMPTY);

		for (int i = 0; i < list.size(); i++) {
			CompoundTag itemTag = list.getCompound(i);
			int slot = itemTag.getInt("Slot");

			if (slot >= 0 && slot < items.size()) {
				items.set(slot, ItemStack.of(itemTag));
			}
		}

		this.hashCode = hashStackList(items);
	}

	static record Slot(int index, ItemStack item) {
		public static final Codec<Slot> CODEC = RecordCodecBuilder.create(p_331695_ -> p_331695_
				.group(Codec.intRange(0, 255).fieldOf("slot").forGetter(Slot::index),
						ItemStack.CODEC.fieldOf("item").forGetter(Slot::item))
				.apply(p_331695_, Slot::new));
	}

	public void addToTooltip(Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
		if (this.getItems() != null && !this.getItems().isEmpty()) {
			NonNullList<ItemStack> i = this.getItems();
			tooltipAdder.accept(Component.translatable(ExtraDelight.MOD_ID + ".tooltip.dynamic"));
			for (ItemStack s : i) {
				if (!s.isEmpty()) {
					tooltipAdder.accept(s.getHoverName());
				}
			}
		} else {
			tooltipAdder.accept(Component.translatable(ExtraDelight.MOD_ID + ".tooltip.dynamic.empty"));
		}
	}

}
